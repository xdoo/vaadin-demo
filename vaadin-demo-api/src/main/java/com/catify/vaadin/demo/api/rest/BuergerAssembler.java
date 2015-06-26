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
        Buerger entity = new Buerger();
        // start mapping
        entity.setOid(resource.getOid());
        entity.setVorname(resource.getVorname());
        entity.setNachname(resource.getNachname());
        entity.setGeburtsdatum(resource.getGeburtsdatum());
        // end mapping
        
        // set links
        entity.setId(resource.getId().getHref());
        entity.setLinks(resource.getLinks());
        return entity;
    }
    
}
