package de.muenchen.demo.service.domain;

import de.muenchen.demo.service.domain.permissions.Perm;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.security.access.annotation.Secured;

import java.util.List;

/**
 * @author claus.straube
 */
@Secured({Perm.READ + "Wohnung"})
public interface WohnungRepository extends CrudRepository<Wohnung, Long> {

    String Wohnung_CACHE = "WOHNUNG_CACHE";

    @Cacheable(value = Wohnung_CACHE, key = "#p0 + #p1")
    Wohnung findFirstByOidAndMandantOid(String oid, String mid);


    @Override
    @CachePut(value = Wohnung_CACHE, key = "#p0.oid + #p0.mandant.oid")
    @Secured({Perm.WRITE + "Wohnung"})
    Wohnung save(Wohnung entity);

    @Override
    @CacheEvict(value = Wohnung_CACHE, key = "#p0.oid + #p0.mandant.oid")
    @Secured({Perm.DELETE + "Wohnung"})
    void delete(Wohnung entity);

    @Override
    @Secured({Perm.DELETE + "Wohnung"})
    void delete(Long aLong);

    @Override
    @Secured({Perm.DELETE + "Wohnung"})
    void delete(Iterable<? extends Wohnung> iterable);

    @Override
    @Secured({Perm.DELETE + "Wohnung"})
    void deleteAll();

    Wohnung findByAdresseOidAndMandantOid(String oid, String mid);

    List<Wohnung> findByMandantOid(String oid);

}
