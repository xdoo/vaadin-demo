/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.demo.service.services;

import de.muenchen.demo.service.domain.Account;
import java.util.List;

/**
 *
 * @author praktikant.tmar
 */
public interface AccountService {
    
    public List<Account> query();
    
    public List<Account> query(String query);
    
    public Account create();
    
    public Account save(Account users);
    
    public Account read(String oid);
    
    public Account update(Account users);
    
    public void delete(String oid);
    
}
