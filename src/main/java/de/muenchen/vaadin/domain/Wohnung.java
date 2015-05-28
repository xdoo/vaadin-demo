/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.vaadin.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author claus.straube
 */
@Entity
@Table(name = "WOHNUNGEN")
public class Wohnung implements Serializable {
    
    @Id @GeneratedValue
    private Long id;
    
    @Column(name = "WOHN_STOCK")
    private int stock;
    
    @Column(length = 20, name = "WOHN_AUSRICHTUNG")
    private String ausrichtung;
    
    @Column(length = 30, nullable = false, name = "WOHN_ADRESSE_OID")
    private String adresseOid;

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getAusrichtung() {
        return ausrichtung;
    }

    public void setAusrichtung(String ausrichtung) {
        this.ausrichtung = ausrichtung;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAdresseOid() {
        return adresseOid;
    }

    public void setAdresseOid(String adresseOid) {
        this.adresseOid = adresseOid;
    }
}
