package de.muenchen.demo.service.domain;

import org.springframework.data.repository.CrudRepository;
import org.springframework.security.access.prepost.PreAuthorize;

/**
 *
 * @author claus.straube
 */
@PreAuthorize("hasRole('PERM_READ_AdresseInterne')")
public interface AdresseInterneRepository extends CrudRepository<AdresseInterne, Long> {

    @Override
    @PreAuthorize("hasRole('PERM_WRITE_AdresseInterne')")
    AdresseInterne save(AdresseInterne entity);

    @Override
    @PreAuthorize("hasRole('PERM_DELETE_AdresseInterne')")
    void delete(Long aLong);

    @Override
    @PreAuthorize("hasRole('PERM_DELETE_AdresseInterne')")
    void delete(AdresseInterne adresseInterne);

    @Override
    @PreAuthorize("hasRole('PERM_DELETE_AdresseInterne')")
    void delete(Iterable<? extends AdresseInterne> iterable);

    @Override
    @PreAuthorize("hasRole('PERM_DELETE_AdresseInterne')")
    void deleteAll();
}
