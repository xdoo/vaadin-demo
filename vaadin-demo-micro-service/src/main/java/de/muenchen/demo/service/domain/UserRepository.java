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

/**
 *
 * @author praktikant.tmar
 */
@RepositoryRestResource(exported = false)
public interface UserRepository extends CrudRepository<User, Long> {

    public final static String User_CACHE = "USER_CACHE";

    @Cacheable(value = User_CACHE, key = "#p0")
    public User findFirstByOid(String oid);

    @Override
    @CachePut(value = User_CACHE, key = "#p0.oid")
    public User save(User entity);

    @Override
    @CacheEvict(value = User_CACHE, key = "#p0.oid")
    public void delete(User entity);

    public User findFirstByUsername(String username);
}
