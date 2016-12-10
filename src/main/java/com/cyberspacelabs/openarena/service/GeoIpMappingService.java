package com.cyberspacelabs.openarena.service;

import com.cyberspacelabs.openarena.model.OpenArenaServerRecord;
import com.cyberspacelabs.openarena.model.geoip.Path;
import com.cyberspacelabs.openarena.model.geoip.ProximityLevel;

import java.util.Set;

public interface GeoIpMappingService {
    Set<OpenArenaServerRecord> nearby(String ip, ProximityLevel proximityLevel) throws Exception;
    Path<OpenArenaServerRecord> locate(OpenArenaServerRecord server) throws Exception;
    void register(Path<OpenArenaServerRecord> entry);
}
