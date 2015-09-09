package de.muenchen.demo.service.domain;

import de.muenchen.demo.service.domain.permissions.Perm;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.security.access.annotation.Secured;

import java.util.List;

/**
 *
 * @author claus.straube
 */
@Secured({"ROLE_PERM_READ_Buerger"})
public interface BuergerRepository extends CrudRepository<Buerger, Long> {

    public final static String BUERGER_CACHE = "BUERGER_CACHE";

    @Override
    @CachePut(value = BUERGER_CACHE, key = "#p0.oid + #p0.mandant.oid")
    @Secured({Perm.WRITE + "Buerger"})
    Buerger save(Buerger entity);

    @Override
    @Secured({Perm.DELETE + "Buerger"})
    void delete(Long aLong);

    @Override
    @Secured({Perm.DELETE + "Buerger"})
    void delete(Iterable<? extends Buerger> iterable);

    @Override
    @Secured({Perm.DELETE + "Buerger"})
    void deleteAll();
    
    @Cacheable(value = BUERGER_CACHE, key = "#p0 + #p1")
    public Buerger findFirstByOidAndMandantOid(String oid, String mid);


    @Override
    @CacheEvict(value = BUERGER_CACHE, key = "#p0.oid + #p0.mandant.oid")
    @Secured({Perm.DELETE + "Buerger"})
    public void delete(Buerger entity);
  
    // Unterläuft das Mandanten Konzept. Sollte deshalb erst einmal nicht verwendet werden.
//    List<Buerger> findByOid(String oid);

    List<Buerger> findByKinderOidAndMandantOid(String oid, String mid);

    List<Buerger> findByWohnungenOidAndMandantOid(String oid, String mid);

    List<Buerger> findByPassOidAndMandantOid(String oid, String mid);

    List<Buerger> findByStaatsangehoerigkeitReferencesReferencedOidAndMandantOid(String oid, String mid);

}
