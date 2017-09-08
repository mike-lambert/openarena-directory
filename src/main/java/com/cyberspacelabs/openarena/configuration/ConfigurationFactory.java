package com.cyberspacelabs.openarena.configuration;

import com.cyberspacelabs.openarena.model.DiscoveryConfiguration;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.io.File;

/**
 * Created by mike on 10.12.16.
 */
@PropertySources({
        @PropertySource(value = "application.properties", ignoreResourceNotFound = true),
        @PropertySource("classpath:application.properties")
})
@Configuration
@EnableWebMvc
public class ConfigurationFactory {
    @Value("${discovery.config:'classpath:discovery.json'}")
    private static String discoveryConfigurationLocation;

    @Bean
    public static PropertySourcesPlaceholderConfigurer properties() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public static DiscoveryConfiguration discoveryConfiguration() throws Exception {
        ObjectMapper json = new ObjectMapper();
        DiscoveryConfiguration config = null;
        if (discoveryConfigurationLocation.startsWith("classpath:")){
            config = json.readValue(ConfigurationFactory.class.getClassLoader().getResourceAsStream(discoveryConfigurationLocation.replace("classpath:", "")), DiscoveryConfiguration.class);
        } else {
            config = json.readValue(new File(discoveryConfigurationLocation), DiscoveryConfiguration.class);
        }
        return config;
    }
}
