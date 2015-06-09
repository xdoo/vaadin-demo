package de.muenchen.demo.service.rest.api;

import de.muenchen.demo.service.domain.Buerger;
import de.muenchen.demo.service.rest.BuergerController;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;

/**
 *
 * @author claus.straube
 */
public class BurgerResourceAssembler extends ResourceAssemblerSupport<Buerger, BuergerResource>{

    public BurgerResourceAssembler(Class<BuergerController> controllerClass, Class<BuergerResource> resourceType) {
        super(controllerClass, resourceType);
    }

    @Override
    public BuergerResource toResource(Buerger buerger) {
        BuergerResource resource = createResourceWithId(buerger.getOid(), buerger);
        return resource;
    }
    
}
