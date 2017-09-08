package com.cyberspacelabs.openarena.dto;

import ru.cyberspacelabs.gamebrowser.GameServer;

/**
 * Created by mike on 07.09.17.
 */
public class Server extends GameServer {
    private String location;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
