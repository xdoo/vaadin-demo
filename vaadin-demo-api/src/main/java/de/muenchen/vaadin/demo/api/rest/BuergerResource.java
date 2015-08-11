package de.muenchen.vaadin.demo.api.rest;

import java.util.Date;

/**
 *
 * @author claus.straube
 */
public class BuergerResource extends BaseResource {
    
    // HATEOAS Relations zu den Relationen
    public static final String KINDER = "kinder";
    public static final String SAVE_KIND = "save_kind";
    public static final String WOHNUNGEN = "wohnungen";
    public static final String STAATSANGEHOERIGKEITEN = "staatsangehoerigkeiten";
    public static final String SAVE_WOHNUNG = "save_wohnung";
    public static final String PAESSE = "paesse";
    public static final String SAVE_PASS = "save_pass";
    
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