package de.muenchen.vaadin.demo.api.services;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.muenchen.vaadin.demo.api.domain.Principal;
import de.muenchen.vaadin.demo.api.rest.SecurityRestClient;
import com.vaadin.navigator.Navigator;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import java.util.Collections;
import java.util.Optional;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.hal.Jackson2HalModule;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * VORSICHT HACK! DAS MUSS ALLES NOCHMAL HINTERFRAGT UND GETESTET WERDEN!
 * 
 * 
 * @author claus.straube
 */
@SpringComponent @UIScope
public class SecurityServiceImpl implements SecurityService {
    
    private static final Logger LOG = LoggerFactory.getLogger(SecurityService.class);
    
    private boolean login;
    private Navigator navigator;
    private Principal principal;
    private RestTemplate restTemplate;
    
    @Autowired private SecurityRestClient restClient;

    @Override
    public boolean isUserInRole(String role) {
        return this.principal.getRoles().contains(role);
    }
    
    @Override
    public boolean hasUserPermission(String permission) {
        return this.principal.getPermissions().contains(permission);
    }

    @Override
    public boolean login(String username, String password) {
        
        // HATEOAS
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.registerModule(new Jackson2HalModule());

        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(MediaType.parseMediaTypes("application/hal+json"));
        converter.setObjectMapper(mapper);
        
        // Security 
        BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));
        
        HttpClient httpClient = HttpClientBuilder
                .create()
                .setDefaultCredentialsProvider(credentialsProvider)
                .build();
        ClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        RestTemplate template = new RestTemplate(requestFactory);
        template.setMessageConverters(Collections.<HttpMessageConverter<?>> singletonList(converter));
        Optional<Principal> p = this.restClient.getPrincipal(template);
        if(p.isPresent()) {
            this.login = Boolean.TRUE;
            this.restTemplate = template;
            this.principal = p.get();
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }
    
    @Override
    public void setNavigator(Navigator navigator) {
        this.navigator = navigator;
    }  

    @Override
    public void logout() {
        this.login = Boolean.FALSE;
        this.navigator.navigateTo("foo"); // TODO -> fix this
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
