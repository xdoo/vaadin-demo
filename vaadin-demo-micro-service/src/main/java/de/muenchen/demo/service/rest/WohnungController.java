package de.muenchen.demo.service.rest;

import de.muenchen.demo.service.domain.AdresseReference;
import de.muenchen.demo.service.domain.Mandant;
import de.muenchen.demo.service.domain.Wohnung;
import de.muenchen.demo.service.domain.Wohnung;
import de.muenchen.demo.service.rest.api.SearchResultResource;
import de.muenchen.demo.service.rest.api.WohnungResource;
import de.muenchen.demo.service.rest.api.WohnungResource;
import de.muenchen.demo.service.rest.api.WohnungResourceAssembler;
import de.muenchen.demo.service.services.AdresseService;
import de.muenchen.demo.service.services.MandantService;
import de.muenchen.demo.service.services.WohnungService;
import de.muenchen.demo.service.util.HateoasRelations;
import javax.annotation.security.RolesAllowed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.ExposesResourceFor;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestBody;

/**
 *
 * @author praktikant.tmar
 */
@Controller
@ExposesResourceFor(WohnungController.class)
@RequestMapping("/wohnung")
public class WohnungController {

    private static final Logger LOG = LoggerFactory.getLogger(WohnungController.class);

    @Autowired
    EntityLinks entityLinks;
    @Autowired
    WohnungService service;
    @Autowired
    WohnungResourceAssembler assembler;
    @Autowired
    AdresseService adresseService;
    @Autowired
    MandantService madantService;

