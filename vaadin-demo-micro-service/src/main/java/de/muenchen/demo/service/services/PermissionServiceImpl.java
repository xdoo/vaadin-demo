/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.demo.service.services;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import de.muenchen.demo.service.domain.Permission;
import de.muenchen.demo.service.domain.PermissionRepository;
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
public class PermissionServiceImpl implements PermissionService {
    
    private static final Logger LOG = LoggerFactory.getLogger(PermissionService.class);
    
    PermissionRepository repo;
    QueryService<Permission> search;

    public PermissionServiceImpl() {
    }

    @Autowired
    public PermissionServiceImpl(PermissionRepository repo, UserService userService, EntityManager em) {
        this.repo = repo;
        this.search = new QueryService<>(userService, em, Permission.class,"userName", "email");
    }
    
    @Override
    public Permission create() {
        Permission permission = new Permission();
        permission.setOid(IdService.next());
        return permission;
    }

    @Override
    public Permission save(Permission permission) {
        LOG.info(permission.toString());
        Preconditions.checkArgument(permission.getId() == null, "On save, the ID must be empty");
        return this.repo.save(permission);
    }
    
    @Override
    public Permission read(String oid) {
        List<Permission> result = this.repo.findByOid(oid);
        if(result.isEmpty()) {
            // TODO
            LOG.warn(String.format("found no permission with oid '%s'", oid));
            return null;
        } else {
            return result.get(0);
        }
    }
    

    
    @Override
    public void delete(String oid) {
        Permission item = this.read(oid);
        this.repo.delete(item);
    }

    @Override
    public List<Permission> query() {
        Iterable<Permission> all = this.repo.findAll();
        return Lists.newArrayList(all);
    }

    @Override
    public List<Permission> query(String query) { 
        return this.search.query(query);
    }

    



    
}

