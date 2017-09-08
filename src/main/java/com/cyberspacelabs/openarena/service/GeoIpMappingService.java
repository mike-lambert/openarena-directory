package com.cyberspacelabs.openarena.service;

import com.cyberspacelabs.openarena.model.geoip.Path;
import com.cyberspacelabs.openarena.model.geoip.ProximityLevel;
import ru.cyberspacelabs.gamebrowser.GameServer;

import java.util.Set;

public interface GeoIpMappingService {
    Set<GameServer> nearby(String ip, ProximityLevel proximityLevel) throws Exception;
    Path<GameServer> locate(GameServer server) throws Exception;
    void register(Path<GameServer> entry);
}
