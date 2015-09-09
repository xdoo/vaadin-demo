/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.demo.service.domain;

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

    public UserAuthority findFirstById(UserAuthId id);

    @Override
    public UserAuthority save(UserAuthority entity);

    @Override
    public void delete(UserAuthority entity);

    public List<UserAuthority> findByIdAuthorityAuthority(String authority);

    public List<UserAuthority> findByIdUserUsername(String username);

}
