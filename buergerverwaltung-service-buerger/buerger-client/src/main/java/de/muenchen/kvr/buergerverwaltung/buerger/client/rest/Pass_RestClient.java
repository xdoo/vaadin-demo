package de.muenchen.kvr.buergerverwaltung.buerger.client.rest;

import de.muenchen.kvr.buergerverwaltung.buerger.client.local.Pass_;
import org.springframework.hateoas.Link;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/*
 * This file will be overwritten on every change of the model!
 * This file was automatically generated by GAIA.
 */
public interface Pass_RestClient {

    /**
     * The name of the Base Endpoint
     */
    public static final String PASSS = "passs";

    /**
     * Get all the Passs (with matching tenancy).
     *
     * @return a List of all Passs.
     */
    List<Pass_> findAll();

    /**
     * Get all the Passs (with matching tenancy) on a specific endpoint.
     * <p>
     *     For example: <code>http://localhost:80/entitys/1/attribute</code>
     * </p>
     * @param relation
     * @return
     */
    List<Pass_> findAll(Link relation);
    
    /**
     * Get all Pass matched with string due to fuzzy search.
     * @param filter the string for the query
     * @return
     */
    List<Pass_> findFullTextFuzzy(String filter);

    /**
     * Try to find one Pass (with matching tenancy) by its ID / self relation.
     *
     * @param link The ID / self relation of the Pass_.
     * @return an optional of the Pass_.
     */
    Optional<Pass_> findOne(Link link);

    /**
     * Set all the relations, specified by links, on a specific relation endpoint.
     * <p>
     * Example for an Endpoint: <code>http://localhost:80/entitys/1/attribute</code>
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
	 * Example for an Endpoint: <code>http://localhost:80/entitys/1/attribute</code>
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
     * Create a new Pass_, the returned Pass_ will have the Links and its ID.
     * @param pass The Pass_ to create, it must not contain any links.
     * @return the created Pass_ (with ID).
     */
    Pass_ create(Pass_ pass);

    /**
     * Update the fields of a Pass_ by the {@link Pass_#getId()}.
     *
     * @param pass The updated Pass_ that will be saved.
     * @return the updated version from the REST Server.
     */
    Pass_ update(Pass_ pass);

    /**
     * Try to delete an ID.
     * @param id The link to the id.
     */
    void delete(Link id);
}
