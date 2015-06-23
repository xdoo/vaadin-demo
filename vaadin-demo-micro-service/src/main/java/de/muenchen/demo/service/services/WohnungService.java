/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.demo.service.services;

import de.muenchen.demo.service.domain.Wohnung;
import java.util.List;

/**
 *
 * @author praktikant.tmar
 */
public interface WohnungService {
    
    public List<Wohnung> query();
    
    public List<Wohnung> query(String query);
    
    public Wohnung create();
    
    public Wohnung save(Wohnung wohnung);
    
    public Wohnung read(String oid);
        
    public Wohnung update(Wohnung whonung);
    
    public void delete(String oid);
    
    public Wohnung copy(String oid);
}
