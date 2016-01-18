package de.muenchen.vaadin.guilib.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.discovery.DiscoveryClient;
import com.vaadin.spring.annotation.UIScope;
import de.muenchen.vaadin.demo.apilib.domain.Principal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.netflix.ribbon.RibbonClientHttpRequestFactory;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.hal.Jackson2HalModule;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2AccessDeniedException;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

/**
 * @author claus.straube
 */
@Component
@UIScope
public class SecurityServiceImpl implements SecurityService, Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(SecurityService.class);

    private boolean login;
    private Principal principal;
    private OAuth2RestTemplate restTemplate;

    private String TOKEN_URL;

    @Value("${security.oauth2.client.id}")
    private String clientID;

    @Autowired
    public SecurityServiceImpl(DiscoveryClient discoveryClient) {
        try {
            TOKEN_URL = discoveryClient.getNextServerFromEureka("authservice", false).getHomePageUrl();
        } catch (RuntimeException e) {
            TOKEN_URL = null;
            LOG.error(e.getMessage());
        }
    }

    @Override
    public boolean isUserInRole(String role) {
        return this.principal.getRoles().contains(role);
    }

    @Override
    public boolean hasUserPermission(String permission) {
        return this.principal.getPermissions().contains(permission);
    }

    @Override
    public Principal getCurrentPrincipal() {
        return principal;
    }

    @Autowired
    RibbonClientHttpRequestFactory ribbonClientHttpRequestFactory;

    @Override
    public boolean login(String username, String password) {

        // Get RestTemplate
        ResourceOwnerPasswordResourceDetails resource = new ResourceOwnerPasswordResourceDetails();
        resource.setUsername(username);
        resource.setPassword(password);
        resource.setGrantType("password");
        resource.setClientId(clientID);
        resource.setAccessTokenUri(TOKEN_URL + "/uaa/oauth/token");

        OAuth2RestTemplate template = new OAuth2RestTemplate(resource, new DefaultOAuth2ClientContext());
        template.setRequestFactory((uri, httpMethod) -> {
            final ClientHttpRequest request = ribbonClientHttpRequestFactory.createRequest(uri, httpMethod);
            if (!uri.equals(request.getURI()))
                request.getHeaders().add("X-Forwarded-Host", uri.getHost());
            return request;
        });

        //MessageConverter
        MappingJackson2HttpMessageConverter halConverter = new MappingJackson2HttpMessageConverter();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new Jackson2HalModule());

        halConverter.setObjectMapper(objectMapper);
        halConverter.setSupportedMediaTypes(Arrays.asList(MediaTypes.HAL_JSON));

        template.setMessageConverters(Arrays.asList(
                new StringHttpMessageConverter(Charset.forName("UTF-8")),
                halConverter
        ));

        //TODO Resolve principle of token
        Principal principal = null;
        try {
            principal = template.getForObject("http://authservice/uaa/profile", Principal.class);
        } catch (RestClientException | OAuth2AccessDeniedException | IllegalArgumentException | IllegalStateException e) {
            LOG.error("HTTP Response Error bei Login: " + e.getMessage());
        }

        if (principal != null) {
            this.login = Boolean.TRUE;
            this.restTemplate = template;
            this.principal = principal;
            LOG.error("Successfully logged in!");
        } else {
            this.login = Boolean.FALSE;
        }
        return login;
    }

    @Override
    public void logout() {
        try {
            restTemplate.exchange(TOKEN_URL + "uaa/logout", HttpMethod.POST, null, Void.class);
        } catch (RestClientException e) {
            LOG.error("Logout-Fehler: " + e.getMessage());
        }
        //Delete Token
        restTemplate = null;
        this.login = Boolean.FALSE;
    }

    @Override
    public boolean isLoggedIn() {
        return login;
    }

    @Override
    public Optional<RestTemplate> getRestTemplate() {
        return Optional.ofNullable(this.restTemplate);
    }

}
