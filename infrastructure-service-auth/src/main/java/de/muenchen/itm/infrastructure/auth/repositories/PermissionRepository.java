/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.itm.infrastructure.auth.repositories;

import de.muenchen.itm.infrastructure.auth.entities.Permission;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PreAuthorize;

/**
 * @author praktikant.tmar
 */
@PreAuthorize("hasRole('ROLE_READ_SEC_Authority')")
@RepositoryRestResource
public interface PermissionRepository extends CrudRepository<Permission, Long> {

    String PERMISSION_CACHE = "SEC_PERMISSION_CACHE";
    String ROLE_WRITE = "hasRole('ROLE_WRITE_SEC_Permission')";
    String ROLE_DELETE = "hasRole('ROLE_DELETE_SEC_Permission')";

    @Override
    Iterable<Permission> findAll();

    @Override
    @Cacheable(value = PERMISSION_CACHE, key = "#p0")
    Permission findOne(Long oid);

    @SuppressWarnings("unchecked")
    @Override
    @CachePut(value = PERMISSION_CACHE, key = "#p0.oid")
    @PreAuthorize(ROLE_WRITE)
    Permission save(Permission Permission);

    @Override
    @CacheEvict(value = PERMISSION_CACHE, key = "#p0")
    @PreAuthorize(ROLE_DELETE)
    void delete(Long oid);

    @Override
    @CacheEvict(value = PERMISSION_CACHE, key = "#p0.oid")
    @PreAuthorize(ROLE_DELETE)
    void delete(Permission entity);

    @Override
    @CacheEvict(value = PERMISSION_CACHE, allEntries = true)
    @PreAuthorize(ROLE_DELETE)
    void delete(Iterable<? extends Permission> entities);

    @Override
    @CacheEvict(value = PERMISSION_CACHE, allEntries = true)
    @PreAuthorize(ROLE_DELETE)
    void deleteAll();

    Permission findByPermission(@Param(value = "permission") String permission);
}

