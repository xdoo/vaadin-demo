package de.muenchen.vaadin.demo.api.domain;

import de.muenchen.vaadin.demo.apilib.domain.BaseEntity;

import java.util.Date;

/**
 * Provides a simple DTO for a BuergerDTO
 * @author p.mueller
 */
public class BuergerDTO extends BaseEntity {

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
        return String.format("%s %s %s", this.vorname, this.nachname, this.geburtsdatum.toString());
    }

    @Override
    public BuergerDTO clone() {
        BuergerDTO clone = new BuergerDTO();
        
        clone.setOid(this.getOid());
        clone.setVorname(this.getVorname());
        clone.setNachname(this.getNachname());
        clone.setGeburtsdatum(this.getGeburtsdatum());
        
        return clone;
    }
}
