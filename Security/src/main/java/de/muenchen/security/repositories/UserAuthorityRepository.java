/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.security.repositories;

import de.muenchen.security.entities.UserAuthId;
import de.muenchen.security.entities.UserAuthority;
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
