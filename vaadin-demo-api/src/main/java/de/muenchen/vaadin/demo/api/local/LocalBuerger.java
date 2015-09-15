package de.muenchen.vaadin.demo.api.local;

import de.muenchen.vaadin.demo.api.util.FieldIdentifier;
import org.springframework.hateoas.ResourceSupport;

import java.util.Date;

/**
 * Created by p.mueller on 15.09.15.
 */
public class LocalBuerger extends ResourceSupport {
    @FieldIdentifier
    private String vorname;
    @FieldIdentifier
    private String nachname;
    @FieldIdentifier
    private Date geburtsdatum;

    public LocalBuerger(String vorname, String nachname, Date geburtsdatum) {
        this.vorname = vorname;
        this.nachname = nachname;
        this.geburtsdatum = geburtsdatum;
    }

    public LocalBuerger() {

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
