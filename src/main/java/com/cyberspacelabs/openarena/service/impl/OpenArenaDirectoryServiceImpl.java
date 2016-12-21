package com.cyberspacelabs.openarena.service.impl;

import com.cyberspacelabs.openarena.model.OpenArenaDiscoveryRecord;
import com.cyberspacelabs.openarena.service.GeoIpMappingService;
import com.cyberspacelabs.openarena.service.OpenArenaDirectoryService;
import com.cyberspacelabs.openarena.service.QStatDiscoveryServiceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@Service
public class OpenArenaDirectoryServiceImpl implements OpenArenaDirectoryService {
    @Autowired
    private QStatDiscoveryServiceFactory discoveryServiceFactory;

    @Autowired
    private GeoIpMappingService mappingService;

    @Override
    public Set<OpenArenaDiscoveryRecord> enumerate() throws Exception {
        Set<OpenArenaDiscoveryRecord> result = new CopyOnWriteArraySet<>();
        discoveryServiceFactory.instantiate().forEach(instance -> {
            OpenArenaDiscoveryRecord discovery = instance.getLatestDiscoveryResults();
            result.add(discovery);
            discovery.getRecords().forEach(record -> {
                try {
                    mappingService.locate(record);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

        });
        return result;
    }

    @Override
    public OpenArenaDiscoveryRecord getDiscovery(String address) throws Exception {
        OpenArenaDiscoveryRecord discovery = discoveryServiceFactory.instantiate().stream().filter(
                instance -> instance.getDiscoveryServiceEndpoint().equals(address)
        )
                .findAny()
                .get()
                .getLatestDiscoveryResults();
        discovery.getRecords().forEach(record -> {
            try {
                mappingService.locate(record);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return discovery;
    }
}
