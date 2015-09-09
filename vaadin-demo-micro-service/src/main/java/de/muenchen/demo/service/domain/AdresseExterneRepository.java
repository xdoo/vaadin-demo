package de.muenchen.demo.service.domain;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

/**
 *
 * @author claus.straube
 */
public interface AdresseExterneRepository extends BaseRepository<AdresseExterne> {

    public final static String ADRESSEEXTERNE_CACHE = "ADRESSEEXTERNE_CACHE";

    @Cacheable(value = ADRESSEEXTERNE_CACHE, key = "#p0 + #p1")
    public AdresseExterne findFirstByOidAndMandantOid(String oid, String mid);

    @Override
    @CachePut(value = ADRESSEEXTERNE_CACHE, key = "#p0.oid + #p0.mandant.oid")
    public AdresseExterne save(AdresseExterne entity);

    @Override
    @CacheEvict(value = ADRESSEEXTERNE_CACHE, key = "#p0.oid + #p0.mandant.oid")
    public void delete(AdresseExterne entity);

}
