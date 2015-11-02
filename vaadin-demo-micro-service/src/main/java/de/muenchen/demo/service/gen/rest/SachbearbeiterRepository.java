package de.muenchen.demo.service.gen.rest;

import de.muenchen.demo.service.gen.domain.Sachbearbeiter;
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
 * Provides a Repository for a {@link Sachbearbeiter}. This Repository can be exported as a REST Resource.
 * <p>
 * The Repository handles CRUD Operations. Every Operation is secured and takes care of the tenancy.
 * For specific Documentation on how the generated REST point behaves, please consider the Spring Data Rest Reference
 * <a href="http://docs.spring.io/spring-data/rest/docs/current/reference/html/">here</a>.
 * </p>
 */
@SuppressWarnings("SpringElInspection")
@RepositoryRestResource(exported = true,
        path = "sachbearbeiters", collectionResourceRel = "sachbearbeiters")
@PreAuthorize("hasRole('ROLE_READ_Sachbearbeiter')")
public interface SachbearbeiterRepository extends JpaRepository<Sachbearbeiter, Long> {

    /**
     * Name for the specific cache.
     */
    String CACHE = "SACHBEARBEITER_CACHE";

    /**
     * Get all the Sachbearbeiter entities that match the current users tenancy.
     * <p>
     * Sachbearbeiter entities that belong to another tenant will get filtered out.
     * </p>
     *
     * @return an Iterable of the Sachbearbeiter entities with the same Tenancy.
     */
    @Override
    @PostFilter(TenantService.IS_TENANT_FILTER)
    List<Sachbearbeiter> findAll();

    /**
     * Get one specific Sachbearbeiter by its unique oid.
     * <p>
     * If the tenant id does not match, no Permission on this resource will be granted.
     * </p>
     *
     * @param oid The identifier of the Sachbearbeiter.
     * @return The Sachbearbeiter with the requested oid.
     */
    @Override
    @Cacheable(value = CACHE, key = "#p0")
    @PreAuthorize("hasRole('ROLE_READ_Sachbearbeiter')")
    @PostAuthorize(TenantService.IS_TENANT_AUTH)
    Sachbearbeiter findOne(Long oid);

    /**
     * Create or update a Sachbearbeiter.
     * <p>
     * If the oid already exists, the Sachbearbeiter will be overridden, hence update.
     * If the oid does no already exist, a new Sachbearbeiter will be created, hence create.
     * </p>
     * <p>
     * The Sachbearbeiter can only be saved if the tenant matches the tenant of the current User.
     * </p>
     *
     * @param sachbearbeiter The Sachbearbeiter that will be saved.
     * @return the saved Sachbearbeiter.
     */
    @SuppressWarnings("unchecked")
    @Override
    @CachePut(value = CACHE, key = "#p0.oid")
    @PreAuthorize("hasRole('ROLE_WRITE_Sachbearbeiter')")
    Sachbearbeiter save(Sachbearbeiter sachbearbeiter);

    /**
     * Delete the Sachbearbeiter by a specified oid.
     * <p>
     * The Sachbearbeiter can only be deleted if the tenant matches.
     * </p>
     *
     * @param oid the unique oid of the Sachbearbeiter that will be deleted.
     */
    @Override
    @CacheEvict(value = CACHE, key = "#p0")
    @PreAuthorize("hasRole('ROLE_DELETE_Sachbearbeiter')")
    @PostAuthorize(TenantService.IS_TENANT_AUTH)
    void delete(Long oid);

    /**
     * Delete a Sachbearbeiter by entity.
     * <p>
     * The delete is only permitted if the tenant is matching.
     * </p>
     *
     * @param entity The Sachbearbeiter that will be deleted.
     */
    @Override
    @CacheEvict(value = CACHE, key = "#p0.oid")
    @PreAuthorize("hasRole('ROLE_DELETE_Sachbearbeiter')")
    @PostAuthorize(TenantService.IS_TENANT_AUTH)
    void delete(Sachbearbeiter entity);

    /**
     * Delete multiple Sachbearbeiter entities by their oid.
     * <p>
     * Only the Sachbearbeiter entities with matching tenant will be deleted.
     * </p>
     *
     * @param entities The Iterable of Sachbearbeiter entities that will be deleted.
     */
    @Override
    @CacheEvict(value = CACHE, allEntries = true)
    @PreAuthorize("hasRole('ROLE_DELETE_Sachbearbeiter')")
    @PreFilter(TenantService.IS_TENANT_FILTER)
    void delete(Iterable<? extends Sachbearbeiter> entities);

    /**
     * Delete all Sachbearbeiter entities.
     * <p>
     * Only the Sachbearbeiter entities with matching tenant will be deleted.
     * </p>
     */
    @Override
    @CacheEvict(value = CACHE, allEntries = true)
    @PreAuthorize("hasRole('ROLE_DELETE_Sachbearbeiter')")
    @PreFilter(TenantService.IS_TENANT_FILTER)
    void deleteAll();

    Sachbearbeiter findByTelefon(@Param(value = "telefon") Long telefon);

    Sachbearbeiter findByFax(@Param(value = "fax") Long fax);

    Sachbearbeiter findByFunktion(@Param(value = "funktion") String funktion);

    Sachbearbeiter findByOrganisationseinheit(@Param(value = "organisationseinheit") String organisationseinheit);

}
