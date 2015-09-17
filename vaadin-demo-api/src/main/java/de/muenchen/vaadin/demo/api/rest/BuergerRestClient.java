package de.muenchen.vaadin.demo.api.rest;

import de.muenchen.vaadin.demo.api.local.Buerger;
import org.springframework.hateoas.Link;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author p.mueller
 */
public interface BuergerRestClient {
    List<Buerger> findAll();

    List<Buerger> findAll(Link relation);

    Optional<Buerger> findOne(Link link);

    void setRelations(Link link, Collection<Link> links);

    Buerger create(Buerger buerger);

    Buerger update(Buerger buerger);

    void delete(Link id);

}
