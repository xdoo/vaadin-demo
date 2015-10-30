package de.muenchen.demo.service.domain;

import de.muenchen.service.TenantService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreFilter;

import java.util.Date;
import java.util.List;

/**
 * Provides a Repository for a {@link Buerger}. This Repository can be exported as a REST Resource.
 * <p>
 * The Repository handles CRUD Operations. Every Operation is secured and takes care of the tenancy.
 * For specific Documentation on how the generated REST point behaves, please consider the Spring Data Rest Reference
 * <a href="http://docs.spring.io/spring-data/rest/docs/current/reference/html/">here</a>.
 * </p>
 *
 * @author p.mueller
 * @version 1.0
 */
@SuppressWarnings("SpringElInspection")
@PreAuthorize("hasRole('ROLE_READ_Buerger')")
public interface BuergerRepository extends JpaRepository<Buerger, Long> {

    /**
     * Name for the specific cache.
     */
    String BUERGER_CACHE = "BUERGER_CACHE";

    /**
     * Get all the Buergers that match the current tenancy.
     * <p>
     * The Buergers that belong to another tenant will get filtered out.
     * </p>
     *
     * @return an Iterable of the Buergers with the same Tenancy.
     */
    @Override
    @PostFilter(TenantService.IS_TENANT_FILTER)
    List<Buerger> findAll();


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
    @Cacheable(value = BUERGER_CACHE, key = "#p0")
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
    @CachePut(value = BUERGER_CACHE, key = "#p0.oid")
    @PreAuthorize("hasRole('ROLE_WRITE_Buerger') and " + TenantService.IS_TENANT_AUTH)
    Buerger save(Buerger buerger);

    /**
     * Delete the Buerger by a specified oid.
     * <p>
     * The Buerger can only be deleted if the tenant matches.
     * </p>
     *
     * @param aLong the unique oid of the Buerger that will be deleted.
     */
    @Override
    @CacheEvict(value = BUERGER_CACHE, key = "#p0")
    @PreAuthorize("hasRole('ROLE_DELETE_Buerger')")
    @PostAuthorize(TenantService.IS_TENANT_AUTH)
    void delete(Long aLong);

    /**
     * Delete multiple Buergers by their oid.
     * <p>
     * Only the Buergers with matching tenant will be deleted.
     * </p>
     *
     * @param iterable The Iterable of Buergers that will be deleted.
     */
    @Override
    @CacheEvict(value = BUERGER_CACHE, allEntries = true)
    @PreAuthorize("hasRole('ROLE_DELETE_Buerger')")
    @PreFilter(TenantService.IS_TENANT_FILTER)
    void delete(Iterable<? extends Buerger> iterable);

    /**
     * Delete all Buergers.
     * <p>
     * Only the Buergers with matching tenant will be deleted.
     * </p>
     */
    @Override
    @CacheEvict(value = BUERGER_CACHE, allEntries = true)
    @PreAuthorize("hasRole('ROLE_DELETE_Buerger')")
    @PreFilter(TenantService.IS_TENANT_FILTER)
    void deleteAll();

    /**
     * Delete a Buerger by entity.
     * <p>
     * The delete is only permitted if the tenant is matching.
     * </p>
     *
     * @param entity The buerger that will be deleted.
     */
    @Override
    @CacheEvict(value = BUERGER_CACHE, key = "#p0.oid")
    @PreAuthorize("hasRole('ROLE_DELETE_Buerger')")
    @PostAuthorize(TenantService.IS_TENANT_AUTH)
    void delete(Buerger entity);

    @PreAuthorize("hasRole('ROLE_READ_Buerger')")
    @PostFilter(TenantService.IS_TENANT_FILTER)
    List<Buerger> findBuergerByNachname(@Param("nachname") String nachname);

    @PreAuthorize("hasRole('ROLE_READ_Buerger')")
    @PostFilter(TenantService.IS_TENANT_FILTER)
    List<Buerger> findBuergerByVorname(@Param("vorname") String vorname);

    @PreAuthorize("hasRole('ROLE_READ_Buerger')")
    @PostFilter(TenantService.IS_TENANT_FILTER)
    List<Buerger> findBuergerByGeburtsdatum(@Param("geburtsdatum") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date geburtsdatum);
}
