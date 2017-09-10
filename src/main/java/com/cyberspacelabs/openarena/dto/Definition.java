package com.cyberspacelabs.openarena.dto;

import java.util.UUID;

/**
 * Created by mike on 08.09.17.
 */
public class Definition {
    private UUID id;
    private String label;
    private boolean _default;

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

    public boolean is_default() {
        return _default;
    }

    public void set_default(boolean _default) {
        this._default = _default;
    }
}
