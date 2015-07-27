/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.demo.service.domain;

import java.util.List;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author praktikant.tmar
 */
public interface UserAuthorityRepository extends CrudRepository<UserAuthority, Long> {

    public List<UserAuthority> findById(UserAuthId id);

    public List<UserAuthority> findByIdAuthorityAuthority(String authority);
        public List<UserAuthority> findByIdUserUsername(String username);

}
