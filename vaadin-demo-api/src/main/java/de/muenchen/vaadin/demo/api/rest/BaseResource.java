package de.muenchen.vaadin.demo.api.rest;

import org.springframework.hateoas.ResourceSupport;

/**
 *
 * @author claus.straube
 */
public class BaseResource extends ResourceSupport {
    
    private String oid;

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }
    
}
