package de.muenchen.vaadin.demo.apilib.services;

import java.util.Optional;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author claus.straube
 */
public interface SecurityService {
    
    public static final String LOGIN_VIEW_NAME = "login";
    
    public Optional<RestTemplate> getRestTemplate();
    
    public boolean isUserInRole(String role);
    
    public boolean hasUserPermission(String permission);
    
    public boolean isLoggedIn();
    
    public boolean login(String username, String password);
    
    public void logout();

    
}