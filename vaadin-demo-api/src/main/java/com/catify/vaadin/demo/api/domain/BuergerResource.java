package com.catify.vaadin.demo.api.domain;

import java.util.Date;

/**
 *
 * @author claus.straube
 */
public class BuergerResource extends BaseResource {
    
    private String vorname;
    private String nachname;
    private Date geburtsdatum;

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

    @Override
    public String toString() {
        return String.format("oid > %s | vorname > %s | nachname > %s | geburtsdatum > %s", this.getOid(), this.vorname, this.nachname, this.geburtsdatum);
    } 
}
