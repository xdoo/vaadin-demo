package de.muenchen.vaadin.demo.configcloud;

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
public class ConfigServerApplication {
    /**
     * Start the Configuration Server.
     * @param args 
     */
    public static void main(String[] args) {
        SpringApplication.run(ConfigServerApplication.class, args);
    }
}
