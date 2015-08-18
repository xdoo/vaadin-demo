package de.muenchen.demo.service.domain;

import java.util.List;

/**
 *
 * @author claus.straube
 */
public interface BuergerRepository extends BaseRepository<Buerger> {

    List<Buerger> findByOid(String oid);

    List<Buerger> findByKinderOid(String oid);

    List<Buerger> findByWohnungenOid(String oid);

    List<Buerger> findByPassOid(String oid);

    List<Buerger> findByStaatsangehoerigkeitReferencesReferencedOid(String oid);

}
