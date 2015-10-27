package de.muenchen.demo.service.gen.domain;

import de.muenchen.service.BaseEntity;
import org.hibernate.search.annotations.Indexed;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;


/*
 * This file will be overwritten on every change of the model!
 * This file was automatically generated by GAIA.
 */

/**
 * This class represents a Adresse.
 * <p>
 * Only oid and reference will be stored in the database.
 * The entity's content will be loaded according to the reference variable.
 * </p>
 */
@Entity
@Indexed
@Table(name = "ADRESSE")
public class Adresse extends BaseEntity {

    // ========= //
    // Variables //
    // ========= //

    @JoinTable(name = "adresses_interneadresses", joinColumns = {@JoinColumn(name = "adresse_oid")}, inverseJoinColumns = {@JoinColumn(name = "interneadresse_oid")})
    @OneToOne(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER)
    @NotNull
    private AdresseIntern interneAdresse;


    @JoinTable(name = "adresses_externeadresses", joinColumns = {@JoinColumn(name = "adresse_oid")}, inverseJoinColumns = {@JoinColumn(name = "externeadresse_oid")})
    @OneToOne(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER)
    @NotNull
    private AdresseExtern externeAdresse;


    /**
     * Default Constructor for Adresse.
     */
    public Adresse() {
    }

    /**
     * Copy Constructor for Adresse.
     */
    public Adresse(Adresse adresse) {
        this.setInterneAdresse(interneAdresse);
        this.setExterneAdresse(externeAdresse);
    }

    // =================== //
    // Getters and Setters //
    // =================== //
    public AdresseIntern getInterneAdresse() {
        return interneAdresse;
    }

    public void setInterneAdresse(AdresseIntern interneAdresse) {
        this.interneAdresse = interneAdresse;
    }


    public AdresseExtern getExterneAdresse() {
        return externeAdresse;
    }

    public void setExterneAdresse(AdresseExtern externeAdresse) {
        this.externeAdresse = externeAdresse;
    }


    /**
     * Returns a String representation for this Adresse.
     * The form is:
     * <EntityName>
     * <attribute1_Type> <attribute1_name>: <attribute1_value>
     * <attribute2_Type> <attribute2_name>: <attribute2_value>
     * ...
     */
    @Override
    public String toString() {
        String s = "Adresse";
        s += "\nAdresseIntern interneAdresse: " + getInterneAdresse();
        s += "\nAdresseExtern externeAdresse: " + getExterneAdresse();
        return s;
    }
}