package com.cyberspacelabs.openarena.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mike on 01.09.17.
 */

public class QueryRequest {
    private long maxPing;
    private String game;
    private String sort;
    private int limit;
    private List<Endpoint> pinnedServers;

    public QueryRequest(){
        pinnedServers = new ArrayList<>();
    }

    public long getMaxPing() {
        return maxPing;
    }

    public String getGame() {
        return game;
    }

    public String getSort() {
        return sort;
    }

    public int getLimit() {
        return limit;
    }

    public void setMaxPing(long maxPing) {
        this.maxPing = maxPing;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public List<Endpoint> getPinnedServers() {
        return pinnedServers;
    }

    public void setPinnedServers(List<Endpoint> pinnedServers) {
        this.pinnedServers = pinnedServers;
    }
}
