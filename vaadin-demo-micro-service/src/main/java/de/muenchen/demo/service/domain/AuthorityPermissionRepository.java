/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.demo.service.domain;

import java.util.List;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author praktikant.tmar
 */
public interface AuthorityPermissionRepository extends CrudRepository<AuthorityPermission, Long> {

    public final static String AuthorityPermission_CACHE = "PERMISSION_CACHE";

    @Cacheable(value = AuthorityPermission_CACHE, key = "#p0")
    public AuthorityPermission findFirstById(AuthPermId id);

    @Override
    @CachePut(value = AuthorityPermission_CACHE, key = "#p0.id")
    public AuthorityPermission save(AuthorityPermission entity);

    @Override
    @CacheEvict(value = AuthorityPermission_CACHE, key = "#p0.id")
    public void delete(AuthorityPermission entity);

    public List<AuthorityPermission> findByIdAuthorityAuthority(String authority);
    public List<AuthorityPermission> findByIdPermissionPermission(String permission);


}
