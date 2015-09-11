package de.muenchen.demo.service.domain;

import de.muenchen.demo.service.security.TenantService;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreFilter;


@RepositoryRestResource(exported = true)
@PreAuthorize("hasRole('ROLE_READ_Pass')")
public interface PassRepository extends CrudRepository<Pass, Long> {

	@Override
	@PostFilter(TenantService.IS_TENANT_FILTER)
	Iterable<Pass> findAll();

	@Override
	@PreAuthorize("hasRole('ROLE_READ_Pass')")
	@PostAuthorize(TenantService.IS_TENANT_AUTH)
	Pass findOne(Long aLong);

	@Override
	@PreAuthorize("hasRole('ROLE_WRITE_Pass')")
	Pass save(Pass Pass);

	@Override
	@PreAuthorize("hasRole('ROLE_DELETE_Pass')")
	@PostAuthorize(TenantService.IS_TENANT_AUTH)
	void delete(Long aLong);

	@Override
	@PreAuthorize("hasRole('ROLE_DELETE_Pass')")
	@PreFilter(TenantService.IS_TENANT_FILTER)
	void delete(Iterable<? extends Pass> iterable);

	@Override
	@PreAuthorize("hasRole('ROLE_DELETE_Pass')")
	@PreFilter(TenantService.IS_TENANT_FILTER)
	void deleteAll();

	@Override
	@PreAuthorize("hasRole('ROLE_DELETE_Pass')")
	@PostAuthorize(TenantService.IS_TENANT_AUTH)
	void delete(Pass entity);
	
}
