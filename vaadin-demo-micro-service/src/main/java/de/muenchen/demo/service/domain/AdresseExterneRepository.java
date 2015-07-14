package de.muenchen.demo.service.domain;

import java.util.List;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author claus.straube
 */
public interface AdresseExterneRepository extends CrudRepository<AdresseExterne, Long> {

    public List<AdresseExterne> findByOid(String oid);


}
