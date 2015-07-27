/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.demo.service.services;

import com.google.common.collect.Lists;
import de.muenchen.demo.service.domain.AuthPermId;
import de.muenchen.demo.service.domain.AuthorityPermission;
import de.muenchen.demo.service.domain.AuthorityPermissionRepository;
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
public class AuthorityPermissionServiceImpl implements AuthorityPermissionService {

    private static final Logger LOG = LoggerFactory.getLogger(AuthorityPermissionService.class);

    AuthorityPermissionRepository repo;
    QueryService<AuthorityPermission> search;

    public AuthorityPermissionServiceImpl() {
    }

    @Autowired
    public AuthorityPermissionServiceImpl(AuthorityPermissionRepository repo, EntityManager em) {
        this.repo = repo;
        this.search = new QueryService<>(em, AuthorityPermission.class, "authority", "permission");
    }



    @Override
    public AuthorityPermission save(AuthorityPermission usersAuthoritys) {
        LOG.info(usersAuthoritys.toString());
        return this.repo.save(usersAuthoritys);
    }


    @Override
    public List<AuthorityPermission> query() {
        Iterable<AuthorityPermission> all = this.repo.findAll();
        return Lists.newArrayList(all);
    }

    @Override
    public List<AuthorityPermission> query(String query) {
        return this.search.query(query);
    }

    @Override
    public AuthorityPermission read(AuthPermId id) {
        List<AuthorityPermission> result = this.repo.findById(id);
        if(result.isEmpty()) {
            // TODO
            LOG.warn(String.format("found no users with oid '%s'", id));
            return null;
        } else {
            return result.get(0);
        }
    }

    @Override
    public void delete(AuthPermId id) {
        AuthorityPermission item = this.read(id);
        this.repo.delete(item);    }

    @Override
    public List<AuthorityPermission> readByAuthority(String authority) {

        List<AuthorityPermission> result = this.repo.findByIdAuthorityAuthority(authority);
        if (result.isEmpty()) {
            // TODO
            LOG.warn(String.format("found no users with oid '%s'", authority));
            return null;
        } else {
            return result;
        }
    }

}
