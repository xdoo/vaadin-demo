package de.muenchen.vaadin.demo.apilib.rest;

import de.muenchen.vaadin.demo.apilib.domain.Principal;
import de.muenchen.vaadin.demo.apilib.domain.ServiceInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

/**
 *
 * @author claus.straube
 */
@Service
public class SecurityRestClientImpl implements SecurityRestClient {

    private static final Logger LOG = LoggerFactory.getLogger(SecurityRestClientImpl.class);

    @Value("${service.principal.url}")
    private String principalURL;
    
    @Override
    public Optional<Principal> getPrincipal(RestTemplate template) {
        Principal principal = null;
        try {
            principal= template.getForEntity(principalURL, Principal.class).getBody();
        } catch (RestClientException e) {
            LOG.debug("HTTP Response Error bei Login: "+ e.getMessage());
        }
        return Optional.ofNullable(principal);
    }
    
}
