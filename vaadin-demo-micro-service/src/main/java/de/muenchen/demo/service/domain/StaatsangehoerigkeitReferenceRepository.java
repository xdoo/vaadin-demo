package de.muenchen.demo.service.domain;

import java.util.List;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 *
 * @author claus.straube
 */
public interface StaatsangehoerigkeitReferenceRepository extends PagingAndSortingRepository<StaatsangehoerigkeitReference, Long>{
        
    public List<StaatsangehoerigkeitReference> findByReferencedOid(String referencedOid);
    public List<StaatsangehoerigkeitReference> findByMandantOid(String mid);
    public List<StaatsangehoerigkeitReference> findByReferencedOidAndMandantOid(String referencedOid, String mid);
    
}
