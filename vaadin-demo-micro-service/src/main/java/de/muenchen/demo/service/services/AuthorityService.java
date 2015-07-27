/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.demo.service.services;

import de.muenchen.demo.service.domain.Authority;
import java.util.List;

/**
 *
 * @author praktikant.tmar
 */
public interface AuthorityService {
    
    public List<Authority> query();
    
    public List<Authority> query(String query);
    
    public Authority create();
    
    public Authority save(Authority authoritys);
    
    public Authority read(String oid);
    
    public Authority update(Authority authoritys);
    
    public void delete(String oid);
    
}
