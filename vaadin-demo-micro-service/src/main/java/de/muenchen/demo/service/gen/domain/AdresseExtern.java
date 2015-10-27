package de.muenchen.demo.service.gen.domain;

import de.muenchen.service.BaseEntity;
import de.muenchen.service.PetersPerfectBridge;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.Indexed;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;


/*
 * This file will be overwritten on every change of the model!
 * This file was automatically generated by GAIA.
 */

/**
 * This class represents a AdresseExtern.
 * <p>
 * Only oid and reference will be stored in the database.
 * The entity's content will be loaded according to the reference variable.
 * </p>
 */
@Entity
@Indexed
@Table(name = "ADRESSEEXTERN")
public class AdresseExtern extends BaseEntity {

    // ========= //
    // Variables //
    // ========= //

    @Column(name = "strasse")
    @Field
    @FieldBridge(impl = PetersPerfectBridge.class)
    @NotNull
    private String strasse;


    @Column(name = "hausnummer")
    @Field
    @FieldBridge(impl = PetersPerfectBridge.class)
    @NotNull
    private Long hausnummer;


    @Column(name = "plz")
    @Field
    @FieldBridge(impl = PetersPerfectBridge.class)
    @NotNull
    private Long plz;


    @Column(name = "ort")
    @Field
    @FieldBridge(impl = PetersPerfectBridge.class)
    @NotNull
    private String ort;


    /**
     * Default Constructor for AdresseExtern.
     */
    public AdresseExtern() {
    }

    /**
     * Copy Constructor for AdresseExtern.
     */
    public AdresseExtern(AdresseExtern adresseExtern) {
        this.setStrasse(strasse);
        this.setHausnummer(hausnummer);
        this.setPlz(plz);
        this.setOrt(ort);
    }

    // =================== //
    // Getters and Setters //
    // =================== //
    public String getStrasse() {
        return strasse;
    }

    public void setStrasse(String strasse) {
        this.strasse = strasse;
    }


    public Long getHausnummer() {
        return hausnummer;
    }

    public void setHausnummer(Long hausnummer) {
        this.hausnummer = hausnummer;
    }


    public Long getPlz() {
        return plz;
    }

    public void setPlz(Long plz) {
        this.plz = plz;
    }


    public String getOrt() {
        return ort;
    }

    public void setOrt(String ort) {
        this.ort = ort;
    }


    /**
     * Returns a String representation for this AdresseExtern.
     * The form is:
     * <EntityName>
     * <attribute1_Type> <attribute1_name>: <attribute1_value>
     * <attribute2_Type> <attribute2_name>: <attribute2_value>
     * ...
     */
    @Override
    public String toString() {
        String s = "AdresseExtern";
        s += "\nString strasse: " + getStrasse();
        s += "\nLong hausnummer: " + getHausnummer();
        s += "\nLong plz: " + getPlz();
        s += "\nString ort: " + getOrt();
        return s;
    }
}