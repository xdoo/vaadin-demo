/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.demo.service.services;

import de.muenchen.demo.service.domain.Permission;
import de.muenchen.demo.service.domain.PermissionRepository;
import de.muenchen.demo.service.util.QueryService;
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
public class PermissionServiceImpl extends BaseService<Permission>  implements PermissionService {
    
    private static final Logger LOG = LoggerFactory.getLogger(PermissionService.class);

    public PermissionServiceImpl() {
    }

    @Autowired
    public PermissionServiceImpl(PermissionRepository repo, EntityManager em) {
        this.repo = repo;
        this.search = new QueryService<>(em, Permission.class, "Permission");
    }
    
}

