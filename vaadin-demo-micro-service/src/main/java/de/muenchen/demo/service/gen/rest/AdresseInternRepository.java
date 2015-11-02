package de.muenchen.demo.service.gen.rest;

import de.muenchen.demo.service.gen.domain.AdresseIntern;
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
 * Provides a Repository for a {@link AdresseIntern}. This Repository can be exported as a REST Resource.
 * <p>
 * The Repository handles CRUD Operations. Every Operation is secured and takes care of the tenancy.
 * For specific Documentation on how the generated REST point behaves, please consider the Spring Data Rest Reference
 * <a href="http://docs.spring.io/spring-data/rest/docs/current/reference/html/">here</a>.
 * </p>
 */
@SuppressWarnings("SpringElInspection")
@RepositoryRestResource(exported = true,
        path = "adresseInterns", collectionResourceRel = "adresseInterns")
@PreAuthorize("hasRole('ROLE_READ_AdresseIntern')")
public interface AdresseInternRepository extends CrudRepository<AdresseIntern, Long> {

    /**
     * Name for the specific cache.
     */
    String CACHE = "ADRESSEINTERN_CACHE";

    /**
     * Get all the AdresseIntern entities that match the current users tenancy.
     * <p>
     * AdresseIntern entities that belong to another tenant will get filtered out.
     * </p>
     *
     * @return an Iterable of the AdresseIntern entities with the same Tenancy.
     */
    @Override
    @PostFilter(TenantService.IS_TENANT_FILTER)
    Iterable<AdresseIntern> findAll();

    /**
     * Get one specific AdresseIntern by its unique oid.
     * <p>
     * If the tenant id does not match, no Permission on this resource will be granted.
     * </p>
     *
     * @param oid The identifier of the AdresseIntern.
     * @return The AdresseIntern with the requested oid.
     */
    @Override
    @Cacheable(value = CACHE, key = "#p0")
    @PreAuthorize("hasRole('ROLE_READ_AdresseIntern')")
    @PostAuthorize(TenantService.IS_TENANT_AUTH)
    AdresseIntern findOne(Long oid);

    /**
     * Create or update a AdresseIntern.
     * <p>
     * If the oid already exists, the AdresseIntern will be overridden, hence update.
     * If the oid does no already exist, a new AdresseIntern will be created, hence create.
     * </p>
     * <p>
     * The AdresseIntern can only be saved if the tenant matches the tenant of the current User.
     * </p>
     *
     * @param adresseIntern The AdresseIntern that will be saved.
     * @return the saved AdresseIntern.
     */
    @SuppressWarnings("unchecked")
    @Override
    @CachePut(value = CACHE, key = "#p0.oid")
    @PreAuthorize("hasRole('ROLE_WRITE_AdresseIntern')")
    AdresseIntern save(AdresseIntern adresseIntern);

    /**
     * Delete the AdresseIntern by a specified oid.
     * <p>
     * The AdresseIntern can only be deleted if the tenant matches.
     * </p>
     *
     * @param oid the unique oid of the AdresseIntern that will be deleted.
     */
    @Override
    @CacheEvict(value = CACHE, key = "#p0")
    @PreAuthorize("hasRole('ROLE_DELETE_AdresseIntern')")
    @PostAuthorize(TenantService.IS_TENANT_AUTH)
    void delete(Long oid);

    /**
     * Delete a AdresseIntern by entity.
     * <p>
     * The delete is only permitted if the tenant is matching.
     * </p>
     *
     * @param entity The AdresseIntern that will be deleted.
     */
    @Override
    @CacheEvict(value = CACHE, key = "#p0.oid")
    @PreAuthorize("hasRole('ROLE_DELETE_AdresseIntern')")
    @PostAuthorize(TenantService.IS_TENANT_AUTH)
    void delete(AdresseIntern entity);

    /**
     * Delete multiple AdresseIntern entities by their oid.
     * <p>
     * Only the AdresseIntern entities with matching tenant will be deleted.
     * </p>
     *
     * @param entities The Iterable of AdresseIntern entities that will be deleted.
     */
    @Override
    @CacheEvict(value = CACHE, allEntries = true)
    @PreAuthorize("hasRole('ROLE_DELETE_AdresseIntern')")
    @PreFilter(TenantService.IS_TENANT_FILTER)
    void delete(Iterable<? extends AdresseIntern> entities);

    /**
     * Delete all AdresseIntern entities.
     * <p>
     * Only the AdresseIntern entities with matching tenant will be deleted.
     * </p>
     */
    @Override
    @CacheEvict(value = CACHE, allEntries = true)
    @PreAuthorize("hasRole('ROLE_DELETE_AdresseIntern')")
    @PreFilter(TenantService.IS_TENANT_FILTER)
    void deleteAll();

    AdresseIntern findByStrassenSchluessel(@Param(value = "strassenSchluessel") Long strassenSchluessel);

    AdresseIntern findByHausnummer(@Param(value = "hausnummer") Long hausnummer);

}
