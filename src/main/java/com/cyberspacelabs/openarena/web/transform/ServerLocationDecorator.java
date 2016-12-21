package com.cyberspacelabs.openarena.web.transform;

import com.cyberspacelabs.openarena.model.geoip.Path;
import com.cyberspacelabs.openarena.service.GeoIpResolutionService;
import com.cyberspacelabs.openarena.web.dto.Directory;

/**
 * Created by mike on 10.12.16.
 */
public class ServerLocationDecorator {
    public void decorate(Directory directory, GeoIpResolutionService resolutionService) {
        directory.getServers().parallelStream().forEach(server -> {
            try {
                Path<String> path = resolutionService.resolve(getHost(server.getAddress()));
                server.getLocation().setCode(path.getCountryCode());
                server.getLocation().setDomain(path.getPath());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private String getHost(String endpoint) {
        int port = endpoint.indexOf(":");
        if (port == -1) {
            return endpoint;
        } else {
            return endpoint.substring(0, port);
        }
    }
}
