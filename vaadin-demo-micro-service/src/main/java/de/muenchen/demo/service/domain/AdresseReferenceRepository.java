package de.muenchen.demo.service.domain;

import de.muenchen.service.TenantService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreFilter;

import java.util.List;

@RepositoryRestResource(exported = true)
@PreAuthorize("hasRole('ROLE_READ_AdresseReference')")
public interface AdresseReferenceRepository extends JpaRepository<AdresseReference, Long> {

	public final static String ADRESSEREFERENCE_CACHE = "ADRESSEREFERENCE_CACHE";

	@Override
	@PostFilter(TenantService.IS_TENANT_FILTER)
	List<AdresseReference> findAll();

	@Override
	@Cacheable(value = ADRESSEREFERENCE_CACHE, key = "#p0")
	@PreAuthorize("hasRole('ROLE_READ_AdresseReference')")
	@PostAuthorize(TenantService.IS_TENANT_AUTH)
	AdresseReference findOne(Long aLong);

	@Override
	@CachePut(value = ADRESSEREFERENCE_CACHE, key = "#p0.oid")
	@PreAuthorize("hasRole('ROLE_WRITE_AdresseReference')")
	AdresseReference save(AdresseReference AdresseReference);

	@Override
	@CacheEvict(value = ADRESSEREFERENCE_CACHE, key = "#p0")
	@PreAuthorize("hasRole('ROLE_DELETE_AdresseReference')")
	@PostAuthorize(TenantService.IS_TENANT_AUTH)
	void delete(Long aLong);

	@Override
	@CacheEvict(value = ADRESSEREFERENCE_CACHE, allEntries = true)
	@PreAuthorize("hasRole('ROLE_DELETE_AdresseReference')")
	@PreFilter(TenantService.IS_TENANT_FILTER)
	void delete(Iterable<? extends AdresseReference> iterable);

	@Override
	@CacheEvict(value = ADRESSEREFERENCE_CACHE, allEntries = true)
	@PreAuthorize("hasRole('ROLE_DELETE_AdresseReference')")
	@PreFilter(TenantService.IS_TENANT_FILTER)
	void deleteAll();

	@Override
	@CacheEvict(value = ADRESSEREFERENCE_CACHE, key = "#p0.oid")
	@PreAuthorize("hasRole('ROLE_DELETE_AdresseReference')")
	@PostAuthorize(TenantService.IS_TENANT_AUTH)
	void delete(AdresseReference entity);
	
}
