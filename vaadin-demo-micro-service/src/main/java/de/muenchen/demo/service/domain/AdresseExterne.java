/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.demo.service.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 *
 * @author praktikant.tmar
 */
@Entity
@Table(name = "ADRESSE_EXTERNE")
public class AdresseExterne extends BaseEntity {

    @Column(length = 70, nullable = true, name = "ADR_STR")
    private String strasse;
    @Column(length = 70, nullable = true, name = "ADR_HAUSNUMMER")
    private String hausnummer;
    @Column(length = 20, nullable = true, name = "ADR_STADT")
    private String stadt;
    @Column(length = 10, nullable = true, name = "ADR_PLZ")
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
