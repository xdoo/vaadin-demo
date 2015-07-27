/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.demo.service.domain;

import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 *
 * @author praktikant.tmar
 */
@Entity
@Table(name = "PASS")
public class Pass extends BaseEntity {

    @Column(length = 70, name = "PASS_PASSNUMMER")
    private String passNummer;

    @Column(length = 10, name = "PASS_TYP")
    private String Typ;

    @Column(length = 10, name = "PASS_KODE")
    private String kode;
    
    @Column(length = 20, name = "PASS_GROESSE")
    private String groesse;
    
    @Column(length = 70, name = "PASS_AUGENFARBE")
    private String augenFarbe;


    @Column(name = "PASS_AUSTELLUNGSDATUM")
    @Temporal(TemporalType.DATE)
    private Date austellungsdatum;

    @Column(name = "PASS_GUELTIG_BIS")
    @Temporal(TemporalType.DATE)
    private Date gueltigBis;

    @Column(length = 70, name = "PASS_BEHOERDE")
    private String behoerde;

    @OneToOne(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER)
    private StaatsangehoerigkeitReference staatsangehoerigkeitReference;

    @Transient
    private Staatsangehoerigkeit staatsangehoerigkeit;

    public String getPassNummer() {
        return passNummer;
    }

    public void setPassNummer(String passNummer) {
        this.passNummer = passNummer;
    }

    public String getTyp() {
        return Typ;
    }

    public void setTyp(String Typ) {
        this.Typ = Typ;
    }

    public String getKode() {
        return kode;
    }

    public void setKode(String kode) {
        this.kode = kode;
    }

    public String getGroesse() {
        return groesse;
    }

    public void setGroesse(String groesse) {
        this.groesse = groesse;
    }

    public String getAugenFarbe() {
        return augenFarbe;
    }

    public void setAugenFarbe(String augenFarbe) {
        this.augenFarbe = augenFarbe;
    }



    public Date getAustellungsdatum() {
        return austellungsdatum;
    }

    public void setAustellungsdatum(Date austellungsdatum) {
        this.austellungsdatum = austellungsdatum;
    }

    public Date getGueltigBis() {
        return gueltigBis;
    }

    public void setGueltigBis(Date gueltigBis) {
        this.gueltigBis = gueltigBis;
    }

    public String getBehoerde() {
        return behoerde;
    }

    public void setBehoerde(String behoerde) {
        this.behoerde = behoerde;
    }

    public Staatsangehoerigkeit getStaatsangehoerigkeit() {
        return staatsangehoerigkeit;
    }

    public void setStaatsangehoerigkeit(Staatsangehoerigkeit staatsangehoerigkeit) {
        this.staatsangehoerigkeit = staatsangehoerigkeit;
    }

    public StaatsangehoerigkeitReference getStaatsangehoerigkeitReference() {
        return staatsangehoerigkeitReference;
    }

    public void setStaatsangehoerigkeitReference(StaatsangehoerigkeitReference staatsangehoerigkeitReference) {
        this.staatsangehoerigkeitReference = staatsangehoerigkeitReference;
    }

}
