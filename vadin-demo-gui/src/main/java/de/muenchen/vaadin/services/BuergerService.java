package de.muenchen.vaadin.services;

import de.muenchen.vaadin.domain.Buerger;
import java.util.List;

/**
 *
 * @author claus.straube
 */
public interface BuergerService {
    
    public Buerger createBuerger();
    
    public Buerger readBuerger(Long id);
    
    public Buerger updateBuerger(Buerger person);
    
    public void deleteBuerger(String oid);
    
    public List<Buerger> findAll();
    
    public Buerger copyBuerger(String oid);
    
}
