/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.demo.service.domain;

import de.muenchen.demo.service.domain.permissions.Perm;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.security.access.annotation.Secured;

import java.util.List;

/**
 *
 * @author praktikant.tmar
 */
@Secured({Perm.READ + "Mandant"})
public interface MandantRepository extends PagingAndSortingRepository<Mandant, Long> {
    
    List<Mandant> findByOid(String oid);

    @Override
    @Secured({Perm.WRITE + "Mandant"})
    Mandant save(Mandant s);

    @Override
    @Secured({Perm.DELETE + "Mandant"})
    void delete(Long aLong);

    @Override
    @Secured({Perm.DELETE + "Mandant"})
    void delete(Iterable<? extends Mandant> iterable);

    @Override
    @Secured({Perm.DELETE + "Mandant"})
    void delete(Mandant authorityPermission);

    @Override
    @Secured({Perm.DELETE + "Mandant"})
    void deleteAll();
    
}
