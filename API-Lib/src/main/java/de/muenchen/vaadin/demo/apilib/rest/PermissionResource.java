/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.vaadin.demo.apilib.rest;

/**
 *
 * @author praktikant.tmar
 */
public class PermissionResource extends SecurityResource {
    private String permision;

    public String getPermision() {
        return permision;
    }

    public void setPermision(String permision) {
        this.permision = permision;
    }

}