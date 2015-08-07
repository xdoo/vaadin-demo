/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.demo.service.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.search.annotations.Field;

/**
 *
 * @author praktikant.tmar
 */
@Entity
@Table(name = "MANDANT")
public class Mandant implements Serializable {

    private static final long serialVersionUID = 2279536262695714631L;

    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Long id;

    @Field(name = "mid")
    @Column(length = 30, unique = true, nullable = false, name = "OID")
    private String mid;


    public Mandant() {
    }

    public Mandant(Mandant mandant) {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }



}
