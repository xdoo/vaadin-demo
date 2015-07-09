package de.muenchen.demo.service.rest;

import de.muenchen.demo.service.domain.Buerger;
import de.muenchen.demo.service.rest.api.BuergerResource;
import de.muenchen.demo.service.rest.api.BuergerResourceAssembler;
import de.muenchen.demo.service.rest.api.SearchResultResource;
import de.muenchen.demo.service.services.BuergerService;
import de.muenchen.demo.service.util.HateoasRelations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Schnittstelle um einen Bürger verwalten zu können.
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
    
    /**
     * Alle Bürger suchen.
     * 
     * @return 
     */
    @RequestMapping(value = "/query", method = {RequestMethod.GET})
    public ResponseEntity queryBuerger() {
        if(LOG.isDebugEnabled())
            LOG.debug("query buerger");
        SearchResultResource<BuergerResource> resource = this.assembler.toResource(this.service.query());
        resource.add(linkTo(methodOn(BuergerController.class).queryBuerger()).withSelfRel()); // add self link
        return ResponseEntity.ok(resource);
    }
    
    /**
     * Bürger mit Parametern suchen.
     * 
     * @param filter
     * @return 
     */
    @RequestMapping(value = "/query", params = {"filter"})
    public ResponseEntity queryBuerger(@RequestParam (value = "filter") String filter) {
        if(LOG.isDebugEnabled())
            LOG.debug("query buerger with filter > " + filter);
        SearchResultResource<BuergerResource> resource = this.assembler.toResource(this.service.query(filter));
        resource.add(linkTo(methodOn(BuergerController.class).queryBuerger(filter)).withSelfRel()); // add self link with params
        return ResponseEntity.ok(resource);
    }
    
    /**
     * Erzeugt ein neues Bürger Objekt mit gefüllter OID. Das 
     * Objekt ist noch in der DB gespeichert.
     * 
     * @return 
     */
    @RequestMapping(value = "/new", method = {RequestMethod.GET})
    public ResponseEntity newBuerger() {
        if(LOG.isDebugEnabled())
            LOG.debug("new buerger");
        Buerger entity = this.service.create();
        BuergerResource resource = this.assembler.toResource(entity, HateoasRelations.NEW, HateoasRelations.SAVE);
        return ResponseEntity.ok(resource);
    }
    
    /**
     * Macht eine Kopie eines Büergers. Diese Kopie wird bei
     * Erstellung in der DB gespeichert.
     * 
     * @param oid
     * @return 
     */
    @RequestMapping(value = "/copy/{oid}", method = {RequestMethod.GET})
    public ResponseEntity copyBuerger(@PathVariable String oid) {
        if(LOG.isDebugEnabled())
            LOG.debug("copy buerger");
        Buerger entity = this.service.copy(oid);
        BuergerResource resource = this.assembler.toResource(entity, HateoasRelations.SELF, HateoasRelations.NEW, HateoasRelations.DELETE, HateoasRelations.UPDATE, HateoasRelations.COPY);
        return ResponseEntity.ok(resource);
    }
    
    /**
     * Liest einen Bürger zur OID.
     * 
     * @param oid
     * @return 
     */
    @RequestMapping(value = "/{oid}", method = {RequestMethod.GET})
    public ResponseEntity readBuerger(@PathVariable("oid") String oid) {
        if(LOG.isDebugEnabled())
            LOG.debug("read buerger");
        Buerger entity = this.service.read(oid);
        BuergerResource resource = this.assembler.toResource(entity, HateoasRelations.SELF, HateoasRelations.NEW, HateoasRelations.DELETE, HateoasRelations.UPDATE, HateoasRelations.COPY);
        return ResponseEntity.ok(resource);
    }
    
    /**
     * Speichert Änderungen an einem bereits vorhandenen Bürger.
     * 
     * @param oid
     * @param request
     * @return 
     */
    @RequestMapping(value = "/{oid}", method = {RequestMethod.POST})
    public ResponseEntity updateBuerger(@PathVariable("oid") String oid, @RequestBody BuergerResource request) {
        if(LOG.isDebugEnabled())
            LOG.debug("update buerger");
        Buerger entity = service.read(request.getOid());
        LOG.info("davor > " + entity.toString());
        this.assembler.fromResource(request, entity);
        LOG.info("danach > " + entity.toString());
        this.service.update(entity);
        BuergerResource resource = this.assembler.toResource(entity, HateoasRelations.SELF, HateoasRelations.NEW, HateoasRelations.DELETE, HateoasRelations.UPDATE, HateoasRelations.COPY);
        return ResponseEntity.ok(resource);
    }
    
    /**
     * Speichert einen neuen Bürger.
     * 
     * @param request
     * @return 
     */
    @RequestMapping(value = "/save", method = {RequestMethod.POST})
    public ResponseEntity saveBuerger(@RequestBody BuergerResource request) {
        if(LOG.isDebugEnabled())
            LOG.debug("save buerger");
        Buerger entity = new Buerger();
        this.assembler.fromResource(request, entity);
        this.service.save(entity);
        BuergerResource resource = this.assembler.toResource(entity, HateoasRelations.SELF, HateoasRelations.NEW, HateoasRelations.DELETE, HateoasRelations.UPDATE, HateoasRelations.COPY);
        return ResponseEntity.ok(resource);
    }
    
    /**
     * Löscht einen Bürger.
     * 
     * @param oid
     * @return 
     */
    @RequestMapping(value = "/{oid}", method = {RequestMethod.DELETE})
    public ResponseEntity deleteBuerger(@PathVariable("oid") String oid) {
        if(LOG.isDebugEnabled())
            LOG.debug("delete buerger");
        this.service.delete(oid);
        return ResponseEntity.ok().build();
    }
    
}
