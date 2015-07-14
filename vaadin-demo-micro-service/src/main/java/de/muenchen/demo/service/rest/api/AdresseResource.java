/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.demo.service.rest.api;

/**
 *
 * @author praktikant.tmar
 */
public class AdresseResource extends BaseResource{
    

    private String strasse;
    private String hausnummer;
    private String stadt;
    private int plz;


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
    @Override
    public String toString() {
        return String.format("Oid > %s |Strasse > %s | hausnummer > %s | stadt > %s| plz > %s  ", this.getOid(), this.strasse,this.hausnummer, this.stadt, this.plz);
    }
}
