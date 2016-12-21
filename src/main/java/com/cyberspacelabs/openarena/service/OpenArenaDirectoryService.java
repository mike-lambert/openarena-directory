package com.cyberspacelabs.openarena.service;

import com.cyberspacelabs.openarena.model.OpenArenaDiscoveryRecord;

import java.util.List;
import java.util.Set;

public interface OpenArenaDirectoryService {
    Set<OpenArenaDiscoveryRecord> refreshDiscovery() throws Exception;
    OpenArenaDiscoveryRecord getDiscovery(String address) throws  Exception;
    List<String> enumerateDiscoveryServers() throws Exception;
}
