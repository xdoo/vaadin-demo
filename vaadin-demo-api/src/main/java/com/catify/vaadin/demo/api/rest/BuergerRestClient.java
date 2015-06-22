/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.catify.vaadin.demo.api.rest;

import com.catify.vaadin.demo.api.domain.BuergerResource;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author claus
 */
public class BuergerRestClient {
    
    public BuergerResource read(String oid) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getForEntity(null, null);
        return null;
    }
    
}
