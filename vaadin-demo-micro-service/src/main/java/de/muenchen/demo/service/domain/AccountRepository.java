/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.demo.service.domain;

import de.muenchen.demo.service.domain.permissions.Perm;
import org.springframework.data.repository.CrudRepository;
import org.springframework.security.access.annotation.Secured;

import java.util.List;

/**
 *
 * @author praktikant.tmar
 */
@Secured({Perm.READ + "Account"})
public interface AccountRepository  extends CrudRepository<Account, Long>  {

    public List<Authority> findByOid(String oid);

    @Override
    @Secured({Perm.WRITE + "Account"})
    Buerger save(Account entity);

    @Override
    @Secured({Perm.DELETE + "Account"})
    void delete(Long aLong);

    @Override
    @Secured({Perm.DELETE + "Account"})
    void delete(Account account);

    @Override
    @Secured({Perm.DELETE + "Account"})
    void delete(Iterable<? extends Account> iterable);

    @Override
    @Secured({Perm.DELETE + "Account"})
    void deleteAll();



}

