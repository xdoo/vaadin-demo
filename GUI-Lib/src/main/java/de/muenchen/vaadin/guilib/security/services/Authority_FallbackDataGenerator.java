package de.muenchen.vaadin.guilib.security.services;

import de.muenchen.vaadin.demo.apilib.local.Authority;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/*
 * This file will be overwritten on every change of the model!
 * This file was automatically generated by GAIA.
 */
public class Authority_FallbackDataGenerator{
	
	/** Fallback-Daten generierung. Diese Methoden werden durch Barrakuda mit dem gewünschten Verhalten befüllt. */
	
	
	/**
	 * Generiert Fallback-Daten für Methoden, die einen einzelnen Authority_ zurückliefern.
	 * 
	 * @return Generierter fallback-Wert
	 */
	public static Authority createAuthorityFallback(){
		return null;
	}
	
	/**
	 * Generiert Fallback-Daten für Methoden, die eine Liste von Authority_ zurückliefern.
	 * 
	 * @return Generierter fallback-Wert
	 */
	public static List<Authority> createAuthoritysFallback(){
		return new ArrayList<>();
	}
	
	/**
	 * Generiert Fallback-Daten für Methoden, die ein Optional von Authority_ zurückliefern.
	 * 
	 * @return Generierter fallback-Wert
	 */
	public static Optional<Authority> createOptionalAuthorityFallback(){
		return Optional.empty();
	}
	
}