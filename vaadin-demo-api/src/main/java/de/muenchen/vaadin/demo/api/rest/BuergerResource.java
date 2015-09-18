package de.muenchen.vaadin.demo.api.rest;

import de.muenchen.vaadin.demo.api.domain.BuergerDTO;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;

/**
 * A simple wrapper for the Resource of a Buerger, this should not be changed.
 * <p>
 *     Contains the ParameterizedTypeReference for multiple Resources.
 * </p>
 * @author p.mueller
 * @version 1.0
 */
public class BuergerResource extends Resource<BuergerDTO> {
    /**
     * This is a simple way to get the Resources "class" for the {@link org.springframework.web.client.RestTemplate#exchange(String, HttpMethod, HttpEntity, ParameterizedTypeReference, Object...)}.
     */
    public static final ParameterizedTypeReference<Resources<BuergerResource>> LIST = new ParameterizedTypeReference<Resources<BuergerResource>>() {
    };

    /**
     * Create a new Resource with a blank BuergerDTO.
     */
    public BuergerResource() {
        super(new BuergerDTO());
    }

    /**
     * Create a new Resource from  a BuergerDTO and multiple Resources.
     * @param content The BuergerDTO
     * @param links The links to add to the resource
     */
    public BuergerResource(BuergerDTO content, Link... links) {
        super(content, links);
    }

    /**
     * Create a new Resource from  a BuergerDTO and multiple Resources.
     * @param content The BuergerDTO
     * @param links The links to add to the resource
     */
    public BuergerResource(BuergerDTO content, Iterable<Link> links) {
        super(content, links);
    }
}
