package com.cyberspacelabs.openarena.model.geoip;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ValueNode<T> extends Node {
    @JsonTypeInfo( use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.WRAPPER_OBJECT )
    private T value;

    public ValueNode(){
        super();
    }

    public ValueNode(String name, T value){
        super();
        setName(name);
        setValue(value);
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    @Override
    public void setNextNode(Node nextNode) {
        if (nextNode != null) {
            throw new IllegalStateException("Leaf node doesn't allow chaining");
        }
    }

    @Override
    public String toString() {
        return super.toString() + " -> \"" + value + "\"";
    }

}
