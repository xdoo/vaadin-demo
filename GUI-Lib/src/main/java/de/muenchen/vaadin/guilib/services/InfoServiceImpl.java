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

    @Value(value = "${service.buerger.url}")
    private String url;
    @Value(value = "${service.info.id.eureka}")
    private String eurekaId;

    @Override
    public URI getBaseUri() {
        try {
            url = discoveryClient.getNextServerFromEureka(eurekaId, false).getHomePageUrl();
        } catch (RuntimeException e) {
            // Exception occurs if no eureka server was found.
            // service.info.url will be used
        }
        return URI.create(url);
    }
}
