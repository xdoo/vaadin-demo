package de.muenchen.demo.service.domain;

import de.muenchen.demo.service.domain.permissions.Perm;
import org.springframework.security.access.annotation.Secured;

/**
 *
 * @author claus.straube
 */
@Secured({Perm.READ + "AdresseInterne"})
public interface AdresseInterneRepository extends BaseRepository<AdresseInterne> {

    @Override
    @Secured({Perm.WRITE + "AdresseInterne"})
    AdresseInterne save(AdresseInterne entity);

    @Override
    @Secured({Perm.DELETE + "AdresseInterne"})
    void delete(Long aLong);

    @Override
    @Secured({Perm.DELETE + "AdresseInterne"})
    void delete(AdresseInterne adresseInterne);

    @Override
    @Secured({Perm.DELETE + "AdresseInterne"})
    void delete(Iterable<? extends AdresseInterne> iterable);

    @Override
    @Secured({Perm.DELETE + "AdresseInterne"})
    void deleteAll();
}
