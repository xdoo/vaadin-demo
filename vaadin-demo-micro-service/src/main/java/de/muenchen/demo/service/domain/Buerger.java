package de.muenchen.demo.service.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import de.muenchen.demo.service.util.MUCAudited;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author claus.straube
 */
@Entity
@Indexed
@MUCAudited
@Table(name = "BUERGER")
public class Buerger extends BaseEntity {

    @Field
    @Column(length = 70, nullable = true, name = "BUER_VORNAME")
    private String vorname;

    @Field
    @Column(length = 70, nullable = false, name = "BUER_NACHNAME")
    private String nachname;

//    @Field(index = Index.YES, store = Store.YES)
//    @DateBridge(resolution = Resolution.DAY, encoding = EncodingType.STRING)
    @Column(name = "BUER_GEBURTSDATUM")
    @Temporal(TemporalType.DATE)
    private Date geburtsdatum;

    @JsonManagedReference
    @JsonBackReference
    @OneToMany (cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER)
    private Set<Sachbearbeiter> sachbearbeiter= new HashSet<>();

    @Transient
    private Set<Staatsangehoerigkeit> staatsangehoerigkeiten = new HashSet<>();

    @OneToMany(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER)
    private Set<StaatsangehoerigkeitReference> staatsangehoerigkeitReferences = new HashSet<>();

    @OneToMany(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER)
    private Set<Pass> pass = new HashSet<>();

    @JsonManagedReference
    @OneToMany(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER)
    private Set<Buerger> kinder = new HashSet<>();

    @OneToMany(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER)
    private Set<Wohnung> wohnungen = new HashSet<>();

    @JsonManagedReference
    @JsonBackReference
    @OneToMany(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER)
    private Set<Buerger> partner = new HashSet<>();

    public Buerger() {
    }

    public Buerger(Buerger buerger) {
        this.vorname = buerger.vorname;
        this.nachname = buerger.nachname;
        this.geburtsdatum = buerger.geburtsdatum;
        this.staatsangehoerigkeitReferences.addAll(buerger.staatsangehoerigkeitReferences);
        // this.pass.addAll(buerger.pass);
        this.wohnungen.addAll(buerger.wohnungen);
        this.staatsangehoerigkeiten.addAll(buerger.staatsangehoerigkeiten);
        this.kinder.addAll(buerger.kinder);
        this.partner.addAll(buerger.partner);
    }

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
        return pass;
    }

    public void setPass(Set<Pass> Pass) {
        this.pass = Pass;
    }

    public Set<Buerger> getBeziehungsPartner() {
        return this.partner;
    }

    public void setBeziehungsPartner(Buerger partner) {
        this.partner.add(partner);
    }

    @Override
    public String toString() {
        return String.format("id > %s | oid > %s | vorname > %s | nachname > %s | geburtsdatum > %s", this.getId(), this.getOid(), this.vorname, this.nachname, this.geburtsdatum);
    }

}
