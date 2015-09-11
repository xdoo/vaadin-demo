package de.muenchen.demo.service.domain;

import org.springframework.data.repository.CrudRepository;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreFilter;

/**
 *
 * @author claus.straube
 */
@PreAuthorize("hasRole('PERM_READ_Buerger')")
public interface BuergerRepository extends CrudRepository<Buerger, Long> {

    public final static String BUERGER_CACHE = "BUERGER_CACHE";

    @Override
    @PreFilter("@tenantPermissionEvaluator.isTenant(authentication,filterObject)")
    Iterable<Buerger> findAll();

    @Override
    @PreAuthorize("hasRole('PERM_READ_Buerger')")
    @PostAuthorize("@tenantPermissionEvaluator.isTenant(authentication,returnObject)")
    Buerger findOne(Long aLong);

    @Override
    @PreAuthorize("hasRole('PERM_WRITE_Buerger')")
    Buerger save(Buerger buerger);

    @Override
    @PreAuthorize("hasRole('PERM_DELETE_Buerger')")
    @PostAuthorize("@tenantPermissionEvaluator.isTenant(authentication,returnObject)")
    void delete(Long aLong);

    @Override
    @PreAuthorize("hasRole('PERM_DELETE_Buerger')")
    @PreFilter("@tenantPermissionEvaluator.isTenant(authentication,filterObject)")
    void delete(Iterable<? extends Buerger> iterable);

    @Override
    @PreAuthorize("hasRole('PERM_DELETE_Buerger')")
    @PreFilter("@tenantPermissionEvaluator.isTenant(authentication,filterObject)")
    void deleteAll();

    @Override
    @PreAuthorize("hasRole('PERM_DELETE_Buerger')")
    @PostAuthorize("@tenantPermissionEvaluator.isTenant(authentication,returnObject)")
    public void delete(Buerger entity);

}
