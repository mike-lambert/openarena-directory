package com.cyberspacelabs.openarena.service.impl;

import com.cyberspacelabs.openarena.model.OpenArenaDiscoveryRecord;
import com.cyberspacelabs.openarena.model.OpenArenaServerRecord;
import com.cyberspacelabs.openarena.service.GeoIpMappingService;
import com.cyberspacelabs.openarena.service.OpenArenaDirectoryService;
import com.cyberspacelabs.openarena.service.QStatDiscoveryServiceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

@Service
public class OpenArenaDirectoryServiceImpl implements OpenArenaDirectoryService {
    @Autowired
    private QStatDiscoveryServiceFactory discoveryServiceFactory;

    @Autowired
    private GeoIpMappingService mappingService;

    @Override
    public Set<OpenArenaDiscoveryRecord> refreshDiscovery() throws Exception {
        Set<OpenArenaDiscoveryRecord> result = new CopyOnWriteArraySet<>();
        discoveryServiceFactory.instantiate().forEach(instance -> {
            OpenArenaDiscoveryRecord discovery = instance.getLatestDiscoveryResults();
            result.add(discovery);
            discovery.getRecords().parallelStream().forEach(record -> {
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

    @Override
    public List<String> enumerateDiscoveryServers() throws Exception {
        List<String> result = new CopyOnWriteArrayList<>();
        discoveryServiceFactory.instantiate().parallelStream().forEach(service -> {
            result.add(service.getDiscoveryServiceEndpoint());
        });
        return result;
    }

    @Override
    public Set<OpenArenaServerRecord> filterForDiscovery(Set<OpenArenaServerRecord> source, List<String> servers) throws Exception {
        Set<OpenArenaServerRecord> result = new CopyOnWriteArraySet<>();
        discoveryServiceFactory.instantiate().stream().filter(
                instance -> servers == null || servers.isEmpty() || servers.contains(instance.getDiscoveryServiceEndpoint())
        ).parallel().forEach(service -> service.getLatestDiscoveryResults().getRecords()
                .parallelStream()
                .filter(record -> source.contains(record)).forEach( record -> result.add(record))
        );
        return result;
    }
}
