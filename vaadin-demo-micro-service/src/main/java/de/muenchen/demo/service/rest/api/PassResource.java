/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.demo.service.rest.api;

import de.muenchen.demo.service.domain.Staatsangehoerigkeit;
import java.util.Date;

/**
 *
 * @author praktikant.tmar
 */
public class PassResource extends BaseResource {
    
    private String passNummer;

    private String Typ;

    private String Kode;

    private Date austellungsdatum;

    private Date gueltigBis;
    
    private String behoerde;
    
    private Staatsangehoerigkeit staatsangehoerigkeit;
    

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
        return Kode;
    }

    public void setKode(String Kode) {
        this.Kode = Kode;
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

    public Staatsangehoerigkeit getStaatsangehoerigkeit() {
        return staatsangehoerigkeit;
    }

    public void setStaatsangehoerigkeit(Staatsangehoerigkeit staatsangehoerigkeit) {
        this.staatsangehoerigkeit = staatsangehoerigkeit;
    }

    
}
