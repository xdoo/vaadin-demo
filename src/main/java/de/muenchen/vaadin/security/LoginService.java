/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.vaadin.security;

/**
 *
 * @author claus.straube
 */
public interface LoginService {
 
    public boolean login(String username, String password);
    
}
