package de.muenchen.kvr.buergerverwaltung.buerger.client.hateoas;

import de.muenchen.kvr.buergerverwaltung.buerger.client.domain.Sachbearbeiter_DTO;
import de.muenchen.kvr.buergerverwaltung.buerger.client.local.Sachbearbeiter_;
import de.muenchen.kvr.buergerverwaltung.buerger.client.rest.Sachbearbeiter_Resource;
import org.springframework.hateoas.Resource;

import java.util.UUID;

/*
 * This file will be overwritten on every change of the model!
 * This file was automatically generated by GAIA.
 */
public class Sachbearbeiter_Assembler {


	/**
	 * Transform the Resource (from the REST Server) to the local object representation.
	 *
	 * @param resource the REST DTO Resource
	 * @return the local Object Representation
	 */
	public Sachbearbeiter_ toBean(Resource<Sachbearbeiter_DTO> resource) {
		Sachbearbeiter_DTO sachbearbeiterDTO = resource.getContent();
		Sachbearbeiter_ bean = new Sachbearbeiter_();		
		bean.setTelefon(sachbearbeiterDTO.getTelefon());
		bean.setFax(sachbearbeiterDTO.getFax());
		bean.setFunktion(sachbearbeiterDTO.getFunktion());
		bean.setOrganisationseinheit(sachbearbeiterDTO.getOrganisationseinheit());
		bean.add(resource.getLinks());
		
		return bean;
	}
	
	/**
	 * Transform the local object representation to the DTO resource.
	 *
	 * @param bean the local object representation
	 * @return the REST DTO Resource
	 */
	public Sachbearbeiter_Resource toResource(Sachbearbeiter_ bean) {
		return new Sachbearbeiter_Resource(toDTO(bean), bean.getLinks());
	}
	
	/**
	 * Transform the local object representation to the DTO.
	 *
	 * @param bean the local object representation
	 * @return the REST DTO
	 */
	public Sachbearbeiter_DTO toDTO(Sachbearbeiter_ bean) {
		Sachbearbeiter_DTO sachbearbeiterDTO = new Sachbearbeiter_DTO();
		
		try{
			String[] id = bean.getId().getHref().split("/");
			sachbearbeiterDTO.setOid(UUID.fromString(id[id.length-1]));
		} catch(Exception e) {/* No Link found */}
		
		sachbearbeiterDTO.setTelefon(bean.getTelefon());
		sachbearbeiterDTO.setFax(bean.getFax());
		sachbearbeiterDTO.setFunktion(bean.getFunktion());
		sachbearbeiterDTO.setOrganisationseinheit(bean.getOrganisationseinheit());
		return sachbearbeiterDTO;
	}
}
