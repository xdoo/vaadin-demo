package de.muenchen.demo.service.domain;

import java.util.List;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 *
 * @author claus.straube
 */
public interface StaatsangehoerigkeitReferenceRepository extends PagingAndSortingRepository<StaatsangehoerigkeitReference, Long>{
        
public final static String StaatsangehoerigkeitReference_CACHE = "STAATSANGEHOERIGKEITREFERENCE_CACHE";

    @Cacheable(value = StaatsangehoerigkeitReference_CACHE, key = "#p0 + #p1")
        public StaatsangehoerigkeitReference findFirstByReferencedOidAndMandantOid(String referencedOid, String mid);


    @Override
    @CachePut(value = StaatsangehoerigkeitReference_CACHE, key = "#p0.referencedOid + #p0.mandant.oid")
    public StaatsangehoerigkeitReference save(StaatsangehoerigkeitReference entity);

    @Override
    @CacheEvict(value = StaatsangehoerigkeitReference_CACHE, key = "#p0.referencedOid + #p0.mandant.oid")
    public void delete(StaatsangehoerigkeitReference entity);
    public List<StaatsangehoerigkeitReference> findByMandantOid(String mid);
   // public List<StaatsangehoerigkeitReference> findByReferencedOidAndMandantOid(String referencedOid, String mid);
    
}
