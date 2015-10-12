package de.muenchen.vaadin.demo.apilib.domain;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author claus.straube
 */
public class Principal {
    
    private String username;
    private List<String> roles = new ArrayList<>();
    private List<String> permissions = new ArrayList<>();

    public List<String> getRoles() {
        return roles;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }
}
