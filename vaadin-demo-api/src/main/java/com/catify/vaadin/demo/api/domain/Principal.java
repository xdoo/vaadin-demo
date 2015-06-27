package com.catify.vaadin.demo.api.domain;

import java.util.List;

/**
 *
 * @author claus.straube
 */
public class Principal {
    
    String username;
    List<String> roles;
    List<String> permissions;
    
    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
}
