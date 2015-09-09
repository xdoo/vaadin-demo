/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.demo.service.domain;

import org.springframework.data.repository.CrudRepository;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

/**
 *
 * @author praktikant.tmar
 */
@PreAuthorize("hasRole('PERM_READ_Pass')")
public interface PassRepository extends CrudRepository<Pass, Long> {

    String Pass_CACHE = "PASS_CACHE";


    @Override
    @PreAuthorize("hasRole('PERM_READ_Pass')")
    Pass save(Pass entity);

    @Override
    @PreAuthorize("hasRole('PERM_READ_Pass')")
    void delete(Long aLong);

    @Override
    @PreAuthorize("hasRole('PERM_READ_Pass')")
    void delete(Iterable<? extends Pass> iterable);

    @Override
    @PreAuthorize("hasRole('PERM_READ_Pass')")
    void delete(Pass authorityPermission);

    @Override
    @PreAuthorize("hasRole('PERM_READ_Pass')")
    void deleteAll();

    Pass findFirstByOidAndMandantOid(String oid, String mid);


    public List<Pass> findByMandantOid(String mid);

    List<Pass> findByOid(String oid);

    public Pass findFirstByStaatsangehoerigkeitReferenceReferencedOidAndMandantOid(String oid, String mid);

}
