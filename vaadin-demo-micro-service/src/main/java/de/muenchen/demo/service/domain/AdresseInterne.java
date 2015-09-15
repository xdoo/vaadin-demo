
package de.muenchen.demo.service.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Size;

/**
 *
 * @author praktikant.Tmar
 */
@Entity
@Table(name = "ADRESSE_INTERNE")
public class AdresseInterne extends BaseEntity implements Serializable {
    
    @Column(nullable = true, name = "ADR_HAUSNUMMER")
    @Size(max = 70)
    private String hausnummer;
    
    @Column(nullable = true, name = "ADR_STRASSE")
    @Size(max = 70)
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

