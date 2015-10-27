package de.muenchen.demo.service.gen.rest;

import de.muenchen.demo.service.gen.domain.Augenfarben;
import de.muenchen.demo.service.gen.domain.Buerger;
import de.muenchen.service.TenantService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreFilter;

import java.util.List;

/**
 * Provides a Repository for a {@link Buerger}. This Repository can be exported as a REST Resource.
 * <p>
 * The Repository handles CRUD Operations. Every Operation is secured and takes care of the tenancy.
 * For specific Documentation on how the generated REST point behaves, please consider the Spring Data Rest Reference
 * <a href="http://docs.spring.io/spring-data/rest/docs/current/reference/html/">here</a>.
 * </p>
 */
@SuppressWarnings("SpringElInspection")
@RepositoryRestResource(exported = true,
        path = "buergers", collectionResourceRel = "buergers")
@PreAuthorize("hasRole('ROLE_READ_Buerger')")
public interface BuergerRepository extends CrudRepository<Buerger, Long> {

    /**
     * Name for the specific cache.
     */
    String CACHE = "BUERGER_CACHE";

    /**
     * Get all the Buerger entities that match the current users tenancy.
     * <p>
     * Buerger entities that belong to another tenant will get filtered out.
     * </p>
     *
     * @return an Iterable of the Buerger entities with the same Tenancy.
     */
    @Override
    @PostFilter(TenantService.IS_TENANT_FILTER)
    Iterable<Buerger> findAll();

    /**
     * Get one specific Buerger by its unique oid.
     * <p>
     * If the tenant id does not match, no Permission on this resource will be granted.
     * </p>
     *
     * @param oid The identifier of the Buerger.
     * @return The Buerger with the requested oid.
     */
    @Override
    @Cacheable(value = CACHE, key = "#p0")
    @PreAuthorize("hasRole('ROLE_READ_Buerger')")
    @PostAuthorize(TenantService.IS_TENANT_AUTH)
    Buerger findOne(Long oid);

    /**
     * Create or update a Buerger.
     * <p>
     * If the oid already exists, the Buerger will be overridden, hence update.
     * If the oid does no already exist, a new Buerger will be created, hence create.
     * </p>
     * <p>
     * The Buerger can only be saved if the tenant matches the tenant of the current User.
     * </p>
     *
     * @param buerger The Buerger that will be saved.
     * @return the saved Buerger.
     */
    @SuppressWarnings("unchecked")
    @Override
    @CachePut(value = CACHE, key = "#p0.oid")
    @PreAuthorize("hasRole('ROLE_WRITE_Buerger')")
    Buerger save(Buerger buerger);

    /**
     * Delete the Buerger by a specified oid.
     * <p>
     * The Buerger can only be deleted if the tenant matches.
     * </p>
     *
     * @param oid the unique oid of the Buerger that will be deleted.
     */
    @Override
    @CacheEvict(value = CACHE, key = "#p0")
    @PreAuthorize("hasRole('ROLE_DELETE_Buerger')")
    @PostAuthorize(TenantService.IS_TENANT_AUTH)
    void delete(Long oid);

    /**
     * Delete a Buerger by entity.
     * <p>
     * The delete is only permitted if the tenant is matching.
     * </p>
     *
     * @param entity The Buerger that will be deleted.
     */
    @Override
    @CacheEvict(value = CACHE, key = "#p0.oid")
    @PreAuthorize("hasRole('ROLE_DELETE_Buerger')")
    @PostAuthorize(TenantService.IS_TENANT_AUTH)
    void delete(Buerger entity);

    /**
     * Delete multiple Buerger entities by their oid.
     * <p>
     * Only the Buerger entities with matching tenant will be deleted.
     * </p>
     *
     * @param entities The Iterable of Buerger entities that will be deleted.
     */
    @Override
    @CacheEvict(value = CACHE, allEntries = true)
    @PreAuthorize("hasRole('ROLE_DELETE_Buerger')")
    @PreFilter(TenantService.IS_TENANT_FILTER)
    void delete(Iterable<? extends Buerger> entities);

    /**
     * Delete all Buerger entities.
     * <p>
     * Only the Buerger entities with matching tenant will be deleted.
     * </p>
     */
    @Override
    @CacheEvict(value = CACHE, allEntries = true)
    @PreAuthorize("hasRole('ROLE_DELETE_Buerger')")
    @PreFilter(TenantService.IS_TENANT_FILTER)
    void deleteAll();

    Buerger findByVorname(@Param(value = "vorname") String vorname);

    Buerger findByNachname(@Param(value = "nachname") String nachname);

    Buerger findByGeburtsdatum(@Param(value = "geburtsdatum") java.util.Date geburtsdatum);

    Buerger findByAugenfarbe(@Param(value = "augenfarbe") Augenfarben augenfarbe);

    Buerger findByAlive(@Param(value = "alive") Boolean alive);

    @PreAuthorize("hasRole('ROLE_READ_Buerger')")
    List<Buerger> findBuergerByVorname(@Param("vorname") String vorname);

    @PreAuthorize("hasRole('ROLE_READ_Buerger')")
    List<Buerger> findBuergerByNachname(@Param("nachname") String nachname);

    @PreAuthorize("hasRole('ROLE_READ_Buerger')")
    List<Buerger> findBuergerByGeburtsdatum(@Param("geburtsdatum") String geburtsdatum);

    @PreAuthorize("hasRole('ROLE_READ_Buerger')")
    List<Buerger> findBuergerByAugenfarbe(@Param("augenfarbe") String augenfarbe);

    /**
     * Find the Buerger entities with a staatsangehoerigkeiten relation to the Staatsangehoerigkeit with the given oid.
     *
     * @param oid the unique oid of the Staatsangehoerigkeit that will be searched for in the staatsangehoerigkeiten relation.
     */
    List<Buerger> findByStaatsangehoerigkeitenOid(@Param(value = "oid") Long oid);

    /**
     * Find the Buerger with a paesse relation to the Pass with the given oid.
     *
     * @param oid the unique oid of the Pass that will be searched for in the paesse relation.
     */
    Buerger findByPaesseOid(@Param(value = "oid") Long oid);

    /**
     * Find the Buerger with a wohnungen relation to the Wohnung with the given oid.
     *
     * @param oid the unique oid of the Wohnung that will be searched for in the wohnungen relation.
     */
    Buerger findByWohnungenOid(@Param(value = "oid") Long oid);

    /**
     * Find the Buerger entities with a sachbearbeiter relation to the Sachbearbeiter with the given oid.
     *
     * @param oid the unique oid of the Sachbearbeiter that will be searched for in the sachbearbeiter relation.
     */
    List<Buerger> findBySachbearbeiterOid(@Param(value = "oid") Long oid);

    /**
     * Find the Buerger with a kinder relation to the Buerger with the given oid.
     *
     * @param oid the unique oid of the Buerger that will be searched for in the kinder relation.
     */
    Buerger findByKinderOid(@Param(value = "oid") Long oid);

    /**
     * Find the Buerger with a partner relation to the Buerger with the given oid.
     *
     * @param oid the unique oid of the Buerger that will be searched for in the partner relation.
     */
    Buerger findByPartnerOid(@Param(value = "oid") Long oid);
}
