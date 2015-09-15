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
@PreAuthorize("hasRole('ROLE_READ_Wohnung')")
public interface WohnungRepository extends CrudRepository<Wohnung, Long> {

	public final static String Wohnung_CACHE = "WOHNUNG_CACHE";

	@Override
	@PostFilter(TenantService.IS_TENANT_FILTER)
	Iterable<Wohnung> findAll();

	@Override
	@Cacheable(value = Wohnung_CACHE, key = "#p0")
	@PreAuthorize("hasRole('ROLE_READ_Wohnung')")
	@PostAuthorize(TenantService.IS_TENANT_AUTH)
	Wohnung findOne(Long id);

	@Override
	@CachePut(value = Wohnung_CACHE, key = "#p0.oid")
	@PreAuthorize("hasRole('ROLE_WRITE_Wohnung')")
	Wohnung save(Wohnung Wohnung);

	@Override
	@CacheEvict(value = Wohnung_CACHE, key = "#p0")
	@PreAuthorize("hasRole('ROLE_DELETE_Wohnung')")
	@PostAuthorize(TenantService.IS_TENANT_AUTH)
	void delete(Long aLong);

	@Override
	@CacheEvict(value = Wohnung_CACHE, allEntries = true)
	@PreAuthorize("hasRole('ROLE_DELETE_Wohnung')")
	@PreFilter(TenantService.IS_TENANT_FILTER)
	void delete(Iterable<? extends Wohnung> iterable);

	@Override
	@CacheEvict(value = Wohnung_CACHE, allEntries = true)
	@PreAuthorize("hasRole('ROLE_DELETE_Wohnung')")
	@PreFilter(TenantService.IS_TENANT_FILTER)
	void deleteAll();

	@Override
	@CacheEvict(value = Wohnung_CACHE, key = "#p0.oid")
	@PreAuthorize("hasRole('ROLE_DELETE_Wohnung')")
	@PostAuthorize(TenantService.IS_TENANT_AUTH)
	void delete(Wohnung entity);
	
}
