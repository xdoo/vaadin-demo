package de.muenchen.demo.service.domain;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

/**
 *
 * @author claus.straube
 */
public interface AdresseInterneRepository extends BaseRepository<AdresseInterne> {

    public final static String ADRESSEINTERNE_CACHE = "ADRESSEINTERNE_CACHE";

    @Cacheable(value = ADRESSEINTERNE_CACHE, key = "#p0 + #p1")
    public AdresseInterne findFirstByOidAndMandantOid(String oid, String mid);
    
    @Override
    @CachePut(value = ADRESSEINTERNE_CACHE, key = "#p0.oid + #p0.mandant.oid")
    public AdresseInterne save(AdresseInterne entity);

    @Override
    @CacheEvict(value = ADRESSEINTERNE_CACHE, key = "#p0.oid + #p0.mandant.oid")
    public void delete(AdresseInterne entity);


}
