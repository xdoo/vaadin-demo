package de.muenchen.kvr.buergerverwaltung.buerger.service.gen.rest;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PreAuthorize;

import de.muenchen.kvr.buergerverwaltung.buerger.service.gen.domain.Sachbearbeiter_;

/**
* Provides a Repository for a {@link Sachbearbeiter_}. This Repository can be exported as a REST Resource.
* <p>
* The Repository handles CRUD Operations. Every Operation is secured and takes care of the tenancy.
* For specific Documentation on how the generated REST point behaves, please consider the Spring Data Rest Reference
* <a href="http://docs.spring.io/spring-data/rest/docs/current/reference/html/">here</a>.
* </p>
*/
@RepositoryRestResource(exported = true,
	path="sachbearbeiters", collectionResourceRel="sachbearbeiters")
@PreAuthorize("hasAuthority('buerger_READ_Sachbearbeiter')")
public interface Sachbearbeiter_Repository extends CrudRepository<Sachbearbeiter_, Long> {
	
	/**
	 * Name for the specific cache.
	 */
	String CACHE = "SACHBEARBEITER_CACHE";
	
	/**
	 * Get all the Sachbearbeiter_ entities.
	 *
	 * @return an Iterable of the Sachbearbeiter_ entities with the same Tenancy.
	 */
	@Override
	Iterable<Sachbearbeiter_> findAll();
	
	/**
	 * Get one specific Sachbearbeiter_ by its unique oid.
	 *
	 * @param oid The identifier of the Sachbearbeiter_.
	 * @return The Sachbearbeiter_ with the requested oid.
	 */
	@Override
	@Cacheable(value = CACHE, key = "#p0")
	Sachbearbeiter_ findOne(Long oid);
	
	/**
	 * Create or update a Sachbearbeiter_.
	 * <p>
	 * If the oid already exists, the Sachbearbeiter_ will be overridden, hence update.
	 * If the oid does no already exist, a new Sachbearbeiter_ will be created, hence create.
	 * </p>
	 *
	 * @param sachbearbeiter The Sachbearbeiter_ that will be saved.
	 * @return the saved Sachbearbeiter_.
	 */
	@SuppressWarnings("unchecked")
	@Override
	@CachePut(value = CACHE, key = "#p0.oid")
	@PreAuthorize("hasAuthority('buerger_WRITE_Sachbearbeiter')")
	Sachbearbeiter_ save(Sachbearbeiter_ sachbearbeiter);
	
	/**
	 * Delete the Sachbearbeiter_ by a specified oid.
	 *
	 * @param oid the unique oid of the Sachbearbeiter_ that will be deleted.
	 */
	@Override
	@CacheEvict(value = CACHE, key = "#p0")
	@PreAuthorize("hasAuthority('buerger_DELETE_Sachbearbeiter')")
	void delete(Long oid);
	
	/**
	 * Delete a Sachbearbeiter_ by entity.
	 *
	 * @param entity The Sachbearbeiter_ that will be deleted.
	 */
	@Override
	@CacheEvict(value = CACHE, key = "#p0.oid")
	@PreAuthorize("hasAuthority('buerger_DELETE_Sachbearbeiter')")
	void delete(Sachbearbeiter_ entity);
	
	/**
	 * Delete multiple Sachbearbeiter_ entities by their oid.
	 *
	 * @param entities The Iterable of Sachbearbeiter_ entities that will be deleted.
	 */
	@Override
	@CacheEvict(value = CACHE, allEntries = true)
	@PreAuthorize("hasAuthority('buerger_DELETE_Sachbearbeiter')")
	void delete(Iterable<? extends Sachbearbeiter_> entities);
	
	/**
	 * Delete all Sachbearbeiter_ entities.
	 */
	@Override
	@CacheEvict(value = CACHE, allEntries = true)
	@PreAuthorize("hasAuthority('buerger_DELETE_Sachbearbeiter')")
	void deleteAll();
	
	Sachbearbeiter_ findByTelefon(@Param(value= "telefon") long telefon);
	Sachbearbeiter_ findByFax(@Param(value= "fax") long fax);
	Sachbearbeiter_ findByFunktion(@Param(value= "funktion") String funktion);
	Sachbearbeiter_ findByOrganisationseinheit(@Param(value= "organisationseinheit") String organisationseinheit);
	List<Sachbearbeiter_> findSachbearbeiterByTelefon(@Param("telefon") String telefon);
	List<Sachbearbeiter_> findSachbearbeiterByFax(@Param("fax") String fax);
	List<Sachbearbeiter_> findSachbearbeiterByFunktion(@Param("funktion") String funktion);
	List<Sachbearbeiter_> findSachbearbeiterByOrganisationseinheit(@Param("organisationseinheit") String organisationseinheit);
	
}
