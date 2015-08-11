/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.demo.service.rest.api;

import de.muenchen.demo.service.domain.AdresseReference;

/**
 *
 * @author praktikant.tmar
 */
public class WohnungResource extends BaseResource {
    
    public final static String ADRESSEN = "adressen";

    private String stock;
    private String ausrichtung;
    
    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getAusrichtung() {
        return ausrichtung;
    }

    public void setAusrichtung(String ausrichtung) {
        this.ausrichtung = ausrichtung;
    }

   
    @Override
    public String toString() {
        return String.format("Oid > %s | ausrichtung > %s | stock > %s ", this.getOid(), this.ausrichtung, this.stock);
    }
}
