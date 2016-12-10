package com.cyberspacelabs.openarena.service.impl;

import com.cyberspacelabs.openarena.model.OpenArenaServerRecord;
import com.cyberspacelabs.openarena.model.geoip.Path;
import com.cyberspacelabs.openarena.model.geoip.ProximityLevel;
import com.cyberspacelabs.openarena.service.GeoIpMappingService;
import com.cyberspacelabs.openarena.service.GeoIpResolutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

public class GeoIpMappingServiceImpl implements GeoIpMappingService {

    @Value("${geoip.cache.path}")
    private String cachePath;
    private Map<String, Path<Set<OpenArenaServerRecord>>> reverseCache;
    private Map<OpenArenaServerRecord, Path<Set<OpenArenaServerRecord>>> cache;
    @Autowired
    private GeoIpResolutionService resolutionService;


    public GeoIpMappingServiceImpl(){
        cachePath = "./.geoip";
        reverseCache = new ConcurrentHashMap<>();
        cache = new ConcurrentHashMap<>();
    }

    public GeoIpMappingServiceImpl(GeoIpResolutionService resolver){
        this();
        resolutionService = resolver;
    }

    @Override
    public Set<OpenArenaServerRecord> nearby(String ip, ProximityLevel proximityLevel) throws Exception {
        return null;
    }

    @Override
    public Path<OpenArenaServerRecord> locate(OpenArenaServerRecord server) throws Exception {
        return null;
    }

    @Override
    public void register(Path<OpenArenaServerRecord> entry) {
        OpenArenaServerRecord record = entry.getValue();
        String ip = getHostAddress(record);
        Path<Set<OpenArenaServerRecord>> geo = cache.get(record);
        if (geo == null){
            try {
                Path address = resolutionService.resolve(ip);
                Set<OpenArenaServerRecord> set = new CopyOnWriteArraySet<>();
                set.add(record);
                geo = new Path<Set<OpenArenaServerRecord>>(address.getName(), address.getCountry(), address.getRegion(), address.getCity(), address.getZipCode(),
                        address.getCity()+"/"+ address.getZipCode(),
                        set
                );
                cache.put(record, geo);
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
        reverseCache.put(ip, geo);
        saveCache();
    }

    private void saveCache() {

    }

    private String getHostAddress(OpenArenaServerRecord record) {
        String endpoint = record.getAddress();
        int port = endpoint.indexOf(":");
        if (port == -1){
            return endpoint;
        } else {
            return endpoint.substring(0, port);
        }
    }
}
