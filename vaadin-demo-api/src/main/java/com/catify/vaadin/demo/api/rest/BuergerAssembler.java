package com.catify.vaadin.demo.api.rest;

import com.catify.vaadin.demo.api.domain.Buerger;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author claus.straube
 */
public class BuergerAssembler {
    
    public static List<Buerger> fromResources(List<BuergerResource> resources) {
        return resources.stream().map(r -> fromResource(r)).collect(Collectors.toList());
    }
    
    public static Buerger fromResource(BuergerResource resource) {
        Buerger buerger = new Buerger();
        // start mapping
        buerger.setOid(resource.getOid());
        buerger.setVorname(resource.getVorname());
        buerger.setNachname(resource.getNachname());
        buerger.setGeburtsdatum(resource.getGeburtsdatum());
        // end mapping
        
        // set links
        buerger.setId(resource.getId().getHref());
        buerger.setLinks(resource.getLinks());
        return buerger;
    }
    
}
