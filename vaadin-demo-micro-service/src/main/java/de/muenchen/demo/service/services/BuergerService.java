package de.muenchen.demo.service.services;

import de.muenchen.demo.service.domain.Buerger;

/**
 *
 * @author claus
 */
public interface BuergerService {
    
    public Buerger create(Buerger buerger);
    
    public Buerger read(String oid);
    
    public Buerger update(Buerger buerger);
    
    public void delete(String oid);
    
}
