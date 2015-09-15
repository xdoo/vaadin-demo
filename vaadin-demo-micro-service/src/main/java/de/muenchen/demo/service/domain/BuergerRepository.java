package de.muenchen.demo.service.domain;

import de.muenchen.demo.service.security.TenantService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
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
    @Cacheable(value = BUERGER_CACHE, key = "#p0")
    @PreAuthorize("hasRole('ROLE_READ_Buerger')")
    @PostAuthorize(TenantService.IS_TENANT_AUTH)
    Buerger findOne(String aLong);

    @Override
    @CachePut(value = BUERGER_CACHE, key = "#p0.oid")
    @PreAuthorize("hasRole('ROLE_WRITE_Buerger') and " + TenantService.IS_TENANT_AUTH)
    Buerger save(Buerger buerger);

    @Override
    @CacheEvict(value = BUERGER_CACHE, key = "#p0")
    @PreAuthorize("hasRole('ROLE_DELETE_Buerger')")
    @PostAuthorize(TenantService.IS_TENANT_AUTH)
    void delete(String aLong);

    @Override
    @CacheEvict(value = BUERGER_CACHE, allEntries = true)
    @PreAuthorize("hasRole('ROLE_DELETE_Buerger')")
    @PreFilter(TenantService.IS_TENANT_FILTER)
    void delete(Iterable<? extends Buerger> iterable);

    @Override
    @CacheEvict(value = BUERGER_CACHE, allEntries = true)
    @PreAuthorize("hasRole('ROLE_DELETE_Buerger')")
    @PreFilter(TenantService.IS_TENANT_FILTER)
    void deleteAll();

    @Override
    @CacheEvict(value = BUERGER_CACHE, key = "#p0.oid")
    @PreAuthorize("hasRole('ROLE_DELETE_Buerger')")
    @PostAuthorize(TenantService.IS_TENANT_AUTH)
    public void delete(Buerger entity);

}
