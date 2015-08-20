package de.muenchen.demo.service.services;

import de.muenchen.demo.service.domain.Sachbearbeiter;
import java.util.List;

/**
 *
 * @author claus
 */
public interface SachbearbeiterService {

    public List<Sachbearbeiter> query();

    public List<Sachbearbeiter> query(String query);

    public Sachbearbeiter create();

    public Sachbearbeiter save(Sachbearbeiter sachbearbeiter);

    public Sachbearbeiter read(String oid);

    public Sachbearbeiter update(Sachbearbeiter sachbearbeiter);

    public void delete(String oid);

    public Sachbearbeiter copy(String oid);

    public void releaseSachbearbeiterBuerger(String buergerOid, String sOid);

    public void releaseSachbearbeiterAllBuerger(String oid);

    public Sachbearbeiter readUserSachbearbeiter(String oid);

    public void releaseUserSachbearbeiter(String userOid);
    
    public void releaseSachbearbeiterUser(String sOid);

}
