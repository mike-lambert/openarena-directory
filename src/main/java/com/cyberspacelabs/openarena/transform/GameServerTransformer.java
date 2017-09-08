package com.cyberspacelabs.openarena.transform;

import com.cyberspacelabs.openarena.dto.Server;
import com.google.common.base.Function;
import ru.cyberspacelabs.gamebrowser.GameServer;

/**
 * Created by mike on 07.09.17.
 */
public class GameServerTransformer implements Function<GameServer, Server> {
    @Override
    public Server apply(GameServer input) {
        Server result = new Server();
        result.setDisplayName(input.getDisplayName());
        result.setAddress(input.getAddress());
        result.setPinned(input.isPinned());
        result.setRequestDuration(input.getRequestDuration());
        result.setGameType(input.getGameType());
        result.setGame(input.getGame());
        result.setSlotsAvailable(input.getSlotsAvailable());
        result.setPlayersPresent(input.getPlayersPresent());
        result.setMap(input.getMap());
        result.setServerProtocol(input.getServerProtocol());
        return result;
    }
}
