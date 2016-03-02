package de.muenchen.kvr.buergerverwaltung.buerger.service.gen.domain;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Min;
import javax.validation.constraints.Max;
import javax.validation.constraints.Size;
import javax.validation.constraints.Pattern;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;	
import java.math.BigDecimal;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ElementCollection;
import javax.persistence.OrderColumn;
import javax.persistence.CollectionTable;
import javax.persistence.JoinColumn;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Embedded;
import javax.persistence.ManyToMany;
import javax.persistence.JoinTable;
import javax.persistence.OneToOne;
import javax.persistence.CascadeType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import org.hibernate.search.annotations.Indexed;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.Field;
import de.muenchen.service.BaseEntity;
import de.muenchen.service.PetersPerfectBridge;
import de.muenchen.vaadin.demo.apilib.domain.Past;
import de.muenchen.auditing.MUCAudited;

/*
 * This file will be overwritten on every change of the model!
 * This file was automatically generated by GAIA.
 */
/**
 * This class represents a Buerger_.
 * <p>
 * Only oid and reference will be stored in the database.
 * The entity's content will be loaded according to the reference variable.
 * </p>
 */
@Entity
@Indexed
@Table(name = "Buerger")
@MUCAudited({MUCAudited.CREATE, MUCAudited.DELETE, MUCAudited.UPDATE})
public class Buerger_ extends BaseEntity {
	
	// ========= //
	// Variables //
	// ========= //
	
	@Column(name="vorname")
	@Field
	@FieldBridge(impl = PetersPerfectBridge.class)
	@NotNull
	private String vorname;
	
	
	@Column(name="nachname")
	@Field
	@FieldBridge(impl = PetersPerfectBridge.class)
	@NotNull
	private String nachname;
	
	
	@Column(name="geburtstag")
	@Field
	@FieldBridge(impl = PetersPerfectBridge.class)
	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonSerialize(using = LocalDateSerializer.class)
	@NotNull
	@Past
	private java.time.LocalDate geburtstag;
	
	
	@Column(name="augenfarbe")
	@Field
	@FieldBridge(impl = PetersPerfectBridge.class)
	@Enumerated(EnumType.STRING)
	@NotNull
	private Augenfarben_ augenfarbe;
	
	
	@Column(name="telefonnummer")
	@Field
	@FieldBridge(impl = PetersPerfectBridge.class)
	@NotNull
	@Min((long)10000.0)
	@Max((long)9.99999999E8)
	private long telefonnummer;
	
	
	@Column(name="email")
	@Field
	@FieldBridge(impl = PetersPerfectBridge.class)
	@NotNull
	@Pattern(regexp="([a-zA-Z0-9]+.[a-zA-Z0-9]+@[a-zA-Z0-9]+.[a-zA-Z0-9]+)")
	@Size(min=3, max=255)
	private String email;
	
	
	@Column(name="lebendig")
	@Field
	@FieldBridge(impl = PetersPerfectBridge.class)
	@NotNull
	private boolean lebendig;
	
	
	@Column(name="eigenschaften")
	@OrderColumn(name="order_index")
	@CollectionTable(name = "Buerger_Eigenschaften", joinColumns = { @JoinColumn(name = "buerger_oid")})
	@ElementCollection
	private java.util.List<String> eigenschaften = new java.util.ArrayList<>();
	
	
	@OrderColumn(name="order_index")
	@JoinTable(name = "Buerger_BisherigeWohnsitze", joinColumns = { @JoinColumn(name = "buerger_oid")})
	@ElementCollection
	@Embedded
	@AttributeOverrides({
		@AttributeOverride(name="strasse", column=@Column(name="bisherigewohnsitze_strasse")),
		@AttributeOverride(name="hausnummer", column=@Column(name="bisherigewohnsitze_hausnummer")),
		@AttributeOverride(name="plz", column=@Column(name="bisherigewohnsitze_plz")),
		@AttributeOverride(name="ort", column=@Column(name="bisherigewohnsitze_ort"))
	})	
	@NotNull
	@Size(min = 1)
	private java.util.List<Adresse_> bisherigeWohnsitze = new java.util.ArrayList<>();
	
	
	@OrderColumn(name="order_index")
	@JoinTable(name = "Buerger_Kinder", joinColumns = { @JoinColumn(name = "buerger_oid")}, inverseJoinColumns = {@JoinColumn(name="kinder_oid")})
	@ManyToMany
	private java.util.List<Buerger_> kinder = new java.util.ArrayList<>();
	
	
	@JoinTable(name = "Buerger_Partner", joinColumns = { @JoinColumn(name = "buerger_oid")}, inverseJoinColumns = {@JoinColumn(name="partner_oid")})
	@OneToOne(cascade = {CascadeType.REFRESH})
	private Buerger_ partner;
	
	
	@JoinTable(name = "Buerger_Hauptwohnung", joinColumns = { @JoinColumn(name = "buerger_oid")}, inverseJoinColumns = {@JoinColumn(name="hauptwohnung_oid")})
	@ManyToOne
	private Wohnung_ hauptwohnung;
	
	
	@Column(name="staatsangehoerigkeiten")
	@OrderColumn(name="order_index")
	@CollectionTable(name = "Buerger_Staatsangehoerigkeiten", joinColumns = { @JoinColumn(name = "buerger_oid")})
	@ElementCollection
	@Enumerated(EnumType.STRING)
	private java.util.List<MoeglicheStaatsangehoerigkeiten_> staatsangehoerigkeiten = new java.util.ArrayList<>();
	
	
	@OrderColumn(name="order_index")
	@JoinTable(name = "Buerger_Sachbearbeiter", joinColumns = { @JoinColumn(name = "buerger_oid")}, inverseJoinColumns = {@JoinColumn(name="sachbearbeiter_oid")})
	@ManyToMany
	private java.util.List<Sachbearbeiter_> sachbearbeiter = new java.util.ArrayList<>();
	
	
	@OrderColumn(name="order_index")
	@JoinTable(name = "Buerger_Pass", joinColumns = { @JoinColumn(name = "buerger_oid")}, inverseJoinColumns = {@JoinColumn(name="pass_oid")})
	@OneToMany(cascade = {CascadeType.REFRESH})
	private java.util.List<Pass_> pass = new java.util.ArrayList<>();
	
	
	/**
	 * Default Constructor for Buerger.
	 */
	public Buerger_() {}
	
