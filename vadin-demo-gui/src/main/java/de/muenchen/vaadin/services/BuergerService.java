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

    void create(LocalBuerger buerger);

    void update(LocalBuerger buerger);

    void delete(Link link);

    void copy(Link link);

    List<LocalBuerger> findAll();

    List<LocalBuerger> findAll(Link relation);

    Optional<LocalBuerger> findOne(Link link);

    List<LocalBuerger> queryBuerger(String query);

    void setRelations(Link link, List<Link> relations);
}
