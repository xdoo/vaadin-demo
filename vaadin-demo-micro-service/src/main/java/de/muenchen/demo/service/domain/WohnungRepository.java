package de.muenchen.demo.service.domain;

import java.util.List;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author claus.straube
 */
public interface WohnungRepository extends CrudRepository<Wohnung, Long> {

    public List<Wohnung> findByOid(String oid);


}
