/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.demo.service.rest.api;

import org.springframework.hateoas.ResourceSupport;

/**
 *
 * @author praktikant.tmar
 */
public class MandantResource extends ResourceSupport {

    private String mid;


    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

}
