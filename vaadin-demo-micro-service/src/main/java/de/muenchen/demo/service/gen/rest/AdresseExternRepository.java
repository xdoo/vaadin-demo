package de.muenchen.demo.service.gen.rest;

import de.muenchen.demo.service.gen.domain.AdresseExtern;
import de.muenchen.service.TenantService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreFilter;

import java.util.List;

/**
 * Provides a Repository for a {@link AdresseExtern}. This Repository can be exported as a REST Resource.
 * <p>
 * The Repository handles CRUD Operations. Every Operation is secured and takes care of the tenancy.
 * For specific Documentation on how the generated REST point behaves, please consider the Spring Data Rest Reference
 * <a href="http://docs.spring.io/spring-data/rest/docs/current/reference/html/">here</a>.
 * </p>
 */
@SuppressWarnings("SpringElInspection")
@RepositoryRestResource(exported = true,
        path = "adresseExterns", collectionResourceRel = "adresseExterns")
@PreAuthorize("hasRole('ROLE_READ_AdresseExtern')")
public interface AdresseExternRepository extends JpaRepository<AdresseExtern, Long> {

    /**
     * Name for the specific cache.
     */
    String CACHE = "ADRESSEEXTERN_CACHE";

    /**
     * Get all the AdresseExtern entities that match the current users tenancy.
     * <p>
     * AdresseExtern entities that belong to another tenant will get filtered out.
     * </p>
     *
     * @return an Iterable of the AdresseExtern entities with the same Tenancy.
     */
    @Override
    @PostFilter(TenantService.IS_TENANT_FILTER)
    List<AdresseExtern> findAll();

    /**
     * Get one specific AdresseExtern by its unique oid.
     * <p>
     * If the tenant id does not match, no Permission on this resource will be granted.
     * </p>
     *
     * @param oid The identifier of the AdresseExtern.
     * @return The AdresseExtern with the requested oid.
     */
    @Override
    @Cacheable(value = CACHE, key = "#p0")
    @PreAuthorize("hasRole('ROLE_READ_AdresseExtern')")
    @PostAuthorize(TenantService.IS_TENANT_AUTH)
    AdresseExtern findOne(Long oid);

    /**
     * Create or update a AdresseExtern.
     * <p>
     * If the oid already exists, the AdresseExtern will be overridden, hence update.
     * If the oid does no already exist, a new AdresseExtern will be created, hence create.
     * </p>
     * <p>
     * The AdresseExtern can only be saved if the tenant matches the tenant of the current User.
     * </p>
     *
     * @param adresseExtern The AdresseExtern that will be saved.
     * @return the saved AdresseExtern.
     */
    @SuppressWarnings("unchecked")
    @Override
    @CachePut(value = CACHE, key = "#p0.oid")
    @PreAuthorize("hasRole('ROLE_WRITE_AdresseExtern')")
    AdresseExtern save(AdresseExtern adresseExtern);

    /**
     * Delete the AdresseExtern by a specified oid.
     * <p>
     * The AdresseExtern can only be deleted if the tenant matches.
     * </p>
     *
     * @param oid the unique oid of the AdresseExtern that will be deleted.
     */
    @Override
    @CacheEvict(value = CACHE, key = "#p0")
    @PreAuthorize("hasRole('ROLE_DELETE_AdresseExtern')")
    @PostAuthorize(TenantService.IS_TENANT_AUTH)
    void delete(Long oid);

    /**
     * Delete a AdresseExtern by entity.
     * <p>
     * The delete is only permitted if the tenant is matching.
     * </p>
     *
     * @param entity The AdresseExtern that will be deleted.
     */
    @Override
    @CacheEvict(value = CACHE, key = "#p0.oid")
    @PreAuthorize("hasRole('ROLE_DELETE_AdresseExtern')")
    @PostAuthorize(TenantService.IS_TENANT_AUTH)
    void delete(AdresseExtern entity);

    /**
     * Delete multiple AdresseExtern entities by their oid.
     * <p>
     * Only the AdresseExtern entities with matching tenant will be deleted.
     * </p>
     *
     * @param entities The Iterable of AdresseExtern entities that will be deleted.
     */
    @Override
    @CacheEvict(value = CACHE, allEntries = true)
    @PreAuthorize("hasRole('ROLE_DELETE_AdresseExtern')")
    @PreFilter(TenantService.IS_TENANT_FILTER)
    void delete(Iterable<? extends AdresseExtern> entities);

    /**
     * Delete all AdresseExtern entities.
     * <p>
     * Only the AdresseExtern entities with matching tenant will be deleted.
     * </p>
     */
    @Override
    @CacheEvict(value = CACHE, allEntries = true)
    @PreAuthorize("hasRole('ROLE_DELETE_AdresseExtern')")
    @PreFilter(TenantService.IS_TENANT_FILTER)
    void deleteAll();

    AdresseExtern findByStrasse(@Param(value = "strasse") String strasse);

    AdresseExtern findByHausnummer(@Param(value = "hausnummer") Long hausnummer);

    AdresseExtern findByPlz(@Param(value = "plz") Long plz);

    AdresseExtern findByOrt(@Param(value = "ort") String ort);

    @PreAuthorize("hasRole('ROLE_READ_AdresseExtern')")
    List<AdresseExtern> findAdresseExternByStrasse(@Param("strasse") String strasse);

    @PreAuthorize("hasRole('ROLE_READ_AdresseExtern')")
    List<AdresseExtern> findAdresseExternByHausnummer(@Param("hausnummer") String hausnummer);

    @PreAuthorize("hasRole('ROLE_READ_AdresseExtern')")
    List<AdresseExtern> findAdresseExternByPlz(@Param("plz") String plz);

    @PreAuthorize("hasRole('ROLE_READ_AdresseExtern')")
    List<AdresseExtern> findAdresseExternByOrt(@Param("ort") String ort);

}
