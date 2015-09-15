package de.muenchen.vaadin.demo.api.rest;

import de.muenchen.vaadin.demo.api.local.LocalBuerger;
import org.springframework.hateoas.Link;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author p.mueller
 */
public interface BuergerRestClient {
    List<LocalBuerger> findAll();

    List<LocalBuerger> findAll(Link relation);

    Optional<LocalBuerger> findOne(Link link);

    void setRelations(Link link, Collection<Link> links);

    LocalBuerger create(LocalBuerger buerger);

    LocalBuerger update(LocalBuerger buerger);

    void delete(Link id);

}
