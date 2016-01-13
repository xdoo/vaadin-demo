package de.muenchen.vaadin.demo.apilib.rest;

import de.muenchen.vaadin.demo.apilib.domain.Principal;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

/**
 *
 * @author claus.straube
 */
public interface SecurityRestClient {
    
    public Optional<Principal> getPrincipal(RestTemplate template);

    public void logout(RestTemplate template);
    
}
