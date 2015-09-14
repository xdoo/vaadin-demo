package de.muenchen.demo.service.domain;

import de.muenchen.demo.service.security.TenantService;
import org.springframework.data.repository.CrudRepository;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreFilter;

/**
 *
 * @author claus.straube
 */
@PreAuthorize("hasRole('ROLE_READ_Buerger')")
public interface BuergerRepository extends CrudRepository<Buerger, String> {

    String BUERGER_CACHE = "BUERGER_CACHE";

    @Override
    @PostFilter(TenantService.IS_TENANT_FILTER)
    Iterable<Buerger> findAll();

    @Override
    @PreAuthorize("hasRole('ROLE_READ_Buerger')")
    @PostAuthorize(TenantService.IS_TENANT_AUTH)
    Buerger findOne(String aLong);

    @Override
    @PreAuthorize("hasRole('ROLE_WRITE_Buerger') and " + TenantService.IS_TENANT_AUTH)
    Buerger save(Buerger buerger);

    @Override
    @PreAuthorize("hasRole('ROLE_DELETE_Buerger')")
    @PostAuthorize(TenantService.IS_TENANT_AUTH)
    void delete(String aLong);

    @Override
    @PreAuthorize("hasRole('ROLE_DELETE_Buerger')")
    @PreFilter(TenantService.IS_TENANT_FILTER)
    void delete(Iterable<? extends Buerger> iterable);

    @Override
    @PreAuthorize("hasRole('ROLE_DELETE_Buerger')")
    @PreFilter(TenantService.IS_TENANT_FILTER)
    void deleteAll();

    @Override
    @PreAuthorize("hasRole('ROLE_DELETE_Buerger')")
    @PostAuthorize(TenantService.IS_TENANT_AUTH)
    public void delete(Buerger entity);

}
