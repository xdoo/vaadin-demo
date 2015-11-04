/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.service.security.repositories;

import de.muenchen.service.TenantService;
import de.muenchen.service.security.entities.Authority;
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
public interface AuthorityRepository  extends CrudRepository<Authority, Long> {

    String AUTHORITY_CACHE = "SEC_Authority_CACHE";
    String ROLE_WRITE = "hasRole('ROLE_WRITE_SEC_Authority')";
    String ROLE_DELETE = "hasRole('ROLE_DELETE_SEC_Authority')";

    @Override
    @PostFilter(TenantService.IS_TENANT_FILTER)
    Iterable<Authority> findAll();

    @Override
    @Cacheable(value = AUTHORITY_CACHE, key = "#p0")
    @PostAuthorize(TenantService.IS_TENANT_AUTH)
    Authority findOne(Long oid);

    @SuppressWarnings("unchecked")
    @Override
    @CachePut(value = AUTHORITY_CACHE, key = "#p0.oid")
    @PreAuthorize(ROLE_WRITE)
    Authority save(Authority Authority);

    @Override
    @CacheEvict(value = AUTHORITY_CACHE, key = "#p0")
    @PreAuthorize(ROLE_DELETE)
    @PostAuthorize(TenantService.IS_TENANT_AUTH)
    void delete(Long oid);

    @Override
    @CacheEvict(value = AUTHORITY_CACHE, key = "#p0.oid")
    @PreAuthorize(ROLE_DELETE)
    @PostAuthorize(TenantService.IS_TENANT_AUTH)
    void delete(Authority entity);

    @Override
    @CacheEvict(value = AUTHORITY_CACHE, allEntries = true)
    @PreAuthorize(ROLE_DELETE)
    @PreFilter(TenantService.IS_TENANT_FILTER)
    void delete(Iterable<? extends Authority> entities);

    @Override
    @CacheEvict(value = AUTHORITY_CACHE, allEntries = true)
    @PreAuthorize(ROLE_DELETE)
    @PreFilter(TenantService.IS_TENANT_FILTER)
    void deleteAll();

}
