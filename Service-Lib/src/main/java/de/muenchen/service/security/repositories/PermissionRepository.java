/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.service.security.repositories;

import de.muenchen.service.TenantService;
import de.muenchen.service.security.entities.Permission;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.security.oauth2.resource.EnableOAuth2Resource;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreFilter;

/**
 *
 * @author praktikant.tmar
 */
@PreAuthorize("hasRole('ROLE_READ_SEC_Authority')")
@EnableOAuth2Resource
@RepositoryRestResource
public interface PermissionRepository  extends CrudRepository<Permission, Long>  {

    String PERMISSION_CACHE = "SEC_PERMISSION_CACHE";
    String ROLE_WRITE = "hasRole('ROLE_WRITE_SEC_Permission')";
    String ROLE_DELETE = "hasRole('ROLE_DELETE_SEC_Permission')";

    /**
     * Get all the Permission entities that match the current Permissions tenancy.
     * <p>
     * Permission entities that belong to another tenant will get filtered out.
     * </p>
     *
     * @return an Iterable of the Permission entities with the same Tenancy.
     */
    @Override
    @PostFilter(TenantService.IS_TENANT_FILTER)
    Iterable<Permission> findAll();

    /**
     * Get one specific Permission by its unique oid.
     * <p>
     * If the tenant id does not match, no Permission on this resource will be granted.
     * </p>
     *
     * @param oid The identifier of the Permission.
     * @return The Permission with the requested oid.
     */
    @Override
    @Cacheable(value = PERMISSION_CACHE, key = "#p0")
    @PostAuthorize(TenantService.IS_TENANT_AUTH)
    Permission findOne(Long oid);

    /**
     * Create or update a Permission.
     * <p>
     * If the oid already exists, the Permission will be overridden, hence update.
     * If the oid does no already exist, a new Permission will be created, hence create.
     * </p>
     * <p>
     * The Permission can only be saved if the tenant matches the tenant of the current Permission.
     * </p>
     *
     * @param Permission The Permission that will be saved.
     * @return the saved Permission.
     */
    @SuppressWarnings("unchecked")
    @Override
    @CachePut(value = PERMISSION_CACHE, key = "#p0.oid")
    @PreAuthorize(ROLE_WRITE)
    Permission save(Permission Permission);

    /**
     * Delete the Permission by a specified oid.
     * <p>
     * The Permission can only be deleted if the tenant matches.
     * </p>
     *
     * @param oid the unique oid of the Permission that will be deleted.
     */
    @Override
    @CacheEvict(value = PERMISSION_CACHE, key = "#p0")
    @PreAuthorize(ROLE_DELETE)
    @PostAuthorize(TenantService.IS_TENANT_AUTH)
    void delete(Long oid);

    /**
     * Delete a Permission by entity.
     * <p>
     * The delete is only permitted if the tenant is matching.
     * </p>
     *
     * @param entity The Permission that will be deleted.
     */
    @Override
    @CacheEvict(value = PERMISSION_CACHE, key = "#p0.oid")
    @PreAuthorize(ROLE_DELETE)
    @PostAuthorize(TenantService.IS_TENANT_AUTH)
    void delete(Permission entity);

    /**
     * Delete multiple Permission entities by their oid.
     * <p>
     * Only the Permission entities with matching tenant will be deleted.
     * </p>
     *
     * @param entities The Iterable of Permission entities that will be deleted.
     */
    @Override
    @CacheEvict(value = PERMISSION_CACHE, allEntries = true)
    @PreAuthorize(ROLE_DELETE)
    @PreFilter(TenantService.IS_TENANT_FILTER)
    void delete(Iterable<? extends Permission> entities);

    /**
     * Delete all Permission entities.
     * <p>
     * Only the Permission entities with matching tenant will be deleted.
     * </p>
     */
    @Override
    @CacheEvict(value = PERMISSION_CACHE, allEntries = true)
    @PreAuthorize(ROLE_DELETE)
    @PreFilter(TenantService.IS_TENANT_FILTER)
    void deleteAll();
}