package de.muenchen.demo.service.domain;

import java.util.List;

/**
 *
 * @author claus.straube
 */
public interface SachbearbeiterRepository extends BaseRepository<Sachbearbeiter> {

    List<Sachbearbeiter> findByBuergerOidAndMandantOid(String oid, String mid);

    Sachbearbeiter findByUserOidAndMandantOid(String oid, String mid);
}
