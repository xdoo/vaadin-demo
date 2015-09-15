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
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

/**
 *
 * @author praktikant.tmar
 */
@RepositoryRestResource(exported = false)
public interface PermissionRepository  extends CrudRepository<Permission, Long>  {
    
    public final static String Permission_CACHE = "PERMISSION_CACHE";

    @Cacheable(value = Permission_CACHE, key = "#p0")
    public Permission findFirstByOid(String oid);

    @Override
    @CachePut(value = Permission_CACHE, key = "#p0.oid")
    public Permission save(Permission entity);

    @Override
    @CacheEvict(value = Permission_CACHE, key = "#p0.oid")
    public void delete(Permission entity);

    public List<Permission> findByOid(String oid);
}

