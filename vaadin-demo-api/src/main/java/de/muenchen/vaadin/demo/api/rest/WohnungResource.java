/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.vaadin.demo.api.rest;

/**
 *
 * @author praktikant.tmar
 */
public class WohnungResource extends BaseResource {

    // HATEOAS Relations zu den Relationen   

    public static final String BUERGER = "buerger";
    public static final String RELEASE_BUERGERS = "release_buergers";

    private String stock;
    private String ausrichtung;
    private String adOid;
    private Integer strasseReference;
    private String strasse;
    private Integer hausnummer;
    private String buchstabe;
    private int plz;
    private String stadt;

    public String getAdOid() {
        return adOid;
    }

    public void setAdOid(String adOid) {
        this.adOid = adOid;
    }

    public Integer getStrasseReference() {
        return strasseReference;
    }

    public void setStrasseReference(Integer strasseReference) {
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
        return String.format("Oid > %s | ausrichtung > %s | stock > %s | strasse > %s | hausnummer > %s | buchstabe > %s| plz > %s | stadt > %s  ", this.getOid(), this.ausrichtung, this.stock,this.strasse,this.hausnummer,this.buchstabe,this.plz,this.stadt);
    }
}
