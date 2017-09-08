package com.cyberspacelabs.openarena.model;

import java.util.UUID;

/**
 * Created by mike on 07.09.17.
 */
public class QueryDefinition {
    private UUID id;
    private String label;
    private String master;
    private String query;
    private QueryRequest params;
    private boolean defaultDefintion;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getMaster() {
        return master;
    }

    public void setMaster(String master) {
        this.master = master;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public QueryRequest getParams() {
        return params;
    }

    public void setParams(QueryRequest params) {
        this.params = params;
    }

    public boolean isDefaultDefintion() {
        return defaultDefintion;
    }

    public void setDefaultDefintion(boolean defaultDefintion) {
        this.defaultDefintion = defaultDefintion;
    }
}
