package de.muenchen.demo.service.domain;

import de.muenchen.demo.service.domain.permissions.Perm;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.security.access.annotation.Secured;

import java.util.List;

/**
 *
 * @author claus.straube
 */
@Secured({Perm.READ + "StaatsangehoerigkeitReference"})
public interface StaatsangehoerigkeitReferenceRepository extends PagingAndSortingRepository<StaatsangehoerigkeitReference, Long>{
        
public final static String StaatsangehoerigkeitReference_CACHE = "STAATSANGEHOERIGKEITREFERENCE_CACHE";




    @Override
    @CachePut(value = StaatsangehoerigkeitReference_CACHE, key = "#p0.referencedOid + #p0.mandant.oid")
    @Secured({Perm.WRITE + "StaatsangehoerigkeitReference"})
    StaatsangehoerigkeitReference save(StaatsangehoerigkeitReference entity);

    @Override
    @CacheEvict(value = StaatsangehoerigkeitReference_CACHE, key = "#p0.referencedOid + #p0.mandant.oid")
    @Secured({Perm.DELETE + "StaatsangehoerigkeitReference"})
    void delete(StaatsangehoerigkeitReference entity);

    @Override
    @Secured({Perm.DELETE + "StaatsangehoerigkeitReference"})
    void delete(Long aLong);

    @Override
    @Secured({Perm.DELETE + "StaatsangehoerigkeitReference"})
    void delete(Iterable<? extends StaatsangehoerigkeitReference> iterable);

    @Override
    @Secured({Perm.DELETE + "StaatsangehoerigkeitReference"})
    void deleteAll();

    @Cacheable(value = StaatsangehoerigkeitReference_CACHE, key = "#p0 + #p1")
    public StaatsangehoerigkeitReference findFirstByReferencedOidAndMandantOid(String referencedOid, String mid);


    public List<StaatsangehoerigkeitReference> findByMandantOid(String mid);
   // public List<StaatsangehoerigkeitReference> findByReferencedOidAndMandantOid(String referencedOid, String mid);
    
}
