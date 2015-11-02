package de.muenchen.demo.service.gen.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import de.muenchen.auditing.MUCAudited;
import de.muenchen.service.BaseEntity;
import de.muenchen.service.PetersPerfectBridge;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.Indexed;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


/*
 * This file will be overwritten on every change of the model!
 * This file was automatically generated by GAIA.
 */

/**
 * This class represents a Buerger.
 * <p>
 * Only oid and reference will be stored in the database.
 * The entity's content will be loaded according to the reference variable.
 * </p>
 */
@Entity
@Indexed
@Table(name = "BUERGER")
@MUCAudited({MUCAudited.CREATE, MUCAudited.DELETE})
public class Buerger extends BaseEntity {

    // ========= //
    // Variables //
    // ========= //

    @Column(name = "vorname")
    @Field
    @FieldBridge(impl = PetersPerfectBridge.class)
    @NotNull
    @Size(min = 2, max = 50)
    private String vorname;


    @Column(name = "nachname")
    @Field
    @FieldBridge(impl = PetersPerfectBridge.class)
    @NotNull
    @Size(min = 2, max = 50)
    private String nachname;


    @Column(name = "geburtsdatum")
    @Field
    @FieldBridge(impl = PetersPerfectBridge.class)
    @Temporal(TemporalType.DATE)
    @NotNull
    @Past
    private java.util.Date geburtsdatum;


