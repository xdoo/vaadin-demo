package de.muenchen.demo.service.services;

import de.muenchen.demo.service.domain.Buerger;
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

    public void copy(List<String> oids);

    public void delete(List<String> oids);

    public Iterable<Buerger> readEltern(String oid);

    public void releaseBuergerEltern(String oid);

    public void releaseBuergerElternteil(String kindOid, String buergerOid);

    public void releaseBuergerKinder(String oid);

    public void releaseBuergerKind(String bOid, String kOid);

    public Iterable<Buerger> readWohnungBuerger(String oid);

    public void releaseWohnungAllBuerger(String oid);

    public void releaseWohnungBuerger(String wohnungOid, String buergerOid);

    public void releaseBuergerWohnungen(String oid);

    public Buerger readPassBuerger(String oid);

    public void releasePassBuerger(String passOid);

    public void releaseBuergerPaesse(String oid);

    public Iterable<Buerger> readStaatsangehoerigkeitBuerger(String oid);

    public void releaseStaatsangehoerigkeitAllBuerger(String staatOid);

    public void releaseStaatsangehoerigkeitBuerger(String staatOid, String buergerOid);

    public void releaseBuergerAllSachbearbeiter(String buergerOid);

}
