package de.muenchen.itm.infrastructure.auth.services;

import de.muenchen.itm.infrastructure.auth.entities.Authority;
import org.springframework.hateoas.Resource;

/*
 * This file will be overwritten on every change of the model!
 * This file was automatically generated by GAIA.
 */

/**
 * Provides methods to alter resources before being sent to a client.
 */
public interface AuthorityResourceService {
    /**
     * Process a resource. You can add links and alter the entity itself.
     */
    Resource<Authority> process(Resource<Authority> resource);
}

