package de.muenchen.vaadin.demo.api.rest;

import de.muenchen.vaadin.demo.api.domain.BuergerDTO;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;

/**
 * @author p.mueller
 */
public class BuergerResource extends Resource<BuergerDTO> {
    public static final ParameterizedTypeReference<Resources<BuergerResource>> LIST = new ParameterizedTypeReference<Resources<BuergerResource>>() {
    };

    public BuergerResource() {
        super(new BuergerDTO());
    }

    public BuergerResource(BuergerDTO content, Link... links) {
        super(content, links);
    }

    public BuergerResource(BuergerDTO content, Iterable<Link> links) {
        super(content, links);
    }
}
