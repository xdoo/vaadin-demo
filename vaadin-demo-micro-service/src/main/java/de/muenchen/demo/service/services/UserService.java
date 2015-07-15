/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.demo.service.services;

import de.muenchen.demo.service.domain.User;
import java.util.List;

/**
 *
 * @author praktikant.tmar
 */
public interface UserService {
    
    public List<User> query();
    
    public List<User> query(String query);
    
    public User create();
    
    public User save(User users);
    
    public User read(String oid);
    
    public User readByUsername(String username);
    
    public User update(User users);
    
    public void delete(String oid);
    
}
