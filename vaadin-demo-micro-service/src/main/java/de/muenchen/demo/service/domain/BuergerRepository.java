package de.muenchen.demo.service.domain;

import java.util.List;

/**
 *
 * @author claus.straube
 */
public interface BuergerRepository extends BaseRepository<Buerger> {


    List<Buerger> findByKinderOidAndMandantOid(String oid, String mid);

    List<Buerger> findByWohnungenOidAndMandantOid(String oid, String mid);

    List<Buerger> findByPassOidAndMandantOid(String oid, String mid);

    List<Buerger> findByStaatsangehoerigkeitReferencesReferencedOidAndMandantOid(String oid, String mid);

}
