package com.cyberspacelabs.openarena.service.impl;

import com.cyberspacelabs.openarena.model.OpenArenaServerRecord;
import com.cyberspacelabs.openarena.model.qstat.Qstat;
import com.cyberspacelabs.openarena.service.QStatConversionService;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@Service
public class QStatConversionServiceImpl implements QStatConversionService {
    @Override
    public Set<OpenArenaServerRecord> convert(Qstat input) throws Exception {
        Set<OpenArenaServerRecord> result = new CopyOnWriteArraySet<>();
        if (input != null && !input.getServer().isEmpty()){
            for(Qstat.Server server : input.getServer()){
                result.add(convertServer(server));
            }
        }
        return result;
    }

    private OpenArenaServerRecord convertServer(Qstat.Server server) {
        OpenArenaServerRecord result = new OpenArenaServerRecord(server.getAddress());
        result.setDisplayName(server.getName());
        result.setGameType(server.getGametype());
        result.setMap(server.getMap());
        result.setPing(server.getPing());
        result.setPlayersPresent(server.getNumplayers());
        result.setSlotsAvailable(server.getMaxplayers());
        result.setRetries(server.getRetries());
        result.setStatus(OpenArenaServerRecord.ServerStatus.valueOf(server.getStatus()));
        result.setQueryTimestamp(System.currentTimeMillis());
        return result;
    }
}
