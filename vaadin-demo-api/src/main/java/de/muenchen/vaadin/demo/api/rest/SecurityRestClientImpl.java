package de.muenchen.vaadin.demo.api.rest;

import de.muenchen.vaadin.demo.api.domain.Principal;
import de.muenchen.vaadin.demo.api.domain.ServiceInfo;
import com.google.gwt.thirdparty.guava.common.collect.Lists;
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
//        template.getForEntity(null, null);
        Principal principal = new Principal();
        principal.setUsername("hans");
        principal.setRoles(Lists.newArrayList("USER", "ADMIN"));
        principal.setPermissions(Lists.newArrayList("FOO", "BAR"));
        return Optional.of(principal);
    }
    
}
