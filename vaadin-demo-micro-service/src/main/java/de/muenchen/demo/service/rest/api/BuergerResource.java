package de.muenchen.demo.service.rest.api;

import de.muenchen.demo.service.domain.Buerger;
import de.muenchen.demo.service.domain.Pass;
import de.muenchen.demo.service.domain.Sachbearbeiter;
import de.muenchen.demo.service.domain.Staatsangehoerigkeit;
import de.muenchen.demo.service.domain.StaatsangehoerigkeitReference;
import de.muenchen.demo.service.domain.User;
import de.muenchen.demo.service.domain.Wohnung;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author claus.straube
 */
public class BuergerResource extends BaseResource {

    private String vorname;
    private String nachname;
    private Date geburtsdatum;
    private User sachbearbeiter;
    private Set<Staatsangehoerigkeit> staatsangehoerigkeiten = new HashSet<>();
    private Set<StaatsangehoerigkeitReference> staatsangehoerigkeitReferences;
    private Set<Buerger> kinder = new HashSet<>();
    private Set<Wohnung> wohnungen = new HashSet<>();
    private Set<Pass> Pass = new HashSet<>();

    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public String getNachname() {
        return nachname;
    }

    public void setNachname(String nachname) {
        this.nachname = nachname;
    }

    public Date getGeburtsdatum() {
        return geburtsdatum;
    }

    public void setGeburtsdatum(Date geburtsdatum) {
        this.geburtsdatum = geburtsdatum;
    }

    public Set<StaatsangehoerigkeitReference> getStaatsangehoerigkeitReferences() {
        return staatsangehoerigkeitReferences;
    }

    public void setStaatsangehoerigkeitReferences(Set<StaatsangehoerigkeitReference> staatsangehoerigkeitReferences) {
        this.staatsangehoerigkeitReferences = staatsangehoerigkeitReferences;
    }

    public Set<Buerger> getKinder() {
        return kinder;
    }

    public void setKinder(Set<Buerger> kinder) {
        this.kinder = kinder;
    }

    public User getSachbearbeiter() {
        return sachbearbeiter;
    }

    public void setSachbearbeiter(User sachbearbeiter) {
        this.sachbearbeiter = sachbearbeiter;
    }

    public Set<Staatsangehoerigkeit> getStaatsangehoerigkeiten() {
        return staatsangehoerigkeiten;
    }

    public void setStaatsangehoerigkeiten(Set<Staatsangehoerigkeit> staatsangehoerigkeiten) {
        this.staatsangehoerigkeiten = staatsangehoerigkeiten;
    }

    public Set<Wohnung> getWohnungen() {
        return wohnungen;
    }

    public void setWohnungen(Set<Wohnung> wohnungen) {
        this.wohnungen = wohnungen;
    }

    public Set<Pass> getPass() {
        return Pass;
    }

    public void setPass(Set<Pass> Pass) {
        this.Pass = Pass;
    }

    @Override
    public String toString() {
        return String.format("oid > %s | vorname > %s | nachname > %s | geburtsdatum > %s", this.getOid(), this.vorname, this.nachname, this.geburtsdatum);
    }
}
