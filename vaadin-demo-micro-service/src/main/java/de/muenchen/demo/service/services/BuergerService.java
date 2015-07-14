package de.muenchen.demo.service.services;

import de.muenchen.demo.service.domain.Buerger;
import java.util.Date;
import java.util.List;

/**
 *
 * @author claus
 */
public interface BuergerService {
    
    public List<Buerger> query();
    
    public List<Buerger> query(String query);
    
    public Buerger create();
    
    public Buerger save(Buerger buerger);
    
    public Buerger read(String oid);
    
    public Buerger update(Buerger buerger);
    
    public void delete(String oid);
    
    public Buerger copy(String oid);
    
}
