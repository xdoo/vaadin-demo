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

import de.muenchen.kvr.buergerverwaltung.buerger.service.gen.domain.Passtyp_;
import de.muenchen.kvr.buergerverwaltung.buerger.service.gen.domain.Pass_;

/**
* Provides a Repository for a {@link Pass_}. This Repository can be exported as a REST Resource.
* <p>
* The Repository handles CRUD Operations. Every Operation is secured and takes care of the tenancy.
* For specific Documentation on how the generated REST point behaves, please consider the Spring Data Rest Reference
* <a href="http://docs.spring.io/spring-data/rest/docs/current/reference/html/">here</a>.
* </p>
*/
@RepositoryRestResource(exported = true,
	path="passs", collectionResourceRel="passs")
@PreAuthorize("hasAuthority('buerger_READ_Pass')")
public interface Pass_Repository extends CrudRepository<Pass_, UUID> {
	
	/**
	 * Name for the specific cache.
	 */
	String CACHE = "PASS_CACHE";
	
	/**
	 * Get all the Pass_ entities.
	 *
	 * @return an Iterable of the Pass_ entities with the same Tenancy.
	 */
	@Override
	Iterable<Pass_> findAll();
	
	/**
	 * Get one specific Pass_ by its unique oid.
	 *
	 * @param oid The identifier of the Pass_.
	 * @return The Pass_ with the requested oid.
	 */
	@Override
	@Cacheable(value = CACHE, key = "#p0")
	Pass_ findOne(UUID oid);
	
	/**
	 * Create or update a Pass_.
	 * <p>
	 * If the oid already exists, the Pass_ will be overridden, hence update.
	 * If the oid does no already exist, a new Pass_ will be created, hence create.
	 * </p>
	 *
	 * @param pass The Pass_ that will be saved.
	 * @return the saved Pass_.
	 */
	@SuppressWarnings("unchecked")
	@Override
	@CachePut(value = CACHE, key = "#p0.oid")
	@PreAuthorize("hasAuthority('buerger_WRITE_Pass')")
	Pass_ save(Pass_ pass);
	
	/**
	 * Delete the Pass_ by a specified oid.
	 *
	 * @param oid the unique oid of the Pass_ that will be deleted.
	 */
	@Override
	@CacheEvict(value = CACHE, key = "#p0")
	@PreAuthorize("hasAuthority('buerger_DELETE_Pass')")
	void delete(UUID oid);
	
	/**
	 * Delete a Pass_ by entity.
	 *
	 * @param entity The Pass_ that will be deleted.
	 */
	@Override
	@CacheEvict(value = CACHE, key = "#p0.oid")
	@PreAuthorize("hasAuthority('buerger_DELETE_Pass')")
	void delete(Pass_ entity);
	
	/**
	 * Delete multiple Pass_ entities by their oid.
	 *
	 * @param entities The Iterable of Pass_ entities that will be deleted.
	 */
	@Override
	@CacheEvict(value = CACHE, allEntries = true)
	@PreAuthorize("hasAuthority('buerger_DELETE_Pass')")
	void delete(Iterable<? extends Pass_> entities);
	
	/**
	 * Delete all Pass_ entities.
	 */
	@Override
	@CacheEvict(value = CACHE, allEntries = true)
	@PreAuthorize("hasAuthority('buerger_DELETE_Pass')")
	void deleteAll();
	
	Pass_ findByPassNummer(@Param(value= "passNummer") long passNummer);
	Pass_ findByTyp(@Param(value= "typ") Passtyp_ typ);
	Pass_ findByKode(@Param(value= "kode") String kode);
	Pass_ findByGroesse(@Param(value= "groesse") long groesse);
	Pass_ findByBehoerde(@Param(value= "behoerde") String behoerde);
	List<Pass_> findPassByPassNummer(@Param("passNummer") String passNummer);
	List<Pass_> findPassByTyp(@Param("typ") String typ);
	List<Pass_> findPassByKode(@Param("kode") String kode);
	List<Pass_> findPassByGroesse(@Param("groesse") String groesse);
	List<Pass_> findPassByBehoerde(@Param("behoerde") String behoerde);
	
}
