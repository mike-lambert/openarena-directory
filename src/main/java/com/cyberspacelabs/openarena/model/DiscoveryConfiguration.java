package com.cyberspacelabs.openarena.model;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by mike on 07.09.17.
 */
public class DiscoveryConfiguration {
    private String baseURL;
    private List<QueryDefinition> definitions;

    public DiscoveryConfiguration(){
        definitions = new CopyOnWriteArrayList<>();
    }

    public DiscoveryConfiguration(String url){
        this();
        baseURL = url;
    }

    public String getBaseURL() {
        return baseURL;
    }

    public void setBaseURL(String baseURL) {
        this.baseURL = baseURL;
    }

    public List<QueryDefinition> getDefinitions() {
        return definitions;
    }

    public void setDefinitions(List<QueryDefinition> definitions) {
        this.definitions = definitions;
    }
}
