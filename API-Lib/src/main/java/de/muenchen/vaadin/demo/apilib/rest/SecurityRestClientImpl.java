package de.muenchen.vaadin.demo.apilib.rest;

import com.netflix.discovery.DiscoveryClient;
import de.muenchen.vaadin.demo.apilib.domain.Principal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.oauth2.client.resource.OAuth2AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Optional;

/**
 * @author claus.straube
 */
@Service
public class SecurityRestClientImpl implements SecurityRestClient {

    private static final Logger LOG = LoggerFactory.getLogger(SecurityRestClientImpl.class);

    private String securityUrl;

    @Autowired
    public SecurityRestClientImpl(DiscoveryClient discoveryClient){
        try {
            securityUrl = discoveryClient.getNextServerFromEureka("authservice", false).getHomePageUrl();
        } catch (RuntimeException e) {
            LOG.error(e.toString());
            securityUrl = null;
        }
    }

    @Override
    public Optional<Principal> getPrincipal(RestTemplate template) {
        Principal principal = null;

        try {
            principal = template.getForObject(securityUrl + "uaa/user/", Principal.class);
        } catch (RestClientException | OAuth2AccessDeniedException | IllegalArgumentException e) {
            LOG.debug("HTTP Response Error bei Login: " + e.getMessage());
        }
        return Optional.ofNullable(principal);
    }

    @Override
    public void logout(RestTemplate template) {
        try {
            template.exchange(securityUrl + "uaa/logout", HttpMethod.POST, null, Void.class);
        } catch (RestClientException e){
            LOG.debug("Logout-Fehler: " + e.getMessage());
        }
    }

}
