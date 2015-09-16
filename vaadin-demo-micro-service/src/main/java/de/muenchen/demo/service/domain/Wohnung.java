/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.demo.service.domain;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 *
 * @author claus.straube
 */
@Entity
@Table(name = "WOHNUNGEN")
@Audited
public class Wohnung extends BaseEntity implements Serializable {
//    @Transient
//    @Autowired
//    AdresseService adresseService;

    @Transient
    Adresse adr;
    @Column(name = "WOHN_STOCK")
    private String stock;
    @Column(name = "WOHN_AUSRICHTUNG")
    @Size(max = 20)
    private String ausrichtung;
    @NotAudited
    @OneToOne(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    @JoinColumn(name = "Adresse_Oid", referencedColumnName = "OID")
    private AdresseReference adresse;

    public Wohnung() {
    }

    public Wohnung(Wohnung wohnung) {
        this.stock = wohnung.stock;
        this.ausrichtung = wohnung.ausrichtung;
        this.adresse = wohnung.adresse;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getAusrichtung() {
        return ausrichtung;
    }

    public void setAusrichtung(String ausrichtung) {
        this.ausrichtung = ausrichtung;
    }

    public AdresseReference getAdresse() {
        return adresse;
    }

    public void setAdresse(AdresseReference adresse) {

        this.adresse = adresse;
    }

    @Override
    public String toString() {
        return String.format("oid > %s  | ausrichtung > %s | stock > %s", this.getOid(), this.ausrichtung, this.stock);
    }

    public Adresse getAdr() {
        
//        return adresseService.read(this.adresse.getOid());
        
        return this.adr;
    }

    public void setAdr(Adresse adresse) {
        this.adr = adresse;
    }
}
