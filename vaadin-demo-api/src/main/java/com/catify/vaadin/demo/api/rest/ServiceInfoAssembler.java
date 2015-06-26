package com.catify.vaadin.demo.api.rest;

import com.catify.vaadin.demo.api.domain.ServiceInfo;

/**
 *
 * @author claus.straube
 */
public class ServiceInfoAssembler {
    
    public static ServiceInfo fromResource(ServiceInfoResource resource) {
        ServiceInfo entity = new ServiceInfo();
        // start mapping
        entity.setOid(resource.getOid());
        entity.setName(resource.getName());
        entity.setVersion(resource.getVersion());
        entity.setEntityLinks(resource.getEntityLinks());
        // end mapping
        
        // set links
        entity.setId(resource.getId().getHref());
        entity.setLinks(resource.getLinks());
        return entity;
    }
    
}
