package de.muenchen.demo.service;

import de.muenchen.service.security.CustomResourceUserAuthenticationConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.resource.JwtAccessTokenConverterConfigurer;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

/**
 * Application class for starting the micro-service.
 */
@Configuration
@ComponentScan(basePackages = {"de.muenchen.demo.service", "de.muenchen.service", "de.muenchen.auditing"})
@EntityScan(basePackages = {"de.muenchen.demo.service", "de.muenchen.service", "de.muenchen.auditing"})
@EnableJpaRepositories(basePackages = {"de.muenchen.demo.service", "de.muenchen.service", "de.muenchen.auditing"})
@EnableAutoConfiguration
@EnableEurekaClient //TODO not generated
@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL)
@EnableResourceServer
// rev: Warum wird hier nicht die Annotation @SpringBootApplication benutzt?
public class MicroServiceApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(MicroServiceApplication.class, args);
    }


    @Configuration
    protected static class CustomJwtAccessTokenConverterConfigurer implements JwtAccessTokenConverterConfigurer {

        @Override
        public void configure(JwtAccessTokenConverter converter) {
            DefaultAccessTokenConverter accessTokenConverter = new DefaultAccessTokenConverter();
            accessTokenConverter.setUserTokenConverter(new CustomResourceUserAuthenticationConverter());
            converter.setAccessTokenConverter(accessTokenConverter);
        }
    }
}
