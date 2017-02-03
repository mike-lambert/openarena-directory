package com.cyberspacelabs.openarena.service.impl;

import com.cyberspacelabs.openarena.service.QStatDiscoveryService;
import com.cyberspacelabs.openarena.service.QStatDiscoveryServiceFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@Service
@PropertySources({
        @PropertySource(value = "application.properties", ignoreResourceNotFound = true),
        @PropertySource("classpath:application.properties")
})
public class QStatDiscoveryServiceFactoryImpl implements QStatDiscoveryServiceFactory {
    protected static class QStatDiscoveryServiceFactoryConfiguration {
        public boolean wine;
        public String qstat;
        public List<QStatDiscoveryServiceConfiguration> services;

        public QStatDiscoveryServiceFactoryConfiguration(){
            services = new ArrayList<>();
        }
    }
    protected static class QStatDiscoveryServiceConfiguration {
        public String server;
        public String report;
        public long expires;
        public boolean useNativeClient;
        public long nativeClientTimeout;
    }
    @Value("${qstat.factory.config:'classpath:discovery.json'}")
    private String discoveryServicesConfigurationLocation;
    private Set<QStatDiscoveryService> instances;
    private Object lock;
    private ObjectMapper json;

    public QStatDiscoveryServiceFactoryImpl(){
        lock = new Object();
        json = new ObjectMapper();
        discoveryServicesConfigurationLocation = "classpath:discovery.json";
    }

    @Override
    public Set<QStatDiscoveryService> instantiate() {
        synchronized (lock){
            if (instances == null){
                createInstances();
            }
        }
        return instances;
    }

    @PostConstruct
    private void createInstances() {
        Set<QStatDiscoveryService> result = new CopyOnWriteArraySet<>();
        try {
            QStatDiscoveryServiceFactoryConfiguration config;
            if (discoveryServicesConfigurationLocation.startsWith("classpath:")){
                config = json.readValue(this.getClass().getClassLoader().getResourceAsStream(discoveryServicesConfigurationLocation.replace("classpath:", "")), QStatDiscoveryServiceFactoryConfiguration.class);
            } else {
                config = json.readValue(new File(discoveryServicesConfigurationLocation), QStatDiscoveryServiceFactoryConfiguration.class);
            }
            config.services.forEach(serviceConfig -> result.add(configureInstance(serviceConfig, config)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        instances = Collections.unmodifiableSet(result);
    }

    private QStatDiscoveryService configureInstance(QStatDiscoveryServiceConfiguration serviceConfig, QStatDiscoveryServiceFactoryConfiguration factoryConfig) {
        QStatDiscoveryService result = null;
        if (!serviceConfig.useNativeClient){
            QStatDiscoveryServiceImpl wrapped = new QStatDiscoveryServiceImpl(new QStatConversionServiceImpl());
            wrapped.setUseWine(factoryConfig.wine);
            wrapped.setQstatBinaryLocation(factoryConfig.qstat);
            wrapped.setQstatXmlReport(serviceConfig.report);
            wrapped.setDiscoveryServiceEndpoint(serviceConfig.server);
            wrapped.setRefreshInterval(serviceConfig.expires);
            result = wrapped;
        } else {
            NativeDiscoveryServiceImpl nativeClient = new NativeDiscoveryServiceImpl();
            nativeClient.setDiscoveryServiceEndpoint(serviceConfig.server);
            nativeClient.setRefreshInterval(serviceConfig.expires);
            nativeClient.setRequestTimeout(serviceConfig.nativeClientTimeout);
            nativeClient.updateGameBrowserProperties();
            result = nativeClient;
        }
        return result;
    }

    public String getDiscoveryServicesConfigurationLocation() {
        return discoveryServicesConfigurationLocation;
    }

    public void setDiscoveryServicesConfigurationLocation(String discoveryServicesConfigurationLocation) {
        this.discoveryServicesConfigurationLocation = discoveryServicesConfigurationLocation;
    }
}
