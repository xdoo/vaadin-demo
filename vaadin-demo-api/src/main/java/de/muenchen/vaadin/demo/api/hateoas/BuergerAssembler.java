package de.muenchen.vaadin.demo.api.hateoas;

import de.muenchen.vaadin.demo.api.domain.BuergerDTO;
import de.muenchen.vaadin.demo.api.local.Buerger;
import de.muenchen.vaadin.demo.api.rest.BuergerResource;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpHeaders;

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
        return toBean(resource, null);
    }

    /**
     * Transform the local object representation to the DTO resource.
     *
     * @param bean the local object representation
     * @return the REST DTO Resource
     */
    public BuergerResource toResource(Buerger bean) {
        if (bean == null)
            throw new IllegalArgumentException("The bean cannot be null!");

        BuergerDTO buergerDTO = new BuergerDTO();
        buergerDTO.setVorname(bean.getVorname());
        buergerDTO.setNachname(bean.getNachname());
        buergerDTO.setGeburtsdatum(bean.getGeburtsdatum());

        return new BuergerResource(buergerDTO, bean.getLinks());
    }

    public Buerger toBean(Resource<BuergerDTO> resource, HttpHeaders headers) {
        if (resource == null)
            throw new IllegalArgumentException("The Resource cannot be null!");
        if (resource.getId() == null)
            throw new IllegalArgumentException("The Resource must have an ID!");
        if (resource.getContent() == null)
            throw new IllegalArgumentException("The resource must have a content (is null)!");

        BuergerDTO buergerDTO = resource.getContent();

        Buerger bean = new Buerger(
                buergerDTO.getVorname(),
                buergerDTO.getNachname(),
                buergerDTO.getGeburtsdatum()
        );
        bean.add(resource.getLinks());

        // The headers are optional
        bean.setHeaders(headers);

        return bean;
    }
}
