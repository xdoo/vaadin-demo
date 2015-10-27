package de.muenchen.demo.service.gen.services;

import de.muenchen.demo.service.gen.domain.Adresse;
import org.springframework.hateoas.Resource;

/*
 * This file will be overwritten on every change of the model!
 * This file was automatically generated by GAIA.
 */

/**
 * Provides methods to alter resources before being sent to a client.
 */
public interface AdresseResourceService {
    /**
     * Process a resource. You can add links and alter the entity itself.
     */
    Resource<Adresse> process(Resource<Adresse> resource);
}