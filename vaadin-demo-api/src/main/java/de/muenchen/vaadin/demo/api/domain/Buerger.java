package de.muenchen.vaadin.demo.api.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Date;

/**
 *
 * @author claus.straube
 */
public class Buerger extends BaseEntity {
    
    public static final String VORNAME = "vorname";
    public static final String NACHNAME = "nachname";
    public static final String GEBURTSDATUM = "geburtsdatum";
    
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
    
    
}
