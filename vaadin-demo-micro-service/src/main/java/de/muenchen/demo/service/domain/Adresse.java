/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.demo.service.domain;

/**
 *
 * @author claus.straube
 */
public class Adresse {


    
    private String oid;
    
    private Integer strasseReference;

    private String strasse;

    private Integer hausnummer;
    
    private String buchstabe;

    private String mandant;

    private String stadt;

    private int plz;

    public Adresse(Adresse ad) {
        this.strasseReference = ad.strasseReference;
        this.strasse = ad.strasse;
        this.hausnummer = ad.hausnummer;
        this.mandant = ad.mandant;
        this.stadt = ad.stadt;
        this.plz = ad.plz;
    }

    public Adresse() {
    }

    public String getStrasse() {
        return strasse;
    }

    public void setStrasse(String strasse) {
        this.strasse = strasse;
    }

    public Integer getStrasseReference() {
        return strasseReference;
    }

    public void setStrasseReference(Integer strasseReference) {
        this.strasseReference = strasseReference;
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

    public String getMandant() {
        return mandant;
    }

    public void setMandant(String mandant) {
        this.mandant = mandant;
    }

    public int getPlz() {
        return plz;
    }

    public void setPlz(int plz) {
        this.plz = plz;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    @Override
    public String toString() {
        return String.format(" Oid > %s |Strasse > %s | hausnummer > %s | stadt > %s| plz > %s  ", this.getOid(), this.strasse, this.hausnummer, this.stadt, this.plz);
    }
}
