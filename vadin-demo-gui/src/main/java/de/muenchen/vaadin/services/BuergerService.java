package de.muenchen.vaadin.services;

import com.catify.vaadin.demo.api.domain.Buerger;
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
    
    public List<Buerger> queryBuerger();
    
    public Buerger copyBuerger(String oid);
    
}
