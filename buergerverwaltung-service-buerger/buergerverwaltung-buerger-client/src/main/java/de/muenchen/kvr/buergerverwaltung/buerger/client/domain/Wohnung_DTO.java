package de.muenchen.kvr.buergerverwaltung.buerger.client.domain;

import de.muenchen.vaadin.demo.apilib.domain.BaseEntity;

/*
 * This file will be overwritten on every change of the model!
 * This file was automatically generated by GAIA.
 */
public class Wohnung_DTO extends BaseEntity {
	
	private String stock;
	
	private String ausrichtung;
	
	private Adresse_DTO adresse;
	
	// Getters and Setters
	public String getStock(){
		return stock;
	}
	public void setStock(String stock){
		this.stock = stock;
	}
	
	public String getAusrichtung(){
		return ausrichtung;
	}
	public void setAusrichtung(String ausrichtung){
		this.ausrichtung = ausrichtung;
	}
	
	public Adresse_DTO getAdresse(){
		return adresse;
	}
	public void setAdresse(Adresse_DTO value){
		this.adresse = value;
	}
	
	@Override
	public String toString() {
	   	return String.format("%s = {\"stock\": \"%s\", \"ausrichtung\": \"%s\"}", getClass(),
	   		this.stock,
	   		this.ausrichtung);
	}
}
