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
@Secured({Perm.READ + "Sachbearbeiter"})
public interface SachbearbeiterRepository extends CrudRepository<Sachbearbeiter, Long> {

    String Sachbearbeiter_CACHE = "SACHBEARBEITER_CACHE";

    @Override
    @Secured({Perm.WRITE + "Sachbearbeiter"})
    @CachePut(value = Sachbearbeiter_CACHE, key = "#p0.oid + #p0.mandant.oid")
    Sachbearbeiter save(Sachbearbeiter entity);

    @Override
    @Secured({Perm.DELETE + "Sachbearbeiter"})
    void delete(Long aLong);

    @Override
    @Secured({Perm.DELETE + "Sachbearbeiter"})
    void delete(Iterable<? extends Sachbearbeiter> iterable);

    @Override
    @Secured({Perm.DELETE + "Sachbearbeiter"})
    @CacheEvict(value = Sachbearbeiter_CACHE, key = "#p0.oid + #p0.mandant.oid")
    void delete(Sachbearbeiter authorityPermission);

    @Override
    @Secured({Perm.DELETE + "Sachbearbeiter"})
    void deleteAll();


    @Cacheable(value = Sachbearbeiter_CACHE, key = "#p0 + #p1")
    Sachbearbeiter findFirstByOidAndMandantOid(String oid, String mid);
    
    List<Sachbearbeiter> findByMandantOid(String oid);

    List<Sachbearbeiter> findByBuergerOidAndMandantOid(String oid, String mid);

    Sachbearbeiter findByUserOidAndMandantOid(String oid, String mid);
}
