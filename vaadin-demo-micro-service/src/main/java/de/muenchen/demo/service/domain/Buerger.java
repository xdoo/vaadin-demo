package de.muenchen.demo.service.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import de.muenchen.auditing.MUCAudited;
import de.muenchen.demo.service.util.PetersPerfectBridge;
import de.muenchen.vaadin.demo.api.domain.Augenfarbe;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @author claus.straube
 */
@Entity
@Indexed
@MUCAudited
@Table(name = "BUERGER")
public class Buerger extends BaseEntity {

    @Field
    @IndexedEmbedded
    @Column(nullable = true, name = "BUER_VORNAME")
    @Size(max = 70)
    private String vorname;

    @Field
    @IndexedEmbedded
    @Column(name = "BUER_NACHNAME")
    @NotNull
    @Size(max = 70)
    private String nachname;

    @Field
    @IndexedEmbedded
    @Column(name = "BUER_GEBURTSDATUM")
    @FieldBridge(impl = PetersPerfectBridge.class)
    @Temporal(TemporalType.DATE)
    private Date geburtsdatum;

    @Field
    @IndexedEmbedded
    @FieldBridge(impl = PetersPerfectBridge.class)
    @Column(nullable = false, name = "BUER_AUGENFARBE")
    @Enumerated(EnumType.STRING)
    private Augenfarbe augenfarbe;

    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "oid")
    @JsonIdentityReference(alwaysAsId = true)
    @OneToMany(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER)
    private Set<Sachbearbeiter> sachbearbeiter = new HashSet<>();

    @Transient
    private Set<Staatsangehoerigkeit> staatsangehoerigkeiten = new HashSet<>();

    @OneToMany(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER)
    private Set<StaatsangehoerigkeitReference> staatsangehoerigkeitReferences = new HashSet<>();

    @OneToMany(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER)
    private Set<Pass> pass = new HashSet<>();

    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "oid")
    @JsonIdentityReference(alwaysAsId = true)
    @OneToMany(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER)
    private Set<Buerger> kinder = new HashSet<>();

    @OneToMany(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER)
    private Set<Wohnung> wohnungen = new HashSet<>();

    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "oid")
    @JsonIdentityReference(alwaysAsId = true)
    @OneToOne(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER)
    private Buerger partner;

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
        this.partner = buerger.partner;
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

    public Buerger getPartner() {
        return partner;
    }

    public void setPartner(Buerger partner) {
        this.partner = partner;
    }

    public Augenfarbe getAugenfarbe() {
        return augenfarbe;
    }

    public void setAugenfarbe(Augenfarbe augenfarbe) {
        this.augenfarbe = augenfarbe;
    }

    @Override
    public String toString() {
        return String.format("oid > %s | vorname > %s | nachname > %s | geburtsdatum > %s | augenfarbe > %s",
                this.getOid(), this.vorname, this.nachname, this.geburtsdatum, this.augenfarbe);
    }

}

