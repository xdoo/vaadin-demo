package de.muenchen.demo.service.domain;

import org.springframework.data.repository.CrudRepository;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;

/**
 *
 * @author claus.straube
 */
@PreAuthorize("hasRole('PERM_READ_Buerger')")
@PostFilter("hasPermission(filterObject.mandant, 'tenant')")
public interface BuergerRepository extends CrudRepository<Buerger, Long> {

    public final static String BUERGER_CACHE = "BUERGER_CACHE";

    @Override
    Iterable<Buerger> findAll();

    @Override
    @PreAuthorize("hasRole('PERM_WRITE_Buerger')")
    Buerger save(Buerger entity);

    @Override
    @PreAuthorize("hasRole('PERM_DELETE_Buerger')")
    void delete(Long aLong);

    @Override
    @PreAuthorize("hasRole('PERM_DELETE_Buerger')")
    void delete(Iterable<? extends Buerger> iterable);

    @Override
    @PreAuthorize("hasRole('PERM_DELETE_Buerger')")
    void deleteAll();


    @Override
    @PreAuthorize("hasRole('PERM_DELETE_Buerger')")
    public void delete(Buerger entity);

}
