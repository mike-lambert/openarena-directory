package com.cyberspacelabs.openarena.web.dto;

/**
 {
 "code": "RU",
 "domain": "Russia/Novosibirsk/Novosibirsk"
 }
 */
public class Location {
    private String code;
    private String domain;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }
}
