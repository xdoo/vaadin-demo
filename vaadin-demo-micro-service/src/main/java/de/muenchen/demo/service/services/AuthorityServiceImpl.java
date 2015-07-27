/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.demo.service.services;

import de.muenchen.demo.service.domain.Authority;
import de.muenchen.demo.service.domain.AuthorityRepository;
import de.muenchen.demo.service.util.QueryService;
import javax.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author praktikant.tmar
 */
@Service
public class AuthorityServiceImpl extends BaseService<Authority> implements AuthorityService {


    public AuthorityServiceImpl() {
    }

    @Autowired
    public AuthorityServiceImpl(AuthorityRepository repo, EntityManager em) {
        this.repo = repo;
        this.search = new QueryService<>(em, Authority.class, "authority");
    }
    
}

