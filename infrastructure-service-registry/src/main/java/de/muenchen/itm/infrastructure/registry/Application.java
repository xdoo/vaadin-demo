package de.muenchen.itm.infrastructure.registry;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * The Eureka Server for all Services.
 * Configure it in resources/application.properties.
 * (Port and Location can be found there)
 *
 * @author p.mueller maximilian.zollbrecht
 */
@SpringBootApplication
@EnableEurekaServer
public class Application {

    /**
     * Start the Eureka-Server.
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
    }
}