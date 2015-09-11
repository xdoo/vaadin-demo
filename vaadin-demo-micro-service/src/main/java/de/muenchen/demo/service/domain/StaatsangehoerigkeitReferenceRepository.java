package de.muenchen.demo.service.domain;

import de.muenchen.demo.service.security.TenantService;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreFilter;

@RepositoryRestResource(exported = true)
@PreAuthorize("hasRole('PERM_READ_StaatsangehoerigkeitReference')")
public interface StaatsangehoerigkeitReferenceRepository extends CrudRepository<StaatsangehoerigkeitReference, Long> {

	@Override
	@PostFilter(TenantService.IS_TENANT_FILTER)
	Iterable<StaatsangehoerigkeitReference> findAll();

	@Override
	@PreAuthorize("hasRole('PERM_READ_StaatsangehoerigkeitReference')")
	@PostAuthorize(TenantService.IS_TENANT_AUTH)
	StaatsangehoerigkeitReference findOne(Long aLong);

	@Override
	@PreAuthorize("hasRole('PERM_WRITE_StaatsangehoerigkeitReference')")
	StaatsangehoerigkeitReference save(StaatsangehoerigkeitReference StaatsangehoerigkeitReference);

	@Override
	@PreAuthorize("hasRole('PERM_DELETE_StaatsangehoerigkeitReference')")
	@PostAuthorize(TenantService.IS_TENANT_AUTH)
	void delete(Long aLong);

	@Override
	@PreAuthorize("hasRole('PERM_DELETE_StaatsangehoerigkeitReference')")
	@PreFilter(TenantService.IS_TENANT_FILTER)
	void delete(Iterable<? extends StaatsangehoerigkeitReference> iterable);

	@Override
	@PreAuthorize("hasRole('PERM_DELETE_StaatsangehoerigkeitReference')")
	@PreFilter(TenantService.IS_TENANT_FILTER)
	void deleteAll();

	@Override
	@PreAuthorize("hasRole('PERM_DELETE_StaatsangehoerigkeitReference')")
	@PostAuthorize(TenantService.IS_TENANT_AUTH)
	void delete(StaatsangehoerigkeitReference entity);
	
}
