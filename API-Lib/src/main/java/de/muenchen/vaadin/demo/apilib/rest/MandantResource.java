/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.vaadin.demo.apilib.rest;

import org.springframework.hateoas.ResourceSupport;

/**
 *
 * @author praktikant.tmar
 */
public class MandantResource extends ResourceSupport {

    private String oid;

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }




}