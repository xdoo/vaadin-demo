/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.demo.service.domain;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

/**
 *
 * @author praktikant.tmar
 */
@PreAuthorize("hasRole('PERM_READ_Mandant')")
public interface MandantRepository extends PagingAndSortingRepository<Mandant, Long> {
    
    List<Mandant> findByOid(String oid);

    @Override
    @PreAuthorize("hasRole('PERM_WRITE_Mandant')")
    Mandant save(Mandant s);

    @Override
    @PreAuthorize("hasRole('PERM_DELETE_Mandant')")
    void delete(Long aLong);

    @Override
    @PreAuthorize("hasRole('PERM_DELETE_Mandant')")
    void delete(Iterable<? extends Mandant> iterable);

    @Override
    @PreAuthorize("hasRole('PERM_DELETE_Mandant')")
    void delete(Mandant authorityPermission);

    @Override
    @PreAuthorize("hasRole('PERM_DELETE_Mandant')")
    void deleteAll();
    
}
