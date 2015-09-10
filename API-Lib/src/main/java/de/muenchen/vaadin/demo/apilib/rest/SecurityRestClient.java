package de.muenchen.vaadin.demo.apilib.rest;

import java.util.Optional;

import de.muenchen.vaadin.demo.apilib.domain.Principal;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author claus.straube
 */
public interface SecurityRestClient {
    
    public Optional<Principal> getPrincipal(RestTemplate template);
    
}
