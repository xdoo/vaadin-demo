/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.auth.entities;


import de.muenchen.auth.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author praktikant.tmar
 */
@Entity
@Table(name = "_AUTHORITIES")
public class Authority extends BaseEntity {

    @Column(name = "AUTH_AUTHORITY")
    private String authority;

    public Authority() {
    }

    public Authority(Authority authority) {
        this.authority = authority.authority;
    }


    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }
}
