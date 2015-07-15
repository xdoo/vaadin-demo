package de.muenchen.demo.service.rest;

import de.muenchen.vaadin.demo.api.rest.ServiceInfoResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

/**
 * Schnittstelle um allgemeine Informationen über den Service abrufen zu können.
 * 
 * @author claus.straube
 */
@Controller
@ExposesResourceFor(ServiceInfoController.class)
@RequestMapping("/service_info")
public class ServiceInfoController {
    
    private static final Logger LOG = LoggerFactory.getLogger(ServiceInfoController.class);
    
    /**
     * OID des services (z.B. EWO)
     */
    @Value(value = "${service.info.oid}") String oid;
    
    /**
     * Name des Services (z.B. Einwohnermeldewesen).
     */
    @Value(value = "${service.info.name}") String name;
    
    /**
     * Version des Services (z.B. 1.0.4). Hier müssen die
     * vorgegebenen NAmenskonventionen verwendet werden.
     */
    @Value(value = "${service.info.version}") String version;
    
    ServiceInfoResource resource;
    
    /**
     * Diese Operation gibt alle Links zur Suche und Neuerstellung von Objekten zurück. 
     * Ziel ist es, dass man bei Einstieg in eine (z.B. grafische) Anwendung mit einem 
     * Link (nämlich zum Server) sämtliche Navigationsmöglichkeiten geliefert bekommt.
     * 
     * @return 
     */
    @RequestMapping(method = {RequestMethod.GET})
    public ResponseEntity getInfo() {
        if(LOG.isDebugEnabled())
            LOG.debug("get info for service");
        //this.getResource().add(linkTo(methodOn(ServiceInfoController.class).getInfo()).withSelfRel());
        // hier müssen die 'query' und 'new' links aller Entitäten rein.
        this.getResource().addEntityLink(linkTo(methodOn(BuergerController.class).newBuerger()).withRel("buerger_new"));
        this.getResource().addEntityLink(linkTo(methodOn(BuergerController.class).queryBuerger()).withRel("buerger_query"));
        return ResponseEntity.ok(this.getResource());
    }

    public ServiceInfoResource getResource() {
        if(this.resource == null) {
            this.resource = new ServiceInfoResource(oid, name, version);
        }
        return resource;
    }

    public void setResource(ServiceInfoResource resource) {
        this.resource = resource;
    }
}
