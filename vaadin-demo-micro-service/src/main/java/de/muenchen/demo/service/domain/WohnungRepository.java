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
public interface WohnungRepository extends CrudRepository<Wohnung, Long> {

    public final static String Wohnung_CACHE = "WOHNUNG_CACHE";

    @Cacheable(value = Wohnung_CACHE, key = "#p0 + #p1")
        public Wohnung findFirstByOidAndMandantOid(String oid, String mid);


    @Override
    @CachePut(value = Wohnung_CACHE, key = "#p0.oid + #p0.mandant.oid")
    public Wohnung save(Wohnung entity);

    @Override
    @CacheEvict(value = Wohnung_CACHE, key = "#p0.oid + #p0.mandant.oid")
    public void delete(Wohnung entity);
    
    public Wohnung findByAdresseOidAndMandantOid(String oid, String mid);
    
        List<Wohnung> findByMandantOid(String oid);

}
