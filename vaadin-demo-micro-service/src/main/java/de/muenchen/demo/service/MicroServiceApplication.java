package de.muenchen.demo.service;

import de.muenchen.security.configurator.AuthenticationConfiguratorAdapter;
import de.muenchen.security.configurator.JDBCAuthenticationConfigurator;
import de.muenchen.security.configurator.LDAPAuthenticationConfigurator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

/**
 * Application class for starting the micro-service.
 */
@Configuration
@ComponentScan(basePackages = {"de.muenchen.demo.service", "de.muenchen.service", "de.muenchen.auditing", "de.muenchen.security"})
@EntityScan(basePackages = {"de.muenchen.demo.service", "de.muenchen.service", "de.muenchen.auditing", "de.muenchen.security"})
@EnableJpaRepositories(basePackages = {"de.muenchen.demo.service", "de.muenchen.service", "de.muenchen.auditing", "de.muenchen.security"})
@EnableAutoConfiguration
@EnableEurekaClient //TODO not generated
@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL)
@EnableResourceServer
public class MicroServiceApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(MicroServiceApplication.class, args);
    }


    // Database-Security
    @Bean
    public GlobalAuthenticationConfigurerAdapter configurationAdapter1(){
        return new JDBCAuthenticationConfigurator();
    }

    // LDAP-Security
    @Bean
    public GlobalAuthenticationConfigurerAdapter configurationAdapter2(){
        return new LDAPAuthenticationConfigurator();
    }
}
