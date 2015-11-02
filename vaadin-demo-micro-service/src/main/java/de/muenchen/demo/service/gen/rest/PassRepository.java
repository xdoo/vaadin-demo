package de.muenchen.demo.service.gen.rest;

import de.muenchen.demo.service.gen.domain.Pass;
import de.muenchen.demo.service.gen.domain.PassTyp;
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
 * Provides a Repository for a {@link Pass}. This Repository can be exported as a REST Resource.
 * <p>
 * The Repository handles CRUD Operations. Every Operation is secured and takes care of the tenancy.
 * For specific Documentation on how the generated REST point behaves, please consider the Spring Data Rest Reference
 * <a href="http://docs.spring.io/spring-data/rest/docs/current/reference/html/">here</a>.
 * </p>
 */
@SuppressWarnings("SpringElInspection")
@RepositoryRestResource(exported = true,
        path = "passs", collectionResourceRel = "passs")
@PreAuthorize("hasRole('ROLE_READ_Pass')")
public interface PassRepository extends JpaRepository<Pass, Long> {

    /**
     * Name for the specific cache.
     */
    String CACHE = "PASS_CACHE";

    /**
     * Get all the Pass entities that match the current users tenancy.
     * <p>
     * Pass entities that belong to another tenant will get filtered out.
     * </p>
     *
     * @return an Iterable of the Pass entities with the same Tenancy.
     */
    @Override
    @PostFilter(TenantService.IS_TENANT_FILTER)
    List<Pass> findAll();

    /**
     * Get one specific Pass by its unique oid.
     * <p>
     * If the tenant id does not match, no Permission on this resource will be granted.
     * </p>
     *
     * @param oid The identifier of the Pass.
     * @return The Pass with the requested oid.
     */
    @Override
    @Cacheable(value = CACHE, key = "#p0")
    @PreAuthorize("hasRole('ROLE_READ_Pass')")
    @PostAuthorize(TenantService.IS_TENANT_AUTH)
    Pass findOne(Long oid);

    /**
     * Create or update a Pass.
     * <p>
     * If the oid already exists, the Pass will be overridden, hence update.
     * If the oid does no already exist, a new Pass will be created, hence create.
     * </p>
     * <p>
     * The Pass can only be saved if the tenant matches the tenant of the current User.
     * </p>
     *
     * @param pass The Pass that will be saved.
     * @return the saved Pass.
     */
    @SuppressWarnings("unchecked")
    @Override
    @CachePut(value = CACHE, key = "#p0.oid")
    @PreAuthorize("hasRole('ROLE_WRITE_Pass')")
    Pass save(Pass pass);

    /**
     * Delete the Pass by a specified oid.
     * <p>
     * The Pass can only be deleted if the tenant matches.
     * </p>
     *
     * @param oid the unique oid of the Pass that will be deleted.
     */
    @Override
    @CacheEvict(value = CACHE, key = "#p0")
    @PreAuthorize("hasRole('ROLE_DELETE_Pass')")
    @PostAuthorize(TenantService.IS_TENANT_AUTH)
    void delete(Long oid);

    /**
     * Delete a Pass by entity.
     * <p>
     * The delete is only permitted if the tenant is matching.
     * </p>
     *
     * @param entity The Pass that will be deleted.
     */
    @Override
    @CacheEvict(value = CACHE, key = "#p0.oid")
    @PreAuthorize("hasRole('ROLE_DELETE_Pass')")
    @PostAuthorize(TenantService.IS_TENANT_AUTH)
    void delete(Pass entity);

    /**
     * Delete multiple Pass entities by their oid.
     * <p>
     * Only the Pass entities with matching tenant will be deleted.
     * </p>
     *
     * @param entities The Iterable of Pass entities that will be deleted.
     */
    @Override
    @CacheEvict(value = CACHE, allEntries = true)
    @PreAuthorize("hasRole('ROLE_DELETE_Pass')")
    @PreFilter(TenantService.IS_TENANT_FILTER)
    void delete(Iterable<? extends Pass> entities);

    /**
     * Delete all Pass entities.
     * <p>
     * Only the Pass entities with matching tenant will be deleted.
     * </p>
     */
    @Override
    @CacheEvict(value = CACHE, allEntries = true)
    @PreAuthorize("hasRole('ROLE_DELETE_Pass')")
    @PreFilter(TenantService.IS_TENANT_FILTER)
    void deleteAll();

    Pass findByPassNummer(@Param(value = "passNummer") Long passNummer);

    Pass findByTyp(@Param(value = "typ") PassTyp typ);

    Pass findByKode(@Param(value = "kode") String kode);

    Pass findByGroesse(@Param(value = "groesse") Long groesse);

    Pass findByBehoerde(@Param(value = "behoerde") String behoerde);

}
