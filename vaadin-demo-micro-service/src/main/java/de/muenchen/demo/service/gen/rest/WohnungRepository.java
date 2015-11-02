package de.muenchen.demo.service.gen.rest;

import de.muenchen.demo.service.gen.domain.Wohnung;
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

/**
 * Provides a Repository for a {@link Wohnung}. This Repository can be exported as a REST Resource.
 * <p>
 * The Repository handles CRUD Operations. Every Operation is secured and takes care of the tenancy.
 * For specific Documentation on how the generated REST point behaves, please consider the Spring Data Rest Reference
 * <a href="http://docs.spring.io/spring-data/rest/docs/current/reference/html/">here</a>.
 * </p>
 */
@SuppressWarnings("SpringElInspection")
@RepositoryRestResource(exported = true,
        path = "wohnungs", collectionResourceRel = "wohnungs")
@PreAuthorize("hasRole('ROLE_READ_Wohnung')")
public interface WohnungRepository extends CrudRepository<Wohnung, Long> {

    /**
     * Name for the specific cache.
     */
    String CACHE = "WOHNUNG_CACHE";

    /**
     * Get all the Wohnung entities that match the current users tenancy.
     * <p>
     * Wohnung entities that belong to another tenant will get filtered out.
     * </p>
     *
     * @return an Iterable of the Wohnung entities with the same Tenancy.
     */
    @Override
    @PostFilter(TenantService.IS_TENANT_FILTER)
    Iterable<Wohnung> findAll();

    /**
     * Get one specific Wohnung by its unique oid.
     * <p>
     * If the tenant id does not match, no Permission on this resource will be granted.
     * </p>
     *
     * @param oid The identifier of the Wohnung.
     * @return The Wohnung with the requested oid.
     */
    @Override
    @Cacheable(value = CACHE, key = "#p0")
    @PreAuthorize("hasRole('ROLE_READ_Wohnung')")
    @PostAuthorize(TenantService.IS_TENANT_AUTH)
    Wohnung findOne(Long oid);

    /**
     * Create or update a Wohnung.
     * <p>
     * If the oid already exists, the Wohnung will be overridden, hence update.
     * If the oid does no already exist, a new Wohnung will be created, hence create.
     * </p>
     * <p>
     * The Wohnung can only be saved if the tenant matches the tenant of the current User.
     * </p>
     *
     * @param wohnung The Wohnung that will be saved.
     * @return the saved Wohnung.
     */
    @SuppressWarnings("unchecked")
    @Override
    @CachePut(value = CACHE, key = "#p0.oid")
    @PreAuthorize("hasRole('ROLE_WRITE_Wohnung')")
    Wohnung save(Wohnung wohnung);

    /**
     * Delete the Wohnung by a specified oid.
     * <p>
     * The Wohnung can only be deleted if the tenant matches.
     * </p>
     *
     * @param oid the unique oid of the Wohnung that will be deleted.
     */
    @Override
    @CacheEvict(value = CACHE, key = "#p0")
    @PreAuthorize("hasRole('ROLE_DELETE_Wohnung')")
    @PostAuthorize(TenantService.IS_TENANT_AUTH)
    void delete(Long oid);

    /**
     * Delete a Wohnung by entity.
     * <p>
     * The delete is only permitted if the tenant is matching.
     * </p>
     *
     * @param entity The Wohnung that will be deleted.
     */
    @Override
    @CacheEvict(value = CACHE, key = "#p0.oid")
    @PreAuthorize("hasRole('ROLE_DELETE_Wohnung')")
    @PostAuthorize(TenantService.IS_TENANT_AUTH)
    void delete(Wohnung entity);

    /**
     * Delete multiple Wohnung entities by their oid.
     * <p>
     * Only the Wohnung entities with matching tenant will be deleted.
     * </p>
     *
     * @param entities The Iterable of Wohnung entities that will be deleted.
     */
    @Override
    @CacheEvict(value = CACHE, allEntries = true)
    @PreAuthorize("hasRole('ROLE_DELETE_Wohnung')")
    @PreFilter(TenantService.IS_TENANT_FILTER)
    void delete(Iterable<? extends Wohnung> entities);

    /**
     * Delete all Wohnung entities.
     * <p>
     * Only the Wohnung entities with matching tenant will be deleted.
     * </p>
     */
    @Override
    @CacheEvict(value = CACHE, allEntries = true)
    @PreAuthorize("hasRole('ROLE_DELETE_Wohnung')")
    @PreFilter(TenantService.IS_TENANT_FILTER)
    void deleteAll();

    Wohnung findByStock(@Param(value = "stock") String stock);

    Wohnung findByAusrichtung(@Param(value = "ausrichtung") String ausrichtung);

    /**
     * Find the Wohnung with a adresse relation to the Adresse with the given oid.
     *
     * @param oid the unique oid of the Adresse that will be searched for in the adresse relation.
     */
    Wohnung findByAdresseOid(@Param(value = "oid") Long oid);
}
