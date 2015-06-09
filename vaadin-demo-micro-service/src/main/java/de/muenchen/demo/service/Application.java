package de.muenchen.demo.service;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.hateoas.config.EnableHypermediaSupport;

/**
 *
 * @author claus.straube
 */
@SpringBootApplication
@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL)
public class Application {

    public static void main(String[] args) throws Exception {
        new SpringApplicationBuilder(Application.class).run(args);
    }
    
}
