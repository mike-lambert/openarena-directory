package com.cyberspacelabs.openarena.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class OpenArenaDiscoveryRecord {
    public static final int PORT_OPENARENA_MASTER = 27950;
    public static final int PORT_OPENARENA_HOST = 27960;
    private String serverType;
    private String serverHost;
    private int serverPort;
    private Set<OpenArenaServerRecord> records;
    private long lastQueried;
    private String directoryName;

    public OpenArenaDiscoveryRecord(){
        setServerType("oam");
        setServerHost("255.255.255.255");
        setServerPort(PORT_OPENARENA_MASTER);
        records = new CopyOnWriteArraySet<>();
        lastQueried = -1;
    }

    public OpenArenaDiscoveryRecord(String host, int port){
        this();
        setServerHost(host);
        setServerPort(port);
    }

    public OpenArenaDiscoveryRecord(String address){
        this();
        if (address.contains(":")){
            String[] components = address.split(":", 2);
            setServerHost(components[0]);
            setServerPort(Integer.parseInt(components[1]));
        } else {
            setServerHost(address);
        }
    }

    public String getServerType() {
        return serverType;
    }

    public void setServerType(String serverType) {
        this.serverType = serverType;
    }

    public String getServerHost() {
        return serverHost;
    }

    public void setServerHost(String serverHost) {
        this.serverHost = serverHost;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public Set<OpenArenaServerRecord> getRecords() {
        return records;
    }

    public long getLastQueried() {
        return lastQueried;
    }

    public String getDirectoryName() {
        return directoryName;
    }

    public void setDirectoryName(String directoryName) {
        this.directoryName = directoryName;
    }

    public void setLastQueried(long lastQueried) {
        this.lastQueried = lastQueried;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OpenArenaDiscoveryRecord that = (OpenArenaDiscoveryRecord) o;

        if (getServerPort() != that.getServerPort()) return false;
        if (!getServerType().equals(that.getServerType())) return false;
        return getServerHost().equals(that.getServerHost());

    }

    @Override
    public int hashCode() {
        int result = getServerType().hashCode();
        result = 31 * result + getServerHost().hashCode();
        result = 31 * result + getServerPort();
        return result;
    }

    @Override
    public String toString() {
        return getServerType().toUpperCase() + " " +getServerHost() + ":" + getServerPort() + " "
                + (getLastQueried() == -1 ? "NEVER" : new SimpleDateFormat("yyyy-MM-dd HH:mm:ss XXX").format(new Date(getLastQueried())));
    }
}
