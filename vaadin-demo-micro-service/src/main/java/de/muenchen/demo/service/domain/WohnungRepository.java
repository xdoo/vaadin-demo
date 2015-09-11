package de.muenchen.demo.service.domain;

import de.muenchen.demo.service.security.TenantService;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreFilter;

@RepositoryRestResource(exported = true)
@PreAuthorize("hasRole('PERM_READ_Wohnung')")
public interface WohnungRepository extends CrudRepository<Wohnung, Long> {

	@Override
	@PostFilter(TenantService.IS_TENANT_FILTER)
	Iterable<Wohnung> findAll();

	@Override
	@PreAuthorize("hasRole('PERM_READ_Wohnung')")
	@PostAuthorize(TenantService.IS_TENANT_AUTH)
	Wohnung findOne(Long aLong);

	@Override
	@PreAuthorize("hasRole('PERM_WRITE_Wohnung')")
	Wohnung save(Wohnung Wohnung);

	@Override
	@PreAuthorize("hasRole('PERM_DELETE_Wohnung')")
	@PostAuthorize(TenantService.IS_TENANT_AUTH)
	void delete(Long aLong);

	@Override
	@PreAuthorize("hasRole('PERM_DELETE_Wohnung')")
	@PreFilter(TenantService.IS_TENANT_FILTER)
	void delete(Iterable<? extends Wohnung> iterable);

	@Override
	@PreAuthorize("hasRole('PERM_DELETE_Wohnung')")
	@PreFilter(TenantService.IS_TENANT_FILTER)
	void deleteAll();

	@Override
	@PreAuthorize("hasRole('PERM_DELETE_Wohnung')")
	@PostAuthorize(TenantService.IS_TENANT_AUTH)
	void delete(Wohnung entity);
	
}
