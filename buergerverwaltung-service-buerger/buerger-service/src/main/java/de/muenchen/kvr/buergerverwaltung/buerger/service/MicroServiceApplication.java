package de.muenchen.kvr.buergerverwaltung.buerger.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

import de.muenchen.service.security.OAuth2RestTemplateConfig;

/**
 * Application class for starting the micro-service.
 */
@Configuration
@ComponentScan(basePackages = {"de.muenchen.kvr.buergerverwaltung.buerger.service", "de.muenchen.service", "de.muenchen.auditing"})
@EntityScan(basePackages = {"de.muenchen.kvr.buergerverwaltung.buerger.service", "de.muenchen.service", "de.muenchen.auditing"})
@EnableJpaRepositories(basePackages = {"de.muenchen.kvr.buergerverwaltung.buerger.service", "de.muenchen.service", "de.muenchen.auditing"})
@EnableAutoConfiguration
@EnableEurekaClient
@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL)
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@Import(OAuth2RestTemplateConfig.class)
public class MicroServiceApplication {
	
	public static void main(String[] args) throws Exception {
        SpringApplication.run(MicroServiceApplication.class, args);
    }
    
}
