package com.cyberspacelabs.openarena.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenArenaServerRecord {
    public enum ServerStatus {
        UNKNOWN,
        TIMEOUT,
        UP
    }
    private long queryTimestamp;
    private String address;
    private String serverType;
    private ServerStatus status;
    private String map;
    private int playersPresent;
    private int slotsAvailable;
    private long ping;
    private int retries;
    private String displayName;
    private String gameType;

    public OpenArenaServerRecord(){
        setQueryTimestamp(-1);
        setAddress("0.0.0.0:0");
        setServerType("OPENARENA");
        setStatus(ServerStatus.UNKNOWN);
        setMap("");
        setPlayersPresent(-1);
        setSlotsAvailable(-1);
        setPing(-1);
        setRetries(-1);
        setDisplayName("");
        setGameType("");
    }

    public OpenArenaServerRecord(String address){
        this();
        setAddress(address);
    }

    public long getQueryTimestamp() {
        return queryTimestamp;
    }

    public void setQueryTimestamp(long queryTimestamp) {
        this.queryTimestamp = queryTimestamp;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getServerType() {
        return serverType;
    }

    public void setServerType(String serverType) {
        this.serverType = serverType;
    }

    public ServerStatus getStatus() {
        return status;
    }

    public void setStatus(ServerStatus status) {
        this.status = status;
    }

    public String getMap() {
        return map;
    }

    public void setMap(String map) {
        this.map = map;
    }

    public int getPlayersPresent() {
        return playersPresent;
    }

    public void setPlayersPresent(int playersPresent) {
        this.playersPresent = playersPresent;
    }

    public int getSlotsAvailable() {
        return slotsAvailable;
    }

    public void setSlotsAvailable(int slotsAvailable) {
        this.slotsAvailable = slotsAvailable;
    }

    public long getPing() {
        return ping;
    }

    public void setPing(long ping) {
        this.ping = ping;
    }

    public int getRetries() {
        return retries;
    }

    public void setRetries(int retries) {
        this.retries = retries;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getGameType() {
        return gameType;
    }

    public void setGameType(String gameType) {
        this.gameType = gameType;
    }

    public boolean isValid() {
        return getStatus() != ServerStatus.UNKNOWN && getPlayersPresent() >= 0 && getSlotsAvailable() > 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OpenArenaServerRecord that = (OpenArenaServerRecord) o;

        if (!getAddress().equals(that.getAddress())) return false;
        if (!getServerType().equals(that.getServerType())) return false;
        return getGameType() != null ? getGameType().equals(that.getGameType()) : that.getGameType() == null;

    }

    @Override
    public int hashCode() {
        int result = getAddress().hashCode();
        result = 31 * result + getServerType().hashCode();
        result = 31 * result + (getGameType() != null ? getGameType().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return getServerType() + ": " + getAddress() + " \"" + getGameType() + "\" " + getStatus()
                + (isValid() && ServerStatus.UP.equals(getStatus()) ? " " + getMap() + ";" + getPlayersPresent() + "/" + getSlotsAvailable() + " (" + getPing() + " ms)" : "");
    }
}
