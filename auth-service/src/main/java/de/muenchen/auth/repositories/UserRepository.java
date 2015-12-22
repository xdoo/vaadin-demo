/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.auth.repositories;

import de.muenchen.auth.UpdateOwnUserValidator;
import de.muenchen.auth.entities.User;
import de.muenchen.service.TenantService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreFilter;

/**
 * @author praktikant.tmar
 */
@RepositoryRestResource
public interface UserRepository extends CrudRepository<User, Long> {

    String User_CACHE = "SEC_USER_CACHE";
    String ROLE_WRITE = "hasRole('ROLE_WRITE_SEC_User')";
    String ROLE_DELETE = "hasRole('ROLE_DELETE_SEC_User')";

    /**
     * Used by Tenant-Service. Not exported due to security-reasons. Tenant-Filtering leads to circulating calls.
     *
     * @param username
     * @return
     */
    @RestResource(exported = false)
    @Cacheable(value = User_CACHE, key = "#p0")
    User findFirstByUsername(@Param("username") String username);

    @Override
    @PostFilter(TenantService.IS_TENANT_FILTER)
    Iterable<User> findAll();

    @Override
    @Cacheable(value = User_CACHE, key = "#p0")
    @PostAuthorize(TenantService.IS_TENANT_AUTH)
    User findOne(Long oid);

    @SuppressWarnings("unchecked")
    @Override
    @CachePut(value = User_CACHE, key = "#p0.oid")
    @PostAuthorize(ROLE_WRITE + "or" + UpdateOwnUserValidator.IS_OWN_USER_AUTH)
    User save(User User);

    @Override
    @CacheEvict(value = User_CACHE, key = "#p0")
    @PreAuthorize(ROLE_DELETE)
    @PostAuthorize(TenantService.IS_TENANT_AUTH)
    void delete(Long oid);


    @Override
    @CacheEvict(value = User_CACHE, key = "#p0.oid")
    @PreAuthorize(ROLE_DELETE)
    @PostAuthorize(TenantService.IS_TENANT_AUTH)
    void delete(User entity);

    @Override
    @CacheEvict(value = User_CACHE, allEntries = true)
    @PreAuthorize(ROLE_DELETE)
    @PreFilter(TenantService.IS_TENANT_FILTER)
    void delete(Iterable<? extends User> entities);


    @Override
    @CacheEvict(value = User_CACHE, allEntries = true)
    @PreAuthorize(ROLE_DELETE)
    @PreFilter(TenantService.IS_TENANT_FILTER)
    void deleteAll();

    @PreAuthorize(TenantService.IS_TENANT_AUTH)
    User findByUsername(@Param(value = "username") String username);

}
