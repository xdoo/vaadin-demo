package de.muenchen.demo.service.domain;

import java.util.List;

/**
 *
 * @author claus.straube
 */
public interface AdresseExterneRepository extends BaseRepository<AdresseExterne> {

    public List<AdresseExterne> findByOid(String oid);


}
