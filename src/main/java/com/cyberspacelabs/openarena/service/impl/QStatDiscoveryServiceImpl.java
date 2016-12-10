package com.cyberspacelabs.openarena.service.impl;

import com.cyberspacelabs.openarena.model.OpenArenaDiscoveryRecord;
import com.cyberspacelabs.openarena.model.OpenArenaServerRecord;
import com.cyberspacelabs.openarena.model.qstat.Qstat;
import com.cyberspacelabs.openarena.service.QStatConversionService;
import com.cyberspacelabs.openarena.service.QStatDiscoveryService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.xml.bind.JAXBContext;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class QStatDiscoveryServiceImpl  implements QStatDiscoveryService{

    private long refreshInterval;
    private String discoveryServiceEndpoint; // dpmaster.deathmask.net:27950
    private OpenArenaDiscoveryRecord cache;
    private boolean useWine;
    private String qstatBinaryLocation; // ~/bin/qstat/qstat-2.11-win32/qstat.exe -xml -nf
    private String qstatServerType; // -oam
    private String qstatXmlReport; // -of deathmask.xml
    private Object lock;

    public QStatDiscoveryServiceImpl(){
        lock = new Object();
        setRefreshInterval(60000);
        setUseWine(true);
        setQstatServerType("oam");
        setQstatXmlReport("qstat.xml");
        setQstatBinaryLocation("qstat-win32/qstat.exe");
        setDiscoveryServiceEndpoint("dpmaster.deathmask.net");
    }

    public QStatDiscoveryServiceImpl(QStatConversionService converter){
        this();
        setConversionService(converter);
    }

    @Autowired
    QStatConversionService conversionService;

    @Override
    public String getDiscoveryServiceEndpoint() {
        return discoveryServiceEndpoint;
    }

    @Override
    public long getRefreshInterval() {
        return refreshInterval;
    }

    @Override
    public OpenArenaDiscoveryRecord getLatestDiscoveryResults() {
        long current = System.currentTimeMillis();
        synchronized (lock){
            if (cache == null || cache.getLastQueried() == -1 || current - cache.getLastQueried() > refreshInterval){
                refreshDiscovery();
            }
            return cache;
        }
    }

    private void refreshDiscovery() {
        OpenArenaDiscoveryRecord result = new OpenArenaDiscoveryRecord(discoveryServiceEndpoint);
        result.setDirectoryName(getDiscoveryServiceEndpoint());
        try {
            synchronized (lock){
                removeReport();
                runAndWaitQStat();
                Qstat mapped = readReport();
                Set<OpenArenaServerRecord> loaded = conversionService.convert(mapped);
                Set<OpenArenaServerRecord> filtered = filterInvalid(loaded);
                result.getRecords().addAll(filtered);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        result.setLastQueried(System.currentTimeMillis());
        cache = result;
    }

    private void removeReport() {
        File report = new File(getQstatXmlReport());
        if (report.isFile() && report.exists()){
            report.delete();
        }
    }

    private void runAndWaitQStat() throws IOException, InterruptedException {
        Runtime.getRuntime().exec(buildCommandLine()).waitFor();
    }

    private String buildCommandLine() {
        StringBuilder sb = new StringBuilder();
        if (isUseWine()){
            sb.append("wine ");
        }
        sb.append(getQstatBinaryLocation())
                .append(" -xml ")
                .append("-nf ")
                .append("-").append(getQstatServerType()).append(" ")
                .append(getDiscoveryServiceEndpoint())
                .append(" -of ").append(getQstatXmlReport());
        System.out.println("QStat launching: \"" + sb.toString() + "\"");
        return sb.toString();
    }

    private Set<OpenArenaServerRecord> filterInvalid(Set<OpenArenaServerRecord> in) {
        return in.stream().filter(r -> r.isValid()).collect(Collectors.toSet());
    }

    private Qstat readReport() throws Exception {
        return (Qstat)JAXBContext.newInstance(Qstat.class).createUnmarshaller().unmarshal(new File(qstatXmlReport));
    }

    public void setRefreshInterval(long refreshInterval) {
        this.refreshInterval = refreshInterval;
    }

    public void setDiscoveryServiceEndpoint(String discoveryServiceEndpoint) {
        this.discoveryServiceEndpoint = discoveryServiceEndpoint;
    }

    public void setUseWine(boolean useWine) {
        this.useWine = useWine;
    }

    public void setQstatBinaryLocation(String qstatBinaryLocation) {
        this.qstatBinaryLocation = qstatBinaryLocation;
    }

    public void setQstatServerType(String qstatServerType) {
        this.qstatServerType = qstatServerType;
    }

    public void setQstatXmlReport(String qstatXmlReport) {
        this.qstatXmlReport = qstatXmlReport;
    }

    public boolean isUseWine() {
        return useWine;
    }

    public String getQstatBinaryLocation() {
        return qstatBinaryLocation;
    }

    public String getQstatServerType() {
        return qstatServerType;
    }

    public String getQstatXmlReport() {
        return qstatXmlReport;
    }

    public void setConversionService(QStatConversionService conversionService) {
        this.conversionService = conversionService;
    }
}
