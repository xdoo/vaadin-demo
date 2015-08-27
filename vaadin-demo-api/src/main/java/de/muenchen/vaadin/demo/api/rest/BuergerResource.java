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
    public static final String ADD_KIND = "add_kind";
    public static final String WOHNUNGEN = "wohnungen";
    public static final String SACHBEARBEITER = "sachbearbeiter";
    public static final String STAATSANGEHOERIGKEITEN = "staatsangehoerigkeiten";
    public static final String ADD_STAATSANGEHOERIGKEITEN = "add_staatsangehoerigkeiten";
    public static final String SAVE_WOHNUNG = "save_wohnung";
    public static final String ADD_WOHNUNG = "add_wohnung";
    public static final String SAVE_SACHBEARBEITER = "save_sachbearbeiter";
    public static final String ADD_SACHBEARBEITER = "add_sachbearbeiter";
    public static final String PAESSE = "paesse";
    public static final String SAVE_PASS = "save_pass";
    public static final String ADD_PASS = "add_pass";
    public static final String ELTERN = "eltern";
    public static final String RELEASE_ELTERN = "release_eltern";
    public static final String RELEASE_ELTERNTEIL = "release_elternteil";    
    public static final String RELEASE_KINDER = "release_kinder";
    public static final String RELEASE_PAESSE = "release_paesse";
    public static final String RELEASE_WOHNUNG = "release_wohnung";
    public static final String RELEASE_WOHNUNGEN = "release_wohnungen";
    public static final String RELEASE_SACHBEARBEITER = "release_sachbearbeiter";



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
