package de.muenchen.kvr.buergerverwaltung.buerger.client.rest;

/*
 * This file will be overwritten on every change of the model!
 * This file was automatically generated by GAIA.
 */

import de.muenchen.kvr.buergerverwaltung.buerger.client.domain.Buerger_DTO;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;

/*
 * This file will be overwritten on every change of the model!
 * This file was automatically generated by GAIA.
 */
public class Buerger_Resource extends Resource<Buerger_DTO> {
	/**
     * This is a simple way to get the Resources "class" for the {@link org.springframework.web.client.RestTemplate#exchange(String, HttpMethod, HttpEntity, ParameterizedTypeReference, Object...)}.
     */
    public static final ParameterizedTypeReference<Resources<Buerger_Resource>> LIST = new ParameterizedTypeReference<Resources<Buerger_Resource>>() {
    };

    /**
     * Create a new Resource with a blank Buerger_DTO.
     */
    public Buerger_Resource() {
        super(new Buerger_DTO());
    }

    /**
     * Create a new Resource from  a Buerger_DTO and multiple Resources.
     * @param content The Buerger_DTO
     * @param links The links to add to the resource
     */
    public Buerger_Resource(Buerger_DTO content, Link... links) {
        super(content, links);
    }

    /**
     * Create a new Resource from  a Buerger_DTO and multiple Resources.
     * @param content The Buerger_DTO
     * @param links The links to add to the resource
     */
    public Buerger_Resource(Buerger_DTO content, Iterable<Link> links) {
        super(content, links);
    }
}
