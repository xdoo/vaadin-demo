package de.muenchen.demo.service.domain;

import de.muenchen.service.TenantService;
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
@PreAuthorize("hasRole('ROLE_READ_Pass')")
public interface PassRepository extends CrudRepository<Pass, Long> {

	public final static String Pass_CACHE = "PASS_CACHE";

	@Override
	@PostFilter(TenantService.IS_TENANT_FILTER)
	Iterable<Pass> findAll();

	@Override
	@Cacheable(value = Pass_CACHE, key = "#p0")
	@PreAuthorize("hasRole('ROLE_READ_Pass')")
	@PostAuthorize(TenantService.IS_TENANT_AUTH)
	Pass findOne(Long id);

	@Override
	@CachePut(value = Pass_CACHE, key = "#p0.oid")
	@PreAuthorize("hasRole('ROLE_WRITE_Pass')")
	Pass save(Pass Pass);

	@Override
	@CacheEvict(value = Pass_CACHE, key = "#p0")
	@PreAuthorize("hasRole('ROLE_DELETE_Pass')")
	@PostAuthorize(TenantService.IS_TENANT_AUTH)
	void delete(Long id);

	@Override
	@CacheEvict(value = Pass_CACHE, allEntries = true)
	@PreAuthorize("hasRole('ROLE_DELETE_Pass')")
	@PreFilter(TenantService.IS_TENANT_FILTER)
	void delete(Iterable<? extends Pass> iterable);

	@Override
	@CacheEvict(value = Pass_CACHE, allEntries = true)
	@PreAuthorize("hasRole('ROLE_DELETE_Pass')")
	@PreFilter(TenantService.IS_TENANT_FILTER)
	void deleteAll();

	@Override
	@CacheEvict(value = Pass_CACHE, key = "#p0.oid")
	@PreAuthorize("hasRole('ROLE_DELETE_Pass')")
	@PostAuthorize(TenantService.IS_TENANT_AUTH)
	void delete(Pass entity);
	
}
