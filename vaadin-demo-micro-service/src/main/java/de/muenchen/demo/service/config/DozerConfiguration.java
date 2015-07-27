package de.muenchen.demo.service.config;

import org.dozer.DozerBeanMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author claus.straube
 */
@Configuration
public class DozerConfiguration {
    
    @Bean
    public DozerBeanMapper dozerBean() {
        DozerBeanMapper dozer = new DozerBeanMapper();
        return dozer;
    }
    
}
