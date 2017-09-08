package com.cyberspacelabs.openarena.transform;

import com.cyberspacelabs.openarena.dto.Server;
import com.cyberspacelabs.openarena.service.GeoIpResolutionService;
import ru.cyberspacelabs.gamebrowser.GameServer;

/**
 * Created by mike on 07.09.17.
 */
public class ServerLocationDecorator {
    public void apply(GeoIpResolutionService resolver, Server server){
        try {
            String location = resolver.resolve(getHostAddress(server)).getPath();
            if (location == null || location.trim().isEmpty()){
                server.setLocation("[Unknown]");
            } else {
                server.setLocation(location);
            }
        } catch (Exception e){
            server.setLocation("[Unknown]");
        }
    }

    private String getHostAddress(GameServer record) {
        String endpoint = record.getAddress();
        int port = endpoint.indexOf(":");
        if (port == -1) {
            return endpoint;
        } else {
            return endpoint.substring(0, port);
        }
    }
}
