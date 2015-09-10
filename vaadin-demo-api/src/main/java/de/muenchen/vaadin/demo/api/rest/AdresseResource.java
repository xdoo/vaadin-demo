/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.vaadin.demo.api.rest;

import de.muenchen.vaadin.demo.apilib.rest.BaseResource;

/**
 *
 * @author praktikant.tmar
 */
public class AdresseResource extends BaseResource {
    
    public static final String SUCHE = "suche";

    private String strasseReference;
    private String strasse;
    private Integer hausnummer;
    private String buchstabe;
    private String stadt;
    private int plz;
   

    public String getStrasseReference() {
        return strasseReference;
    }

    public void setStrasseReference(String strasseReference) {
        this.strasseReference = strasseReference;
    }


    public String getStrasse() {
        return strasse;
    }

    public void setStrasse(String strasse) {
        this.strasse = strasse;
    }

    public Integer getHausnummer() {
        return hausnummer;
    }

    public void setHausnummer(Integer hausnummer) {
        this.hausnummer = hausnummer;
    }

    public String getBuchstabe() {
        return buchstabe;
    }

    public void setBuchstabe(String buchstabe) {
        this.buchstabe = buchstabe;
    }

    public String getStadt() {
        return stadt;
    }

    public void setStadt(String stadt) {
        this.stadt = stadt;
    }

    public int getPlz() {
        return plz;
    }

    public void setPlz(int plz) {
        this.plz = plz;
    }
    @Override
    public String toString() {
        return String.format("Oid > %s |Strasse > %s | hausnummer > %s | stadt > %s| plz > %s  ", this.getOid(), this.strasse,this.hausnummer, this.stadt, this.plz);
    }
}