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
public interface AuthorityPermissionRepository extends CrudRepository<AuthorityPermission, Long> {

    public final static String AuthorityPermission_CACHE = "PERMISSION_CACHE";

    @Override
    AuthorityPermission save(AuthorityPermission entity);

    @Override
    void delete(Long aLong);

    @Override
    void delete(Iterable<? extends AuthorityPermission> iterable);

    @Override
    void delete(AuthorityPermission authorityPermission);

    @Override
    void deleteAll();

    public AuthorityPermission findFirstById(AuthPermId id);



    public List<AuthorityPermission> findByIdAuthorityAuthority(String authority);
    public List<AuthorityPermission> findByIdPermissionPermission(String permission);


}
