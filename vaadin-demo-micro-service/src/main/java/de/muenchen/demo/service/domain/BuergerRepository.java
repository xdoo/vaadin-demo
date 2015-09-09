package de.muenchen.demo.service.domain;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

/**
 *
 * @author claus.straube
 */
@PreAuthorize("hasRole('PERM_READ_Buerger')")
public interface BuergerRepository extends CrudRepository<Buerger, Long> {

    public final static String BUERGER_CACHE = "BUERGER_CACHE";

    @Override
    @PreAuthorize("hasRole('PERM_WRITE_Buerger')")
    Buerger save(Buerger entity);

    @Override
    @PreAuthorize("hasRole('PERM_DELETE_Buerger')")
    void delete(Long aLong);

    @Override
    @PreAuthorize("hasRole('PERM_DELETE_Buerger')")
    void delete(Iterable<? extends Buerger> iterable);

    @Override
    @PreAuthorize("hasRole('PERM_DELETE_Buerger')")
    void deleteAll();
    
    @Cacheable(value = BUERGER_CACHE, key = "#p0 + #p1")
    public Buerger findFirstByOidAndMandantOid(String oid, String mid);


    @Override
    @PreAuthorize("hasRole('PERM_DELETE_Buerger')")
    public void delete(Buerger entity);
  
    // Unterläuft das Mandanten Konzept. Sollte deshalb erst einmal nicht verwendet werden.
//    List<Buerger> findByOid(String oid);

    List<Buerger> findByKinderOidAndMandantOid(String oid, String mid);

    List<Buerger> findByWohnungenOidAndMandantOid(String oid, String mid);

    List<Buerger> findByPassOidAndMandantOid(String oid, String mid);

    List<Buerger> findByStaatsangehoerigkeitReferencesReferencedOidAndMandantOid(String oid, String mid);

}
