/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.demo.service.rest.api;

import de.muenchen.demo.service.domain.Authority;
import de.muenchen.demo.service.domain.Permission;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 *
 * @author praktikant.tmar
 */
public class AuthPermIdResource {
   private Permission permission;
   private Authority authority;

    

    public Permission getPermission() {
        return permission;
    }

    public void setPermission(Permission permission) {
        this.permission = permission;
    }

    public Authority getAuthority() {
        return authority;
    }

    public void setAuthority(Authority authority) {
        this.authority = authority;
    }


}
