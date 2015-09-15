package de.muenchen.vaadin.demo.api.rest;

import de.muenchen.vaadin.demo.apilib.rest.BaseResource;

import java.util.Date;
import de.muenchen.vaadin.demo.api.domain.Buerger;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;

/**
 * @author p.mueller
 */
public class BuergerResource extends Resource<Buerger> {
    public static final ParameterizedTypeReference<Resources<BuergerResource>> LIST = new ParameterizedTypeReference<Resources<BuergerResource>>() {
    };

    public BuergerResource() {
        super(new Buerger());
    }

    public BuergerResource(Buerger content, Link... links) {
        super(content, links);
    }

    public BuergerResource(Buerger content, Iterable<Link> links) {
        super(content, links);
    }

    public enum Rel {
        kinder, partner
    }
}
