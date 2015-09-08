/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.demo.service.domain;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.security.access.annotation.Secured;

import java.util.List;

/**
 *
 * @author praktikant.tmar
 */
@Secured("PERM_READ_AuthorityPermission")
public interface AuthorityPermissionRepository extends CrudRepository<AuthorityPermission, Long> {

    public final static String AuthorityPermission_CACHE = "PERMISSION_CACHE";

    @Override
    @Secured({"PERM_WRITE_AuthorityPermission"})
    @CachePut(value = AuthorityPermission_CACHE, key = "#p0.id")
    Buerger save(AuthorityPermission entity);

    @Override
    @Secured({"PERM_DELETE_AuthorityPermission"})
    @CacheEvict(value = AuthorityPermission_CACHE, key = "#p0.id")
    void delete(Long aLong);

    @Override
    @Secured({"PERM_DELETE_AuthorityPermission"})
    void delete(Iterable<? extends AuthorityPermission> iterable);

    @Override
    @Secured({"PERM_DELETE_AuthorityPermission"})
    void delete(AuthorityPermission authorityPermission);

    @Override
    @Secured({"PERM_DELETE_AdresseReference"})
    void deleteAll();

    @Cacheable(value = AuthorityPermission_CACHE, key = "#p0")
    public AuthorityPermission findFirstById(AuthPermId id);



    public List<AuthorityPermission> findByIdAuthorityAuthority(String authority);
    public List<AuthorityPermission> findByIdPermissionPermission(String permission);


}
