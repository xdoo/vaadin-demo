package de.muenchen.demo.service.domain;

import de.muenchen.service.TenantService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreFilter;

@RepositoryRestResource(exported = true)
@PreAuthorize("hasRole('ROLE_READ_AdresseInterne')")
public interface AdresseInterneRepository extends PagingAndSortingRepository<AdresseInterne, Long> {

	public final static String ADRESSEINTERNE_CACHE = "ADRESSEINTERNE_CACHE";

	@Override
	@PostFilter(TenantService.IS_TENANT_FILTER)
	Iterable<AdresseInterne> findAll();

	@Override
	@Cacheable(value = ADRESSEINTERNE_CACHE, key = "#p0")
	@PreAuthorize("hasRole('ROLE_READ_AdresseInterne')")
	@PostAuthorize(TenantService.IS_TENANT_AUTH)
	AdresseInterne findOne(Long aLong);

	@Override
	@CachePut(value = ADRESSEINTERNE_CACHE, key = "#p0.oid")
	@PreAuthorize("hasRole('ROLE_WRITE_AdresseInterne')")
	AdresseInterne save(AdresseInterne AdresseInterne);

	@Override
	@CacheEvict(value = ADRESSEINTERNE_CACHE, key = "#p0")
	@PreAuthorize("hasRole('ROLE_DELETE_AdresseInterne')")
	@PostAuthorize(TenantService.IS_TENANT_AUTH)
	void delete(Long aLong);

	@Override
	@CacheEvict(value = ADRESSEINTERNE_CACHE, allEntries = true)
	@PreAuthorize("hasRole('ROLE_DELETE_AdresseInterne')")
	@PreFilter(TenantService.IS_TENANT_FILTER)
	void delete(Iterable<? extends AdresseInterne> iterable);

	@Override
	@CacheEvict(value = ADRESSEINTERNE_CACHE, allEntries = true)
	@PreAuthorize("hasRole('ROLE_DELETE_AdresseInterne')")
	@PreFilter(TenantService.IS_TENANT_FILTER)
	void deleteAll();

	@Override
	@CacheEvict(value = ADRESSEINTERNE_CACHE, key = "#p0.oid")
	@PreAuthorize("hasRole('ROLE_DELETE_AdresseInterne')")
	@PostAuthorize(TenantService.IS_TENANT_AUTH)
	void delete(AdresseInterne entity);
	
}
