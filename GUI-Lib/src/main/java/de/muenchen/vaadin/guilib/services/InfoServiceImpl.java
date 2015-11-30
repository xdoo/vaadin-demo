package de.muenchen.vaadin.guilib.services;

import com.netflix.discovery.DiscoveryClient;
import de.muenchen.vaadin.demo.apilib.domain.DomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.net.URI;

/**
 * @author claus.straube
 */
@Service
public class InfoServiceImpl implements InfoService {

    @Autowired
    DiscoveryClient discoveryClient;

    @Autowired
    private Environment environment;

    @Override
    public URI getBaseUri(DomainService service) {

        String eurekaId = environment.getProperty(service.getClientId()+".info.id.eureka");

        String url;
        try {
            url = discoveryClient.getNextServerFromEureka(eurekaId, false).getHomePageUrl();
        } catch (RuntimeException e) {
            url = environment.getProperty(service.getClientId()+".url");
        }
        return URI.create(url);
    }
}
