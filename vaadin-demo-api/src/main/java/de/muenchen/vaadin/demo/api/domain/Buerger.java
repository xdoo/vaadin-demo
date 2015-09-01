package de.muenchen.vaadin.demo.api.domain;

import java.util.Date;

/**
 *
 * @author claus.straube
 */
public class Buerger extends BaseEntity {
    
    public static final String VORNAME = "vorname";
    public static final String NACHNAME = "nachname";
    public static final String GEBURTSDATUM = "geburtsdatum";
    public static final String PARTNER = "partner";
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
    public Buerger clone() {
        Buerger clone = new Buerger();
        
        clone.setOid(this.getOid());
        clone.setVorname(this.getVorname());
        clone.setNachname(this.getNachname());
        clone.setGeburtsdatum(this.getGeburtsdatum());
        clone.setLinks(this.getLinks());
        
        return clone;
    }
    
}
