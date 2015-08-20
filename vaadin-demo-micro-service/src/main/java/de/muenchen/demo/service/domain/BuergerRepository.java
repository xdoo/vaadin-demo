package de.muenchen.demo.service.domain;

import java.util.List;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

/**
 *
 * @author claus.straube
 */
public interface BuergerRepository extends BaseRepository<Buerger> {
    
    public final static String BUERGER_CACHE = "BUERGER_CACHE";
    
    @Override
    @Cacheable(value = BUERGER_CACHE, key = "#p0 + #p1")
    public List<Buerger> findByOidAndMandantOid(String oid, String mid);

    @Override
    @CachePut(value = BUERGER_CACHE, key = "#p0.oid + #p0.mandant.oid")
    public Buerger save(Buerger entity);
    
    List<Buerger> findByOid(String oid);

    List<Buerger> findByKinderOid(String oid);

    List<Buerger> findByWohnungenOid(String oid);

    List<Buerger> findByPassOid(String oid);

    List<Buerger> findByStaatsangehoerigkeitReferencesReferencedOid(String oid);

}
