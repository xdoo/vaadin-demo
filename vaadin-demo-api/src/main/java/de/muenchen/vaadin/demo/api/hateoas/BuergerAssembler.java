package de.muenchen.vaadin.demo.api.hateoas;

import de.muenchen.vaadin.demo.api.domain.BuergerDTO;
import de.muenchen.vaadin.demo.api.local.Buerger;
import de.muenchen.vaadin.demo.api.rest.BuergerResource;
import org.springframework.hateoas.Resource;

/**
 * Provides a simple Assembler to transform the resource of an BuergerDTO to the Buerger of
 * used in this Application.
 *
 * @author p.mueller
 * @version 1.0
 */
public class BuergerAssembler {

    /**
     * Transform the Resource (from the REST Server) to the local object representation.
     *
     * @param resource the REST DTO Resource
     * @return the local Object Representation
     */
    public Buerger toBean(Resource<BuergerDTO> resource) {
        BuergerDTO buergerDTO = resource.getContent();

        Buerger bean = new Buerger(
                buergerDTO.getVorname(),
                buergerDTO.getNachname(),
                buergerDTO.getGeburtsdatum()
        );
        bean.add(resource.getLinks());

        return bean;
    }

    /**
     * Transform the local object representation to the DTO resource.
     *
     * @param bean the local object representation
     * @return the REST DTO Resource
     */
    public BuergerResource toResource(Buerger bean) {
        BuergerDTO buergerDTO = new BuergerDTO();
        buergerDTO.setVorname(bean.getVorname());
        buergerDTO.setNachname(bean.getNachname());
        buergerDTO.setGeburtsdatum(bean.getGeburtsdatum());

        return new BuergerResource(buergerDTO, bean.getLinks());
    }
}
