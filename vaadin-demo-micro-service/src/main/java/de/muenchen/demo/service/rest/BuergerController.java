package de.muenchen.demo.service.rest;

import de.muenchen.demo.service.rest.api.BuergerResource;
import de.muenchen.demo.service.rest.api.BuergerResourceAssembler;
import de.muenchen.demo.service.services.BuergerService;
import java.util.Date;
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
    
    @RequestMapping(method = {RequestMethod.GET})
    ResponseEntity queryBuerger() {
        if(LOG.isDebugEnabled())
            LOG.debug("query buerger");
        return null;
    }
    
    @RequestMapping(value = "/{vorname}/{nachname}/{geburtsdatum}")
    ResponseEntity queryBuerger(@PathVariable("vorname") String vorname
                                , @PathVariable("nachname") String nachname
                                , @PathVariable("geburtsdatum") Date geburtsdatum) {
        if(LOG.isDebugEnabled())
            LOG.debug("query buerger with params: vorname, nachname, geburtsdatum");
        
        return null;
    }
    
    @RequestMapping(method = {RequestMethod.POST})
    ResponseEntity createBuerger(RequestEntity<BuergerResource> request) {
        if(LOG.isDebugEnabled())
            LOG.debug("create buerger");
        ResponseEntity<BuergerResource> response = new ResponseEntity<BuergerResource>(HttpStatus.CONFLICT);
        return null;
    }
    
    @RequestMapping(value = "/{id}", method = {RequestMethod.GET})
    ResponseEntity readBuerger(@PathVariable("id") String id) {
        if(LOG.isDebugEnabled())
            LOG.debug("read buerger");
        return null;
    }
    
    @RequestMapping(value = "/{id}", method = {RequestMethod.POST})
    ResponseEntity updateBuerger() {
        if(LOG.isDebugEnabled())
            LOG.debug("update buerger");
        return null;
    }
    
    @RequestMapping(value = "/{id}", method = {RequestMethod.DELETE})
    ResponseEntity deleteBuerger(@PathVariable("id") String id) {
        if(LOG.isDebugEnabled())
            LOG.debug("delete buerger");
        return null;
    }
    
}
