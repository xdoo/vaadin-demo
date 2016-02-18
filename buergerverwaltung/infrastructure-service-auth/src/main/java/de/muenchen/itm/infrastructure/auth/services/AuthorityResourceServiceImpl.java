package de.muenchen.itm.infrastructure.auth.services;

import de.muenchen.itm.infrastructure.auth.entities.Authority;
import org.springframework.hateoas.Resource;
import org.springframework.stereotype.Service;

/*
 * This file will NOT be overwritten by GAIA.
 * This file was automatically generated by GAIA.
 */

/**
 * Provides methods to alter resources before being sent to a client.
 * If used as generated by GAIA this service will be autowired and called by Authority_ResourceProcessor.
 */
@Service
public class AuthorityResourceServiceImpl implements AuthorityResourceService {
    // If you need access to the database you can autowire a Repository.
    // Repositories are generated into the package: .gen.rest
    //
    // @Autowired
    // <EntityName>Repository repo;

    /**
     * Process a resource.
     * You can add links and alter the entity itself.
     * When used as generated by GAIA this method will be called whenever a resource is sent to a client.
     */
    public Resource<Authority> process(Resource<Authority> resource) {
        // Implement your logic here.
        return resource;
    }
}
