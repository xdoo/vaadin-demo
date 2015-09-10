package de.muenchen.demo.service.rest;

import com.google.common.collect.Lists;
import de.muenchen.demo.service.domain.Adresse;
import de.muenchen.demo.service.domain.Buerger;
import de.muenchen.demo.service.domain.Wohnung;
import de.muenchen.demo.service.rest.api.AdresseResourceAssembler;
import de.muenchen.vaadin.demo.api.rest.BuergerResource;
import de.muenchen.demo.service.rest.api.BuergerResourceAssembler;
import de.muenchen.vaadin.demo.apilib.rest.SearchResultResource;
import de.muenchen.vaadin.demo.api.rest.WohnungResource;
import de.muenchen.demo.service.rest.api.WohnungResourceAssembler;
import de.muenchen.demo.service.services.AdresseService;
import de.muenchen.demo.service.services.BuergerService;
import de.muenchen.demo.service.services.MandantService;
import de.muenchen.demo.service.services.WohnungService;
import de.muenchen.vaadin.demo.apilib.hateoas.HateoasUtil;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.ExposesResourceFor;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
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
    @Autowired
    AdresseResourceAssembler adresseAssembler;
    @Autowired
    BuergerService buergerService;
    @Autowired
    BuergerResourceAssembler buergerAssembler;

    /**
     * Alle Wohnungen suchen.
     *
     * @return
     */
    @Secured({"PERM_queryWohnung"})
    @RequestMapping(value = "/query", method = {RequestMethod.GET})
    public ResponseEntity queryWohnung() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("query wohnung");
        }
        SearchResultResource<WohnungResource> resource;
        resource = this.assembler.toResource(this.service.query());
        resource.add(linkTo(methodOn(WohnungController.class).queryWohnung()).withSelfRel()); // add self link
        return ResponseEntity.ok(resource);
    }

    /**
     * Erzeugt ein neue Wohnung Objekt mit gefüllter OID. Das Objekt ist noch in
     * der DB gespeichert.
     *
     * @return
     */
    @Secured({"PERM_newWohnung"})
    @RequestMapping(value = "/new", method = {RequestMethod.GET})
    public ResponseEntity newWohnung() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("new wohnung");
        }
        Wohnung entity = this.service.create();
        WohnungResource resource = this.assembler.toResource(entity, HateoasUtil.REL_NEW, HateoasUtil.REL_SAVE);
        return ResponseEntity.ok(resource);
    }

    /**
     * Macht eine Kopie einer Wohnung. Diese Kopie wird bei Erstellung in der DB
     * gespeichert.
     *
     * @param oid
     * @return
     */
    @Secured({"PERM_copyWohnung"})
    @RequestMapping(value = "/copy/{oid}", method = {RequestMethod.GET})
    public ResponseEntity copyWohnung(@PathVariable String oid) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("copy wohnung");
        }
        Wohnung entity = this.service.copy(oid);
        WohnungResource resource = this.assembler.assembleWithAllLinks(entity);
        return ResponseEntity.ok(resource);
    }

    /**
     * Liest eine Wohnung zur OID.
     *
     * @param oid
     * @return
     */
    @Secured({"PERM_readWohnung"})
    @RequestMapping(value = "/{oid}", method = {RequestMethod.GET})
    public ResponseEntity readWohnung(@PathVariable("oid") String oid) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("read wohnung");
        }
        Wohnung entity = this.service.read(oid);
        WohnungResource resource = this.assembler.assembleWithAllLinks(entity);
        return ResponseEntity.ok(resource);
    }

    /**
     * Speichert Änderungen an einer bereits vorhandenen Wohnung.
     *
     * @param oid
     * @param request
     * @return
     */
    @Secured({"PERM_updateWohnung"})
    @RequestMapping(value = "/{oid}", method = {RequestMethod.POST})
    public ResponseEntity updateWohnung(@PathVariable("oid") String oid, @RequestBody WohnungResource request) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("update wohnung");
        }
        Wohnung entity = service.read(request.getOid());
        Adresse adresse = adresseService.read(entity.getAdresse().getOid());

        LOG.info("davor > " + entity.toString());
        this.assembler.fromResource(request, entity, adresse);
        LOG.info("danach > " + entity.toString());
        this.service.update(entity, adresse);
        WohnungResource resource = this.assembler.assembleWithAllLinks(entity);
        return ResponseEntity.ok(resource);
    }

    /**
     * Speichert eine neue Wohnung.
     *
     * @param request
     * @return
     */
    @Secured({"PERM_saveWohnung"})
    @RequestMapping(value = "/save", method = {RequestMethod.POST})
    public ResponseEntity saveWohnung(@RequestBody WohnungResource request) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("save wohnung");
        }
        Wohnung entity = new Wohnung();
        Adresse adresse = new Adresse();
        this.assembler.fromResource(request, entity, adresse);
        this.service.save(entity, adresse);

        WohnungResource resource = this.assembler.assembleWithAllLinks(entity);
        return ResponseEntity.ok(resource);
    }

    /**
     * Löscht eine Wohnung.
     *
     * @param oid
     * @return
     */
    @Secured({"PERM_deleteWohnung"})
    @RequestMapping(value = "/{oid}", method = {RequestMethod.DELETE})
    public ResponseEntity deleteWohnung(@PathVariable("oid") String oid) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("delete wohnung");
        }
        this.service.delete(oid);
        return ResponseEntity.ok().build();
    }

    /**
     * Liest die Buerger einer Wohnung.
     *
     * @param wohnungOid
     * @return
     */
    @Secured({"PERM_readWohnungBuerger"})
    @RequestMapping(value = "/buerger/{wohnungOid}", method = {RequestMethod.GET})
    public ResponseEntity readWohnungBuerger(@PathVariable("wohnungOid") String wohnungOid) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("read Wohnung Buerger");
        }
        Iterable<Buerger> buerger = this.buergerService.readWohnungBuerger(wohnungOid);
        SearchResultResource<BuergerResource> resource = this.buergerAssembler.toResource(Lists.newArrayList(buerger));
        resource.add(linkTo(methodOn(BuergerController.class).readBuergerKinder(wohnungOid)).withSelfRel());
        return ResponseEntity.ok(resource);
    }

    /**
     * Entfernt die Beziehung zwischen eine Wohnung und Alle Buerger.
     *
     * @param wohnungOid
     * @return
     */
    @Secured({"PERM_releaseWohnungAllBuerger"})
    @RequestMapping(value = "/release/buerger/{wohnungOid}", method = {RequestMethod.GET})
    public ResponseEntity releaseWohnungAllBuerger(@PathVariable("wohnungOid") String wohnungOid) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("delete Wohnung Buerger");
        }
        this.buergerService.releaseWohnungAllBuerger(wohnungOid);
        return ResponseEntity.ok().build();

    }

    /**
     * Löscht mehrere Wohnungen.
     *
     * @param oids
     * @return
     */
    //@Secured({"PERM_deleteListWohnung"})
    @RequestMapping(method = {RequestMethod.POST})
    public ResponseEntity deleteListWohnung(@RequestBody ArrayList<String> oids) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("delete Wohnung");
        }
        this.service.delete(oids);
        return ResponseEntity.ok().build();
    }

    /**
     * Macht eine Kopie mehrere Wohnungnen. Diese Kopie wird bei Erstellung in
     * der DB gespeichert.
     *
     * @param oids
     * @return
     */
    //@Secured({"PERM_copyListWohnung"})
    @RequestMapping(value = "/copy", method = {RequestMethod.POST})
    public ResponseEntity copyListWohnung(@RequestBody ArrayList<String> oids) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("copy Wohnung");
        }
        this.service.copy(oids);
        return ResponseEntity.ok().build();
    }

}
