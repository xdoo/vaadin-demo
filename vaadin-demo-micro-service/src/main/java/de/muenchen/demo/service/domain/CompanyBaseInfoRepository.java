package de.muenchen.demo.service.domain;

import de.muenchen.demo.service.services.TenantService;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreFilter;

@RepositoryRestResource(exported = true)
@PreAuthorize("hasRole('ROLE_READ_CompanyBaseInfo')")
public interface CompanyBaseInfoRepository extends CrudRepository<CompanyBaseInfo, Long> {

	@Override
	@PostFilter(TenantService.IS_TENANT_FILTER)
	Iterable<CompanyBaseInfo> findAll();

	@Override
	@PreAuthorize("hasRole('ROLE_READ_CompanyBaseInfo')")
	@PostAuthorize(TenantService.IS_TENANT_AUTH)
	CompanyBaseInfo findOne(Long id);

	@Override
	@PreAuthorize("hasRole('ROLE_WRITE_CompanyBaseInfo')")
	CompanyBaseInfo save(CompanyBaseInfo CompanyBaseInfo);

	@Override
	@PreAuthorize("hasRole('ROLE_DELETE_CompanyBaseInfo')")
	@PostAuthorize(TenantService.IS_TENANT_AUTH)
	void delete(Long aLong);

	@Override
	@PreAuthorize("hasRole('ROLE_DELETE_CompanyBaseInfo')")
	@PreFilter(TenantService.IS_TENANT_FILTER)
	void delete(Iterable<? extends CompanyBaseInfo> iterable);

	@Override
	@PreAuthorize("hasRole('ROLE_DELETE_CompanyBaseInfo')")
	@PreFilter(TenantService.IS_TENANT_FILTER)
	void deleteAll();

	@Override
	@PreAuthorize("hasRole('ROLE_DELETE_CompanyBaseInfo')")
	@PostAuthorize(TenantService.IS_TENANT_AUTH)
	void delete(CompanyBaseInfo entity);
	
}
