package com.cyberspacelabs.openarena.web.transform;

import com.cyberspacelabs.openarena.model.OpenArenaServerRecord;
import com.cyberspacelabs.openarena.web.dto.Location;
import com.cyberspacelabs.openarena.web.dto.Server;
import com.google.common.base.Function;

public class ServerRecordToServerDTO implements Function<OpenArenaServerRecord, Server> {
    @Override
    public Server apply(OpenArenaServerRecord input) {
        Server result = new Server();
        result.setAddress(input.getAddress());
        result.setLoad(input.getPlayersPresent() + "/" + input.getSlotsAvailable());
        result.setLocation(new Location());
        result.setMap(input.getMap());
        result.setMode(input.getGameType().isEmpty() ? "baseoa" : input.getGameType());
        result.setPing(input.getPing());
        result.setType(input.getGameType().equalsIgnoreCase("Quake3Arena") ? "QUAKE3" : "OPENARENA");
        result.setName(input.getDisplayName());
        return result;
    }
}
