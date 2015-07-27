package de.muenchen.vaadin.demo.api.rest;

import de.muenchen.vaadin.demo.api.domain.Buerger;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.hateoas.Resource;

/**
 *
 * @author claus.straube
 */
public class BuergerAssembler {
    
    public static List<Buerger> fromResources(SearchResultResource<BuergerResource> resource) {
        return resource.getResult().stream().map(r -> fromResource(r)).collect(Collectors.toList());
    }
    
    public static Buerger fromResource(Resource<BuergerResource> resource) {
        Buerger entity = new Buerger();
        BuergerResource content = resource.getContent();
        // start mapping
        entity.setOid(content.getOid());
        entity.setVorname(content.getVorname());
        entity.setNachname(content.getNachname());
        entity.setGeburtsdatum(content.getGeburtsdatum());
        
        // set links
        entity.setLinks(resource.getLinks());
        return entity;
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
        entity.setLinks(resource.getLinks());
        return entity;
    }
    
}
