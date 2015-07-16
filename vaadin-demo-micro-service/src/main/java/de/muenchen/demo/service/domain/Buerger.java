package de.muenchen.demo.service.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import org.hibernate.search.annotations.DateBridge;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Resolution;

/**
 *
 * @author claus.straube
 */
@Entity
@Indexed
@Table(name = "BUERGER")
public class Buerger extends BaseEntity {

    @Field
    @Column(length = 70, nullable = true, name = "BUER_VORNAME")
    private String vorname;

    @Field
    @Column(length = 70, nullable = false, name = "BUER_NACHNAME")
    private String nachname;

    @Column(name = "BUER_GEBURTSDATUM")
    @Temporal(TemporalType.DATE)
    private Date geburtsdatum;

    @OneToMany(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER)
    private Set<Sachbearbeiter> sachbearbeiter;
    
    @Transient
    private Set<Staatsangehoerigkeit> staatsangehoerigkeiten= new HashSet<>();
    
    @OneToMany(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER)
    private Set<StaatsangehoerigkeitReference> staatsangehoerigkeitReferences;
    
    @OneToMany(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER)
    private Set<Pass> Pass = new HashSet<>();

    @OneToMany(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER)
    private Set<Buerger> kinder = new HashSet<>();
    
    @OneToMany(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER)
    private Set<Wohnung> wohnungen= new HashSet<>();

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

    public Set<Wohnung> getWohnungen() {
        return wohnungen;
    }

    public void setWohnungen(Set<Wohnung> wohnungen) {
        this.wohnungen = wohnungen;
    }

    public Set<Sachbearbeiter> getSachbearbeiter() {
        return sachbearbeiter;
    }

    public void setSachbearbeiter(Set<Sachbearbeiter> sachbearbeiter) {
        this.sachbearbeiter = sachbearbeiter;
    }

    public Set<Staatsangehoerigkeit> getStaatsangehoerigkeiten() {
        return staatsangehoerigkeiten;
    }

    public void setStaatsangehoerigkeiten(Set<Staatsangehoerigkeit> staatsangehoerigkeiten) {
        this.staatsangehoerigkeiten = staatsangehoerigkeiten;
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

    public Set<Pass> getPass() {
        return Pass;
    }

    public void setPass(Set<Pass> Pass) {
        this.Pass = Pass;
    }

    @Override
    public String toString() {
        return String.format("id > %s | oid > %s | vorname > %s | nachname > %s | geburtsdatum > %s", this.getId(), this.getOid(), this.vorname, this.nachname, this.geburtsdatum);
    }

}
