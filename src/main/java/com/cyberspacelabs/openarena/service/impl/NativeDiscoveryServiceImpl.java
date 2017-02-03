package com.cyberspacelabs.openarena.service.impl;

import com.cyberspacelabs.openarena.model.OpenArenaDiscoveryRecord;
import com.cyberspacelabs.openarena.model.OpenArenaServerRecord;
import com.cyberspacelabs.openarena.service.QStatDiscoveryService;
import com.google.common.base.Function;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.cyberspacelabs.gamebrowser.GameBrowser;
import ru.cyberspacelabs.gamebrowser.GameServer;

import javax.annotation.PostConstruct;
import java.util.Set;

/**
 * Created by mike on 02.02.17.
 */
@Service
public class NativeDiscoveryServiceImpl implements QStatDiscoveryService {
    private long refreshInterval;
    private String discoveryServiceEndpoint; // dpmaster.deathmask.net:27950
    private OpenArenaDiscoveryRecord cache;
    private Object lock;
    private long requestTimeout;
    private GameBrowser gameBrowser;

    public NativeDiscoveryServiceImpl(){
        lock = new Object();
        setRefreshInterval(60000);
        setDiscoveryServiceEndpoint("dpmaster.deathmask.net");
        gameBrowser = new GameBrowser();
    }

    public void setRefreshInterval(long refreshInterval) {
        this.refreshInterval = refreshInterval;
    }

    public void setDiscoveryServiceEndpoint(String discoveryServiceEndpoint) {
        this.discoveryServiceEndpoint = discoveryServiceEndpoint;
    }

    @PostConstruct
    public void updateGameBrowserProperties() {
        String address = getDiscoveryServiceEndpoint();
        if (!address.contains(":")){ address += ":27950"; }
        gameBrowser.setMasterAddress(address);
        gameBrowser.setCompletionTimeout(requestTimeout);
    }

    @Override
    public String getDiscoveryServiceEndpoint() {
        return discoveryServiceEndpoint;
    }

    @Override
    public long getRefreshInterval() {
        return refreshInterval;
    }

    public long getRequestTimeout() {
        return requestTimeout;
    }

    public void setRequestTimeout(long requestTimeout) {
        this.requestTimeout = requestTimeout;
    }

    @Override
    public OpenArenaDiscoveryRecord getLatestDiscoveryResults() {
        long current = System.currentTimeMillis();
        synchronized (lock){
            if (cache == null || cache.getLastQueried() == -1 || current - cache.getLastQueried() > refreshInterval){
                updateGameBrowserProperties();
                refreshDiscovery();
            }
            return cache;
        }
    }

    private void refreshDiscovery() {
        OpenArenaDiscoveryRecord result = new OpenArenaDiscoveryRecord(discoveryServiceEndpoint);
        result.setDirectoryName(getDiscoveryServiceEndpoint());
        Function<GameServer, OpenArenaServerRecord> converter = new Function<GameServer, OpenArenaServerRecord>() {
            @Override
            public OpenArenaServerRecord apply(GameServer input) {
                OpenArenaServerRecord result = new OpenArenaServerRecord();
                result.setPlayersPresent(input.getPlayersPresent());
                result.setDisplayName(input.getDisplayName());
                result.setMap(input.getMap());
                result.setAddress(input.getAddress());
                result.setStatus(OpenArenaServerRecord.ServerStatus.UP);
                result.setQueryTimestamp(System.currentTimeMillis());
                result.setGameType(input.getGameType());
                result.setPing(input.getRequestDuration());
                result.setRetries(1);
                result.setSlotsAvailable(input.getSlotsAvailable());
                return result;
            }
        };

        try {
            synchronized (lock){
                gameBrowser.refresh().parallelStream().forEach(server -> {
                    result.getRecords().add(converter.apply(server));
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        result.setLastQueried(System.currentTimeMillis());
        cache = result;
    }
}
