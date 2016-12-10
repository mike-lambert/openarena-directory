package com.cyberspacelabs.openarena.service;

import java.util.Set;

public interface QStatDiscoveryServiceFactory {
    Set<QStatDiscoveryService> instantiate();
}
