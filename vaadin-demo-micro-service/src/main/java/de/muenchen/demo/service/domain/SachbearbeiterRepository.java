package de.muenchen.demo.service.domain;

import java.util.List;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author claus.straube
 */
public interface SachbearbeiterRepository extends CrudRepository<Sachbearbeiter, Long> {

    public final static String Sachbearbeiter_CACHE = "SACHBEARBEITER_CACHE";

    @Cacheable(value = Sachbearbeiter_CACHE, key = "#p0 + #p1")
    public Sachbearbeiter findFirstByOidAndMandantOid(String oid, String mid);

    @Override
    @CachePut(value = Sachbearbeiter_CACHE, key = "#p0.oid + #p0.mandant.oid")
    public Sachbearbeiter save(Sachbearbeiter entity);

    @Override
    @CacheEvict(value = Sachbearbeiter_CACHE, key = "#p0.oid + #p0.mandant.oid")
    public void delete(Sachbearbeiter entity);
    
    List<Sachbearbeiter> findByMandantOid(String oid);

    List<Sachbearbeiter> findByBuergerOidAndMandantOid(String oid, String mid);

    Sachbearbeiter findByUserOidAndMandantOid(String oid, String mid);
}
