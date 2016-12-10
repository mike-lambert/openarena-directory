package com.cyberspacelabs.openarena.web.transform;

import com.cyberspacelabs.openarena.model.OpenArenaServerRecord;
import com.cyberspacelabs.openarena.web.dto.LocationDTO;
import com.cyberspacelabs.openarena.web.dto.ServerDTO;
import com.google.common.base.Function;

public class ServerRecordToServerDTO implements Function<OpenArenaServerRecord, ServerDTO> {
    @Override
    public ServerDTO apply(OpenArenaServerRecord input) {
        ServerDTO result = new ServerDTO();
        result.setAddress(input.getAddress());
        result.setLoad(input.getPlayersPresent() + "/" + input.getSlotsAvailable());
        result.setLocation(new LocationDTO()); // TODO: implement
        result.setMap(input.getMap());
        result.setMode(input.getGameType().isEmpty() ? "baseoa" : input.getGameType());
        result.setPing(input.getPing());
        result.setType(input.getGameType().equalsIgnoreCase("Quake3Arena") ? "QUAKE3" : "OPENARENA");
        return result;
    }
}
