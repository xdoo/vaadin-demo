/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.demo.service.services;

import de.muenchen.demo.service.domain.AuthPermId;
import de.muenchen.demo.service.domain.UserAuthId;
import de.muenchen.demo.service.domain.AuthorityPermission;
import java.util.List;

/**
 *
 * @author praktikant.tmar
 */
public interface AuthorityPermissionService {
    
    public List<AuthorityPermission> query();
    
    public List<AuthorityPermission> query(String query);
    
    public AuthorityPermission save(AuthorityPermission authPerm);
   
    public AuthorityPermission  read(AuthPermId id);
        
    public void delete(AuthPermId id);
    

    
}
