package de.muenchen.kvr.buergerverwaltung.buerger.service.gen.domain;

import javax.validation.constraints.NotNull;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;	
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.Field;
import de.muenchen.service.BaseEntity;
import de.muenchen.service.PetersPerfectBridge;
import de.muenchen.auditing.MUCAudited;

/*
 * This file will be overwritten on every change of the model!
 * This file was automatically generated by GAIA.
 */
/**
 * This class represents a Pass_.
 * <p>
 * Only oid and reference will be stored in the database.
 * The entity's content will be loaded according to the reference variable.
 * </p>
 */
@Entity
@Indexed
@Table(name = "Pass")
@MUCAudited({MUCAudited.CREATE, MUCAudited.DELETE, MUCAudited.UPDATE})
public class Pass_ extends BaseEntity {
	
	// ========= //
	// Variables //
	// ========= //
	
	@Column(name="passNummer")
	@Field
	@FieldBridge(impl = PetersPerfectBridge.class)
	@NotNull
	private long passNummer;
	
	
	@Column(name="typ")
	@Field
	@FieldBridge(impl = PetersPerfectBridge.class)
	@Enumerated(EnumType.STRING)
	@NotNull
	private Passtyp_ typ;
	
	
	@Column(name="kode")
	@Field
	@FieldBridge(impl = PetersPerfectBridge.class)
	@NotNull
	private String kode;
	
	
	@Column(name="groesse")
	@Field
	@FieldBridge(impl = PetersPerfectBridge.class)
	@NotNull
	private long groesse;
	
	
	@Column(name="behoerde")
	@Field
	@FieldBridge(impl = PetersPerfectBridge.class)
	@NotNull
	private String behoerde;
	
	
	/**
	 * Default Constructor for Pass.
	 */
	public Pass_() {}
	
	// =================== //
	// Getters and Setters //
	// =================== //
	public long getPassNummer(){
		return passNummer;
	}
	
	public void setPassNummer(long passNummer){
		this.passNummer = passNummer;
	}
	
	
	public Passtyp_ getTyp(){
		return typ;
	}
	
	public void setTyp(Passtyp_ typ){
		this.typ = typ;
	}
	
	
	public String getKode(){
		return kode;
	}
	
	public void setKode(String kode){
		this.kode = kode;
	}
	
	
	public long getGroesse(){
		return groesse;
	}
	
	public void setGroesse(long groesse){
		this.groesse = groesse;
	}
	
	
	public String getBehoerde(){
		return behoerde;
	}
	
	public void setBehoerde(String behoerde){
		this.behoerde = behoerde;
	}
	
	
	/**
	 * Returns a String representation for this Pass.
	 * The form is: 
	 * <EntityName>
	 * <attribute1_Type> <attribute1_name>: <attribute1_value>
	 * <attribute2_Type> <attribute2_name>: <attribute2_value>
	 * ...
	 */
	@Override
	public String toString(){
		String s = "Pass";
		s += "\nlong passNummer: " + getPassNummer();
		s += "\nPasstyp_ typ: " + getTyp();
		s += "\nString kode: " + getKode();
		s += "\nlong groesse: " + getGroesse();
		s += "\nString behoerde: " + getBehoerde();
		return s;
	}
}