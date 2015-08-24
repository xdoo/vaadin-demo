/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.demo.service.domain;

import java.util.List;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author praktikant.tmar
 */
public interface PassRepository extends CrudRepository<Pass, Long> {

    public final static String Pass_CACHE = "PASS_CACHE";

    @Cacheable(value = Pass_CACHE, key = "#p0 + #p1")
        public Pass findFirstByOidAndMandantOid(String oid, String mid);


    @Override
    @CachePut(value = Pass_CACHE, key = "#p0.oid + #p0.mandant.oid")
    public Pass save(Pass entity);

    @Override
    @CacheEvict(value = Pass_CACHE, key = "#p0.oid + #p0.mandant.oid")
    public void delete(Pass entity);
    
    public List<Pass> findByMandantOid(String mid);

    List<Pass> findByOid(String oid);

    public Pass findFirstByStaatsangehoerigkeitReferenceReferencedOidAndMandantOid(String oid, String mid);

}
