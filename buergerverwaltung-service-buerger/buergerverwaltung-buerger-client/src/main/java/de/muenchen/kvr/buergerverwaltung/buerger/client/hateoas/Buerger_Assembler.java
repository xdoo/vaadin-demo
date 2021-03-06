package de.muenchen.kvr.buergerverwaltung.buerger.client.hateoas;

import de.muenchen.kvr.buergerverwaltung.buerger.client.domain.Buerger_DTO;
import de.muenchen.kvr.buergerverwaltung.buerger.client.local.Buerger_;
import de.muenchen.kvr.buergerverwaltung.buerger.client.rest.Buerger_Resource;
import org.springframework.hateoas.Resource;

import java.util.UUID;
import java.util.stream.Collectors;

/*
 * This file will be overwritten on every change of the model!
 * This file was automatically generated by GAIA.
 */
public class Buerger_Assembler {


	/**
	 * Transform the Resource (from the REST Server) to the local object representation.
	 *
	 * @param resource the REST DTO Resource
	 * @return the local Object Representation
	 */
	public Buerger_ toBean(Resource<Buerger_DTO> resource) {
		Buerger_DTO buergerDTO = resource.getContent();
		Buerger_ bean = new Buerger_();		
		bean.setVorname(buergerDTO.getVorname());
		bean.setNachname(buergerDTO.getNachname());
		bean.setGeburtstag(buergerDTO.getGeburtstag());
		bean.setAugenfarbe(buergerDTO.getAugenfarbe());
		bean.setTelefonnummer(buergerDTO.getTelefonnummer());
		bean.setEmail(buergerDTO.getEmail());
		bean.setLebendig(buergerDTO.isLebendig());
		if(buergerDTO.getEigenschaften() != null)
			bean.setEigenschaften(buergerDTO.getEigenschaften());
		if(buergerDTO.getStaatsangehoerigkeiten() != null)
			bean.setStaatsangehoerigkeiten(buergerDTO.getStaatsangehoerigkeiten());

		Adresse_Assembler adresseAssembler = new Adresse_Assembler();
		bean.setBisherigeWohnsitze(buergerDTO.getBisherigeWohnsitze().stream()
				.map(adresseAssembler::toBean)
				.collect(Collectors.toSet()));

		bean.add(resource.getLinks());
		
		return bean;
	}
	
	/**
	 * Transform the local object representation to the DTO resource.
	 *
	 * @param bean the local object representation
	 * @return the REST DTO Resource
	 */
	public Buerger_Resource toResource(Buerger_ bean) {
		return new Buerger_Resource(toDTO(bean), bean.getLinks());
	}
	
	/**
	 * Transform the local object representation to the DTO.
	 *
	 * @param bean the local object representation
	 * @return the REST DTO
	 */
	public Buerger_DTO toDTO(Buerger_ bean) {
		Buerger_DTO buergerDTO = new Buerger_DTO();
		
		try{
			String[] id = bean.getId().getHref().split("/");
			buergerDTO.setOid(UUID.fromString(id[id.length-1]));
		} catch(Exception e) {/* No Link found */}
		
		buergerDTO.setVorname(bean.getVorname());
		buergerDTO.setNachname(bean.getNachname());
		buergerDTO.setGeburtstag(bean.getGeburtstag());
		buergerDTO.setAugenfarbe(bean.getAugenfarbe());
		buergerDTO.setTelefonnummer(bean.getTelefonnummer());
		buergerDTO.setEmail(bean.getEmail());
		buergerDTO.setLebendig(bean.isLebendig());
		if(bean.getEigenschaften() != null)
			buergerDTO.setEigenschaften(bean.getEigenschaften());
		if(bean.getStaatsangehoerigkeiten() != null)
			buergerDTO.setStaatsangehoerigkeiten(bean.getStaatsangehoerigkeiten());

		Adresse_Assembler adresseAssembler = new Adresse_Assembler();
		buergerDTO.setBisherigeWohnsitze(bean.getBisherigeWohnsitze().stream()
				.map(adresseAssembler::toDTO)
				.collect(Collectors.toSet()));

		if(bean.getKinder() != null)
			buergerDTO.setKinder(bean.getKinder());
		if(bean.getPartner() != null)
			buergerDTO.setPartner(bean.getPartner());
		if(bean.getHauptwohnung() != null)
			buergerDTO.setHauptwohnung(bean.getHauptwohnung());
		if(bean.getSachbearbeiter() != null)
			buergerDTO.setSachbearbeiter(bean.getSachbearbeiter());
		if(bean.getPass() != null)
			buergerDTO.setPass(bean.getPass());
		return buergerDTO;
	}
}
