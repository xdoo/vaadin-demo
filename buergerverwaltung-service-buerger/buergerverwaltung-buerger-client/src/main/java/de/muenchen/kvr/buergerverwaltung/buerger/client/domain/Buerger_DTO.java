package de.muenchen.kvr.buergerverwaltung.buerger.client.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import de.muenchen.vaadin.demo.apilib.domain.BaseEntity;

import java.util.Set;

/*
 * This file will be overwritten on every change of the model!
 * This file was automatically generated by GAIA.
 */
public class Buerger_DTO extends BaseEntity {
	
	private String vorname;
	
	private String nachname;
	
	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonSerialize(using = LocalDateSerializer.class)
	private java.time.LocalDate geburtstag;
	
	private Augenfarben_ augenfarbe;
	
	private long telefonnummer;
	
	private String email;
	
	private boolean lebendig;
	
	private java.util.List<String> eigenschaften = new java.util.ArrayList<>();
	
	private java.util.List<MoeglicheStaatsangehoerigkeiten_> staatsangehoerigkeiten = new java.util.ArrayList<>();
	
	private Set<Adresse_DTO> bisherigeWohnsitze;
	
	private java.util.List<String> kinder;
	
	private String partner;
	
	private String hauptwohnung;
	
	private java.util.List<String> sachbearbeiter;
	
	private java.util.List<String> pass;
	
	// Getters and Setters
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
	
	public java.util.List<MoeglicheStaatsangehoerigkeiten_> getStaatsangehoerigkeiten(){
		return staatsangehoerigkeiten;
	}
	public void setStaatsangehoerigkeiten(java.util.List<MoeglicheStaatsangehoerigkeiten_> staatsangehoerigkeiten){
		this.staatsangehoerigkeiten = staatsangehoerigkeiten;
	}
	
	public Set<Adresse_DTO> getBisherigeWohnsitze(){
		return bisherigeWohnsitze;
	}
	public void setBisherigeWohnsitze(Set<Adresse_DTO> value){
		this.bisherigeWohnsitze = value;
	}
	
	public java.util.List<String> getKinder(){
		return kinder;
	}
	public void setKinder(java.util.List<String> value){
		this.kinder = value;
	}
	
	public String getPartner(){
		return partner;
	}
	public void setPartner(String value){
		this.partner = value;
	}
	
	public String getHauptwohnung(){
		return hauptwohnung;
	}
	public void setHauptwohnung(String value){
		this.hauptwohnung = value;
	}
	
	public java.util.List<String> getSachbearbeiter(){
		return sachbearbeiter;
	}
	public void setSachbearbeiter(java.util.List<String> value){
		this.sachbearbeiter = value;
	}
	
	public java.util.List<String> getPass(){
		return pass;
	}
	public void setPass(java.util.List<String> value){
		this.pass = value;
	}
	
	@Override
	public String toString() {
	   	return String.format("%s = {\"vorname\": \"%s\", \"nachname\": \"%s\", \"geburtstag\": \"%s\", \"augenfarbe\": \"%s\", \"telefonnummer\": \"%s\", \"email\": \"%s\", \"lebendig\": \"%s\", \"eigenschaften\": \"%s\", \"staatsangehoerigkeiten\": \"%s\"}", getClass(),
	   		this.vorname,
	   		this.nachname,
	   		this.geburtstag,
	   		this.augenfarbe,
	   		this.telefonnummer,
	   		this.email,
	   		this.lebendig,
	   		this.eigenschaften,
	   		this.staatsangehoerigkeiten);
	}
}
