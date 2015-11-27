/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.auth.repositories;

import de.muenchen.auth.entities.Authority;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PreAuthorize;

/**
 * @author praktikant.tmar
 */
@PreAuthorize("hasRole('ROLE_READ_SEC_Authority')")
@RepositoryRestResource
public interface AuthorityRepository extends CrudRepository<Authority, Long> {

    String AUTHORITY_CACHE = "SEC_Authority_CACHE";
    String ROLE_WRITE = "hasRole('ROLE_WRITE_SEC_Authority')";
    String ROLE_DELETE = "hasRole('ROLE_DELETE_SEC_Authority')";

    @Override
    Iterable<Authority> findAll();

    @Override
    @Cacheable(value = AUTHORITY_CACHE, key = "#p0")
    Authority findOne(Long oid);

    @SuppressWarnings("unchecked")
    @Override
    @CachePut(value = AUTHORITY_CACHE, key = "#p0.oid")
    @PreAuthorize(ROLE_WRITE)
    Authority save(Authority Authority);

    @Override
    @CacheEvict(value = AUTHORITY_CACHE, key = "#p0")
    @PreAuthorize(ROLE_DELETE)
    void delete(Long oid);

    @Override
    @CacheEvict(value = AUTHORITY_CACHE, key = "#p0.oid")
    @PreAuthorize(ROLE_DELETE)
    void delete(Authority entity);

    @Override
    @CacheEvict(value = AUTHORITY_CACHE, allEntries = true)
    @PreAuthorize(ROLE_DELETE)
    void delete(Iterable<? extends Authority> entities);

    @Override
    @CacheEvict(value = AUTHORITY_CACHE, allEntries = true)
    @PreAuthorize(ROLE_DELETE)
    void deleteAll();

}
