package de.muenchen.kvr.buergerverwaltung.buerger.service.gen.controller.resource;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.stereotype.Component;

import de.muenchen.kvr.buergerverwaltung.buerger.service.gen.domain.Wohnung_;
import de.muenchen.kvr.buergerverwaltung.buerger.service.gen.services.resource.Wohnung_ResourceService;

/*
 * This file will be overwritten on every change of the model!
 * This file was automatically generated by GAIA.
 */
/**
 * This RessourceProcessor can alter Resources, before they are sent out.
 */
@Component
public class Wohnung_ResourceProcessor implements ResourceProcessor<Resource<Wohnung_>> {

	/**
	 * Service for processing Wohnung_ entities.
	 */
	@Autowired
	Wohnung_ResourceService resourceService;

	@Override
    public Resource<Wohnung_> process(Resource<Wohnung_> resource) {
	    // Hand the resource to the service.
		resourceService.process(resource);
        return resource;
    }
    
}

