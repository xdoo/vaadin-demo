package de.muenchen.demo.service.services;

import de.muenchen.demo.service.domain.Buerger;
import java.util.List;

/**
 *
 * @author claus
 */
public interface BuergerService {

    public List<Buerger> query();

    public Iterable<Buerger> readKinder(String oid);

    public void deleteKindAllBuerger(String oid);
    
    public void deleteKindBuerger(String kindOid, String buergerOid);

    public Iterable<Buerger> readWohnung(String oid);

    public void deleteWohnungAllBuerger(String oid);

    public void deleteWohnungBuerger(String wohnungOid, String buergerOid);

    public Iterable<Buerger> readPass(String oid);

    public void deleteBuergerPass(String oid);
    
    public void deleteBuergerPass(String passOid, String buergerOid);

    public Iterable<Buerger> readStaatsangehoerigkeit(String oid);

    public  void deleteBuergerStaatsangehoerigkeit(String staatOid);
    
    public void deleteBuergerStaatsangehoerigkeit(String staatOid, String buergerOid);

    public List<Buerger> query(String query);

    public Buerger create();

    public Buerger save(Buerger buerger);

    public Buerger read(String oid);

    public Buerger update(Buerger buerger);

    public void delete(String oid);

    public Buerger copy(String oid);

}
