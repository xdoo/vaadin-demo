package de.muenchen.demo.service.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 *
 * @author claus.straube
 */
@Entity
@Table(name = "ADRESSE_INTERNE")
public class AdresseInterne extends BaseEntity {

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
@Override
    public String toString() {
        return String.format("id > %s | Oid > %s |Strasse > %s | hausnummer > %s | stadt > %s| plz > %s  ", this.getId(), this.getOid(), this.strasse,this.hausnummer, this.stadt, this.plz);
    }
}

