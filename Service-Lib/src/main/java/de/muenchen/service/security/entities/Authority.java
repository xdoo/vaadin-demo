/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.service.security.entities;

import javax.persistence.*;
import java.util.Set;

/**
 * @author praktikant.tmar
 */
@Entity
@Table(name = "AUTHORITIES")
public class Authority extends BaseEntity {

    @Column(name = "AUTH_AUTHORITY")
    private String authority;

    @ElementCollection
    @JoinTable(name = "authorities_permissions", joinColumns = {@JoinColumn(name = "authority_oid")})
    private Set<String> permissions;

    public Authority() {
    }

    public Authority(Authority authority, Set<String> permissions) {
        this.authority = authority.authority;
        this.permissions = permissions;

    }


    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }


    public Set<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<String> permissions) {
        this.permissions = permissions;
    }
}
