package com.cyberspacelabs.openarena.service;

import com.cyberspacelabs.openarena.model.geoip.Path;

public interface GeoIpResolutionService {
    Path<String> resolve(String ip) throws Exception;
}
