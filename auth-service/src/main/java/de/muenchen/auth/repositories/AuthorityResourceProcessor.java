package de.muenchen.auth.repositories;

import de.muenchen.auth.entities.Authority;
import de.muenchen.auth.services.AuthorityResourceService;
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
public class AuthorityResourceProcessor implements ResourceProcessor<Resource<Authority>> {

    /**
     * Service for processing Authority_ entities.
     */
    @Autowired
    AuthorityResourceService resourceService;

    @Override
    public Resource<Authority> process(Resource<Authority> resource) {
        // Hand the resource to the service.
        resourceService.process(resource);
        return resource;
    }

}

