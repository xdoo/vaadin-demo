package de.muenchen.demo.service.rest;

import de.muenchen.demo.service.domain.Buerger;
import de.muenchen.demo.service.rest.api.BuergerResource;
import de.muenchen.demo.service.rest.api.BuergerResourceAssembler;
import de.muenchen.demo.service.services.BuergerService;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author claus.straube
 */
@Controller
@ExposesResourceFor(BuergerController.class)
@RequestMapping("/buerger")
public class BuergerController {
    
    private static final Logger LOG = LoggerFactory.getLogger(BuergerController.class);
    
    @Autowired EntityLinks entityLinks;
    @Autowired BuergerService service;
    @Autowired BuergerResourceAssembler assembler;
    
    @RequestMapping(value = "/search", method = {RequestMethod.GET})
    public ResponseEntity queryBuerger() {
        if(LOG.isDebugEnabled())
            LOG.debug("query buerger");
        ResponseEntity<List<Buerger>> response = ResponseEntity.created(null).body(this.service.query());
        
        return null;
    }
    
    @RequestMapping(value = "/{vorname}/{nachname}/{geburtsdatum}")
    public ResponseEntity queryBuerger(@PathVariable("vorname") String vorname
                                , @PathVariable("nachname") String nachname
                                , @PathVariable("geburtsdatum") Date geburtsdatum) {
        if(LOG.isDebugEnabled())
            LOG.debug("query buerger with params: vorname, nachname, geburtsdatum");
        
        return null;
    }
    
    @RequestMapping(value = "/new", method = {RequestMethod.GET})
    public ResponseEntity createBuerger() {
        if(LOG.isDebugEnabled())
            LOG.debug("create buerger");
        Buerger entity = this.service.create();
        BuergerResource resource = this.assembler.toResource(entity);
        return ResponseEntity.ok(resource);
    }
    
    @RequestMapping(value = "/{oid}", method = {RequestMethod.GET})
    public ResponseEntity readBuerger(@PathVariable("oid") String oid) {
        if(LOG.isDebugEnabled())
            LOG.debug("read buerger");
        Buerger entity = this.service.read(oid);
        BuergerResource resource = this.assembler.toResource(entity);
        return ResponseEntity.ok(resource);
    }
    
    @RequestMapping(value = "/{oid}", method = {RequestMethod.POST})
    public ResponseEntity updateBuerger(@PathVariable("oid") String oid, RequestEntity request) {
        if(LOG.isDebugEnabled())
            LOG.debug("update buerger");
        return null;
    }
    
    @RequestMapping(value = "/{id}", method = {RequestMethod.DELETE})
    public ResponseEntity deleteBuerger(@PathVariable("id") String id) {
        if(LOG.isDebugEnabled())
            LOG.debug("delete buerger");
        return null;
    }
    
}
