package de.muenchen.vaadin.demo.apilib.domain;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author claus.straube
 */
public class Principal {
    
    private final String username;
    private final List<String> roles = new ArrayList<>();
    private final List<String> permissions = new ArrayList<>();

    public Principal(String username) {
        this.username = username;
    }


    public List<String> getRoles() {
        return roles;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public String getUsername() {
        return username;
    }
    
}
