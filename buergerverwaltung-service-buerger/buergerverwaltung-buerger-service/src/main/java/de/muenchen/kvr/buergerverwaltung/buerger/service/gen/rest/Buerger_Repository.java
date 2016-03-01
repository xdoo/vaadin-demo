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

import de.muenchen.kvr.buergerverwaltung.buerger.service.gen.domain.Augenfarben_;
import de.muenchen.kvr.buergerverwaltung.buerger.service.gen.domain.MoeglicheStaatsangehoerigkeiten_;
import de.muenchen.kvr.buergerverwaltung.buerger.service.gen.domain.Buerger_;

/**
* Provides a Repository for a {@link Buerger_}. This Repository can be exported as a REST Resource.
* <p>
* The Repository handles CRUD Operations. Every Operation is secured and takes care of the tenancy.
* For specific Documentation on how the generated REST point behaves, please consider the Spring Data Rest Reference
* <a href="http://docs.spring.io/spring-data/rest/docs/current/reference/html/">here</a>.
* </p>
*/
@RepositoryRestResource(exported = true,
	path="buergers", collectionResourceRel="buergers")
@PreAuthorize("hasAuthority('buerger_READ_Buerger')")
public interface Buerger_Repository extends CrudRepository<Buerger_, UUID> {
	
	/**
	 * Name for the specific cache.
	 */
	String CACHE = "BUERGER_CACHE";
	
	/**
	 * Get all the Buerger_ entities.
	 *
	 * @return an Iterable of the Buerger_ entities with the same Tenancy.
	 */
	@Override
	Iterable<Buerger_> findAll();
	
	/**
	 * Get one specific Buerger_ by its unique oid.
	 *
	 * @param oid The identifier of the Buerger_.
	 * @return The Buerger_ with the requested oid.
	 */
	@Override
	@Cacheable(value = CACHE, key = "#p0")
	Buerger_ findOne(UUID oid);
	
	/**
	 * Create or update a Buerger_.
	 * <p>
	 * If the oid already exists, the Buerger_ will be overridden, hence update.
	 * If the oid does no already exist, a new Buerger_ will be created, hence create.
	 * </p>
	 *
	 * @param buerger The Buerger_ that will be saved.
	 * @return the saved Buerger_.
	 */
	@SuppressWarnings("unchecked")
	@Override
	@CachePut(value = CACHE, key = "#p0.oid")
	@PreAuthorize("hasAuthority('buerger_WRITE_Buerger')")
	Buerger_ save(Buerger_ buerger);
	
	/**
	 * Delete the Buerger_ by a specified oid.
	 *
	 * @param oid the unique oid of the Buerger_ that will be deleted.
	 */
	@Override
	@CacheEvict(value = CACHE, key = "#p0")
	@PreAuthorize("hasAuthority('buerger_DELETE_Buerger')")
	void delete(UUID oid);
	
	/**
	 * Delete a Buerger_ by entity.
	 *
	 * @param entity The Buerger_ that will be deleted.
	 */
	@Override
	@CacheEvict(value = CACHE, key = "#p0.oid")
	@PreAuthorize("hasAuthority('buerger_DELETE_Buerger')")
	void delete(Buerger_ entity);
	
	/**
	 * Delete multiple Buerger_ entities by their oid.
	 *
	 * @param entities The Iterable of Buerger_ entities that will be deleted.
	 */
	@Override
	@CacheEvict(value = CACHE, allEntries = true)
	@PreAuthorize("hasAuthority('buerger_DELETE_Buerger')")
	void delete(Iterable<? extends Buerger_> entities);
	
	/**
	 * Delete all Buerger_ entities.
	 */
	@Override
	@CacheEvict(value = CACHE, allEntries = true)
	@PreAuthorize("hasAuthority('buerger_DELETE_Buerger')")
	void deleteAll();
	
	Buerger_ findByVorname(@Param(value= "vorname") String vorname);
	Buerger_ findByNachname(@Param(value= "nachname") String nachname);
	Buerger_ findByGeburtstag(@Param(value= "geburtstag") java.time.LocalDate geburtstag);
	Buerger_ findByAugenfarbe(@Param(value= "augenfarbe") Augenfarben_ augenfarbe);
	Buerger_ findByTelefonnummer(@Param(value= "telefonnummer") long telefonnummer);
	Buerger_ findByEmail(@Param(value= "email") String email);
	Buerger_ findByLebendig(@Param(value= "lebendig") boolean lebendig);
	Buerger_ findByStaatsangehoerigkeiten(@Param(value= "staatsangehoerigkeiten") java.util.List<MoeglicheStaatsangehoerigkeiten_> staatsangehoerigkeiten);
	Buerger_ findByEigenschaften(@Param(value= "eigenschaften") java.util.List<String> eigenschaften);
	List<Buerger_> findBuergerByVorname(@Param("vorname") String vorname);
	List<Buerger_> findBuergerByNachname(@Param("nachname") String nachname);
	List<Buerger_> findBuergerByGeburtstag(@Param("geburtstag") String geburtstag);
	List<Buerger_> findBuergerByAugenfarbe(@Param("augenfarbe") String augenfarbe);
	List<Buerger_> findBuergerByTelefonnummer(@Param("telefonnummer") String telefonnummer);
	List<Buerger_> findBuergerByEmail(@Param("email") String email);
	List<Buerger_> findBuergerByLebendig(@Param("lebendig") String lebendig);
	
	/**
	 * Find the Buerger_ entities with a kinder relation to the Buerger with the given oid.
	 * @param oid the unique oid of the Buerger that will be searched for in the kinder relation.
	 */
	java.util.List<Buerger_> findByKinderOid(@Param(value = "oid") UUID oid);
	/**
	 * Find the Buerger_ with a partner relation to the Buerger with the given oid.
	 * @param oid the unique oid of the Buerger that will be searched for in the partner relation.
	 */
	Buerger_ findByPartnerOid(@Param(value = "oid") UUID oid);
	/**
	 * Find the Buerger_ entities with a pass relation to the Pass with the given oid.
	 * @param oid the unique oid of the Pass that will be searched for in the pass relation.
	 */
	java.util.List<Buerger_> findByPassOid(@Param(value = "oid") UUID oid);
	/**
	 * Find the Buerger_ entities with a sachbearbeiter relation to the Sachbearbeiter with the given oid.
	 * @param oid the unique oid of the Sachbearbeiter that will be searched for in the sachbearbeiter relation.
	 */
	java.util.List<Buerger_> findBySachbearbeiterOid(@Param(value = "oid") UUID oid);
	/**
	 * Find the Buerger_ entities with a wohnungen relation to the Wohnung with the given oid.
	 * @param oid the unique oid of the Wohnung that will be searched for in the wohnungen relation.
	 */
	java.util.List<Buerger_> findByWohnungenOid(@Param(value = "oid") UUID oid);
}
