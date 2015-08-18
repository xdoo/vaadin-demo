package de.muenchen.vaadin.demo.api.rest;

import org.springframework.hateoas.ResourceSupport;
/**
 *
 * @author claus.straube
 */
public class BaseResource extends ResourceSupport {
    
    private String oid;
    private MandantResource mandant;

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }
    public MandantResource getMandant() {
        return mandant;
    }

    public void setMandant(MandantResource mandant) {
        this.mandant = mandant;
    }
    
}
