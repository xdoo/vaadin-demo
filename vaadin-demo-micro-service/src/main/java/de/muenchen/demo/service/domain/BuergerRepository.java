package de.muenchen.demo.service.domain;

import java.util.List;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

/**
 *
 * @author claus.straube
 */
public interface BuergerRepository extends BaseRepository<Buerger> {
    
    public final static String BUERGER_CACHE = "BUERGER_CACHE";
    
    @Cacheable(value = BUERGER_CACHE, key = "#p0 + #p1")
    public Buerger findFirstByOidAndMandantOid(String oid, String mid);

    @Override
    @CachePut(value = BUERGER_CACHE, key = "#p0.oid + #p0.mandant.oid")
    public Buerger save(Buerger entity);

    @Override
    @CacheEvict(value = BUERGER_CACHE, key = "#p0.oid + #p0.mandant.oid")
    public void delete(Buerger entity);
  
    // Unterläuft das Mandanten Konzept. Sollte deshalb erst einmal nicht verwendet werden.
//    List<Buerger> findByOid(String oid);

    List<Buerger> findByKinderOidAndMandantOid(String oid, String mid);

    List<Buerger> findByWohnungenOidAndMandantOid(String oid, String mid);

    List<Buerger> findByPassOidAndMandantOid(String oid, String mid);

    List<Buerger> findByStaatsangehoerigkeitReferencesReferencedOidAndMandantOid(String oid, String mid);

}
