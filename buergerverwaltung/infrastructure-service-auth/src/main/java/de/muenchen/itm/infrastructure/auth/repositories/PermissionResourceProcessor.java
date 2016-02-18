package de.muenchen.itm.infrastructure.auth.repositories;

import de.muenchen.itm.infrastructure.auth.entities.Permission;
import de.muenchen.itm.infrastructure.auth.services.PermissionResourceService;
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
public class PermissionResourceProcessor implements ResourceProcessor<Resource<Permission>> {

    /**
     * Service for processing Permission_ entities.
     */
    @Autowired
    PermissionResourceService resourceService;

    @Override
    public Resource<Permission> process(Resource<Permission> resource) {
        // Hand the resource to the service.
        resourceService.process(resource);
        return resource;
    }

}

