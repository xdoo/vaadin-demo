/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.demo.service.rest.api;

import java.util.Date;

/**
 *
 * @author praktikant.tmar
 */
public class PassResource extends BaseResource {

    private String passNummer;

    private String Typ;

    private String kode;
    
    private String groesse;
    
    private String augenFarbe;

    private Date austellungsdatum;

    private Date gueltigBis;

    private String behoerde;
    
    public static final String ADD_STAATSANGEHOERIGKEIT = "add_staatsangehoerigkeit";
    public static final String STAATSANGEHOERIGKEIT = "staatsangehoerigkeit";
    public static final String BUERGER = "buerger";
    public static final String RELEASE_BUERGER = "release_buerger";    


    public String getPassNummer() {
        return passNummer;
    }

    public void setPassNummer(String passNummer) {
        this.passNummer = passNummer;
    }

    public String getTyp() {
        return Typ;
    }

    public void setTyp(String Typ) {
        this.Typ = Typ;
    }

    public String getKode() {
        return kode;
    }

    public void setKode(String kode) {
        this.kode = kode;
    }

    public String getGroesse() {
        return groesse;
    }

    public void setGroesse(String groesse) {
        this.groesse = groesse;
    }

    public String getAugenFarbe() {
        return augenFarbe;
    }

    public void setAugenFarbe(String augenFarbe) {
        this.augenFarbe = augenFarbe;
    }



    public Date getAustellungsdatum() {
        return austellungsdatum;
    }

    public void setAustellungsdatum(Date austellungsdatum) {
        this.austellungsdatum = austellungsdatum;
    }

    public Date getGueltigBis() {
        return gueltigBis;
    }

    public void setGueltigBis(Date gueltigBis) {
        this.gueltigBis = gueltigBis;
    }

    public String getBehoerde() {
        return behoerde;
    }

    public void setBehoerde(String behoerde) {
        this.behoerde = behoerde;
    }

}
