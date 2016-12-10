package com.cyberspacelabs.openarena.service;

import com.cyberspacelabs.openarena.model.OpenArenaDiscoveryRecord;

import java.util.Set;

public interface OpenArenaDirectoryService {
    Set<OpenArenaDiscoveryRecord> enumerate() throws Exception;
    OpenArenaDiscoveryRecord getDiscovery(String address) throws  Exception;
}
