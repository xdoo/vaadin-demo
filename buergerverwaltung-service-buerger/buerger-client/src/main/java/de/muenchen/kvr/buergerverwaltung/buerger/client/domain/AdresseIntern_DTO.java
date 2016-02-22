package de.muenchen.kvr.buergerverwaltung.buerger.client.domain;

import de.muenchen.vaadin.demo.apilib.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/*
 * This file will be overwritten on every change of the model!
 * This file was automatically generated by GAIA.
 */
public class AdresseIntern_DTO {
	
	private long strassenSchluessel;
	
	private long hausnummer;
	
	// Getters and Setters
	public long getStrassenSchluessel(){
		return strassenSchluessel;
	}
	public void setStrassenSchluessel(long strassenSchluessel){
		this.strassenSchluessel = strassenSchluessel;
	}
	
	public long getHausnummer(){
		return hausnummer;
	}
	public void setHausnummer(long hausnummer){
		this.hausnummer = hausnummer;
	}
	
	@Override
	public String toString() {
	   	return String.format("%s = {\"strassenSchluessel\": \"%s\", \"hausnummer\": \"%s\"}", getClass(),
	   		this.strassenSchluessel,
	   		this.hausnummer);
	}
}
