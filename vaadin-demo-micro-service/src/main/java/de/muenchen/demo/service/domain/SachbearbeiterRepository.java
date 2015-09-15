package de.muenchen.demo.service.domain;

import de.muenchen.demo.service.security.TenantService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreFilter;

@RepositoryRestResource(exported = true)
@PreAuthorize("hasRole('ROLE_READ_Sachbearbeiter')")
public interface SachbearbeiterRepository extends CrudRepository<Sachbearbeiter, Long> {

	public final static String Sachbearbeiter_CACHE = "SACHBEARBEITER_CACHE";

	@Override
	@PostFilter(TenantService.IS_TENANT_FILTER)
	Iterable<Sachbearbeiter> findAll();

	@Override
	@Cacheable(value = Sachbearbeiter_CACHE, key = "#p0")
	@PreAuthorize("hasRole('ROLE_READ_Sachbearbeiter')")
	@PostAuthorize(TenantService.IS_TENANT_AUTH)
	Sachbearbeiter findOne(Long id);

	@Override
	@CachePut(value = Sachbearbeiter_CACHE, key = "#p0.id")
	@PreAuthorize("hasRole('ROLE_WRITE_Sachbearbeiter')")
	Sachbearbeiter save(Sachbearbeiter Sachbearbeiter);

	@Override
	@CacheEvict(value = Sachbearbeiter_CACHE, key = "#p0")
	@PreAuthorize("hasRole('ROLE_DELETE_Sachbearbeiter')")
	@PostAuthorize(TenantService.IS_TENANT_AUTH)
	void delete(Long id);

	@Override
	@CacheEvict(value = Sachbearbeiter_CACHE, allEntries = true)
	@PreAuthorize("hasRole('ROLE_DELETE_Sachbearbeiter')")
	@PreFilter(TenantService.IS_TENANT_FILTER)
	void delete(Iterable<? extends Sachbearbeiter> iterable);

	@Override
	@CacheEvict(value = Sachbearbeiter_CACHE, allEntries = true)
	@PreAuthorize("hasRole('ROLE_DELETE_Sachbearbeiter')")
	@PreFilter(TenantService.IS_TENANT_FILTER)
	void deleteAll();

	@Override
	@CacheEvict(value = Sachbearbeiter_CACHE, key = "#p0.oid")
	@PreAuthorize("hasRole('ROLE_DELETE_Sachbearbeiter')")
	@PostAuthorize(TenantService.IS_TENANT_AUTH)
	void delete(Sachbearbeiter entity);
	
}
