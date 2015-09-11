package de.muenchen.demo.service.domain;

import de.muenchen.demo.service.security.TenantService;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreFilter;

@RepositoryRestResource(exported = true)
@PreAuthorize("hasRole('PERM_READ_AdresseInterne')")
public interface AdresseInterneRepository extends CrudRepository<AdresseInterne, Long> {

	@Override
	@PostFilter(TenantService.IS_TENANT_FILTER)
	Iterable<AdresseInterne> findAll();

	@Override
	@PreAuthorize("hasRole('PERM_READ_AdresseInterne')")
	@PostAuthorize(TenantService.IS_TENANT_AUTH)
	AdresseInterne findOne(Long aLong);

	@Override
	@PreAuthorize("hasRole('PERM_WRITE_AdresseInterne')")
	AdresseInterne save(AdresseInterne AdresseInterne);

	@Override
	@PreAuthorize("hasRole('PERM_DELETE_AdresseInterne')")
	@PostAuthorize(TenantService.IS_TENANT_AUTH)
	void delete(Long aLong);

	@Override
	@PreAuthorize("hasRole('PERM_DELETE_AdresseInterne')")
	@PreFilter(TenantService.IS_TENANT_FILTER)
	void delete(Iterable<? extends AdresseInterne> iterable);

	@Override
	@PreAuthorize("hasRole('PERM_DELETE_AdresseInterne')")
	@PreFilter(TenantService.IS_TENANT_FILTER)
	void deleteAll();

	@Override
	@PreAuthorize("hasRole('PERM_DELETE_AdresseInterne')")
	@PostAuthorize(TenantService.IS_TENANT_AUTH)
	void delete(AdresseInterne entity);
	
}
