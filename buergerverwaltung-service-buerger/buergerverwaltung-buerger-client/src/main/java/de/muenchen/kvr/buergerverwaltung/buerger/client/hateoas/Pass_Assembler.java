package de.muenchen.kvr.buergerverwaltung.buerger.client.hateoas;

import de.muenchen.kvr.buergerverwaltung.buerger.client.domain.Pass_DTO;
import de.muenchen.kvr.buergerverwaltung.buerger.client.local.Pass_;
import de.muenchen.kvr.buergerverwaltung.buerger.client.rest.Pass_Resource;
import org.springframework.hateoas.Resource;

import java.util.UUID;

/*
 * This file will be overwritten on every change of the model!
 * This file was automatically generated by GAIA.
 */
public class Pass_Assembler {


	/**
	 * Transform the Resource (from the REST Server) to the local object representation.
	 *
	 * @param resource the REST DTO Resource
	 * @return the local Object Representation
	 */
	public Pass_ toBean(Resource<Pass_DTO> resource) {
		Pass_DTO passDTO = resource.getContent();
		Pass_ bean = new Pass_();		
		bean.setPassNummer(passDTO.getPassNummer());
		bean.setTyp(passDTO.getTyp());
		bean.setKode(passDTO.getKode());
		bean.setGroesse(passDTO.getGroesse());
		bean.setBehoerde(passDTO.getBehoerde());
		bean.add(resource.getLinks());
		
		return bean;
	}
	
	/**
	 * Transform the local object representation to the DTO resource.
	 *
	 * @param bean the local object representation
	 * @return the REST DTO Resource
	 */
	public Pass_Resource toResource(Pass_ bean) {
		return new Pass_Resource(toDTO(bean), bean.getLinks());
	}
	
	/**
	 * Transform the local object representation to the DTO.
	 *
	 * @param bean the local object representation
	 * @return the REST DTO
	 */
	public Pass_DTO toDTO(Pass_ bean) {
		Pass_DTO passDTO = new Pass_DTO();
		
		try{
			String[] id = bean.getId().getHref().split("/");
			passDTO.setOid(UUID.fromString(id[id.length-1]));
		} catch(Exception e) {/* No Link found */}
		
		passDTO.setPassNummer(bean.getPassNummer());
		passDTO.setTyp(bean.getTyp());
		passDTO.setKode(bean.getKode());
		passDTO.setGroesse(bean.getGroesse());
		passDTO.setBehoerde(bean.getBehoerde());
		return passDTO;
	}
}
