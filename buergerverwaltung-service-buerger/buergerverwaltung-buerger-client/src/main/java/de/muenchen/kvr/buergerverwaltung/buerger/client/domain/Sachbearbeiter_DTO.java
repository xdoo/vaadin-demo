package de.muenchen.kvr.buergerverwaltung.buerger.client.domain;

import de.muenchen.vaadin.demo.apilib.domain.BaseEntity;

/*
 * This file will be overwritten on every change of the model!
 * This file was automatically generated by GAIA.
 */
public class Sachbearbeiter_DTO extends BaseEntity {
	
	private long telefon;
	
	private long fax;
	
	private String funktion;
	
	private String organisationseinheit;
	
	// Getters and Setters
	public long getTelefon(){
		return telefon;
	}
	public void setTelefon(long telefon){
		this.telefon = telefon;
	}
	
	public long getFax(){
		return fax;
	}
	public void setFax(long fax){
		this.fax = fax;
	}
	
	public String getFunktion(){
		return funktion;
	}
	public void setFunktion(String funktion){
		this.funktion = funktion;
	}
	
	public String getOrganisationseinheit(){
		return organisationseinheit;
	}
	public void setOrganisationseinheit(String organisationseinheit){
		this.organisationseinheit = organisationseinheit;
	}
	
	@Override
	public String toString() {
	   	return String.format("%s = {\"telefon\": \"%s\", \"fax\": \"%s\", \"funktion\": \"%s\", \"organisationseinheit\": \"%s\"}", getClass(),
	   		this.telefon,
	   		this.fax,
	   		this.funktion,
	   		this.organisationseinheit);
	}
}
