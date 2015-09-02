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

    public final static String ADRESSEN = "adressen";

    private String stock;
    private String ausrichtung;
    private String adOid;
    private String strasseReference;
    private String strasse;
    private String hausnummer;
    private String stadt;
    private int plz;

    public String getAdOid() {
        return adOid;
    }

    public void setAdOid(String adOid) {
        this.adOid = adOid;
    }

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

    public String getHausnummer() {
        return hausnummer;
    }

    public void setHausnummer(String hausnummer) {
        this.hausnummer = hausnummer;
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
        return String.format("Oid > %s | ausrichtung > %s | stock > %s ", this.getOid(), this.ausrichtung, this.stock);
    }
}
