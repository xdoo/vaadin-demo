/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.demo.service.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.NaturalId;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

/**
 *
 * @author claus.straube
 */
@Entity
@Table(name = "WOHNUNGEN")
@Audited
public class Wohnung extends BaseEntity  {

  
    @Column(name = "WOHN_STOCK")
    private String stock;

    @Column(length = 20, name = "WOHN_AUSRICHTUNG")
    private String ausrichtung;
    
    @Column(length = 30, nullable = false, name = "WOHN_ADRESSE_OID")
    private String adresseOid;
    

    @NotAudited
    @OneToOne
    @JoinColumn(name = "Adresse_Id", referencedColumnName = "Id")
    private AdresseReference adresse;

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
        return String.format("id > %s | oid > %s  | ausrichtung > %s | stock > %s", this.getId(), this.getOid(), this.ausrichtung, this.stock);
    }



    } 
    


