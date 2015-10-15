/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.security.entities;

import javax.persistence.*;
import java.util.Set;

/**
 * @author praktikant.tmar
 */
@Entity
@Table(name = "AUTHORITIES")
public class Authority extends SecurityEntity {


    @Column(name = "AUTH_AUTHORITY")
    private String authority;

    @ManyToMany(cascade = {CascadeType.REFRESH})
    @JoinTable(name = "authorities_permissions", joinColumns = {@JoinColumn(name = "authority_oid")}, inverseJoinColumns = {@JoinColumn(name = "permission_oid")})
    private Set<Permission> permissions;

    public Authority() {
    }

    public Authority(Authority authority, Set<Permission> permissions) {
        this.authority = authority.authority;
        this.permissions = permissions;

    }


    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }


    public Set<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<Permission> permissions) {
        this.permissions = permissions;
    }
}
