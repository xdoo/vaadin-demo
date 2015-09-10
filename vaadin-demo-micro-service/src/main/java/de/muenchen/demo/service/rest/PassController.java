package de.muenchen.demo.service.rest;

import de.muenchen.demo.service.domain.Buerger;
import de.muenchen.demo.service.domain.Pass;
import de.muenchen.demo.service.domain.Staatsangehoerigkeit;
import de.muenchen.demo.service.domain.StaatsangehoerigkeitReference;
import de.muenchen.vaadin.demo.api.rest.BuergerResource;
import de.muenchen.demo.service.rest.api.BuergerResourceAssembler;
import de.muenchen.demo.service.rest.api.PassResource;

import de.muenchen.vaadin.demo.apilib.rest.SearchResultResource;
import de.muenchen.vaadin.demo.api.rest.StaatsangehoerigkeitResource;

import de.muenchen.demo.service.rest.api.StaatsangehoerigkeitResourceAssembler;
import de.muenchen.demo.service.rest.api.PassResourceAssembler;
import de.muenchen.demo.service.services.BuergerService;
import de.muenchen.demo.service.services.PassService;
import de.muenchen.demo.service.services.StaatsangehoerigkeitService;
import de.muenchen.vaadin.demo.apilib.hateoas.HateoasUtil;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Schnittstelle um einen Pass verwalten zu können.
 *
 * @author praktikant.tmar
 */
@Controller
@ExposesResourceFor(PassController.class)
@RequestMapping("/pass")
public class PassController {

    private static final Logger LOG = LoggerFactory.getLogger(PassController.class);

    @Autowired
    EntityLinks entityLinks;
    @Autowired
    PassService service;
    @Autowired
    BuergerService buergerService;
    @Autowired
    StaatsangehoerigkeitService staatsService;
    @Autowired
    PassResourceAssembler assembler;
    @Autowired
    BuergerResourceAssembler buergerAssembler;
    @Autowired
    StaatsangehoerigkeitResourceAssembler staatAssembler;
    @Value("${URL}")
    private String URL;

