
package de.muenchen.demo.service.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 *
 * @author praktikant.Tmar
 */
@Entity
@Table(name = "ADRESSE_INTERNE")
public class AdresseInterne extends BaseEntity {
    
    @Column(length = 70, nullable = true, name = "ADR_HAUSNUMMER")
    private String hausnummer;
    
    @Column(length = 70, nullable = true, name = "ADR_STRASSE")
    private String strasseReference;
    
    
    public String getHausnummer() {
        return hausnummer;
    }

    
    public void setHausnummer(String hausnummer) {
        this.hausnummer = hausnummer;
    }

    public String getStrasseReference() {
        return strasseReference;
    }

    public void setStrasseReference(String strasseReference) {
        this.strasseReference = strasseReference;
    }

    
}