	// =================== //
	// Getters and Setters //
	// =================== //
	public String getVorname(){
		return vorname;
	}
	
	public void setVorname(String vorname){
		this.vorname = vorname;
	}
	
	
	public String getNachname(){
		return nachname;
	}
	
	public void setNachname(String nachname){
		this.nachname = nachname;
	}
	
	
	public java.time.LocalDate getGeburtstag(){
		return geburtstag;
	}
	
	public void setGeburtstag(java.time.LocalDate geburtstag){
		this.geburtstag = geburtstag;
	}
	
	
	public Augenfarben_ getAugenfarbe(){
		return augenfarbe;
	}
	
	public void setAugenfarbe(Augenfarben_ augenfarbe){
		this.augenfarbe = augenfarbe;
	}
	
	
	public long getTelefonnummer(){
		return telefonnummer;
	}
	
	public void setTelefonnummer(long telefonnummer){
		this.telefonnummer = telefonnummer;
	}
	
	
	public String getEmail(){
		return email;
	}
	
	public void setEmail(String email){
		this.email = email;
	}
	
	
	public boolean isLebendig(){
		return lebendig;
	}
	
	public void setLebendig(boolean lebendig){
		this.lebendig = lebendig;
	}
	
	
	public java.util.List<String> getEigenschaften(){
		return eigenschaften;
	}
	
	public void setEigenschaften(java.util.List<String> eigenschaften){
		this.eigenschaften = eigenschaften;
	}
	
	
	public java.util.List<Adresse_> getBisherigeWohnsitze(){
		return bisherigeWohnsitze;
	}
	
	public void setBisherigeWohnsitze(java.util.List<Adresse_> bisherigeWohnsitze){
		this.bisherigeWohnsitze = bisherigeWohnsitze;
	}
	
	
	public java.util.List<Buerger_> getKinder(){
		return kinder;
	}
	
	public void setKinder(java.util.List<Buerger_> kinder){
		this.kinder = kinder;
	}
	
	
	public Buerger_ getPartner(){
		return partner;
	}
	
	public void setPartner(Buerger_ partner){
		this.partner = partner;
	}
	
	
	public Wohnung_ getHauptwohnung(){
		return hauptwohnung;
	}
	
	public void setHauptwohnung(Wohnung_ hauptwohnung){
		this.hauptwohnung = hauptwohnung;
	}
	
	
	public java.util.List<MoeglicheStaatsangehoerigkeiten_> getStaatsangehoerigkeiten(){
		return staatsangehoerigkeiten;
	}
	
	public void setStaatsangehoerigkeiten(java.util.List<MoeglicheStaatsangehoerigkeiten_> staatsangehoerigkeiten){
		this.staatsangehoerigkeiten = staatsangehoerigkeiten;
	}
	
	
	public java.util.List<Sachbearbeiter_> getSachbearbeiter(){
		return sachbearbeiter;
	}
	
	public void setSachbearbeiter(java.util.List<Sachbearbeiter_> sachbearbeiter){
		this.sachbearbeiter = sachbearbeiter;
	}
	
	
	public java.util.List<Pass_> getPass(){
		return pass;
	}
	
	public void setPass(java.util.List<Pass_> pass){
		this.pass = pass;
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
	public String toString(){
		String s = "Buerger";
		s += "\nString vorname: " + getVorname();
		s += "\nString nachname: " + getNachname();
		s += "\njava.time.LocalDate geburtstag: " + getGeburtstag();
		s += "\nAugenfarben_ augenfarbe: " + getAugenfarbe();
		s += "\nlong telefonnummer: " + getTelefonnummer();
		s += "\nString email: " + getEmail();
		s += "\nboolean lebendig: " + isLebendig();
		s += "\njava.util.List<String> eigenschaften: " + getEigenschaften();
		s += "\njava.util.List<Adresse_> bisherigeWohnsitze: " + getBisherigeWohnsitze();
		s += "\njava.util.List<Buerger_> kinder: " + getKinder();
		s += "\nBuerger_ partner: " + getPartner();
		s += "\nWohnung_ hauptwohnung: " + getHauptwohnung();
		s += "\njava.util.List<MoeglicheStaatsangehoerigkeiten_> staatsangehoerigkeiten: " + getStaatsangehoerigkeiten();
		s += "\njava.util.List<Sachbearbeiter_> sachbearbeiter: " + getSachbearbeiter();
		s += "\njava.util.List<Pass_> pass: " + getPass();
		return s;
	}
}
