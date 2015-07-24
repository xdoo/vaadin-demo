package de.muenchen.demo.service.domain;

import java.util.List;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author claus.straube
 */
public interface SachbearbeiterRepository  extends BaseRepository<Sachbearbeiter> {
    
    List<Buerger> findByOid(String oid);
    
}