    /**
     * Alle Wohnungen suchen.
     *
     * @return
     */
    @RolesAllowed({"PERM_queryWohnung"})
    @RequestMapping(value = "/query", method = {RequestMethod.GET})
    public ResponseEntity queryWohnung() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("query wohnung");
        }
        SearchResultResource<WohnungResource> resource;
        resource = this.assembler.toResource(this.service.query());
        resource.add(linkTo(methodOn(BuergerController.class).queryBuerger()).withSelfRel()); // add self link
        return ResponseEntity.ok(resource);
    }

    /**
     * Erzeugt ein neue Wohnung Objekt mit gefüllter OID. Das Objekt ist noch in
     * der DB gespeichert.
     *
     * @return
     */
    @RolesAllowed({"PERM_newWohnung"})
    @RequestMapping(value = "/new", method = {RequestMethod.GET})
    public ResponseEntity newWohnung() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("new wohnung");
        }
        Wohnung entity = this.service.create();
        WohnungResource resource = this.assembler.toResource(entity, HateoasRelations.NEW, HateoasRelations.SAVE);
        return ResponseEntity.ok(resource);
    }

    /**
     * Macht eine Kopie einer Wohnung. Diese Kopie wird bei Erstellung in der DB
     * gespeichert.
     *
     * @param oid
     * @return
     */
    @RolesAllowed({"PERM_copyWohnung"})
    @RequestMapping(value = "/copy/{oid}", method = {RequestMethod.GET})
    public ResponseEntity copyWohnung(@PathVariable String oid) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("copy wohnung");
        }
        Wohnung entity = this.service.copy(oid);
        WohnungResource resource = this.assembler.toResource(entity);
        return ResponseEntity.ok(resource);
    }

    /**
     * Liest eine Wohnung zur OID.
     *
     * @param oid
     * @return
     */
    @RolesAllowed({"PERM_readWohnung"})
    @RequestMapping(value = "/{oid}", method = {RequestMethod.GET})
    public ResponseEntity readWohnung(@PathVariable("oid") String oid) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("read wohnung");
        }
        Wohnung entity = this.service.read(oid);
        WohnungResource resource = this.assembler.toResource(entity, HateoasRelations.SELF, HateoasRelations.NEW, HateoasRelations.DELETE, HateoasRelations.UPDATE, HateoasRelations.COPY);
        return ResponseEntity.ok(resource);
    }

    /**
     * Speichert Änderungen an einer bereits vorhandenen Wohnung.
     *
     * @param oid
     * @param request
     * @return
     */
    @RolesAllowed({"PERM_updateWohnung"})
    @RequestMapping(value = "/{oid}", method = {RequestMethod.POST})
    public ResponseEntity updateWohnung(@PathVariable("oid") String oid, @RequestBody WohnungResource request) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("update wohnung");
        }
        Wohnung entity = service.read(request.getOid());
        LOG.info("davor > " + entity.toString());
        this.assembler.fromResource(request, entity);
        LOG.info("danach > " + entity.toString());
        this.service.update(entity);
        WohnungResource resource = this.assembler.toResource(entity, HateoasRelations.SELF, HateoasRelations.NEW, HateoasRelations.DELETE, HateoasRelations.UPDATE, HateoasRelations.COPY);
        return ResponseEntity.ok(resource);
    }

    /**
     * Speichert eine neue Wohnung.
     *
     * @param request
     * @return
     */
    @RolesAllowed({"PERM_saveWohnung"})
    @RequestMapping(value = "/save", method = {RequestMethod.POST})
    public ResponseEntity saveWohnung(@RequestBody WohnungResource request) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("save wohnung");
        }
        Wohnung entity = new Wohnung();
        this.assembler.fromResource(request, entity);
        this.service.save(entity);
        WohnungResource resource = this.assembler.toResource(entity, HateoasRelations.SELF, HateoasRelations.NEW, HateoasRelations.DELETE, HateoasRelations.UPDATE, HateoasRelations.COPY);
        return ResponseEntity.ok(resource);
    }

    /**
     * Löscht eine Wohnung.
     *
     * @param oid
     * @return
     */
    @RolesAllowed({"PERM_deleteWohnung"})
    @RequestMapping(value = "/{oid}", method = {RequestMethod.DELETE})
    public ResponseEntity deleteWohnung(@PathVariable("oid") String oid) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("delete wohnung");
        }
        this.service.delete(oid);
        return ResponseEntity.ok().build();
    }

    /**
     * Assoziiert eine Adresse mit einer Wonunug .
     *
     * @param wOid
     * @param aOid
     * @return
     */
    @RolesAllowed({"PERM_addAdresseWohnung"})
    @RequestMapping(value = "add/wohnung/{wOid}/adresse/{aOid}", method = {RequestMethod.GET})
    public ResponseEntity addAdresseWohnung(@PathVariable("wOid") String wOid, @PathVariable("aOid") String aOid) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Add Adresse to wohnung");
        }

        AdresseReference adresse = adresseService.readReference(aOid);
        Wohnung entity = service.read(wOid);

        entity.setAdresse(adresse);
        this.service.update(entity);

        WohnungResource resource = this.assembler.toResource(entity, HateoasRelations.SELF, HateoasRelations.NEW, HateoasRelations.DELETE, HateoasRelations.UPDATE, HateoasRelations.COPY, HateoasRelations.WOHNUNGEN, HateoasRelations.KINDER);
        return ResponseEntity.ok(resource);
    }

    /**
     * Assoziiert ein Mandant mit einer Wonunug .
     *
     * @param wohnungOid
     * @param mandantOid
     * @return
     */
    @RolesAllowed({"PERM_addMandantWohnung"})
    @RequestMapping(value = "add/wohnung/{uOid}/mandant/{mOid}", method = {RequestMethod.GET})
    public ResponseEntity addMandantWohnung(@PathVariable("uOid") String wohnungOid, @PathVariable("mOid") String mandantOid) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Add Mandant Wohnung");
        }

        Mandant mandant = madantService.read(mandantOid);
        Wohnung entity = service.read(wohnungOid);

        entity.setMandant(mandant);
        this.service.update(entity);

        WohnungResource resource = this.assembler.toResource(entity, HateoasRelations.SELF, HateoasRelations.NEW, HateoasRelations.DELETE, HateoasRelations.UPDATE, HateoasRelations.COPY);
        return ResponseEntity.ok(resource);
    }
}
