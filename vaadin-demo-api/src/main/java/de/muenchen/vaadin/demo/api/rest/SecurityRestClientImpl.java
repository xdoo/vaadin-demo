package de.muenchen.vaadin.demo.api.rest;

import de.muenchen.vaadin.demo.api.domain.Principal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

/**
 *
 * @author claus.straube
 */
@Service
public class SecurityRestClientImpl implements SecurityRestClient {

    private static final Logger LOG = LoggerFactory.getLogger(SecurityRestClientImpl.class);

    @Autowired private ServiceInfoRestClient serviceInfoClient;
    
    @Override
    public Optional<Principal> getPrincipal(RestTemplate template) {
        /*TODOServiceInfo serviceInfo = serviceInfoClient.getServiceInfo();
        String URL= "http://localhost:8080/principal";

        Principal principal = null;
        try {
            principal= template.getForEntity(URL, Principal.class).getBody();
        } catch (RestClientException e) {
            LOG.debug("HTTP Response Error bei Login: "+ e.getMessage());
        }*/
        return Optional.of(new Principal());
    }
    
}
