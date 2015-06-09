package de.muenchen.demo.service.rest;

import de.muenchen.demo.service.rest.api.BuergerResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.http.HttpStatus;
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
    
    @RequestMapping(method = {RequestMethod.GET})
    ResponseEntity listBuerger() {
        return null;
    }
    
    @RequestMapping(method = {RequestMethod.POST})
    ResponseEntity createBuerger() {
        ResponseEntity<BuergerResource> response = new ResponseEntity<BuergerResource>(HttpStatus.CONFLICT);
        
        return null;
    }
    
    @RequestMapping(value = "/{id}", method = {RequestMethod.GET})
    ResponseEntity readBuerger(@PathVariable("id") String id) {
        return null;
    }
    
    @RequestMapping(value = "/{id}", method = {RequestMethod.POST})
    ResponseEntity updateBuerger() {
        return null;
    }
    
    @RequestMapping(value = "/{id}", method = {RequestMethod.DELETE})
    ResponseEntity deleteBuerger(@PathVariable("id") String id) {
        return null;
    }
    
}
