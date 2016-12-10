package com.cyberspacelabs.openarena.model.geoip;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Path<V> {
    private Node rootNode;
    private String name;
    private String countryCode;

    public Path(){
        setName("");
    }

    public Path(String name){
        setName(name);
    }

    public Path(String name, Node root){
        this(name);
        setRootNode(root);
    }

    public Path(String name, String country, String region, String city, String zip, String label, V value){
        this(name, new Node(country));
        this.getRootNode().setNextNode(new Node(region));
        this.getRootNode().getNextNode().setNextNode(new Node(city));
        this.getRootNode().getNextNode().getNextNode().setNextNode(new Node(zip));
        this.getRootNode().getNextNode().getNextNode().getNextNode().setNextNode(new ValueNode<V>(label, value));
    }

    public Node getRootNode() {
        return rootNode;
    }

    public void setRootNode(Node rootNode) {
        this.rootNode = rootNode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Path path = (Path) o;

        if (getRootNode() != null ? !getRootNode().equals(path.getRootNode()) : path.getRootNode() != null)
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = getRootNode() != null ? getRootNode().hashCode() : 0;
        return result;
    }

    @Override
    public String toString() {
        return "Path \"" + name+ "\":[/" + rootNode +  "]";
    }

    public String getCountry(){
        return getRootNode().getName();
    }

    public String getRegion() {
        return getRootNode().getNextNode().getName();
    }

    public String getCity(){
        return getRootNode().getNextNode().getNextNode().getName();
    }

    public String getZipCode() {
        return getRootNode().getNextNode().getNextNode().getNextNode().getName();
    }

    public <T> T getValue(Class<T> type){
        return ((ValueNode<T>)getRootNode().getNextNode().getNextNode().getNextNode().getNextNode()).getValue();
    }

    public <V> V getValue(){
        return ((ValueNode<V>)getRootNode().getNextNode().getNextNode().getNextNode().getNextNode()).getValue();
    }

    public String getLabel(){
        return getRootNode().getNextNode().getNextNode().getNextNode().getNextNode().getName();
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getPath(){
        return    "/" + getRootNode().getName()
                + "/" + getRootNode().getNextNode().getName()
                + "/" + getRootNode().getNextNode().getNextNode().getName()
                + "/" + getRootNode().getNextNode().getNextNode().getNextNode().getName();
    }
}
