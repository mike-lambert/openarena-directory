package ru.cyberspacelabs.gamebrowser;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.*;

/**
 * Created by mzakharov on 31.01.17.
 */
public class Main {
    public static void main(String[] args){
        GameBrowser browser = new GameBrowser();
        GameBrowser xgb = new GameBrowser(GameBrowser.DPMASTER, GameBrowser.QUERY_XONOTIC_DEFAULT);
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
    }
}
