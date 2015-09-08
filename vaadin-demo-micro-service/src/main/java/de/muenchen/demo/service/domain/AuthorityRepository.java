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
@Secured("PERM_Authority")
public interface AuthorityRepository  extends CrudRepository<Authority, Long> {

    public final static String Authority_CACHE = "AUTHORITYPERMISSION_CACHE";

    @Cacheable(value = Authority_CACHE, key = "#p0")
    public Authority findFirstByOid(String oid);

    @Override
    @CachePut(value = Authority_CACHE, key = "#p0.oid")
    public Authority save(Authority entity);


    @Override
    @CacheEvict(value = Authority_CACHE, key = "#p0.oid")
    public void delete(Authority entity);

    public List<Authority> findByOid(String oid);

}

