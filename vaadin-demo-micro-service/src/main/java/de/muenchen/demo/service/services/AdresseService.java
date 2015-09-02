/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.demo.service.services;

import de.muenchen.demo.service.domain.Adresse;
import de.muenchen.demo.service.domain.AdresseReference;
import java.util.List;

/**
 *
 * @author praktikant.tmar
 */
public interface AdresseService {

    public Adresse create();

    public Adresse save(Adresse adresse);

    public Adresse read(String oid);
    
    public AdresseReference readReference(String oid);

    public List<Adresse> query();

    public void delete(String oid);

    public Adresse copy(String oid);
    
    public Adresse update(Adresse adresse);
    
    public Adresse[] suche(String query);

}
