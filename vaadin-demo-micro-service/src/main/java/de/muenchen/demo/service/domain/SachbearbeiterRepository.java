package de.muenchen.demo.service.domain;

import org.springframework.data.repository.CrudRepository;
import org.springframework.security.access.prepost.PreAuthorize;

/**
 *
 * @author claus.straube
 */
@PreAuthorize("hasRole('PERM_READ_Sachbearbeiter')")
public interface SachbearbeiterRepository extends CrudRepository<Sachbearbeiter, Long> {

    String Sachbearbeiter_CACHE = "SACHBEARBEITER_CACHE";

    @Override
    @PreAuthorize("hasRole('PERM_WRITE_Sachbearbeiter')")
    Sachbearbeiter save(Sachbearbeiter entity);

    @Override
    @PreAuthorize("hasRole('PERM_DELETE_Sachbearbeiter')")
    void delete(Long aLong);

    @Override
    @PreAuthorize("hasRole('PERM_DELETE_Sachbearbeiter')")
    void delete(Iterable<? extends Sachbearbeiter> iterable);

    @Override
    @PreAuthorize("hasRole('PERM_DELETE_Sachbearbeiter')")
    void delete(Sachbearbeiter authorityPermission);

    @Override
    @PreAuthorize("hasRole('PERM_DELETE_Sachbearbeiter')")
    void deleteAll();


}
