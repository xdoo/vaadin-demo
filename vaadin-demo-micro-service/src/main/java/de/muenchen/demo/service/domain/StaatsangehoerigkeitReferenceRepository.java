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
@PreAuthorize("hasRole('ROLE_READ_StaatsangehoerigkeitReference')")
public interface StaatsangehoerigkeitReferenceRepository extends CrudRepository<StaatsangehoerigkeitReference, Long> {

	public final static String StaatsangehoerigkeitReference_CACHE = "STAATSANGEHOERIGKEITREFERENCE_CACHE";

	@Override
	@PostFilter(TenantService.IS_TENANT_FILTER)
	Iterable<StaatsangehoerigkeitReference> findAll();

	@Override
	@Cacheable(value = StaatsangehoerigkeitReference_CACHE, key = "#p0")
	@PreAuthorize("hasRole('ROLE_READ_StaatsangehoerigkeitReference')")
	@PostAuthorize(TenantService.IS_TENANT_AUTH)
	StaatsangehoerigkeitReference findOne(Long id);

	@Override
	@CachePut(value = StaatsangehoerigkeitReference_CACHE, key = "#p0.getId")
	@PreAuthorize("hasRole('ROLE_WRITE_StaatsangehoerigkeitReference')")
	StaatsangehoerigkeitReference save(StaatsangehoerigkeitReference StaatsangehoerigkeitReference);

	@Override
	@CacheEvict(value = StaatsangehoerigkeitReference_CACHE, key = "#p0")
	@PreAuthorize("hasRole('ROLE_DELETE_StaatsangehoerigkeitReference')")
	@PostAuthorize(TenantService.IS_TENANT_AUTH)
	void delete(Long id);

	@Override
	@CacheEvict(value = StaatsangehoerigkeitReference_CACHE, allEntries = true)
	@PreAuthorize("hasRole('ROLE_DELETE_StaatsangehoerigkeitReference')")
	@PreFilter(TenantService.IS_TENANT_FILTER)
	void delete(Iterable<? extends StaatsangehoerigkeitReference> iterable);

	@Override
	@CacheEvict(value = StaatsangehoerigkeitReference_CACHE, allEntries = true)
	@PreAuthorize("hasRole('ROLE_DELETE_StaatsangehoerigkeitReference')")
	@PreFilter(TenantService.IS_TENANT_FILTER)
	void deleteAll();

	@Override
	@CacheEvict(value = StaatsangehoerigkeitReference_CACHE, key = "#p0.getId")
	@PreAuthorize("hasRole('ROLE_DELETE_StaatsangehoerigkeitReference')")
	@PostAuthorize(TenantService.IS_TENANT_AUTH)
	void delete(StaatsangehoerigkeitReference entity);
	
}
