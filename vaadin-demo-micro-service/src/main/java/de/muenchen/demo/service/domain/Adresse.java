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
    
    private String strasseReference;

    private String strasse;

    private String hausnummer;
    
    private Mandant mandant;

    private String stadt;

    private int plz;

    public String getStrasse() {
        return strasse;
    }

    public void setStrasse(String strasse) {
        this.strasse = strasse;
    }

    public String getStrasseReference() {
        return strasseReference;
    }

    public void setStrasseReference(String strasseReference) {
        this.strasseReference = strasseReference;
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

    public Mandant getMandant() {
        return mandant;
    }

    public void setMandant(Mandant mandant) {
        this.mandant = mandant;
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
