package de.muenchen.vaadin.demo.api.rest;

import de.muenchen.vaadin.demo.api.domain.Buerger;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resources;

import java.util.Collection;
import java.util.Optional;

/**
 *
 * @author p.mueller
 */
public interface BuergerRestClient {
    Resources<BuergerResource> findAll();

    Resources<BuergerResource> findAll(Link relation);

    Optional<BuergerResource> findOne(Link link);

    void setRelations(Link link, Collection<Link> links);

    void create(Buerger buerger);

    void update(BuergerResource buerger);

    void delete(Link id);

}
