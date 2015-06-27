package com.catify.vaadin.demo.api.rest;

import com.catify.vaadin.demo.api.domain.Principal;
import java.util.Optional;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author claus.straube
 */
public interface SecurityRestClient {
    
    public Optional<Principal> getPrincipal(RestTemplate template);
    
}