    @Column(name = "augenfarbe")
    @Field
    @FieldBridge(impl = PetersPerfectBridge.class)
    @Enumerated(EnumType.STRING)
    //TODO @NotNull
    private Augenfarben augenfarbe;

    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "oid")
    @JsonIdentityReference(alwaysAsId = true)
    @JoinTable(name = "buergers_wohnungens", joinColumns = {@JoinColumn(name = "buerger_oid")}, inverseJoinColumns = {@JoinColumn(name = "wohnungen_oid")})
    @OneToOne(cascade = {CascadeType.REFRESH}, fetch = FetchType.LAZY)
    //TODO @NotNull
    private Wohnung wohnungen;

    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "oid")
    @JsonIdentityReference(alwaysAsId = true)
    @JoinTable(name = "buergers_staatsangehoerigkeitens", joinColumns = {@JoinColumn(name = "buerger_oid")}, inverseJoinColumns = {@JoinColumn(name = "staatsangehoerigkeiten_oid")})
    @ManyToMany
    @NotNull
    private java.util.Collection<Staatsangehoerigkeit> staatsangehoerigkeiten = new ArrayList<>();


    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "oid")
    @JsonIdentityReference(alwaysAsId = true)
    @JoinTable(name = "buergers_kinders", joinColumns = {@JoinColumn(name = "buerger_oid")}, inverseJoinColumns = {@JoinColumn(name = "kinder_oid")})
    @OneToMany(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER)
    private java.util.Collection<Buerger> kinder = new ArrayList<>();


    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "oid")
    @JsonIdentityReference(alwaysAsId = true)
    @JoinTable(name = "buergers_partners", joinColumns = {@JoinColumn(name = "buerger_oid")}, inverseJoinColumns = {@JoinColumn(name = "partner_oid")})
    @OneToOne(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER)
    private Buerger partner;

    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "oid")
    @JsonIdentityReference(alwaysAsId = true)
    @JoinTable(name = "buergers_sachbearbeiters", joinColumns = {@JoinColumn(name = "buerger_oid")}, inverseJoinColumns = {@JoinColumn(name = "sachbearbeiter_oid")})
    @ManyToMany
    @NotNull
    private java.util.Collection<Sachbearbeiter> sachbearbeiter = new ArrayList<>();

    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "oid")
    @JsonIdentityReference(alwaysAsId = true)
    @JoinTable(name = "buergers_paesses", joinColumns = {@JoinColumn(name = "buerger_oid")}, inverseJoinColumns = {@JoinColumn(name = "paesse_oid")})
    @OneToMany(cascade = {CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @NotNull
    private java.util.Collection<Pass> paesse = new ArrayList<>();

    @Column(name = "alive")
    @NotNull
    private Boolean alive;

    @Field
    @FieldBridge(impl = PetersPerfectBridge.class)
    @Column(name = "BUER_EIGENSCHAFTEN")
    @ElementCollection
    @NotNull
    @Size(min = 1, max = 5)
    /** TODO not generated */
    private Set<String> eigenschaften = new HashSet<>();

    /**
     * Default Constructor for Buerger.
     */
    public Buerger() {
    }

    /**
     * Copy Constructor for Buerger.
     */
    public Buerger(Buerger buerger) {
        this.setVorname(vorname);
        this.setNachname(nachname);
        this.setGeburtsdatum(geburtsdatum);
        this.setAugenfarbe(augenfarbe);
        this.setWohnungen(wohnungen);
        this.setStaatsangehoerigkeiten(staatsangehoerigkeiten);
        this.setKinder(kinder);
        this.setPartner(partner);
        this.setSachbearbeiter(sachbearbeiter);
        this.setPaesse(paesse);
        this.setAlive(alive);
    }

    // =================== //
    // Getters and Setters //
    // =================== //
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


    public java.util.Date getGeburtsdatum() {
        return geburtsdatum;
    }

    public void setGeburtsdatum(java.util.Date geburtsdatum) {
        this.geburtsdatum = geburtsdatum;
    }


    public Augenfarben getAugenfarbe() {
        return augenfarbe;
    }

    public void setAugenfarbe(Augenfarben augenfarbe) {
        this.augenfarbe = augenfarbe;
    }


    public Wohnung getWohnungen() {
        return wohnungen;
    }

    public void setWohnungen(Wohnung wohnungen) {
        this.wohnungen = wohnungen;
    }


    public java.util.Collection<Staatsangehoerigkeit> getStaatsangehoerigkeiten() {
        return staatsangehoerigkeiten;
    }

    public void setStaatsangehoerigkeiten(java.util.Collection<Staatsangehoerigkeit> staatsangehoerigkeiten) {
        this.staatsangehoerigkeiten = staatsangehoerigkeiten;
    }


    public java.util.Collection<Buerger> getKinder() {
        return kinder;
    }

    public void setKinder(java.util.Collection<Buerger> kinder) {
        this.kinder = kinder;
    }


    public Buerger getPartner() {
        return partner;
    }

    public void setPartner(Buerger partner) {
        this.partner = partner;
    }


    public java.util.Collection<Sachbearbeiter> getSachbearbeiter() {
        return sachbearbeiter;
    }

    public void setSachbearbeiter(java.util.Collection<Sachbearbeiter> sachbearbeiter) {
        this.sachbearbeiter = sachbearbeiter;
    }


    public java.util.Collection<Pass> getPaesse() {
        return paesse;
    }

    public void setPaesse(java.util.Collection<Pass> paesse) {
        this.paesse = paesse;
    }


    public Boolean getAlive() {
        return alive;
    }

    public void setAlive(Boolean alive) {
        this.alive = alive;
    }


    public Set<String> getEigenschaften() {
        return eigenschaften;
    }

    public void setEigenschaften(Set<String> eigenschaften) {
        this.eigenschaften = eigenschaften;
    }


    /**
     * Returns a String representation for this Buerger.
     * The form is:
     * <EntityName>
     * <attribute1_Type> <attribute1_name>: <attribute1_value>
     * <attribute2_Type> <attribute2_name>: <attribute2_value>
     * ...
     */
    @Override
    public String toString() {
        String s = "Buerger";
        s += "\nString vorname: " + getVorname();
        s += "\nString nachname: " + getNachname();
        s += "\njava.util.Date geburtsdatum: " + getGeburtsdatum();
        s += "\nAugenfarben augenfarbe: " + getAugenfarbe();
        s += "\nWohnung wohnungen: " + getWohnungen();
        s += "\njava.util.Collection<Staatsangehoerigkeit> staatsangehoerigkeiten: " + getStaatsangehoerigkeiten();
        s += "\njava.util.Collection<Buerger> kinder: " + getKinder();
        s += "\nBuerger partner: " + getPartner();
        s += "\njava.util.Collection<Sachbearbeiter> sachbearbeiter: " + getSachbearbeiter();
        s += "\njava.util.Collection<Pass> paesse: " + getPaesse();
        s += "\nBoolean alive: " + getAlive();
        return s;
    }
}
