/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.demo.service.domain;

import de.muenchen.demo.service.domain.permissions.Perm;
import org.springframework.security.access.annotation.Secured;
/**
 *
 * @author praktikant.tmar
 */
@Secured(Perm.READ + "AdresseReference")
public interface AdresseReferenceRepository extends BaseRepository<AdresseReference>{

    @Override
    @Secured({Perm.WRITE + "AdresseReference"})
    AdresseReference save(AdresseReference entity);

    @Override
    @Secured({Perm.DELETE + "AdresseReference"})
    void delete(Long aLong);

    @Override
    @Secured({Perm.DELETE + "AdresseReference"})
    void delete(AdresseReference adresseReference);

    @Override
    @Secured({Perm.DELETE + "AdresseReference"})
    void delete(Iterable<? extends AdresseReference> iterable);

    @Override
    @Secured({Perm.DELETE + "AdresseReference"})
    void deleteAll();
}
