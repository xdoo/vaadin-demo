package de.muenchen.demo.service.domain;

import java.util.List;

/**
 *
 * @author claus.straube
 */
public interface AdresseInterneRepository extends BaseRepository<AdresseInterne> {

    public List<AdresseInterne> findByOid(String oid);

}
