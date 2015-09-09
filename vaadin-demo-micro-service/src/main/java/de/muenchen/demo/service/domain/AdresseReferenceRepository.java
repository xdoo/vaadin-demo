/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.demo.service.domain;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
/**
 *
 * @author praktikant.tmar
 */
public interface AdresseReferenceRepository extends BaseRepository<AdresseReference>{
        

    public final static String ADRESSEREFERENCE_CACHE = "ADRESSEREFERENCE_CACHE";
    
    @Cacheable(value = ADRESSEREFERENCE_CACHE, key = "#p0 + #p1")
    public AdresseReference findFirstByOidAndMandantOid(String oid, String mid);
    
    @Override
    @CachePut(value = ADRESSEREFERENCE_CACHE, key = "#p0.oid + #p0.mandant.oid")
    public AdresseReference save(AdresseReference entity);

    @Override
    @CacheEvict(value = ADRESSEREFERENCE_CACHE, key = "#p0.oid + #p0.mandant.oid")
    public void delete(AdresseReference entity);

  
}