    /**
     * Alle Pass suchen.
     *
     * @return
     */
    @Secured({"PERM_queryPass"})
    @RequestMapping(value = "/query", method = {RequestMethod.GET})
    public ResponseEntity queryPass() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("query pass");
        }
        SearchResultResource<PassResource> resource = this.assembler.toResource(this.service.query());
        resource.add(linkTo(methodOn(PassController.class).queryPass()).withSelfRel()); // add self link
        return ResponseEntity.ok(resource);
    }

    /**
     * Pass mit Parametern suchen.
     *
     * @param filter
     * @return
     */
    @Secured({"PERM_queryPass"})
    @RequestMapping(value = "/query", params = {"filter"})
    public ResponseEntity queryPass(@RequestParam(value = "filter") String filter) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("query pass with filter > " + filter);
        }
        SearchResultResource<PassResource> resource = this.assembler.toResource(this.service.query(filter));
        resource.add(linkTo(methodOn(PassController.class).queryPass(filter)).withSelfRel()); // add self link with params
        return ResponseEntity.ok(resource);
    }

    /**
     * Erzeugt ein neues Pass Objekt mit gefüllter OID. Das Objekt ist noch in
     * der DB gespeichert.
     *
     * @return
     */
    @Secured({"PERM_newPass"})
    @RequestMapping(value = "/new", method = {RequestMethod.GET})
    public ResponseEntity newPass() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("new pass");
        }
        System.out.println(URL);
        Pass entity = this.service.create();
        PassResource resource = this.assembler.toResource(entity, HateoasUtil.REL_NEW, HateoasUtil.REL_SAVE);
        return ResponseEntity.ok(resource);
    }

    /**
     * Macht eine Kopie eines Pass. Diese Kopie wird bei Erstellung in der DB
     * gespeichert.
     *
     * @param oid
     * @return
     */
    @Secured({"PERM_copyPass"})
    @RequestMapping(value = "/copy/{oid}", method = {RequestMethod.GET})
    public ResponseEntity copyPass(@PathVariable String oid) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("copy pass");
        }
        Pass entity = this.service.copy(oid);
        PassResource resource = this.assembler.assembleWithAllLinks(entity);
        return ResponseEntity.ok(resource);
    }

    /**
     * Liest einen Pass zur OID.
     *
     * @param oid
     * @return
     */
    @Secured({"PERM_readPass"})
    @RequestMapping(value = "/{oid}", method = {RequestMethod.GET})
    public ResponseEntity readPass(@PathVariable("oid") String oid) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("read pass");
        }
        Pass entity = this.service.read(oid);
        PassResource resource = this.assembler.assembleWithAllLinks(entity);
        return ResponseEntity.ok(resource);
    }

    /**
     * Speichert Änderungen an einem bereits vorhandenen Pass.
     *
     * @param oid
     * @param request
     * @return
     */
    @Secured({"PERM_updatePass"})
    @RequestMapping(value = "/{oid}", method = {RequestMethod.PUT})
    public ResponseEntity updatePass(@PathVariable("oid") String oid, @RequestBody PassResource request) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("update pass");
        }
        Pass entity = service.read(request.getOid());
        LOG.info("davor > " + entity.toString());
        this.assembler.fromResource(request, entity);
        LOG.info("danach > " + entity.toString());
        this.service.update(entity);
        PassResource resource = this.assembler.assembleWithAllLinks(entity);
        return ResponseEntity.ok(resource);
    }

    /**
     * Speichert einen neuen Pass.
     *
     * @param request
     * @return
     */
    @Secured({"PERM_savePass"})
    @RequestMapping(value = "/save", method = {RequestMethod.POST})
    public ResponseEntity savePass(@RequestBody PassResource request) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("save pass");
        }
        Pass entity = new Pass();
        this.assembler.fromResource(request, entity);
        this.service.save(entity);
        PassResource resource = this.assembler.assembleWithAllLinks(entity);
        return ResponseEntity.ok(resource);
    }

    /**
     * Löscht einen Pass.
     *
     * @param oid
     * @return
     */
    @Secured({"PERM_deletePass"})
    @RequestMapping(value = "/{oid}", method = {RequestMethod.DELETE})
    public ResponseEntity deletePass(@PathVariable("oid") String oid) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("delete pass");
        }
        this.service.delete(oid);
        return ResponseEntity.ok().build();
    }

    /**
     * Löscht mehrere Pässe.
     *
     * @param oids
     * @return
     */
    @Secured({"PERM_deleteListPass"})
    @RequestMapping(method = {RequestMethod.POST})
    public ResponseEntity deleteListPass(@RequestBody ArrayList<String> oids) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("delete Pass");
        }
        this.service.delete(oids);
        return ResponseEntity.ok().build();
    }

    /**
     * Macht eine Kopie mehreres Pässes. Diese Kopie wird bei Erstellung in der
     * DB gespeichert.
     *
     * @param oids
     * @return
     */
    @Secured({"PERM_copyListPass"})
    @RequestMapping(value = "/copy", method = {RequestMethod.POST})
    public ResponseEntity copyListPass(@RequestBody ArrayList<String> oids) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("copy pass");
        }
        this.service.copy(oids);
        return ResponseEntity.ok().build();
    }

    /**
     * Assoziiert ein Staatsangehoerigkeit mit einem Pass .
     *
     * @param passOid
     * @param statsOid
     * @return
     */
    @Secured({"PERM_addStaatangehoerigkeitPass"})
    @RequestMapping(value = "add/{pOid}/staats", method = {RequestMethod.POST})
    public ResponseEntity addStaatangehoerigkeitPass(@PathVariable("pOid") String passOid, @RequestBody String statsOid) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Add staatsangehoerigkeit pass");
        }

        StaatsangehoerigkeitReference staatRef = staatsService.readReference(statsOid);
        Staatsangehoerigkeit staat = staatsService.read(statsOid);

        Pass entity = service.read(passOid);
        entity.setStaatsangehoerigkeit(staat);
        entity.setStaatsangehoerigkeitReference(staatRef);
        this.service.update(entity);

        PassResource resource = this.assembler.assembleWithAllLinks(entity);
        resource.add(linkTo(methodOn(PassController.class).addStaatangehoerigkeitPass(passOid, statsOid)).withSelfRel()); // add self link with params
        return ResponseEntity.ok(resource);
    }

    /**
     * Liest die Staatsangehoerigkeiten einer Pass.
     *
     * @param oid
     * @return
     */
    @Secured({"PERM_readPassStaatsangehoerigkeiten"})
    @RequestMapping(value = "/staat/{oid}", method = {RequestMethod.GET})
    @SuppressWarnings("empty-statement")
    public ResponseEntity readPassStaatsangehoerigkeiten(@PathVariable("oid") String oid) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("read Pass Staatsangehoerigkeiten");
        }
        Staatsangehoerigkeit staat = this.staatsService.read(this.service.read(oid).getStaatsangehoerigkeitReference().getReferencedOid());

        StaatsangehoerigkeitResource resources;
        resources = staatAssembler.toResource(staat, HateoasUtil.REL_SELF);
        return ResponseEntity.ok(resources);
    }

    /**
     * Liest der Buerger einer Pass.
     *
     * @param passOid
     * @return
     */
    @Secured({"PERM_readPassBuerger"})
    @RequestMapping(value = "/buerger/{passOid}", method = {RequestMethod.GET})
    public ResponseEntity readPassBuerger(@PathVariable("passOid") String passOid) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("read Pass Buerger");
        }
        Buerger buerger = this.buergerService.readPassBuerger(passOid);
        BuergerResource resource = this.buergerAssembler.assembleWithAllLinks(buerger);
        resource.add(linkTo(methodOn(PassController.class).readPassBuerger(passOid)).withSelfRel());
        return ResponseEntity.ok(resource);
    }

    /**
     * Release Operation für den Pass eines Passs.
     *
     * @param passOid
     * @return
     */
    @Secured({"PERM_releasePassBuerger"})
    @RequestMapping(value = "/release/buerger/{passOid}", method = {RequestMethod.GET})
    public ResponseEntity releasePassBuerger(@PathVariable("passOid") String passOid) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("delete Pass Buerger");
        }
        this.buergerService.releasePassBuerger(passOid);
        return ResponseEntity.ok().build();

    }

}
