/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.demo.service.rest.api;
import de.muenchen.vaadin.demo.api.rest.SecurityResource;

/**
 *
 * @author praktikant.tmar
 */
public class AuthorityResource extends SecurityResource{
    private String authority;


    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }
    
}
