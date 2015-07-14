/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.demo.service.services;

import de.muenchen.demo.service.domain.UserAuthId;
import de.muenchen.demo.service.domain.UserAuthority;
import java.util.List;

/**
 *
 * @author praktikant.tmar
 */
public interface UserAuthorityService {
    
    public List<UserAuthority> query();
    
    public List<UserAuthority> query(String query);
    
    public UserAuthority save(UserAuthority usersAuthoritys);
   
    public UserAuthority  read(UserAuthId id);
        
    public void delete(UserAuthId usersAuthoritys);
    

    
}
