/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.demo.service.domain;

import de.muenchen.service.BaseEntity;
import org.hibernate.envers.NotAudited;

import javax.persistence.*;
import java.io.Serializable;

/**
 *
 * @author praktikant.tmar
 */
@Entity
@Table(name = "ADRESSE_REFERENCE")
public class AdresseReference extends BaseEntity implements Serializable{
    
    @NotAudited
    @OneToOne(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    @JoinColumn(name = "ADR_EXTERNE_OID", referencedColumnName = "OID")
    private AdresseExterne adresseExterne;
    
    @NotAudited
    @OneToOne(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    @JoinColumn(name = "ADR_INTERNE_OID", referencedColumnName = "OID")
    private AdresseInterne adresseInterne;

    public AdresseExterne getAdresseExterne() {
        return adresseExterne;
    }

    public void setAdresseExterne(AdresseExterne adresseExterne) {
        this.adresseExterne = adresseExterne;
    }

    public AdresseInterne getAdresseInterne() {
        return adresseInterne;
    }

    public void setAdresseInterne(AdresseInterne adresseInterne) {
        this.adresseInterne = adresseInterne;
    }
}
