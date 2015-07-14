/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.demo.service.services;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import de.muenchen.demo.service.domain.Authority;
import de.muenchen.demo.service.domain.AuthorityRepository;
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
public class AuthorityServiceImpl implements AuthorityService {
    
    private static final Logger LOG = LoggerFactory.getLogger(AuthorityService.class);
    
    AuthorityRepository repo;
    QueryService<Authority> search;

    public AuthorityServiceImpl() {
    }

    @Autowired
    public AuthorityServiceImpl(AuthorityRepository repo, EntityManager em) {
        this.repo = repo;
        this.search = new QueryService<>(em, Authority.class, "adresseOid","ausrichtung","stock");
    }
    
    @Override
    public Authority create() {
        Authority authoritys = new Authority();
        authoritys.setOid(IdService.next());
        return authoritys;
    }

    @Override
    public Authority save(Authority authoritys) {
        LOG.info(authoritys.toString());
        Preconditions.checkArgument(authoritys.getId() == null, "On save, the ID must be empty");
        return this.repo.save(authoritys);
    }
    
    @Override
    public Authority read(String oid) {
        List<Authority> result = this.repo.findByOid(oid);
        if(result.isEmpty()) {
            // TODO
            LOG.warn(String.format("found no authoritys with oid '%s'", oid));
            return null;
        } else {
            return result.get(0);
        }
    }
    
    @Override
    public Authority update(Authority authoritys) {
        return this.repo.save(authoritys);
    }
    
    @Override
    public void delete(String oid) {
        Authority item = this.read(oid);
        this.repo.delete(item);
    }

    @Override
    public List<Authority> query() {
        Iterable<Authority> all = this.repo.findAll();
        return Lists.newArrayList(all);
    }

    @Override
    public List<Authority> query(String query) { 
        return this.search.query(query);
    }

    
    
}

