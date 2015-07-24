/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.demo.service.rest.api;

import org.springframework.hateoas.ResourceSupport;

/**
 *
 * @author claus.straube
 */
public class BaseResource extends ResourceSupport {
    
    private String oid;
    private MandantResource mandant;

    

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public MandantResource getMandant() {
        return mandant;
    }

    public void setMandant(MandantResource mandant) {
        this.mandant = mandant;
    }
    
}
