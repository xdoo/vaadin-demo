package de.muenchen.demo.service.domain;

import de.muenchen.demo.service.security.TenantService;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreFilter;

@RepositoryRestResource(exported = true)
@PreAuthorize("hasRole('ROLE_READ_Sachbearbeiter')")
public interface SachbearbeiterRepository extends CrudRepository<Sachbearbeiter, Long> {

	@Override
	@PostFilter(TenantService.IS_TENANT_FILTER)
	Iterable<Sachbearbeiter> findAll();

	@Override
	@PreAuthorize("hasRole('ROLE_READ_Sachbearbeiter')")
	@PostAuthorize(TenantService.IS_TENANT_AUTH)
	Sachbearbeiter findOne(Long aLong);

	@Override
	@PreAuthorize("hasRole('ROLE_WRITE_Sachbearbeiter')")
	Sachbearbeiter save(Sachbearbeiter Sachbearbeiter);

	@Override
	@PreAuthorize("hasRole('ROLE_DELETE_Sachbearbeiter')")
	@PostAuthorize(TenantService.IS_TENANT_AUTH)
	void delete(Long aLong);

	@Override
	@PreAuthorize("hasRole('ROLE_DELETE_Sachbearbeiter')")
	@PreFilter(TenantService.IS_TENANT_FILTER)
	void delete(Iterable<? extends Sachbearbeiter> iterable);

	@Override
	@PreAuthorize("hasRole('ROLE_DELETE_Sachbearbeiter')")
	@PreFilter(TenantService.IS_TENANT_FILTER)
	void deleteAll();

	@Override
	@PreAuthorize("hasRole('ROLE_DELETE_Sachbearbeiter')")
	@PostAuthorize(TenantService.IS_TENANT_AUTH)
	void delete(Sachbearbeiter entity);
	
}
