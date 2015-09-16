package de.muenchen.vaadin.services;

import de.muenchen.vaadin.demo.api.local.LocalBuerger;
import org.springframework.hateoas.Link;

import java.util.List;
import java.util.Optional;

/**
 * Service zum Verwalten von Bürgern aus der GUI heraus.
 * 
 * @author claus.straube
 */
public interface BuergerService {

    LocalBuerger create(LocalBuerger buerger);

    LocalBuerger update(LocalBuerger buerger);

    void delete(Link link);

    LocalBuerger copy(Link link);

    List<LocalBuerger> findAll();

    List<LocalBuerger> findAll(Link relation);

    Optional<LocalBuerger> findOne(Link link);

    List<LocalBuerger> queryBuerger(String query);

    void setRelations(Link link, List<Link> relations);
}
