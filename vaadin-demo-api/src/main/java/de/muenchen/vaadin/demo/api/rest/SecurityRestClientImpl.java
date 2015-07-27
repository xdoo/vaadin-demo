package de.muenchen.vaadin.demo.api.rest;

import de.muenchen.vaadin.demo.api.domain.Principal;
import de.muenchen.vaadin.demo.api.domain.ServiceInfo;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author claus.straube
 */
@Service
public class SecurityRestClientImpl implements SecurityRestClient {

    @Autowired private ServiceInfoRestClient serviceInfoClient;
    
    @Override
    public Optional<Principal> getPrincipal(RestTemplate template) {
        ServiceInfo serviceInfo = serviceInfoClient.getServiceInfo();
        String URL= "http://localhost:8080/principal";       
        Principal principal = template.getForEntity(URL, Principal.class).getBody();
        return Optional.of(principal);
    }
    
}
