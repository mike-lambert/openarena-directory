package integration;

import com.cyberspacelabs.openarena.model.OpenArenaDiscoveryRecord;
import com.cyberspacelabs.openarena.model.OpenArenaServerRecord;
import com.cyberspacelabs.openarena.service.QStatDiscoveryService;
import com.cyberspacelabs.openarena.service.QStatDiscoveryServiceFactory;
import com.cyberspacelabs.openarena.service.impl.NativeDiscoveryServiceImpl;
import com.cyberspacelabs.openarena.service.impl.QStatConversionServiceImpl;
import com.cyberspacelabs.openarena.service.impl.QStatDiscoveryServiceFactoryImpl;
import com.cyberspacelabs.openarena.service.impl.QStatDiscoveryServiceImpl;
import org.junit.Assert;
import org.junit.Test;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class Discovery {
    @Test
    public void discoverySucceeded() throws Exception {
        System.out.println("Discovery service works");
        QStatDiscoveryServiceImpl setup = new QStatDiscoveryServiceImpl(new QStatConversionServiceImpl());
        setup.setQstatBinaryLocation("/opt/openarena/qstat/qstat.exe");
        QStatDiscoveryService discoveryService = setup;
        OpenArenaDiscoveryRecord discovery = discoveryService.getLatestDiscoveryResults();
        System.out.println(discovery);
        discovery.getRecords().forEach(entry -> System.out.println(entry));
        System.out.println("__________________________________________________________________________________________");
        Assert.assertNotNull(discovery);
        Assert.assertNotEquals(true, discovery.getRecords().isEmpty());
    }

    @Test
    public void discoveryFactoryConfiguration() throws Exception {
        System.out.println("Discovery service factory works");
        QStatDiscoveryServiceFactory discoveryServiceFactory = new QStatDiscoveryServiceFactoryImpl();
        Set<QStatDiscoveryService> services = discoveryServiceFactory.instantiate();
        Set<OpenArenaServerRecord> servers = new CopyOnWriteArraySet<>();
        services.forEach(
                discoveryService -> discoveryService.getLatestDiscoveryResults().getRecords()
                        .forEach(
                                record -> {
                                    servers.add(record);
                                    System.out.println(record);
                                }
                        )
        );
        Assert.assertNotNull(services);
        Assert.assertNotEquals(true, services.isEmpty());
        Assert.assertNotEquals(true, servers.isEmpty());
    }

    @Test
    public void nativeDiscoveryWorks() throws Exception {
        System.out.println("Native discovery service works");
        NativeDiscoveryServiceImpl ds = new NativeDiscoveryServiceImpl();
        OpenArenaDiscoveryRecord result = ds.getLatestDiscoveryResults();
        Assert.assertNotNull(result);
        result.getRecords().forEach(record -> System.out.println(record));
        Assert.assertNotEquals(true, result.getRecords().isEmpty());
    }
}
