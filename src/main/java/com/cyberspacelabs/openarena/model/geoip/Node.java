package com.cyberspacelabs.openarena.model.geoip;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ValueNode.class)
})
public class Node {
    private String name;
    private Node nextNode;

    public Node(){
        name = "";
    }

    public Node(String name){
        setName(name);
    }

    public Node(String name, Node next){
        this(name);
        setNextNode(next);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Node getNextNode() {
        return nextNode;
    }

    public void setNextNode(Node nextNode) {
        this.nextNode = nextNode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Node node = (Node) o;

        if (!getName().equals(node.getName()))
            return false;
        if (nextNode != null && node.getNextNode() != null)
            return nextNode.equals(node.getNextNode());
        return true;
    }

    @Override
    public int hashCode() {
        int result = getName().hashCode();
        result = 31 * result + (getNextNode() != null ? getNextNode().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return name + (nextNode != null ? "/" + nextNode : "");
    }
}
