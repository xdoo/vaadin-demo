package de.muenchen.demo.service.services;

import de.muenchen.demo.service.domain.Buerger;
import java.util.List;

/**
 *
 * @author claus
 */
public interface BuergerService {

    public List<Buerger> query();

    public Iterable<Buerger> readEltern(String oid);

    public Buerger deleteBuergerEltern(String oid);

    public Iterable<Buerger> readWohnung(String oid);

    public Buerger deleteBuergerWohnung(String oid);

    public Iterable<Buerger> readPass(String oid);

    public Buerger deleteBuergerPass(String oid);

    public Iterable<Buerger> readStaatsangehoerigkeit(String oid);

    public Buerger deleteBuergerStaatsangehoerigkeit(String staatOid);

    public List<Buerger> query(String query);

    public Buerger create();

    public Buerger save(Buerger buerger);

    public Buerger read(String oid);

    public Buerger update(Buerger buerger);

    public void delete(String oid);

    public Buerger copy(String oid);

}
