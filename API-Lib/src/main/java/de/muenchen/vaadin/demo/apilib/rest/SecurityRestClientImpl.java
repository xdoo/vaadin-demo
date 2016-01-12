package de.muenchen.vaadin.demo.apilib.rest;

import com.netflix.discovery.DiscoveryClient;
import de.muenchen.vaadin.demo.apilib.domain.Principal;
import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.resource.OAuth2AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Optional;

/**
 * @author claus.straube
 */
@Service
public class SecurityRestClientImpl implements SecurityRestClient {

    private static final Logger LOG = LoggerFactory.getLogger(SecurityRestClientImpl.class);

    @Autowired
    private DiscoveryClient discoveryClient;
    private URI uri;
    private String principalURL;

    @Override
    public Optional<Principal> getPrincipal(RestTemplate template) {
        try {
            principalURL = discoveryClient.getNextServerFromEureka("authservice", false).getHomePageUrl();
        } catch (RuntimeException e) {
            LOG.error(e.toString());
            principalURL = null;
        }

        Principal principal = null;

        try {
            principal = template.getForObject(principalURL + "uaa/user/", Principal.class);
        } catch (RestClientException | OAuth2AccessDeniedException | IllegalArgumentException e) {
            LOG.debug("HTTP Response Error bei Login: " + e.getMessage());
        }
        return Optional.ofNullable(principal);
    }

}
