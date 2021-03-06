package de.muenchen.kvr.buergerverwaltung.buerger.client.rest;

import de.muenchen.kvr.buergerverwaltung.buerger.client.local.Wohnung_;
import org.springframework.hateoas.Link;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/*
 * This file will be overwritten on every change of the model!
 * This file was automatically generated by GAIA.
 */
public interface Wohnung_RestClient {

    /**
     * The name of the Base Endpoint
     */
    public static final String WOHNUNGS = "wohnungs";

    /**
     * Get all the Wohnungs (with matching tenancy).
     *
     * @return a List of all Wohnungs.
     */
    List<Wohnung_> findAll();

    /**
     * Get all the Wohnungs (with matching tenancy) on a specific endpoint.
     * <p>
     *     For example: <code>http://localhost:80/entitys/1/attribute</code>
     * </p>
     * @param relation
     * @return
     */
    List<Wohnung_> findAll(Link relation);
    
    /**
     * Get all Wohnung matched with string due to fuzzy search.
     * @param filter the string for the query
     * @return
     */
    List<Wohnung_> findFullTextFuzzy(String filter);

    /**
     * Try to find one Wohnung (with matching tenancy) by its ID / self relation.
     *
     * @param link The ID / self relation of the Wohnung_.
     * @return an optional of the Wohnung_.
     */
    Optional<Wohnung_> findOne(Link link);

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
     * Create a new Wohnung_, the returned Wohnung_ will have the Links and its ID.
     * @param wohnung The Wohnung_ to create, it must not contain any links.
     * @return the created Wohnung_ (with ID).
     */
    Wohnung_ create(Wohnung_ wohnung);

    /**
     * Update the fields of a Wohnung_ by the {@link Wohnung_#getId()}.
     *
     * @param wohnung The updated Wohnung_ that will be saved.
     * @return the updated version from the REST Server.
     */
    Wohnung_ update(Wohnung_ wohnung);

    /**
     * Try to delete an ID.
     * @param id The link to the id.
     */
    void delete(Link id);
}
