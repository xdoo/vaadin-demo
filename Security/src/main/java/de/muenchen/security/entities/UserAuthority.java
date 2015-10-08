/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.security.entities;

import de.muenchen.security.entities.UserAuthId;

import java.io.Serializable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 *
 * @author praktikant.tmar
 */
@Entity
@Table(name = "USERS_AUTHORITYS")
public class UserAuthority implements Serializable {

    @EmbeddedId
    UserAuthId id;

    public UserAuthId getId() {
        return id;
    }

    public void setId(UserAuthId id) {
        this.id = id;
    }

}
