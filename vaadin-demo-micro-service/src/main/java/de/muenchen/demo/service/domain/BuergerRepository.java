package de.muenchen.demo.service.domain;

import java.util.List;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 *
 * @author claus.straube
 */
public interface BuergerRepository extends PagingAndSortingRepository<Buerger, Long> {
    
    List<Buerger> findByOid(String oid);
    
}
