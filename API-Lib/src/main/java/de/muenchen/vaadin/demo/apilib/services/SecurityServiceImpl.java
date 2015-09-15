package de.muenchen.vaadin.demo.apilib.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.muenchen.vaadin.demo.apilib.domain.Principal;
import de.muenchen.vaadin.demo.apilib.rest.SecurityRestClient;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.hal.Jackson2HalModule;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Optional;

/**
 * VORSICHT HACK! DAS MUSS ALLES NOCHMAL HINTERFRAGT UND GETESTET WERDEN!
 * 
 * 
 * @author claus.straube
 */
@Component
public class SecurityServiceImpl implements SecurityService, Serializable {
    
    private static final Logger LOG = LoggerFactory.getLogger(SecurityService.class);
    
    private boolean login;
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

        
        // Security 
        BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));
        
        HttpClient httpClient = HttpClientBuilder
                .create()
                .setDefaultCredentialsProvider(credentialsProvider)
                .build();
        ClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        RestTemplate template = new RestTemplate(requestFactory);

        MappingJackson2HttpMessageConverter halConverter = new MappingJackson2HttpMessageConverter();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new Jackson2HalModule());

        halConverter.setObjectMapper(objectMapper);
        halConverter.setSupportedMediaTypes(Arrays.asList(MediaTypes.HAL_JSON));

        template.setMessageConverters(Arrays.asList(
                new StringHttpMessageConverter(Charset.forName("UTF-8")),
                halConverter
        ));

        Optional<Principal> p = this.restClient.getPrincipal(template);
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
