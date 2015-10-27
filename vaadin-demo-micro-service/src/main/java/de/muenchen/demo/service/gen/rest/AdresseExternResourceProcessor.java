package de.muenchen.demo.service.gen.rest;

import de.muenchen.demo.service.gen.domain.AdresseExtern;
import de.muenchen.demo.service.gen.services.AdresseExternResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.stereotype.Component;

/*
 * This file will be overwritten on every change of the model!
 * This file was automatically generated by GAIA.
 */

/**
 * This RessourceProcessor can alter Resources, before they are sent out.
 */
@Component
public class AdresseExternResourceProcessor implements ResourceProcessor<Resource<AdresseExtern>> {

    /**
     * Service for processing AdresseExtern entities.
     */
    @Autowired
    AdresseExternResourceService resourceService;

    @Override
    public Resource<AdresseExtern> process(Resource<AdresseExtern> resource) {
        // Hand the resource to the service.
        resourceService.process(resource);
        return resource;
    }

}
