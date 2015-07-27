/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.demo.service.services;

import de.muenchen.demo.service.domain.Permission;
import java.util.List;

/**
 *
 * @author praktikant.tmar
 */
public interface PermissionService {
    
    public List<Permission> query();
    
    public List<Permission> query(String query);
    
    public Permission create();
    
    public Permission save(Permission permissions);
    
    public Permission read(String oid);
    
    public Permission update(Permission permissions);
    
    public void delete(String oid);
    
}
