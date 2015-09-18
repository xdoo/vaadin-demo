package de.muenchen.vaadin.demo.api.domain;

import de.muenchen.vaadin.demo.apilib.domain.BaseEntity;

import java.util.Date;

/**
 * Provides a simple DTO for a Buerger.
 * <p>
 * It is used for the REST calls to model the transferred POJO. This DTO only contains the plain Fields, no Relations.
 * Also this DTO does not contain the oid.
 * </p>
 *
 * @author p.mueller
 * @version 1.0
 */
public class BuergerDTO extends BaseEntity {

    /**
     * A plain Field of the DTO
     */
    private String vorname;
    /**
     * A plain Field of the DTO
     */
    private String nachname;
    /**
     * A plain Field of the DTO
     */
    private Date geburtsdatum;

    /**
     * Get the vorname.
     * @return the vorname.
     */
    public String getVorname() {
        return vorname;
    }

    /**
     * Set the vorname. {Validation info here}
     * @param vorname the vorname
     */
    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    /**
     * Get the nachname.
     * @return the nachname
     */
    public String getNachname() {
        return nachname;
    }

    /**
     * Set the nachname. {Validation info here}.
     * @param nachname the nachname
     */
    public void setNachname(String nachname) {
        this.nachname = nachname;
    }

    /**
     * Get the geburtsdatum.
     * @return the geburtsdatum.
     */
    public Date getGeburtsdatum() {
        return geburtsdatum;
    }

    /**
     * Set the geburtsdatum. {Validation info here}
     * @param geburtsdatum the geburtsdatum
     */
    public void setGeburtsdatum(Date geburtsdatum) {
        this.geburtsdatum = geburtsdatum;
    }

    @Override
    public String toString() {
        return String.format("%s = {\"vorname\": \"%s\", \"nachname\": \"%s\", \"geburtsdatum\": \"%s\"}", getClass(),
                this.vorname,
                this.nachname,
                this.geburtsdatum);
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
