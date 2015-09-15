package de.muenchen.vaadin.demo.api.hateoas;

import de.muenchen.vaadin.demo.api.domain.Buerger;
import de.muenchen.vaadin.demo.api.local.LocalBuerger;
import de.muenchen.vaadin.demo.api.rest.BuergerResource;
import org.springframework.hateoas.Resource;

/**
 * Created by p.mueller on 15.09.15.
 */
public class LocalBuergerAssembler {
    public LocalBuerger toBean(Resource<Buerger> resource) {
        Buerger buerger = resource.getContent();

        LocalBuerger bean = new LocalBuerger(
                buerger.getVorname(),
                buerger.getNachname(),
                buerger.getGeburtsdatum()
        );
        bean.add(resource.getLinks());

        return bean;
    }


    public BuergerResource toResource(LocalBuerger bean) {
        Buerger buerger = new Buerger();
        buerger.setVorname(bean.getVorname());
        buerger.setNachname(bean.getNachname());
        buerger.setGeburtsdatum(bean.getGeburtsdatum());


        return new BuergerResource(buerger, bean.getLinks());
    }
}
