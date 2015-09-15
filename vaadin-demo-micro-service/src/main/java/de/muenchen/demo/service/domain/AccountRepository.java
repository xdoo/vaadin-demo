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
public interface AccountRepository  extends CrudRepository<Account, Long>  {

    public List<Authority> findByOid(String oid);

}

