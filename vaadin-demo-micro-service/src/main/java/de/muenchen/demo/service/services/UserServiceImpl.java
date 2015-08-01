/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.demo.service.services;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import de.muenchen.demo.service.domain.User;
import de.muenchen.demo.service.domain.UserRepository;
import de.muenchen.demo.service.util.IdService;
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
public class UserServiceImpl implements UserService {
    
    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);
    
    UserRepository repo;
    QueryService<User> search;

    public UserServiceImpl() {
    }

    @Autowired
    public UserServiceImpl(UserRepository repo, EntityManager em) {
        this.repo = repo;
        this.search = new QueryService<>(this, em, User.class,"userName", "email");
    }
    
    @Override
    public User create() {
        User users = new User();
        users.setOid(IdService.next());
        return users;
    }

    @Override
    public User save(User users) {
        LOG.info(users.toString());
        Preconditions.checkArgument(users.getId() == null, "On save, the ID must be empty");
        return this.repo.save(users);
    }
    
    @Override
    public User read(String oid) {
        List<User> result = this.repo.findByOid(oid);
        if(result.isEmpty()) {
            // TODO
            LOG.warn(String.format("found no users with oid '%s'", oid));
            return null;
        } else {
            return result.get(0);
        }
    }
    
    @Override
    public User update(User users) {
        return this.repo.save(users);
    }
    
    @Override
    public void delete(String oid) {
        User item = this.read(oid);
        this.repo.delete(item);
    }

    @Override
    public List<User> query() {
        Iterable<User> all = this.repo.findAll();
        return Lists.newArrayList(all);
    }

    @Override
    public List<User> query(String query) { 
        return this.search.query(query);
    }

    @Override
    public User readByUsername(String username) {
        List<User> result = this.repo.findByUsername(username);
        if(result.isEmpty()) {
            // TODO
            LOG.warn(String.format("found no users with username '%s'", username));
            return null;
        } else {
            return result.get(0);
        }
    }

    
}

