package de.muenchen.vaadin.services;

import de.muenchen.vaadin.demo.api.local.Buerger;
import org.springframework.hateoas.Link;

import java.util.List;
import java.util.Optional;

/**
 * Service zum Verwalten von Bürgern aus der GUI heraus.
 * 
 * @author claus.straube
 */
public interface BuergerService {

    Buerger create(Buerger buerger);

    Buerger update(Buerger buerger);

    boolean delete(Link link);

    List<Buerger> findAll();

    List<Buerger> findAll(Link relation);

    Optional<Buerger> findOne(Link link);

    List<Buerger> queryBuerger(String query);

    boolean setRelations(Link link, List<Link> relations);

    boolean setRelation(Link link, Link relation);
}
