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
@Table(name = "Mandant")
public class Mandant extends BaseEntity {

    @Column(length = 70, name = "MANDANT_KODE")
    private String Kode;

    public String getKode() {
        return Kode;
    }

    public void setKode(String Kode) {
        this.Kode = Kode;
    }

}
