/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.demo.service.domain;

import java.util.List;
/**
 *
 * @author praktikant.tmar
 */
public interface AdresseReferenceRepository extends BaseRepository<AdresseReference>{
        
    public List<AdresseReference> findByOid(String oid);

    
}