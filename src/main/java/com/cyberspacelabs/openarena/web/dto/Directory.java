package com.cyberspacelabs.openarena.web.dto;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 {
 "updated": "2016-12-09 01:00:33 +07:00",
 "retrieved": "2016-12-09 09:36:53 +07:00",
 "servers": [
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
 },
 {
 "type": "QUAKE3",
 "ping": 127,
 "load": "16/20",
 "location":{
 "code": "RU",
 "domain": "Russia/Novosibirsk/Novosibirsk"
 },
 "address": "cyberspacelabs.ru:27961",
 "mode": "CTF",
 "map": "q3dm1"
 },
 {
 "type": "OPENARENA",
 "ping": 340,
 "load": "11/20",
 "location":{
 "code": "US",
 "domain": "United States/New Jersey/Trenton"
 },
 "address": "cyberspacelabs.ru:27962",
 "mode": "DM",
 "map": "oadm7"
 }
 ]
 }
 */
public class Directory {
    private String updated;
    private String retrieved;
    private List<Server> servers;

    public Directory(){
        retrieved = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss XXX").format(new Date());
        servers = new ArrayList<>();
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getRetrieved() {
        return retrieved;
    }

    public void setRetrieved(String retrieved) {
        this.retrieved = retrieved;
    }

    public List<Server> getServers() {
        return servers;
    }

    public void setServers(List<Server> servers) {
        this.servers = servers;
    }
}
