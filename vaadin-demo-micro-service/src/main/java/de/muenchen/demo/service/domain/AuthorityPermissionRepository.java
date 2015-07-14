/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.demo.service.domain;

import java.util.List;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author praktikant.tmar
 */
public interface AuthorityPermissionRepository extends CrudRepository<AuthorityPermission, Long> {

    public List<AuthorityPermission> findById(AuthPermId id);


}
