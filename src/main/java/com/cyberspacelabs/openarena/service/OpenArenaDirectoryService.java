package com.cyberspacelabs.openarena.service;

import com.cyberspacelabs.openarena.model.OpenArenaDiscoveryRecord;
import com.cyberspacelabs.openarena.model.OpenArenaServerRecord;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface OpenArenaDirectoryService {
    Set<OpenArenaDiscoveryRecord> refreshDiscovery() throws Exception;
    OpenArenaDiscoveryRecord getDiscovery(String address) throws  Exception;
    List<String> enumerateDiscoveryServers() throws Exception;
    Set<OpenArenaServerRecord> filterForDiscovery(Set<OpenArenaServerRecord> source, List<String> servers) throws Exception;
}
