package de.muenchen.vaadin.demo.api.rest;

import de.muenchen.vaadin.demo.api.domain.Principal;
import de.muenchen.vaadin.demo.api.domain.ServiceInfo;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

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
        ServiceInfo serviceInfo = serviceInfoClient.getServiceInfo();
        String URL= "http://localhost:8080/principal";

        Principal principal = null;
        try {
            principal= template.getForEntity(URL, Principal.class).getBody();
        } catch (RestClientException e) {
            LOG.debug("HTTP Response Error bei Login: "+ e.getMessage());
        }
        return Optional.ofNullable(principal);
    }
    
}
