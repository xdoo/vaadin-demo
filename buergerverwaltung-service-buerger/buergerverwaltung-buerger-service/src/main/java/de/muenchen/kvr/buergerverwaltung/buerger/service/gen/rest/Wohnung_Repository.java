package de.muenchen.kvr.buergerverwaltung.buerger.service.gen.rest;

import java.util.List;
import java.util.UUID;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PreAuthorize;

import de.muenchen.kvr.buergerverwaltung.buerger.service.gen.domain.Wohnung_;

/**
* Provides a Repository for a {@link Wohnung_}. This Repository can be exported as a REST Resource.
* <p>
* The Repository handles CRUD Operations. Every Operation is secured and takes care of the tenancy.
* For specific Documentation on how the generated REST point behaves, please consider the Spring Data Rest Reference
* <a href="http://docs.spring.io/spring-data/rest/docs/current/reference/html/">here</a>.
* </p>
*/
@RepositoryRestResource(exported = true,
	path="wohnungs", collectionResourceRel="wohnungs")
@PreAuthorize("hasAuthority('buerger_READ_Wohnung')")
public interface Wohnung_Repository extends CrudRepository<Wohnung_, UUID> {
	
	/**
	 * Name for the specific cache.
	 */
	String CACHE = "WOHNUNG_CACHE";
	
	/**
	 * Get all the Wohnung_ entities.
	 *
	 * @return an Iterable of the Wohnung_ entities with the same Tenancy.
	 */
	@Override
	Iterable<Wohnung_> findAll();
	
	/**
	 * Get one specific Wohnung_ by its unique oid.
	 *
	 * @param oid The identifier of the Wohnung_.
	 * @return The Wohnung_ with the requested oid.
	 */
	@Override
	@Cacheable(value = CACHE, key = "#p0")
	Wohnung_ findOne(UUID oid);
	
	/**
	 * Create or update a Wohnung_.
	 * <p>
	 * If the oid already exists, the Wohnung_ will be overridden, hence update.
	 * If the oid does no already exist, a new Wohnung_ will be created, hence create.
	 * </p>
	 *
	 * @param wohnung The Wohnung_ that will be saved.
	 * @return the saved Wohnung_.
	 */
	@SuppressWarnings("unchecked")
	@Override
	@CachePut(value = CACHE, key = "#p0.oid")
	@PreAuthorize("hasAuthority('buerger_WRITE_Wohnung')")
	Wohnung_ save(Wohnung_ wohnung);
	
	/**
	 * Delete the Wohnung_ by a specified oid.
	 *
	 * @param oid the unique oid of the Wohnung_ that will be deleted.
	 */
	@Override
	@CacheEvict(value = CACHE, key = "#p0")
	@PreAuthorize("hasAuthority('buerger_DELETE_Wohnung')")
	void delete(UUID oid);
	
	/**
	 * Delete a Wohnung_ by entity.
	 *
	 * @param entity The Wohnung_ that will be deleted.
	 */
	@Override
	@CacheEvict(value = CACHE, key = "#p0.oid")
	@PreAuthorize("hasAuthority('buerger_DELETE_Wohnung')")
	void delete(Wohnung_ entity);
	
	/**
	 * Delete multiple Wohnung_ entities by their oid.
	 *
	 * @param entities The Iterable of Wohnung_ entities that will be deleted.
	 */
	@Override
	@CacheEvict(value = CACHE, allEntries = true)
	@PreAuthorize("hasAuthority('buerger_DELETE_Wohnung')")
	void delete(Iterable<? extends Wohnung_> entities);
	
	/**
	 * Delete all Wohnung_ entities.
	 */
	@Override
	@CacheEvict(value = CACHE, allEntries = true)
	@PreAuthorize("hasAuthority('buerger_DELETE_Wohnung')")
	void deleteAll();
	
	Wohnung_ findByStock(@Param(value= "stock") String stock);
	Wohnung_ findByAusrichtung(@Param(value= "ausrichtung") String ausrichtung);
	List<Wohnung_> findWohnungByStock(@Param("stock") String stock);
	List<Wohnung_> findWohnungByAusrichtung(@Param("ausrichtung") String ausrichtung);
	
}
