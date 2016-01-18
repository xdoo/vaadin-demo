package de.muenchen.vaadin.guilib.services;

import com.netflix.discovery.DiscoveryClient;
import de.muenchen.vaadin.demo.apilib.domain.DomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;

/**
 * @author claus.straube
 */
@Service
public class InfoServiceImpl implements InfoService {

    @Autowired
    DiscoveryClient discoveryClient;

    @Value("service.info.url")
    private String url;

    @Override
    public URI getBaseUri(String service) {
        try {
            url = discoveryClient.getNextServerFromEureka(service, false).getHomePageUrl();
        } catch (RuntimeException e) {
            // Exception occurs if no eureka server was found.
            // service.info.url will be used
        }
        return URI.create(url);
    }
}
