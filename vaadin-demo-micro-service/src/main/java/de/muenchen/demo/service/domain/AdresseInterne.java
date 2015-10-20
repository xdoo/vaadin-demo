package de.muenchen.demo.service.domain;

import de.muenchen.service.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 *
 * @author praktikant.Tmar
 */
@Entity
@Table(name = "ADRESSE_INTERNE")
public class AdresseInterne extends BaseEntity implements Serializable {

    @Column(nullable = true, name = "ADR_HAUSNUMMER")
    @Size(max = 70)
    private Integer hausnummer;

    @Column(name = "ADR_BUCHSTABE")
    @Size(max = 10)
    private String buchstabe;


    @Column(nullable = true, name = "ADR_STRASSE")
    @Size(max = 70)
    private Long strasseReference;


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

    public Long getStrasseReference() {
        return strasseReference;
    }

    public void setStrasseReference(Long strasseReference) {
        this.strasseReference = strasseReference;
    }

}
