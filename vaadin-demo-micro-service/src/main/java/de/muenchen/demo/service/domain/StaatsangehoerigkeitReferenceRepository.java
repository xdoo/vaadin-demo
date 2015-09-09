package de.muenchen.demo.service.domain;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

/**
 *
 * @author claus.straube
 */
@PreAuthorize("hasRole('PERM_READ_StaatsangehoerigkeitReference')")
public interface StaatsangehoerigkeitReferenceRepository extends PagingAndSortingRepository<StaatsangehoerigkeitReference, Long>{
        
public final static String StaatsangehoerigkeitReference_CACHE = "STAATSANGEHOERIGKEITREFERENCE_CACHE";




    @Override
    @PreAuthorize("hasRole('PERM_READ_StaatsangehoerigkeitReference')")
    StaatsangehoerigkeitReference save(StaatsangehoerigkeitReference entity);

    @Override
    @PreAuthorize("hasRole('PERM_READ_StaatsangehoerigkeitReference')")
    void delete(StaatsangehoerigkeitReference entity);

    @Override
    @PreAuthorize("hasRole('PERM_READ_StaatsangehoerigkeitReference')")
    void delete(Long aLong);

    @Override
    @PreAuthorize("hasRole('PERM_READ_StaatsangehoerigkeitReference')")
    void delete(Iterable<? extends StaatsangehoerigkeitReference> iterable);

    @Override
    @PreAuthorize("hasRole('PERM_READ_StaatsangehoerigkeitReference')")
    void deleteAll();

    public StaatsangehoerigkeitReference findFirstByReferencedOidAndMandantOid(String referencedOid, String mid);


    public List<StaatsangehoerigkeitReference> findByMandantOid(String mid);
   // public List<StaatsangehoerigkeitReference> findByReferencedOidAndMandantOid(String referencedOid, String mid);
    
}
