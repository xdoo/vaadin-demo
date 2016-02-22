/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.itm.infrastructure.auth.entities;

import de.muenchen.service.BaseEntity;
import org.hibernate.search.annotations.Field;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author praktikant.tmar
 */
@Entity
@Table(name = "_PERMISSIONS")
public class Permission extends BaseEntity {

    @Field
    @Column(name = "PERM_PERMISSION")
    private String permission;

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

}