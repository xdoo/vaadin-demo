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
public interface PermissionRepository  extends CrudRepository<Permission, Long>  {
    
    public final static String Permission_CACHE = "PERMISSION_CACHE";

    public Permission findFirstByOid(String oid);

    @Override
    public Permission save(Permission entity);

    @Override
    public void delete(Permission entity);
    public List<Permission> findByOid(String mid);
}

