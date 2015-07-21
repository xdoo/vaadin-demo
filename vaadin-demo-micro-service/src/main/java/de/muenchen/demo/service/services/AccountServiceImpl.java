/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.demo.service.services;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import de.muenchen.demo.service.domain.Account;
import de.muenchen.demo.service.domain.AccountRepository;
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
public class AccountServiceImpl extends BaseService<Account> implements AccountService {
    
    private static final Logger LOG = LoggerFactory.getLogger(AccountService.class);
    
//    AccountRepository repo;
//    QueryService<Account> search;

    public AccountServiceImpl() {
    }

//    @Autowired
//    public AccountServiceImpl(AccountRepository repo, EntityManager em) {
//        this.repo = repo;
//        this.search = new QueryService<>(em, Account.class, "adresseOid","ausrichtung","stock");
//    }
//    
//    @Override
//    public Account create() {
//        Account accounts = new Account();
//        accounts.setOid(IdService.next());
//        return accounts;
//    }
//
//    @Override
//    public Account save(Account accounts) {
//        LOG.info(accounts.toString());
//        Preconditions.checkArgument(accounts.getId() == null, "On save, the ID must be empty");
//        return this.repo.save(accounts);
//    }
//    
//    @Override
//    public Account read(String oid) {
//        List<Account> result = this.repo.findByOid(oid);
//        if(result.isEmpty()) {
//            // TODO
//            LOG.warn(String.format("found no accounts with oid '%s'", oid));
//            return null;
//        } else {
//            return result.get(0);
//        }
//    }
//    
//    @Override
//    public Account update(Account accounts) {
//        return this.repo.save(accounts);
//    }
//    
//    @Override
//    public void delete(String oid) {
//        Account item = this.read(oid);
//        this.repo.delete(item);
//    }
//
//    @Override
//    public List<Account> query() {
//        Iterable<Account> all = this.repo.findAll();
//        return Lists.newArrayList(all);
//    }
//
//    @Override
//    public List<Account> query(String query) { 
//        return this.search.query(query);
//    }
//
//    
    
}

