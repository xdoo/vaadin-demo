/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.demo.service.rest.api;

import org.springframework.hateoas.ResourceSupport;

/**
 *
 * @author praktikant.tmar
 */
public class SachbearbeiterResource extends BaseResource {
    
    public static final String USER = "user";
    public static final String SAVE_USER = "save_user";
    public static final String RELEASE_USER = "release_user";
    public static final String BUERGER = "buerger";
    public static final String SAVE_BUERGER = "save_buerger";
    public static final String RELEASE_BUERGER = "release_buerger";
    
    String Telephone;

    String funktion;

    String fax;

    String organisationseinheit;

    public String getTelephone() {
        return Telephone;
    }

    public void setTelephone(String Telephone) {
        this.Telephone = Telephone;
    }

    public String getFunktion() {
        return funktion;
    }

    public void setFunktion(String funktion) {
        this.funktion = funktion;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getOrganisationseinheit() {
        return organisationseinheit;
    }

    public void setOrganisationseinheit(String organisationseinheit) {
        this.organisationseinheit = organisationseinheit;
    }

}
