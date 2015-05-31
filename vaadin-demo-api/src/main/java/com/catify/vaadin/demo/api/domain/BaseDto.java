package com.catify.vaadin.demo.api.domain;

import java.io.Serializable;

/**
 * Basis Klasse für alle DTOs.
 * 
 * TODO --> Diese Klasse gehört in eine Querschnitt Bibliothek.
 *  
 * @author claus
 */
public class BaseDto implements Serializable{

    private String oid;
    
    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }
}
