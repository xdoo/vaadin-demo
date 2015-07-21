/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.demo.service.services;

import de.muenchen.demo.service.domain.Staatsangehoerigkeit;
import java.util.List;

/**
 *
 * @author praktikant.tmar
 */
public interface StaatsangehoerigkeitService {
        
    public Staatsangehoerigkeit read(String referencedOid);
    
    public List<Staatsangehoerigkeit> query();
    
    public Staatsangehoerigkeit create(String oid);
    
    public void delete(String oid);




    
}
