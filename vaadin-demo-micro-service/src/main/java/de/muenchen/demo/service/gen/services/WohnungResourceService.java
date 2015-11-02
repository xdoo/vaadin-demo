package de.muenchen.demo.service.gen.services;

import de.muenchen.demo.service.gen.domain.Wohnung;
import org.springframework.hateoas.Resource;

/*
 * This file will be overwritten on every change of the model!
 * This file was automatically generated by GAIA.
 */

/**
 * Provides methods to alter resources before being sent to a client.
 */
public interface WohnungResourceService {
    /**
     * Process a resource. You can add links and alter the entity itself.
     */
    Resource<Wohnung> process(Resource<Wohnung> resource);
}
