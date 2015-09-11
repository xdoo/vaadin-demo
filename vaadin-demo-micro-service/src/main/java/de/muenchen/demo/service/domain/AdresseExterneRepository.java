package de.muenchen.demo.service.domain;

import de.muenchen.demo.service.security.TenantService;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreFilter;

@RepositoryRestResource(exported = true)
@PreAuthorize("hasRole('PERM_READ_AdresseExterne')")
public interface AdresseExterneRepository extends CrudRepository<AdresseExterne, Long> {

	@Override
	@PostFilter(TenantService.IS_TENANT_FILTER)
	Iterable<AdresseExterne> findAll();

	@Override
	@PreAuthorize("hasRole('PERM_READ_AdresseExterne')")
	@PostAuthorize(TenantService.IS_TENANT_AUTH)
	AdresseExterne findOne(Long aLong);

	@Override
	@PreAuthorize("hasRole('PERM_WRITE_AdresseExterne')")
	AdresseExterne save(AdresseExterne AdresseExterne);

	@Override
	@PreAuthorize("hasRole('PERM_DELETE_AdresseExterne')")
	@PostAuthorize(TenantService.IS_TENANT_AUTH)
	void delete(Long aLong);

	@Override
	@PreAuthorize("hasRole('PERM_DELETE_AdresseExterne')")
	@PreFilter(TenantService.IS_TENANT_FILTER)
	void delete(Iterable<? extends AdresseExterne> iterable);

	@Override
	@PreAuthorize("hasRole('PERM_DELETE_AdresseExterne')")
	@PreFilter(TenantService.IS_TENANT_FILTER)
	void deleteAll();

	@Override
	@PreAuthorize("hasRole('PERM_DELETE_AdresseExterne')")
	@PostAuthorize(TenantService.IS_TENANT_AUTH)
	void delete(AdresseExterne entity);
	
}
