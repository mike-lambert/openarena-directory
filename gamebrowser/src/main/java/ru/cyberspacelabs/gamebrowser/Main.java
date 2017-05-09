package ru.cyberspacelabs.gamebrowser;

import java.util.*;

/**
 * Created by mzakharov on 31.01.17.
 */
public class Main {
    public static void main(String[] args){
        GameBrowser browser = new GameBrowser();
        GameBrowser xgb = new GameBrowser(GameBrowser.DPMASTER, GameBrowser.QUERY_XONOTIC_DEFAULT);
        GameBrowser qgb = new GameBrowser(GameBrowser.DPMASTER, GameBrowser.QUERY_QUAKE_3_ARENA_DEFAULT);
        Set<GameServer> servers = browser.refresh();
        System.out.println(servers.size() + " server(s) of OpenArena responsive");
        servers.forEach(server -> {
            System.out.println(server.getAddress() + " " + server.getDisplayName() + " " + server.getRequestDuration() + " "
                    + server.getPlayersPresent() + "/" + server.getSlotsAvailable() + " " + server.getGameType()
            );
        });

        servers = xgb.refresh();
        System.out.println();
        System.out.println(servers.size() + " server(s) of Xonotic responsive");
        servers.forEach(server -> {
            System.out.println(server.getAddress() + " " + server.getDisplayName() + " " + server.getRequestDuration() + " "
                    + server.getPlayersPresent() + "/" + server.getSlotsAvailable() + " " + server.getGameType()
            );
        });

        servers = qgb.refresh();
        System.out.println();
        System.out.println(servers.size() + " server(s) of Quake 3 Arena responsive");
        servers.forEach(server -> {
            System.out.println(server.getAddress() + " " + server.getDisplayName() + " " + server.getRequestDuration() + " "
                    + server.getPlayersPresent() + "/" + server.getSlotsAvailable() + " " + server.getGameType()
            );
        });
    }
}
