/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.demo.service.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Size;

/**
 *
 * @author praktikant.tmar
 */
@Entity
@Table(name = "ADRESSE_EXTERNE")
public class AdresseExterne extends BaseEntity implements Serializable {

    @Column(nullable = true, name = "ADR_STR")
    @Size(max = 70)
    private String strasse;

    @Column(nullable = true, name = "ADR_HAUSNUMMER")
    @Size(max = 70)
    private String hausnummer;

    @Column(nullable = true, name = "ADR_STADT")
    @Size(max = 20)
    private String stadt;

    @Column(nullable = true, name = "ADR_PLZ")
    @Size(max = 10)
    private int plz;

    public String getStrasse() {
        return strasse;
    }

    public void setStrasse(String strasse) {
        this.strasse = strasse;
    }

    public String getHausnummer() {
        return hausnummer;
    }

    public void setHausnummer(String hausnummer) {
        this.hausnummer = hausnummer;
    }

    public String getStadt() {
        return stadt;
    }

    public void setStadt(String stadt) {
        this.stadt = stadt;
    }

    public int getPlz() {
        return plz;
    }

    public void setPlz(int plz) {
        this.plz = plz;
    }

}
