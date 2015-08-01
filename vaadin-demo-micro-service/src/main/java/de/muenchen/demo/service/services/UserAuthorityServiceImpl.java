/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.demo.service.services;

import com.google.common.collect.Lists;
import de.muenchen.demo.service.domain.UserAuthId;
import de.muenchen.demo.service.domain.UserAuthority;
import de.muenchen.demo.service.domain.UserAuthorityRepository;
import de.muenchen.demo.service.util.QueryService;
import java.util.List;
import javax.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author praktikant.tmar
 */
@Service
public class UserAuthorityServiceImpl implements UserAuthorityService {

    private static final Logger LOG = LoggerFactory.getLogger(UserAuthorityService.class);

    UserService userService;
    UserAuthorityRepository repo;
    QueryService<UserAuthority> search;

    public UserAuthorityServiceImpl() {
    }

    @Autowired
    public UserAuthorityServiceImpl(UserAuthorityRepository repo, UserService userService, EntityManager em) {
        this.repo = repo;
        this.userService = userService;
        this.search = new QueryService<>(userService, em, UserAuthority.class, "authority", "permission");
    }

    @Override
    public UserAuthority save(UserAuthority usersAuthoritys) {
        LOG.info(usersAuthoritys.toString());
        return this.repo.save(usersAuthoritys);
    }

    @Override
    public List<UserAuthority> query() {
        Iterable<UserAuthority> all = this.repo.findAll();
        return Lists.newArrayList(all);
    }

    @Override
    public List<UserAuthority> query(String query) {
        return this.search.query(query);
    }

    @Override
    public UserAuthority read(UserAuthId id) {
        List<UserAuthority> result = this.repo.findById(id);
        if (result.isEmpty()) {
            // TODO
            LOG.warn(String.format("found no users with oid '%s'", id));
            return null;
        } else {
            return result.get(0);
        }
    }

    @Override
    public void delete(UserAuthId id) {
        UserAuthority item = this.read(id);
        this.repo.delete(item);
    }

    @Override
    public List<UserAuthority> readByUsername(String username) {
        List<UserAuthority> result = this.repo.findByIdUserUsername(username);
        if (result.isEmpty()) {
            // TODO
            LOG.warn(String.format("found no users with oid '%s'", username));
            return null;
        } else {
            return result;
        }
    }

}
