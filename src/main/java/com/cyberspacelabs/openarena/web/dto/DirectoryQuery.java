package com.cyberspacelabs.openarena.web.dto;

import com.cyberspacelabs.openarena.model.geoip.ProximityLevel;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by mike on 21.12.16.
 */
public class DirectoryQuery {
    private List<String> servers;
    private ProximityLevel level;

    public DirectoryQuery(){
        servers = new CopyOnWriteArrayList<>();
        level = ProximityLevel.GLOBAL;
    }

    public List<String> getServers() {
        return servers;
    }

    public void setServers(List<String> servers) {
        this.servers = servers;
    }

    public ProximityLevel getLevel() {
        return level;
    }

    public void setLevel(ProximityLevel level) {
        this.level = level;
    }
}
