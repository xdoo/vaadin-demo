/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.demo.service.services;

import de.muenchen.demo.service.domain.Mandant;
import java.util.List;

/**
 *
 * @author praktikant.tmar
 */
public interface MandantService {
    
    public List<Mandant> query();
    
    public List<Mandant> query(String query);
    
    public Mandant create();
    
    public Mandant save(Mandant mandant);
    
    public Mandant read(String oid);
    
    public Mandant update(Mandant mandant);
    
    public void delete(String oid);
    
    public Mandant copy(String oid);
    
}