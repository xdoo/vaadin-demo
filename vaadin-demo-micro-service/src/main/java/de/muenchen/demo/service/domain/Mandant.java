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
@Table(name = "Mandant")
public class Mandant implements Serializable {

	private static final long serialVersionUID = 2279536262695714631L;

	@Id
    @GeneratedValue
    @Column(name = "ID")
    private Long id;

    @Field(name="mid")
    @Column(length = 30, unique = true, nullable = false, name = "OID")
    private String oid;
    
    @Column(length = 70, name = "MANDANT_MID")
    private String mid;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

}
