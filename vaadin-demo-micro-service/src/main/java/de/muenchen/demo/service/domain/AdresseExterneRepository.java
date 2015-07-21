package de.muenchen.demo.service.domain;

import java.util.List;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author claus.straube
 */
public interface AdresseExterneRepository extends BaseRepository<AdresseExterne> {

    public List<AdresseExterne> findByOid(String oid);


}
