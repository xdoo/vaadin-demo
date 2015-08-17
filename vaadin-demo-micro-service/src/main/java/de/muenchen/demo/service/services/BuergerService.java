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

    public void releaseBuergerEltern(String oid);

    public void releaseBuergerElternteil(String kindOid, String buergerOid);

    public Iterable<Buerger> readWohnungBuerger(String oid);

    public void releaseWohnungAllBuerger(String oid);

    public void releaseWohnungBuerger(String wohnungOid, String buergerOid);

    public Buerger readPassBuerger(String oid);

    public void releasePassBuerger(String passOid);

    public Iterable<Buerger> readStaatsangehoerigkeitBuerger(String oid);

    public void releaseStaatsangehoerigkeitAllBuerger(String staatOid);

    public void releaseStaatsangehoerigkeitBuerger(String staatOid, String buergerOid);

    public List<Buerger> query(String query);

    public Buerger create();

    public Buerger save(Buerger buerger);

    public Buerger read(String oid);

    public Buerger update(Buerger buerger);

    public void delete(String oid);

    public Buerger copy(String oid);

}
