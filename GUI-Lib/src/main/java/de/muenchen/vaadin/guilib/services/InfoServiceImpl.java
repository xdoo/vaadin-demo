package de.muenchen.vaadin.guilib.services;

import com.netflix.discovery.DiscoveryClient;
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
    public URI getBaseUri(String serviceName) {

        String eurekaId = environment.getProperty(serviceName+".info.id.eureka");

        String url;
        try {
            url = discoveryClient.getNextServerFromEureka(eurekaId, false).getHomePageUrl();
        } catch (RuntimeException e) {
            url = environment.getProperty(serviceName+".url");
        }
        return URI.create(url);
    }
}
