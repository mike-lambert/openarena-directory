//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.12.08 at 01:53:29 PM KRAT 
//


package com.cyberspacelabs.openarena.model.qstat;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="server" maxOccurs="unbounded">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="hostname" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="gametype" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="map" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="numplayers" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *                   &lt;element name="maxplayers" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *                   &lt;element name="ping" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *                   &lt;element name="retries" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *                 &lt;/sequence>
 *                 &lt;attribute name="type" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="address" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="status" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "server"
})
@XmlRootElement(name = "qstat")
public class Qstat {

    @XmlElement(required = true)
    protected List<Qstat.Server> server;

    /**
     * Gets the value of the server property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the server property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getServer().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Qstat.Server }
     * 
     * 
     */
    public List<Qstat.Server> getServer() {
        if (server == null) {
            server = new ArrayList<Qstat.Server>();
        }
        return this.server;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="hostname" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="gametype" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="map" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="numplayers" type="{http://www.w3.org/2001/XMLSchema}int"/>
     *         &lt;element name="maxplayers" type="{http://www.w3.org/2001/XMLSchema}int"/>
     *         &lt;element name="ping" type="{http://www.w3.org/2001/XMLSchema}int"/>
     *         &lt;element name="retries" type="{http://www.w3.org/2001/XMLSchema}int"/>
     *       &lt;/sequence>
     *       &lt;attribute name="type" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="address" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="status" type="{http://www.w3.org/2001/XMLSchema}string" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "hostname",
        "name",
        "gametype",
        "map",
        "numplayers",
        "maxplayers",
        "ping",
        "retries"
    })
    public static class Server {

        @XmlElement(required = true)
        protected String hostname;
        @XmlElement(required = true)
        protected String name;
        @XmlElement(required = true)
        protected String gametype;
        @XmlElement(required = true)
        protected String map;
        protected int numplayers;
        protected int maxplayers;
        protected int ping;
        protected int retries;
        @XmlAttribute(name = "type")
        protected String type;
        @XmlAttribute(name = "address")
        protected String address;
        @XmlAttribute(name = "status")
        protected String status;

        /**
         * Gets the value of the hostname property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getHostname() {
            return hostname;
        }

        /**
         * Sets the value of the hostname property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setHostname(String value) {
            this.hostname = value;
        }

        /**
         * Gets the value of the name property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getName() {
            return name;
        }

        /**
         * Sets the value of the name property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setName(String value) {
            this.name = value;
        }

        /**
         * Gets the value of the gametype property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getGametype() {
            return gametype;
        }

        /**
         * Sets the value of the gametype property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setGametype(String value) {
            this.gametype = value;
        }

        /**
         * Gets the value of the map property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getMap() {
            return map;
        }

        /**
         * Sets the value of the map property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setMap(String value) {
            this.map = value;
        }

        /**
         * Gets the value of the numplayers property.
         * 
         */
        public int getNumplayers() {
            return numplayers;
        }

        /**
         * Sets the value of the numplayers property.
         * 
         */
        public void setNumplayers(int value) {
            this.numplayers = value;
        }

        /**
         * Gets the value of the maxplayers property.
         * 
         */
        public int getMaxplayers() {
            return maxplayers;
        }

        /**
         * Sets the value of the maxplayers property.
         * 
         */
        public void setMaxplayers(int value) {
            this.maxplayers = value;
        }

        /**
         * Gets the value of the ping property.
         * 
         */
        public int getPing() {
            return ping;
        }

        /**
         * Sets the value of the ping property.
         * 
         */
        public void setPing(int value) {
            this.ping = value;
        }

        /**
         * Gets the value of the retries property.
         * 
         */
        public int getRetries() {
            return retries;
        }

        /**
         * Sets the value of the retries property.
         * 
         */
        public void setRetries(int value) {
            this.retries = value;
        }

        /**
         * Gets the value of the type property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getType() {
            return type;
        }

        /**
         * Sets the value of the type property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setType(String value) {
            this.type = value;
        }

        /**
         * Gets the value of the address property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getAddress() {
            return address;
        }

        /**
         * Sets the value of the address property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setAddress(String value) {
            this.address = value;
        }

        /**
         * Gets the value of the status property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getStatus() {
            return status;
        }

        /**
         * Sets the value of the status property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setStatus(String value) {
            this.status = value;
        }

    }

}
