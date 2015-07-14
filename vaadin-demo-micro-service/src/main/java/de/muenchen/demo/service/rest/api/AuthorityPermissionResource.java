/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.demo.service.rest.api;

import de.muenchen.demo.service.domain.AuthPermId;

/**
 *
 * @author praktikant.tmar
 */
public class AuthorityPermissionResource {
    
    AuthPermId id;

    public AuthPermId getId() {
        return id;
    }

    public void setId(AuthPermId id) {
        this.id = id;
    }
}
