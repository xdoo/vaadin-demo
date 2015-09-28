package de.muenchen.vaadin.demo.api.rest;

import de.muenchen.vaadin.demo.api.local.Buerger;
import org.springframework.hateoas.Link;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Provides a simple RestClient for Buergers that implements the default Spring Data Rest endpoints.
 *
 * @author p.mueller
 * @version 1.0
 */
public interface BuergerRestClient {

    /**
     * The name of the Base Endpoint
     */
    String BUERGERS = "buergers";

    /**
     * Get all the Buergers (with matching tenancy).
     *
     * @return a List of all Buergers.
     */
    List<Buerger> findAll();

    /**
     * Get all the Buergers (with matching tenancy) on a specific endpoint.
     * <p>
     *     For example: <code>http://localhost:80/buergers/1/kinder</code>
     * </p>
     * @param relation
     * @return
     */
    List<Buerger> findAll(Link relation);

    List<Buerger> findFullTextFuzzy(String filter);

    /**
     * Try to find one Buerger (with matching tenancy) by its ID / self relation.
     *
     * @param link The ID / self relation of the Buerger.
     * @return an optional of the Buerger.
     */
    Optional<Buerger> findOne(Link link);

    /**
     * Set all the relations, specified by links, on a specific relation endpoint.
     * <p>
     * Example for an Endpoint: <code>http://localhost:80/buergers/1/kinder</code>
     * </p>
     * <p>
     * This method will always (re-) set the associations. The add or delete
     * functionality is not provided at RestClient level.
     * </p>
     *
     * @param endpoint The endpoint of the relation.
     * @param links    The links that are set to be related.
     */
    void setRelations(Link endpoint, Collection<Link> links);

    /**
     * Set the relation, specified by @relation, on a specific relation endpoint.
     * <p>
     * Example for an Endpoint: <code>http://localhost:80/buergers/1/kinder</code>
     * </p>
     * <p>
     * This method will always (re-) set the association. The add or delete
     * functionality is not provided at RestClient level.
     * </p>
     *
     * @param endpoint The endpoint of the relation.
     * @param relation    The link that is set to be related.
     */
    void setRelation(Link endpoint, Link relation);

    /**
     * Create a new Buerger, the returned Buerger will have the Links and its ID.
     * @param buerger The Buerger to create, it must not contain any links.
     * @return the created Buerger (with ID).
     */
    Buerger create(Buerger buerger);

    /**
     * Update the fields of a Buerger by the {@link Buerger#getId()}.
     *
     * @param buerger The updated Buerger that will be saved.
     * @return the updated version from the REST Server.
     */
    Buerger update(Buerger buerger);

    /**
     * Try to delete an ID.
     * @param id The link to the id.
     */
    void delete(Link id);


}
