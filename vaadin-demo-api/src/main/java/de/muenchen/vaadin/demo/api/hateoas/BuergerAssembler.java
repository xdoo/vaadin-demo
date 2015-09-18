package de.muenchen.vaadin.demo.api.hateoas;

import de.muenchen.vaadin.demo.api.domain.BuergerDTO;
import de.muenchen.vaadin.demo.api.local.Buerger;
import de.muenchen.vaadin.demo.api.rest.BuergerResource;
import org.springframework.hateoas.Resource;

/**
 * Created by p.mueller on 15.09.15.
 */
public class BuergerAssembler {
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


    public BuergerResource toResource(Buerger bean) {
        BuergerDTO buergerDTO = new BuergerDTO();
        buergerDTO.setVorname(bean.getVorname());
        buergerDTO.setNachname(bean.getNachname());
        buergerDTO.setGeburtsdatum(bean.getGeburtsdatum());


        return new BuergerResource(buergerDTO, bean.getLinks());
    }
}
