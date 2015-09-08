package de.muenchen.demo.service.domain;

import de.muenchen.demo.service.domain.permissions.Perm;
import org.springframework.data.repository.CrudRepository;
import org.springframework.security.access.annotation.Secured;

/**
 *
 * @author claus.straube
 */
@Secured(Perm.READ + "AdresseExterne")
public interface AdresseExterneRepository extends CrudRepository<AdresseExterne, Long> {

    //public List<AdresseExterne> findByOid(String oid);


    @Override
    @Secured(Perm.WRITE + "AdresseExterne")
    Buerger save(AdresseExterne entity);

    @Override
    @Secured(Perm.DELETE + "AdresseExterne")
    void delete(Long aLong);

    @Override
    @Secured(Perm.DELETE + "AdresseExterne")
    void delete(AdresseExterne adresseExterne);

    @Override
    @Secured(Perm.DELETE + "AdresseExterne")
    void delete(Iterable<? extends AdresseExterne> iterable);

    @Override
    @Secured(Perm.DELETE + "AdresseExterne")
    void deleteAll();
}
