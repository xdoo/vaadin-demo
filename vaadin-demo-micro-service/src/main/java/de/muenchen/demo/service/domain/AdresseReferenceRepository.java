/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.demo.service.domain;

import org.springframework.data.repository.CrudRepository;
import org.springframework.security.access.annotation.Secured;
/**
 *
 * @author praktikant.tmar
 */
@Secured("hasRole('PERM_READ_AdresseReference')")
public interface AdresseReferenceRepository extends CrudRepository<AdresseReference, Long> {

    @Override
    @Secured("hasRole('PERM_WRITE_AdresseReference')")
    AdresseReference save(AdresseReference entity);

    @Override
    @Secured("hasRole('PERM_DELETE_AdresseReference')")
    void delete(Long aLong);

    @Override
    @Secured("hasRole('PERM_DELETE_AdresseReference')")
    void delete(AdresseReference adresseReference);

    @Override
    @Secured("hasRole('PERM_DELETE_AdresseReference')")
    void delete(Iterable<? extends AdresseReference> iterable);

    @Override
    @Secured("hasRole('PERM_DELETE_AdresseReference')")
    void deleteAll();
}
