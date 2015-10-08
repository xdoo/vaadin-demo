/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.security.repositories;

import de.muenchen.security.entities.AuthPermId;
import de.muenchen.security.entities.AuthorityPermission;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

/**
 *
 * @author praktikant.tmar
 */
@RepositoryRestResource(exported = false)
public interface AuthorityPermissionRepository extends CrudRepository<AuthorityPermission, Long> {

    public final static String AuthorityPermission_CACHE = "PERMISSION_CACHE";

    @Override
    @CachePut(value = AuthorityPermission_CACHE, key = "#p0.id")
    AuthorityPermission save(AuthorityPermission entity);

    @Override
    @CacheEvict(value = AuthorityPermission_CACHE, key = "#p0")
    void delete(Long aLong);

    @Override
    @CacheEvict(value = AuthorityPermission_CACHE, allEntries = true)
    void delete(Iterable<? extends AuthorityPermission> iterable);

    @Override
    @CacheEvict(value = AuthorityPermission_CACHE, key = "#p0.id")
    void delete(AuthorityPermission authorityPermission);

    @Override
    @CacheEvict(value = AuthorityPermission_CACHE, allEntries = true)
    void deleteAll();

    @Cacheable(value = AuthorityPermission_CACHE, key = "#p0")
    public AuthorityPermission findFirstById(AuthPermId id);



    public List<AuthorityPermission> findByIdAuthorityAuthority(String authority);
    public List<AuthorityPermission> findByIdPermissionPermission(String permission);


}
