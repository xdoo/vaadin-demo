package de.muenchen.demo.service.rest;

import de.muenchen.demo.service.domain.Mandant;
import de.muenchen.demo.service.rest.api.MandantResource;
import de.muenchen.demo.service.rest.api.MandantResourceAssembler;
import de.muenchen.vaadin.demo.api.rest.SearchResultResource;
import de.muenchen.demo.service.rest.api.WohnungResourceAssembler;
import de.muenchen.demo.service.services.MandantService;
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
 * Schnittstelle um einen Mandant verwalten zu können.
 *
 * @author praktikant.tmar
 */
@Controller
@ExposesResourceFor(MandantController.class)
@RequestMapping("/mandant")
public class MandantController {

    private static final Logger LOG = LoggerFactory.getLogger(MandantController.class);

    @Autowired
    EntityLinks entityLinks;
    @Autowired
    MandantService service;
    @Autowired
    WohnungService wohnungService;
    @Autowired
    StaatsangehoerigkeitService staatsService;
    @Autowired
    MandantResourceAssembler assembler;
    @Autowired
    WohnungResourceAssembler wohnungAssembler;
    @Value("${URL}")
    private String URL;

    /**
     * Alle Mandant suchen.
     *
     * @return
     */
    @RolesAllowed({"PERM_queryMandant"})
    @RequestMapping(value = "/query", method = {RequestMethod.GET})
    public ResponseEntity queryMandant() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("query mandant");
        }
        SearchResultResource<MandantResource> resource = this.assembler.toResource(this.service.query());
        resource.add(linkTo(methodOn(MandantController.class).queryMandant()).withSelfRel()); // add self link
        return ResponseEntity.ok(resource);
    }

    /**
     * Mandant mit Parametern suchen.
     *
     * @param filter
     * @return
     */
    @RolesAllowed({"PERM_queryMandant"})
    @RequestMapping(value = "/query", params = {"filter"})
    public ResponseEntity queryMandant(@RequestParam(value = "filter") String filter) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("query mandant with filter > " + filter);
        }
        SearchResultResource<MandantResource> resource = this.assembler.toResource(this.service.query(filter));
        resource.add(linkTo(methodOn(MandantController.class).queryMandant(filter)).withSelfRel()); // add self link with params
        return ResponseEntity.ok(resource);
    }

    /**
     * Erzeugt ein neues Mandant Objekt mit gefüllter OID. Das Objekt ist noch in
     * der DB gespeichert.
     *
     * @return
     */
    @RolesAllowed({"PERM_newMandant"})
    @RequestMapping(value = "/new", method = {RequestMethod.GET})
    public ResponseEntity newMandant() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("new mandant");
        }
        System.out.println(URL);
        Mandant entity = this.service.create();
        MandantResource resource = this.assembler.toResource(entity, HateoasUtil.REL_NEW, HateoasUtil.REL_SAVE);
        return ResponseEntity.ok(resource);
    }

    /**
     * Macht eine Kopie eines Mandant. Diese Kopie wird bei Erstellung in der
     * DB gespeichert.
     *
     * @param oid
     * @return
     */
    @RolesAllowed({"PERM_copyMandant"})
    @RequestMapping(value = "/copy/{oid}", method = {RequestMethod.GET})
    public ResponseEntity copyMandant(@PathVariable String oid) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("copy mandant");
        }
        Mandant entity = this.service.copy(oid);
        MandantResource resource = this.assembler.toResource(entity, HateoasUtil.REL_SELF, HateoasUtil.REL_NEW, HateoasUtil.REL_DELETE, HateoasUtil.REL_UPDATE, HateoasUtil.REL_COPY);
        return ResponseEntity.ok(resource);
    }

    /**
     * Liest einen Mandant zur OID.
     *
     * @param oid
     * @return
     */
    @RolesAllowed({"PERM_readMandant"})
    @RequestMapping(value = "/{oid}", method = {RequestMethod.GET})
    public ResponseEntity readMandant(@PathVariable("oid") String oid) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("read mandant");
        }
        Mandant entity = this.service.read(oid);
        MandantResource resource = this.assembler.toResource(entity, HateoasUtil.REL_SELF, HateoasUtil.REL_NEW, HateoasUtil.REL_DELETE, HateoasUtil.REL_UPDATE, HateoasUtil.REL_COPY);
        return ResponseEntity.ok(resource);
    }

    /**
     * Speichert Änderungen an einem bereits vorhandenen Mandant.
     *
     * @param oid
     * @param request
     * @return
     */
    @RolesAllowed({"PERM_updateMandant"})
    @RequestMapping(value = "/{oid}", method = {RequestMethod.POST})
    public ResponseEntity updateMandant(@PathVariable("oid") String oid, @RequestBody MandantResource request) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("update mandant");
        }
        Mandant entity = service.read(request.getOid());
        LOG.info("davor > " + entity.toString());
        this.assembler.fromResource(request, entity);
        LOG.info("danach > " + entity.toString());
        this.service.update(entity);
        MandantResource resource = this.assembler.toResource(entity, HateoasUtil.REL_SELF, HateoasUtil.REL_NEW, HateoasUtil.REL_DELETE, HateoasUtil.REL_UPDATE, HateoasUtil.REL_COPY);
        return ResponseEntity.ok(resource);
    }

    /**
     * Speichert einen neuen Mandant.
     *
     * @param request
     * @return
     */
    @RolesAllowed({"PERM_saveMandant"})
    @RequestMapping(value = "/save", method = {RequestMethod.POST})
    public ResponseEntity saveMandant(@RequestBody MandantResource request) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("save mandant");
        }
        Mandant entity = new Mandant();
        this.assembler.fromResource(request, entity);
        this.service.save(entity);
        MandantResource resource = this.assembler.toResource(entity, HateoasUtil.REL_SELF, HateoasUtil.REL_NEW, HateoasUtil.REL_DELETE, HateoasUtil.REL_UPDATE, HateoasUtil.REL_COPY);
        return ResponseEntity.ok(resource);
    }

    /**
     * Löscht einen Mandant.
     *
     * @param oid
     * @return
     */
    @RolesAllowed({"PERM_deleteMandant"})
    @RequestMapping(value = "/{oid}", method = {RequestMethod.DELETE})
    public ResponseEntity deleteMandant(@PathVariable("oid") String oid) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("delete mandant");
        }
        this.service.delete(oid);
        return ResponseEntity.ok().build();
    }
    

}
