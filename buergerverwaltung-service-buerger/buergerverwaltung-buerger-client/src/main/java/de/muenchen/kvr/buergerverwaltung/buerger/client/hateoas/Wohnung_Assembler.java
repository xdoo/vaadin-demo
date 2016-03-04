package de.muenchen.kvr.buergerverwaltung.buerger.client.hateoas;

import de.muenchen.kvr.buergerverwaltung.buerger.client.domain.Wohnung_DTO;
import de.muenchen.kvr.buergerverwaltung.buerger.client.local.Wohnung_;
import de.muenchen.kvr.buergerverwaltung.buerger.client.rest.Wohnung_Resource;
import org.springframework.hateoas.Resource;

import java.util.UUID;

/*
 * This file will be overwritten on every change of the model!
 * This file was automatically generated by GAIA.
 */
public class Wohnung_Assembler {


	/**
	 * Transform the Resource (from the REST Server) to the local object representation.
	 *
	 * @param resource the REST DTO Resource
	 * @return the local Object Representation
	 */
	public Wohnung_ toBean(Resource<Wohnung_DTO> resource) {
		Wohnung_DTO wohnungDTO = resource.getContent();
		Wohnung_ bean = new Wohnung_();		
		bean.setStock(wohnungDTO.getStock());
		if(wohnungDTO.getAusrichtung() != null)
			bean.setAusrichtung(wohnungDTO.getAusrichtung());
		bean.setAdresse(new Adresse_Assembler().toBean(wohnungDTO.getAdresse()));
		bean.add(resource.getLinks());
		
		return bean;
	}
	
	/**
	 * Transform the local object representation to the DTO resource.
	 *
	 * @param bean the local object representation
	 * @return the REST DTO Resource
	 */
	public Wohnung_Resource toResource(Wohnung_ bean) {
		return new Wohnung_Resource(toDTO(bean), bean.getLinks());
	}
	
	/**
	 * Transform the local object representation to the DTO.
	 *
	 * @param bean the local object representation
	 * @return the REST DTO
	 */
	public Wohnung_DTO toDTO(Wohnung_ bean) {
		Wohnung_DTO wohnungDTO = new Wohnung_DTO();
		
		try{
			String[] id = bean.getId().getHref().split("/");
			wohnungDTO.setOid(UUID.fromString(id[id.length-1]));
		} catch(Exception e) {/* No Link found */}
		
		wohnungDTO.setStock(bean.getStock());
		if(bean.getAusrichtung() != null)
			wohnungDTO.setAusrichtung(bean.getAusrichtung());
		wohnungDTO.setAdresse(new Adresse_Assembler().toDTO(bean.getAdresse()));
		return wohnungDTO;
	}
}