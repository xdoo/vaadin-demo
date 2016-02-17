package de.muenchen.kvr.buergerverwaltung.buerger.service.gen.domain;

import javax.validation.constraints.NotNull;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;	
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Embedded;
import javax.persistence.OneToOne;
import javax.persistence.CascadeType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import org.hibernate.search.annotations.Indexed;
import de.muenchen.service.BaseEntity;
import de.muenchen.auditing.MUCAudited;

/*
 * This file will be overwritten on every change of the model!
 * This file was automatically generated by GAIA.
 */
/**
 * This class represents a Adresse_.
 * <p>
 * Only oid and reference will be stored in the database.
 * The entity's content will be loaded according to the reference variable.
 * </p>
 */
@Entity
@Indexed
@Table(name = "Adresse")
@MUCAudited({MUCAudited.CREATE, MUCAudited.DELETE, MUCAudited.UPDATE})
public class Adresse_ extends BaseEntity {
	
	// ========= //
	// Variables //
	// ========= //
	
	@Embedded
	@AttributeOverrides({
		@AttributeOverride(name="strassenSchluessel", column=@Column(name="interneadresse_strassenschluessel")),
		@AttributeOverride(name="hausnummer", column=@Column(name="interneadresse_hausnummer"))
	})	
	@NotNull
	private AdresseIntern_ interneAdresse;
	
	
	@Embedded
	@AttributeOverrides({
		@AttributeOverride(name="strasse", column=@Column(name="externeadresse_strasse")),
		@AttributeOverride(name="hausnummer", column=@Column(name="externeadresse_hausnummer")),
		@AttributeOverride(name="plz", column=@Column(name="externeadresse_plz")),
		@AttributeOverride(name="ort", column=@Column(name="externeadresse_ort"))
	})	
	@NotNull
	private AdresseExtern_ externeAdresse;
	
	
	/**
	 * Default Constructor for Adresse.
	 */
	public Adresse_() {}
	
	// =================== //
	// Getters and Setters //
	// =================== //
	public AdresseIntern_ getInterneAdresse(){
		return interneAdresse;
	}
	
	public void setInterneAdresse(AdresseIntern_ interneAdresse){
		this.interneAdresse = interneAdresse;
	}
	
	
	public AdresseExtern_ getExterneAdresse(){
		return externeAdresse;
	}
	
	public void setExterneAdresse(AdresseExtern_ externeAdresse){
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
	public String toString(){
		String s = "Adresse";
		s += "\nAdresseIntern_ interneAdresse: " + getInterneAdresse();
		s += "\nAdresseExtern_ externeAdresse: " + getExterneAdresse();
		return s;
	}
}
