package de.muenchen.demo.service.rest;

import de.muenchen.demo.service.domain.Pass;
import de.muenchen.demo.service.domain.Staatsangehoerigkeit;
import de.muenchen.demo.service.domain.StaatsangehoerigkeitReference;
import de.muenchen.demo.service.rest.api.PassResource;
import de.muenchen.demo.service.rest.api.PassResourceAssembler;
import de.muenchen.demo.service.rest.api.SearchResultResource;
import de.muenchen.demo.service.rest.api.StaatsangehoerigkeitResource;
import de.muenchen.demo.service.rest.api.StaatsangehoerigkeitResourceAssembler;
import de.muenchen.demo.service.rest.api.WohnungResourceAssembler;
import de.muenchen.demo.service.services.PassService;
import de.muenchen.demo.service.services.StaatsangehoerigkeitService;
import de.muenchen.demo.service.services.WohnungService;
import de.muenchen.vaadin.demo.api.hateoas.HateoasUtil;
import javax.annotation.security.RolesAllowed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    WohnungService wohnungService;
    @Autowired
    StaatsangehoerigkeitService staatsService;
    @Autowired
    PassResourceAssembler assembler;
    @Autowired
    WohnungResourceAssembler wohnungAssembler;
    @Autowired
    StaatsangehoerigkeitResourceAssembler staatAssembler;
    @Value("${URL}")
    private String URL;

    /**
     * Alle Pass suchen.
     *
     * @return
     */
    @RolesAllowed({"PERM_queryPass"})
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
    @RolesAllowed({"PERM_queryPass"})
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
    @RolesAllowed({"PERM_newPass"})
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
    @RolesAllowed({"PERM_copyPass"})
    @RequestMapping(value = "/copy/{oid}", method = {RequestMethod.GET})
    public ResponseEntity copyPass(@PathVariable String oid) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("copy pass");
        }
        Pass entity = this.service.copy(oid);
        PassResource resource = this.assembler.toResource(entity, HateoasUtil.REL_SELF, HateoasUtil.REL_NEW, HateoasUtil.REL_DELETE, HateoasUtil.REL_UPDATE, HateoasUtil.REL_COPY);
        return ResponseEntity.ok(resource);
    }

    /**
     * Liest einen Pass zur OID.
     *
     * @param oid
     * @return
     */
    @RolesAllowed({"PERM_readPass"})
    @RequestMapping(value = "/{oid}", method = {RequestMethod.GET})
    public ResponseEntity readPass(@PathVariable("oid") String oid) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("read pass");
        }
        Pass entity = this.service.read(oid);
        PassResource resource = this.assembler.toResource(entity, HateoasUtil.REL_SELF, HateoasUtil.REL_NEW, HateoasUtil.REL_DELETE, HateoasUtil.REL_UPDATE, HateoasUtil.REL_COPY);
        return ResponseEntity.ok(resource);
    }

    /**
     * Speichert Änderungen an einem bereits vorhandenen Pass.
     *
     * @param oid
     * @param request
     * @return
     */
    @RolesAllowed({"PERM_updatePass"})
    @RequestMapping(value = "/{oid}", method = {RequestMethod.POST})
    public ResponseEntity updatePass(@PathVariable("oid") String oid, @RequestBody PassResource request) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("update pass");
        }
        Pass entity = service.read(request.getOid());
        LOG.info("davor > " + entity.toString());
        this.assembler.fromResource(request, entity);
        LOG.info("danach > " + entity.toString());
        this.service.update(entity);
        PassResource resource = this.assembler.toResource(entity, HateoasUtil.REL_SELF, HateoasUtil.REL_NEW, HateoasUtil.REL_DELETE, HateoasUtil.REL_UPDATE, HateoasUtil.REL_COPY);
        return ResponseEntity.ok(resource);
    }

    /**
     * Speichert einen neuen Pass.
     *
     * @param request
     * @return
     */
    @RolesAllowed({"PERM_savePass"})
    @RequestMapping(value = "/save", method = {RequestMethod.POST})
    public ResponseEntity savePass(@RequestBody PassResource request) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("save pass");
        }
        Pass entity = new Pass();
        this.assembler.fromResource(request, entity);
        this.service.save(entity);
        PassResource resource = this.assembler.toResource(entity, HateoasUtil.REL_SELF, HateoasUtil.REL_NEW, HateoasUtil.REL_DELETE, HateoasUtil.REL_UPDATE, HateoasUtil.REL_COPY);
        return ResponseEntity.ok(resource);
    }

    /**
     * Löscht einen Pass.
     *
     * @param oid
     * @return
     */
    @RolesAllowed({"PERM_deletePass"})
    @RequestMapping(value = "/{oid}", method = {RequestMethod.DELETE})
    public ResponseEntity deletePass(@PathVariable("oid") String oid) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("delete pass");
        }
        this.service.delete(oid);
        return ResponseEntity.ok().build();
    }

    /**
     * Assoziiert ein Staatsangehoerigkeit mit einem Pass .
     *
     * @param passOid
     * @param statsOid
     * @return
     */
    @RolesAllowed({"PERM_addStaatangehoerigkeitPass"})
    @RequestMapping(value = "add/pass/{pOid}/staats/{sOid}", method = {RequestMethod.GET})
    public ResponseEntity addStaatangehoerigkeitPass(@PathVariable("pOid") String passOid, @PathVariable("sOid") String statsOid) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Add staatsangehoerigkeit pass");
        }

        StaatsangehoerigkeitReference staatRef = staatsService.readReference(statsOid);
        Staatsangehoerigkeit staat = staatsService.read(statsOid);

        Pass entity = service.read(passOid);
        entity.setStaatsangehoerigkeit(staat);
        entity.setStaatsangehoerigkeitReference(staatRef);
        this.service.update(entity);

        PassResource resource = this.assembler.toResource(entity, HateoasUtil.REL_SELF, HateoasUtil.REL_NEW, HateoasUtil.REL_DELETE, HateoasUtil.REL_UPDATE, HateoasUtil.REL_COPY);
        return ResponseEntity.ok(resource);
    }

    /**
     * Liest die Staatsangehoerigkeiten einer Bürger.
     *
     * @param oid
     * @return
     */
    @RolesAllowed({"PERM_readPassStaatsangehoerigkeiten"})
    @RequestMapping(value = "/staat/{oid}", method = {RequestMethod.GET})
    @SuppressWarnings("empty-statement")
    public ResponseEntity readPassStaatsangehoerigkeiten(@PathVariable("oid") String oid) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("read Pass Staatsangehoerigkeiten");
        }
        Pass a = this.service.read(oid);
        Staatsangehoerigkeit staat = this.staatsService.read(this.service.read(oid).getStaatsangehoerigkeitReference().getReferencedOid());

        StaatsangehoerigkeitResource resources;
        resources = staatAssembler.toResource(staat, HateoasUtil.REL_SELF);
        return ResponseEntity.ok(resources);
    }

}
