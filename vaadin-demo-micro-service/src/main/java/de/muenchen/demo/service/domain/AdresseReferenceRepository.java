/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.demo.service.domain;

import org.springframework.security.access.annotation.Secured;
/**
 *
 * @author praktikant.tmar
 */
@Secured("PERM_READ_AdresseReference")
public interface AdresseReferenceRepository extends BaseRepository<AdresseReference>{

    @Override
    @Secured({"PERM_WRITE_AdresseReference"})
    Buerger save(AdresseReference entity);

    @Override
    @Secured({"PERM_DELETE_AdresseReference"})
    void delete(Long aLong);

    @Override
    @Secured({"PERM_DELETE_AdresseReference"})
    void delete(AdresseReference adresseReference);

    @Override
    @Secured({"PERM_DELETE_AdresseReference"})
    void delete(Iterable<? extends AdresseReference> iterable);

    @Override
    @Secured({"PERM_DELETE_AdresseReference"})
    void deleteAll();
}
