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
public interface UserAuthorityRepository extends CrudRepository<UserAuthority, Long> {

    public final static String UserAuthority_CACHE = "USERAUTHORITY_CACHE";

    @Cacheable(value = UserAuthority_CACHE, key = "#p0")
    public UserAuthority findFirstById(UserAuthId id);

    @Override
    @CachePut(value = UserAuthority_CACHE, key = "#p0.id")
    public UserAuthority save(UserAuthority entity);

    @Override
    @CacheEvict(value = UserAuthority_CACHE, key = "#p0.id")
    public void delete(UserAuthority entity);

    public List<UserAuthority> findByIdAuthorityAuthority(String authority);

    public List<UserAuthority> findByIdUserUsername(String username);

}
