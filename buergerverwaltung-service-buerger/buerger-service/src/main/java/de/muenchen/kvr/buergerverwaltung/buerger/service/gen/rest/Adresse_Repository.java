package de.muenchen.kvr.buergerverwaltung.buerger.service.gen.rest;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PreAuthorize;

import de.muenchen.kvr.buergerverwaltung.buerger.service.gen.domain.Adresse_;

/**
* Provides a Repository for a {@link Adresse_}. This Repository can be exported as a REST Resource.
* <p>
* The Repository handles CRUD Operations. Every Operation is secured and takes care of the tenancy.
* For specific Documentation on how the generated REST point behaves, please consider the Spring Data Rest Reference
* <a href="http://docs.spring.io/spring-data/rest/docs/current/reference/html/">here</a>.
* </p>
*/
@RepositoryRestResource(exported = true,
	path="adresses", collectionResourceRel="adresses")
@PreAuthorize("hasAuthority('buerger_READ_Adresse')")
public interface Adresse_Repository extends CrudRepository<Adresse_, Long> {
	
	/**
	 * Name for the specific cache.
	 */
	String CACHE = "ADRESSE_CACHE";
	
	/**
	 * Get all the Adresse_ entities.
	 *
	 * @return an Iterable of the Adresse_ entities with the same Tenancy.
	 */
	@Override
	Iterable<Adresse_> findAll();
	
	/**
	 * Get one specific Adresse_ by its unique oid.
	 *
	 * @param oid The identifier of the Adresse_.
	 * @return The Adresse_ with the requested oid.
	 */
	@Override
	@Cacheable(value = CACHE, key = "#p0")
	Adresse_ findOne(Long oid);
	
	/**
	 * Create or update a Adresse_.
	 * <p>
	 * If the oid already exists, the Adresse_ will be overridden, hence update.
	 * If the oid does no already exist, a new Adresse_ will be created, hence create.
	 * </p>
	 *
	 * @param adresse The Adresse_ that will be saved.
	 * @return the saved Adresse_.
	 */
	@SuppressWarnings("unchecked")
	@Override
	@CachePut(value = CACHE, key = "#p0.oid")
	@PreAuthorize("hasAuthority('buerger_WRITE_Adresse')")
	Adresse_ save(Adresse_ adresse);
	
	/**
	 * Delete the Adresse_ by a specified oid.
	 *
	 * @param oid the unique oid of the Adresse_ that will be deleted.
	 */
	@Override
	@CacheEvict(value = CACHE, key = "#p0")
	@PreAuthorize("hasAuthority('buerger_DELETE_Adresse')")
	void delete(Long oid);
	
	/**
	 * Delete a Adresse_ by entity.
	 *
	 * @param entity The Adresse_ that will be deleted.
	 */
	@Override
	@CacheEvict(value = CACHE, key = "#p0.oid")
	@PreAuthorize("hasAuthority('buerger_DELETE_Adresse')")
	void delete(Adresse_ entity);
	
	/**
	 * Delete multiple Adresse_ entities by their oid.
	 *
	 * @param entities The Iterable of Adresse_ entities that will be deleted.
	 */
	@Override
	@CacheEvict(value = CACHE, allEntries = true)
	@PreAuthorize("hasAuthority('buerger_DELETE_Adresse')")
	void delete(Iterable<? extends Adresse_> entities);
	
	/**
	 * Delete all Adresse_ entities.
	 */
	@Override
	@CacheEvict(value = CACHE, allEntries = true)
	@PreAuthorize("hasAuthority('buerger_DELETE_Adresse')")
	void deleteAll();
	
	
}
