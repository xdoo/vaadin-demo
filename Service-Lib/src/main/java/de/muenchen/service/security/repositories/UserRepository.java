/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.service.security.repositories;

import de.muenchen.service.TenantService;
import de.muenchen.service.security.entities.User;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.security.oauth2.resource.EnableOAuth2Resource;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreFilter;

/**
 * @author praktikant.tmar
 */
@PreAuthorize("hasRole('ROLE_READ_SEC_User')")
@EnableOAuth2Resource
@RepositoryRestResource
public interface UserRepository extends CrudRepository<User, Long> {

    String User_CACHE = "SEC_USER_CACHE";


    @PostAuthorize(TenantService.IS_TENANT_AUTH)
    @Cacheable(value = User_CACHE, key = "#p0")
    public User findFirstByUsername(@Param("username") String username);

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
    @PreAuthorize("hasRole('ROLE_WRITE_SEC_User')")
    User save(User User);

    @Override
    @CacheEvict(value = User_CACHE, key = "#p0")
    @PreAuthorize("hasRole('ROLE_DELETE_SEC_User')")
    @PostAuthorize(TenantService.IS_TENANT_AUTH)
    void delete(Long oid);


    @Override
    @CacheEvict(value = User_CACHE, key = "#p0.oid")
    @PreAuthorize("hasRole('ROLE_DELETE_SEC_User')")
    @PostAuthorize(TenantService.IS_TENANT_AUTH)
    void delete(User entity);

    @Override
    @CacheEvict(value = User_CACHE, allEntries = true)
    @PreAuthorize("hasRole('ROLE_DELETE_SEC_User')")
    @PreFilter(TenantService.IS_TENANT_FILTER)
    void delete(Iterable<? extends User> entities);


    @Override
    @CacheEvict(value = User_CACHE, allEntries = true)
    @PreAuthorize("hasRole('ROLE_DELETE_SEC_User')")
    @PreFilter(TenantService.IS_TENANT_FILTER)
    void deleteAll();

}
