package de.muenchen.demo.service.domain;

import java.util.List;

/**
 *
 * @author claus.straube
 */
public interface SachbearbeiterRepository extends BaseRepository<Sachbearbeiter> {

    List<Sachbearbeiter> findByOid(String oid);

    List<Sachbearbeiter> findByBuergerOid(String oid);

    Sachbearbeiter findByUserOid(String Oid);

}
