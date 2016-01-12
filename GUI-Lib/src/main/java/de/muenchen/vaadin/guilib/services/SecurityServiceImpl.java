package de.muenchen.vaadin.guilib.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.discovery.DiscoveryClient;
import com.vaadin.spring.annotation.UIScope;
import de.muenchen.vaadin.demo.apilib.domain.Principal;
import de.muenchen.vaadin.demo.apilib.rest.SecurityRestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.hal.Jackson2HalModule;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Optional;

/**
 * 
 * @author claus.straube
 */
@Component
@UIScope
public class SecurityServiceImpl implements SecurityService, Serializable {
    
    private static final Logger LOG = LoggerFactory.getLogger(SecurityService.class);
    
    private boolean login;
    private Principal principal;
    private OAuth2RestTemplate restTemplate;

    @Value("${security.oauth2.client.id}")
    private String clientID;
    
    @Autowired
    private SecurityRestClient restClient;

    @Autowired
    private DiscoveryClient discoveryClient;
    private String token_url;

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

    @Override
    public boolean login(String username, String password) {
        try {
            token_url = discoveryClient.getNextServerFromEureka("authservice", false).getHomePageUrl();
        } catch (RuntimeException e) {
            LOG.error(e.getMessage());
            token_url = null;
        }
        
        // Get RestTemplate
        ResourceOwnerPasswordResourceDetails resource = new ResourceOwnerPasswordResourceDetails();
        resource.setUsername(username);
        resource.setPassword(password);
        resource.setGrantType("password");
        resource.setClientId(clientID);
        resource.setAccessTokenUri(token_url + "/uaa/oauth/token");

        OAuth2RestTemplate template = new OAuth2RestTemplate(resource, new DefaultOAuth2ClientContext());


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

        Optional<Principal> p = restClient.getPrincipal(template);
        if(p.isPresent()) {
            this.login = Boolean.TRUE;
            this.restTemplate = template;
            this.principal = p.get();
            LOG.info("Successfully logged in!");
        } else {
            this.login = Boolean.FALSE;
        }
        return login;
    }
    
    @Override
    public void logout() {
        restClient.logout(restTemplate);
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
