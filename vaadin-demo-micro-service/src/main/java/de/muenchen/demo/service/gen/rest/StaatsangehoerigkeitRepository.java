package de.muenchen.demo.service.gen.rest;

import de.muenchen.demo.service.gen.domain.MoeglicheStaatsangehoerigkeiten;
import de.muenchen.demo.service.gen.domain.Staatsangehoerigkeit;
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
 * Provides a Repository for a {@link Staatsangehoerigkeit}. This Repository can be exported as a REST Resource.
 * <p>
 * The Repository handles CRUD Operations. Every Operation is secured and takes care of the tenancy.
 * For specific Documentation on how the generated REST point behaves, please consider the Spring Data Rest Reference
 * <a href="http://docs.spring.io/spring-data/rest/docs/current/reference/html/">here</a>.
 * </p>
 */
@SuppressWarnings("SpringElInspection")
@RepositoryRestResource(exported = true,
        path = "staatsangehoerigkeits", collectionResourceRel = "staatsangehoerigkeits")
@PreAuthorize("hasRole('ROLE_READ_Staatsangehoerigkeit')")
public interface StaatsangehoerigkeitRepository extends CrudRepository<Staatsangehoerigkeit, Long> {

    /**
     * Name for the specific cache.
     */
    String CACHE = "STAATSANGEHOERIGKEIT_CACHE";

    /**
     * Get all the Staatsangehoerigkeit entities that match the current users tenancy.
     * <p>
     * Staatsangehoerigkeit entities that belong to another tenant will get filtered out.
     * </p>
     *
     * @return an Iterable of the Staatsangehoerigkeit entities with the same Tenancy.
     */
    @Override
    @PostFilter(TenantService.IS_TENANT_FILTER)
    Iterable<Staatsangehoerigkeit> findAll();

    /**
     * Get one specific Staatsangehoerigkeit by its unique oid.
     * <p>
     * If the tenant id does not match, no Permission on this resource will be granted.
     * </p>
     *
     * @param oid The identifier of the Staatsangehoerigkeit.
     * @return The Staatsangehoerigkeit with the requested oid.
     */
    @Override
    @Cacheable(value = CACHE, key = "#p0")
    @PreAuthorize("hasRole('ROLE_READ_Staatsangehoerigkeit')")
    @PostAuthorize(TenantService.IS_TENANT_AUTH)
    Staatsangehoerigkeit findOne(Long oid);

    /**
     * Create or update a Staatsangehoerigkeit.
     * <p>
     * If the oid already exists, the Staatsangehoerigkeit will be overridden, hence update.
     * If the oid does no already exist, a new Staatsangehoerigkeit will be created, hence create.
     * </p>
     * <p>
     * The Staatsangehoerigkeit can only be saved if the tenant matches the tenant of the current User.
     * </p>
     *
     * @param staatsangehoerigkeit The Staatsangehoerigkeit that will be saved.
     * @return the saved Staatsangehoerigkeit.
     */
    @SuppressWarnings("unchecked")
    @Override
    @CachePut(value = CACHE, key = "#p0.oid")
    @PreAuthorize("hasRole('ROLE_WRITE_Staatsangehoerigkeit')")
    Staatsangehoerigkeit save(Staatsangehoerigkeit staatsangehoerigkeit);

    /**
     * Delete the Staatsangehoerigkeit by a specified oid.
     * <p>
     * The Staatsangehoerigkeit can only be deleted if the tenant matches.
     * </p>
     *
     * @param oid the unique oid of the Staatsangehoerigkeit that will be deleted.
     */
    @Override
    @CacheEvict(value = CACHE, key = "#p0")
    @PreAuthorize("hasRole('ROLE_DELETE_Staatsangehoerigkeit')")
    @PostAuthorize(TenantService.IS_TENANT_AUTH)
    void delete(Long oid);

    /**
     * Delete a Staatsangehoerigkeit by entity.
     * <p>
     * The delete is only permitted if the tenant is matching.
     * </p>
     *
     * @param entity The Staatsangehoerigkeit that will be deleted.
     */
    @Override
    @CacheEvict(value = CACHE, key = "#p0.oid")
    @PreAuthorize("hasRole('ROLE_DELETE_Staatsangehoerigkeit')")
    @PostAuthorize(TenantService.IS_TENANT_AUTH)
    void delete(Staatsangehoerigkeit entity);

    /**
     * Delete multiple Staatsangehoerigkeit entities by their oid.
     * <p>
     * Only the Staatsangehoerigkeit entities with matching tenant will be deleted.
     * </p>
     *
     * @param entities The Iterable of Staatsangehoerigkeit entities that will be deleted.
     */
    @Override
    @CacheEvict(value = CACHE, allEntries = true)
    @PreAuthorize("hasRole('ROLE_DELETE_Staatsangehoerigkeit')")
    @PreFilter(TenantService.IS_TENANT_FILTER)
    void delete(Iterable<? extends Staatsangehoerigkeit> entities);

    /**
     * Delete all Staatsangehoerigkeit entities.
     * <p>
     * Only the Staatsangehoerigkeit entities with matching tenant will be deleted.
     * </p>
     */
    @Override
    @CacheEvict(value = CACHE, allEntries = true)
    @PreAuthorize("hasRole('ROLE_DELETE_Staatsangehoerigkeit')")
    @PreFilter(TenantService.IS_TENANT_FILTER)
    void deleteAll();

    Staatsangehoerigkeit findByName(@Param(value = "name") MoeglicheStaatsangehoerigkeiten name);

}
