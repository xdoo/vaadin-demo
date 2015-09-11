package de.muenchen.demo.service.domain;

import org.springframework.data.repository.CrudRepository;
import org.springframework.security.access.prepost.PreAuthorize;

/**
 *
 * @author claus.straube
 */
@PreAuthorize("hasRole('PERM_READ_AdresseExterne'")
public interface AdresseExterneRepository extends CrudRepository<AdresseExterne, Long> {


    @Override
    @PreAuthorize("hasRole('PERM_WRITE_AdresseExterne'")
    AdresseExterne save(AdresseExterne entity);

    @Override
    @PreAuthorize("hasRole('PERM_DELETE_AdresseExterne'")
    void delete(Long aLong);

    @Override
    @PreAuthorize("hasRole('PERM_DELETE_AdresseExterne'")
    void delete(AdresseExterne adresseExterne);

    @Override
    @PreAuthorize("hasRole('PERM_DELETE_AdresseExterne'")
    void delete(Iterable<? extends AdresseExterne> iterable);

    @Override
    @PreAuthorize("hasRole('PERM_DELETE_AdresseExterne'")
    void deleteAll();
}
