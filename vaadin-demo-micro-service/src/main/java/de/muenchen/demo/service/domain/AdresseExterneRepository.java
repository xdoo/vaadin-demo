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
@PreAuthorize("hasRole('ROLE_READ_AdresseExterne')")
public interface AdresseExterneRepository extends CrudRepository<AdresseExterne, Long> {

	public static final String ADRESSEEXTERNE_CACHE = "ADRESSEEXTERNE_CACHE";

	@Override
	@PostFilter(TenantService.IS_TENANT_FILTER)
	Iterable<AdresseExterne> findAll();

	@Override
	@Cacheable(value = ADRESSEEXTERNE_CACHE, key = "#p0")
	@PreAuthorize("hasRole('ROLE_READ_AdresseExterne')")
	@PostAuthorize(TenantService.IS_TENANT_AUTH)
	AdresseExterne findOne(Long aLong);

	@Override
	@CachePut(value = ADRESSEEXTERNE_CACHE, key = "#p0.oid")
	@PreAuthorize("hasRole('ROLE_WRITE_AdresseExterne')")
	AdresseExterne save(AdresseExterne AdresseExterne);

	@Override
	@CacheEvict(value = ADRESSEEXTERNE_CACHE, key = "#p0")
	@PreAuthorize("hasRole('ROLE_DELETE_AdresseExterne')")
	@PostAuthorize(TenantService.IS_TENANT_AUTH)
	void delete(Long aLong);

	@Override
	@CacheEvict(value = ADRESSEEXTERNE_CACHE, allEntries = true)
	@PreAuthorize("hasRole('ROLE_DELETE_AdresseExterne')")
	@PreFilter(TenantService.IS_TENANT_FILTER)
	void delete(Iterable<? extends AdresseExterne> iterable);

	@Override
	@CacheEvict(value = ADRESSEEXTERNE_CACHE, allEntries = true)
	@PreAuthorize("hasRole('ROLE_DELETE_AdresseExterne')")
	@PreFilter(TenantService.IS_TENANT_FILTER)
	void deleteAll();

	@Override
	@CacheEvict(value = ADRESSEEXTERNE_CACHE, key = "#p0.oid")
	@PreAuthorize("hasRole('ROLE_DELETE_AdresseExterne')")
	@PostAuthorize(TenantService.IS_TENANT_AUTH)
	void delete(AdresseExterne entity);
	
}
