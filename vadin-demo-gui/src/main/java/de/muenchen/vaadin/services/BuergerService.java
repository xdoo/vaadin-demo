package de.muenchen.vaadin.services;

import de.muenchen.vaadin.demo.api.domain.Buerger;
import de.muenchen.vaadin.demo.api.rest.BuergerResource;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resources;

import java.util.List;
import java.util.Optional;

/**
 * Service zum Verwalten von Bürgern aus der GUI heraus.
 * 
 * @author claus.straube
 */
public interface BuergerService {

    void create(Buerger buerger);

    void update(BuergerResource buerger);

    void delete(Link link);

    void copy(Link link);

    Resources<BuergerResource> findAll();

    Resources<BuergerResource> findAll(Link relation);

    Optional<BuergerResource> findOne(Link link);

    Resources<BuergerResource> queryBuerger(String query);

    void setRelations(Link link, List<Link> kinder);
}
