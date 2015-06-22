/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.vaadin.domain;

import java.util.Date;

/**
 *
 * @author claus.straube
 */
public class Buerger {
    
    private String oid;
    private String vorname;
    private String nachname;
    private Date geburtsdatum;

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getOid() {
        return oid;
    }

    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public String getNachname() {
        return nachname;
    }

    public void setNachname(String nachname) {
        this.nachname = nachname;
    }

    public Date getGeburtsdatum() {
        return geburtsdatum;
    }

    public void setGeburtsdatum(Date geburtsdatum) {
        this.geburtsdatum = geburtsdatum;
    }
    
    
    
}
