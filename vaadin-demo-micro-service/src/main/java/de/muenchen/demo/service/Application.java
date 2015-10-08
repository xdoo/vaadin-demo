package de.muenchen.demo.service;

import de.muenchen.eventbus.EventBus;
import de.muenchen.security.configurator.AuthenticationConfiguratorAdapter;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

@Configuration
@ComponentScan(basePackages = {"de.muenchen.demo.service", "de.muenchen.auditing", "de.muenchen.security"})
@EntityScan(basePackages = {"de.muenchen.demo.service", "de.muenchen.auditing", "de.muenchen.security"})
@EnableJpaRepositories(basePackages = {"de.muenchen.demo.service", "de.muenchen.auditing", "de.muenchen.security"})
@EnableAutoConfiguration
@EnableEurekaClient
@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL)
public class Application {
    private static final Logger LOG
            = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public EventBus eventBus() {
        return new EventBus();
    }


//    Dozer
    @Bean
    public Mapper dozerBeanMapper() {
        return new DozerBeanMapper();
    }

//    Security

    @Bean
    public GlobalAuthenticationConfigurerAdapter configurationAdapter1(){
        return AuthenticationConfiguratorAdapter.findAdapter("JDBC_Authentication");
    }

    @Bean
    public GlobalAuthenticationConfigurerAdapter configurationAdapter2(){
        return AuthenticationConfiguratorAdapter.findAdapter("LDAP_Authentication");
    }
}
