package de.muenchen.demo.service.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 *
 * @author praktikant.Tmar
 */
@Entity
@Table(name = "ADRESSE_INTERNE")
public class AdresseInterne extends BaseEntity implements Serializable {

    @Column(nullable = true, name = "ADR_HAUSNUMMER")
    private Integer hausnummer;
    @Column(length = 10, nullable = true, name = "ADR_BUCHSTABE")
    private String buchstabe;

    @Column(length = 70, nullable = true, name = "ADR_STRASSE")
    private Integer strasseReference;

    public Integer getHausnummer() {
        return hausnummer;
    }

    public void setHausnummer(Integer hausnummer) {
        this.hausnummer = hausnummer;
    }

    public String getBuchstabe() {
        return buchstabe;
    }

    public void setBuchstabe(String buchstabe) {
        this.buchstabe = buchstabe;
    }

    public Integer getStrasseReference() {
        return strasseReference;
    }

    public void setStrasseReference(Integer strasseReference) {
        this.strasseReference = strasseReference;
    }

}
