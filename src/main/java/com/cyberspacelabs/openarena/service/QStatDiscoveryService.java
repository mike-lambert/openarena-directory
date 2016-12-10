package com.cyberspacelabs.openarena.service;

import com.cyberspacelabs.openarena.model.OpenArenaDiscoveryRecord;

public interface QStatDiscoveryService {
    String getDiscoveryServiceEndpoint();
    long getRefreshInterval();
    OpenArenaDiscoveryRecord getLatestDiscoveryResults();
}
