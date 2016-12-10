package com.cyberspacelabs.openarena.web.dto;

/**
 {
 "type": "OPENARENA",
 "ping": 32,
 "load": "4/32",
 "location":{
 "code": "RU",
 "domain": "Russia/Novosibirsk/Novosibirsk"
 },
 "address": "cyberspacelabs.ru:27960",
 "mode": "FFA",
 "map": "oasago2"
 }
 */
public class ServerDTO {
    private String type;
    private long ping;
    private String load;
    private String address;
    private String mode;
    private String map;
    private LocationDTO location;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getPing() {
        return ping;
    }

    public void setPing(long ping) {
        this.ping = ping;
    }

    public String getLoad() {
        return load;
    }

    public void setLoad(String load) {
        this.load = load;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getMap() {
        return map;
    }

    public void setMap(String map) {
        this.map = map;
    }

    public LocationDTO getLocation() {
        return location;
    }

    public void setLocation(LocationDTO location) {
        this.location = location;
    }
}
