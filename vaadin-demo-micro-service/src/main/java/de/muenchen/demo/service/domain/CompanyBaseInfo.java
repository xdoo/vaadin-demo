/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.demo.service.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import de.muenchen.security.entities.SecurityEntity;
import org.hibernate.search.annotations.Indexed;

/**
 *
 * @author praktikant.tmar
 */
@Entity
@Indexed
@Table(name = "COMPANY_BASE_INFOS")
public class CompanyBaseInfo extends SecurityEntity {

    @Column(name = "COMP_NAME")
    private String name;
    @Column(name = "COMP_ADRESS")
    private String adresse;
    
    @OneToMany(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER)

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }
}
