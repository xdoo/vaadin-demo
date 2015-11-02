package de.muenchen.demo.service.gen.rest;

import de.muenchen.demo.service.gen.domain.Adresse;
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
 * Provides a Repository for a {@link Adresse}. This Repository can be exported as a REST Resource.
 * <p>
 * The Repository handles CRUD Operations. Every Operation is secured and takes care of the tenancy.
 * For specific Documentation on how the generated REST point behaves, please consider the Spring Data Rest Reference
 * <a href="http://docs.spring.io/spring-data/rest/docs/current/reference/html/">here</a>.
 * </p>
 */
@SuppressWarnings("SpringElInspection")
@RepositoryRestResource(exported = true,
        path = "adresses", collectionResourceRel = "adresses")
@PreAuthorize("hasRole('ROLE_READ_Adresse')")
public interface AdresseRepository extends JpaRepository<Adresse, Long> {

    /**
     * Name for the specific cache.
     */
    String CACHE = "ADRESSE_CACHE";

    /**
     * Get all the Adresse entities that match the current users tenancy.
     * <p>
     * Adresse entities that belong to another tenant will get filtered out.
     * </p>
     *
     * @return an Iterable of the Adresse entities with the same Tenancy.
     */
    @Override
    @PostFilter(TenantService.IS_TENANT_FILTER)
    List<Adresse> findAll();

    /**
     * Get one specific Adresse by its unique oid.
     * <p>
     * If the tenant id does not match, no Permission on this resource will be granted.
     * </p>
     *
     * @param oid The identifier of the Adresse.
     * @return The Adresse with the requested oid.
     */
    @Override
    @Cacheable(value = CACHE, key = "#p0")
    @PreAuthorize("hasRole('ROLE_READ_Adresse')")
    @PostAuthorize(TenantService.IS_TENANT_AUTH)
    Adresse findOne(Long oid);

    /**
     * Create or update a Adresse.
     * <p>
     * If the oid already exists, the Adresse will be overridden, hence update.
     * If the oid does no already exist, a new Adresse will be created, hence create.
     * </p>
     * <p>
     * The Adresse can only be saved if the tenant matches the tenant of the current User.
     * </p>
     *
     * @param adresse The Adresse that will be saved.
     * @return the saved Adresse.
     */
    @SuppressWarnings("unchecked")
    @Override
    @CachePut(value = CACHE, key = "#p0.oid")
    @PreAuthorize("hasRole('ROLE_WRITE_Adresse')")
    Adresse save(Adresse adresse);

    /**
     * Delete the Adresse by a specified oid.
     * <p>
     * The Adresse can only be deleted if the tenant matches.
     * </p>
     *
     * @param oid the unique oid of the Adresse that will be deleted.
     */
    @Override
    @CacheEvict(value = CACHE, key = "#p0")
    @PreAuthorize("hasRole('ROLE_DELETE_Adresse')")
    @PostAuthorize(TenantService.IS_TENANT_AUTH)
    void delete(Long oid);

    /**
     * Delete a Adresse by entity.
     * <p>
     * The delete is only permitted if the tenant is matching.
     * </p>
     *
     * @param entity The Adresse that will be deleted.
     */
    @Override
    @CacheEvict(value = CACHE, key = "#p0.oid")
    @PreAuthorize("hasRole('ROLE_DELETE_Adresse')")
    @PostAuthorize(TenantService.IS_TENANT_AUTH)
    void delete(Adresse entity);

    /**
     * Delete multiple Adresse entities by their oid.
     * <p>
     * Only the Adresse entities with matching tenant will be deleted.
     * </p>
     *
     * @param entities The Iterable of Adresse entities that will be deleted.
     */
    @Override
    @CacheEvict(value = CACHE, allEntries = true)
    @PreAuthorize("hasRole('ROLE_DELETE_Adresse')")
    @PreFilter(TenantService.IS_TENANT_FILTER)
    void delete(Iterable<? extends Adresse> entities);

    /**
     * Delete all Adresse entities.
     * <p>
     * Only the Adresse entities with matching tenant will be deleted.
     * </p>
     */
    @Override
    @CacheEvict(value = CACHE, allEntries = true)
    @PreAuthorize("hasRole('ROLE_DELETE_Adresse')")
    @PreFilter(TenantService.IS_TENANT_FILTER)
    void deleteAll();


    /**
     * Find the Adresse with a externeAdresse relation to the AdresseExtern with the given oid.
     *
     * @param oid the unique oid of the AdresseExtern that will be searched for in the externeAdresse relation.
     */
    Adresse findByExterneAdresseOid(@Param(value = "oid") Long oid);

    /**
     * Find the Adresse with a interneAdresse relation to the AdresseIntern with the given oid.
     *
     * @param oid the unique oid of the AdresseIntern that will be searched for in the interneAdresse relation.
     */
    Adresse findByInterneAdresseOid(@Param(value = "oid") Long oid);
}
