package de.muenchen.demo.service.domain;

import org.springframework.security.access.annotation.Secured;

/**
 *
 * @author claus.straube
 */
@Secured("PERM_READ_AdresseInterne")
public interface AdresseInterneRepository extends BaseRepository<AdresseInterne> {

    @Override
    @Secured({"PERM_WRITE_AdresseInterne"})
    Buerger save(AdresseInterne entity);

    @Override
    @Secured({"PERM_DELETE_AdresseInterne"})
    void delete(Long aLong);

    @Override
    @Secured({"PERM_DELETE_AdresseInterne"})
    void delete(AdresseInterne adresseInterne);

    @Override
    @Secured({"PERM_DELETE_AdresseInterne"})
    void delete(Iterable<? extends AdresseInterne> iterable);

    @Override
    @Secured({"PERM_DELETE_AdresseInterne"})
    void deleteAll();
}
