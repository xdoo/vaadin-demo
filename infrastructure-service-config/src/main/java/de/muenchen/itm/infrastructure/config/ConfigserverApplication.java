package de.muenchen.itm.infrastructure.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Configuration;


/**
 * Provides a ConfigServer that registers in Eureka.
 * @author p.mueller maximilian.zollbrecht
 */
@Configuration
@EnableAutoConfiguration
@EnableEurekaClient
@EnableConfigServer
public class ConfigserverApplication {
    /**
     * Start the Configuration Server.
     * @param args 
     */
    public static void main(String[] args) {
        System.setProperty("https.proxyHost","10.158.0.79");
        System.setProperty("https.proxyPort","80");

        SpringApplication.run(ConfigserverApplication.class, args);
    }
}

