package de.muenchen.demo.service.rest;

import de.muenchen.demo.service.domain.Buerger;
import de.muenchen.demo.service.domain.Sachbearbeiter;
import de.muenchen.demo.service.domain.User;
import de.muenchen.demo.service.rest.api.BuergerResourceAssembler;
import de.muenchen.demo.service.rest.api.UserResourceAssembler;
import de.muenchen.demo.service.rest.api.SachbearbeiterResource;
import de.muenchen.vaadin.demo.api.rest.SearchResultResource;
import de.muenchen.demo.service.rest.api.SachbearbeiterResourceAssembler;
import de.muenchen.demo.service.rest.api.UserResource;
import de.muenchen.demo.service.services.BuergerService;
import de.muenchen.demo.service.services.UserService;
import de.muenchen.demo.service.services.MandantService;
import de.muenchen.demo.service.services.SachbearbeiterService;
import de.muenchen.vaadin.demo.api.hateoas.HateoasUtil;
import de.muenchen.vaadin.demo.api.rest.BuergerResource;
import java.util.List;
import java.util.Set;
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
@ExposesResourceFor(SachbearbeiterController.class)
@RequestMapping("/sachbearbeiter")
public class SachbearbeiterController {

    private static final Logger LOG = LoggerFactory.getLogger(SachbearbeiterController.class);

    @Autowired
    EntityLinks entityLinks;
    @Autowired
    SachbearbeiterService service;
    @Autowired
    SachbearbeiterResourceAssembler assembler;
    @Autowired
    UserService userService;
    @Autowired
    MandantService madantService;
    @Autowired
    UserResourceAssembler userAssembler;
    @Autowired
    BuergerService buergerService;
    @Autowired
    BuergerResourceAssembler buergerAssembler;

    /**
     * Alle Sachbearbeiteren suchen.
     *
     * @return
     */
    @RolesAllowed({"PERM_querySachbearbeiter"})
    @RequestMapping(value = "/query", method = {RequestMethod.GET})
    public ResponseEntity querySachbearbeiter() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("query sachbearbeiter");
        }
        SearchResultResource<SachbearbeiterResource> resource;
        resource = this.assembler.toResource(this.service.query());
        resource.add(linkTo(methodOn(SachbearbeiterController.class).querySachbearbeiter()).withSelfRel()); // add self link
        return ResponseEntity.ok(resource);
    }

    /**
     * Erzeugt ein neue Sachbearbeiter Objekt mit gefüllter OID. Das Objekt ist
     * noch in der DB gespeichert.
     *
     * @return
     */
    @RolesAllowed({"PERM_newSachbearbeiter"})
    @RequestMapping(value = "/new", method = {RequestMethod.GET})
    public ResponseEntity newSachbearbeiter() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("new sachbearbeiter");
        }
        Sachbearbeiter entity = this.service.create();
        SachbearbeiterResource resource = this.assembler.toResource(entity, HateoasUtil.REL_NEW, HateoasUtil.REL_SAVE);
        return ResponseEntity.ok(resource);
    }

    /**
     * Macht eine Kopie einer Sachbearbeiter. Diese Kopie wird bei Erstellung in
     * der DB gespeichert.
     *
     * @param oid
     * @return
     */
    @RolesAllowed({"PERM_copySachbearbeiter"})
    @RequestMapping(value = "/copy/{oid}", method = {RequestMethod.GET})
    public ResponseEntity copySachbearbeiter(@PathVariable String oid) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("copy sachbearbeiter");
        }
        Sachbearbeiter entity = this.service.copy(oid);
        SachbearbeiterResource resource = this.assembler.toResource(entity, HateoasUtil.REL_SELF, HateoasUtil.REL_NEW, HateoasUtil.REL_DELETE, HateoasUtil.REL_UPDATE, HateoasUtil.REL_COPY);
        return ResponseEntity.ok(resource);
    }

    /**
     * Liest eine Sachbearbeiter zur OID.
     *
     * @param oid
     * @return
     */
    @RolesAllowed({"PERM_readSachbearbeiter"})
    @RequestMapping(value = "/{oid}", method = {RequestMethod.GET})
    public ResponseEntity readSachbearbeiter(@PathVariable("oid") String oid) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("read sachbearbeiter");
        }
        Sachbearbeiter entity = this.service.read(oid);
        SachbearbeiterResource resource = this.assembler.assembleWithAllLinks(entity);
        return ResponseEntity.ok(resource);
    }

    /**
     * Speichert Änderungen an einer bereits vorhandenen Sachbearbeiter.
     *
     * @param oid
     * @param request
     * @return
     */
    @RolesAllowed({"PERM_updateSachbearbeiter"})
    @RequestMapping(value = "/{oid}", method = {RequestMethod.POST})
    public ResponseEntity updateSachbearbeiter(@PathVariable("oid") String oid, @RequestBody SachbearbeiterResource request) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("update sachbearbeiter");
        }
        Sachbearbeiter entity = service.read(request.getOid());
        LOG.info("davor > " + entity.toString());
        this.assembler.fromResource(request, entity);
        LOG.info("danach > " + entity.toString());
        this.service.update(entity);
        SachbearbeiterResource resource = this.assembler.assembleWithAllLinks(entity);
        return ResponseEntity.ok(resource);
    }

    /**
     * Speichert eine neue Sachbearbeiter.
     *
     * @param request
     * @return
     */
    @RolesAllowed({"PERM_saveSachbearbeiter"})
    @RequestMapping(value = "/save", method = {RequestMethod.POST})
    public ResponseEntity saveSachbearbeiter(@RequestBody SachbearbeiterResource request) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("save sachbearbeiter");
        }
        Sachbearbeiter entity = new Sachbearbeiter();
        this.assembler.fromResource(request, entity);
        this.service.save(entity);
        SachbearbeiterResource resource = this.assembler.assembleWithAllLinks(entity);
        return ResponseEntity.ok(resource);
    }

    /**
     * Löscht eine Sachbearbeiter.
     *
     * @param oid
     * @return
     */
    @RolesAllowed({"PERM_deleteSachbearbeiter"})
    @RequestMapping(value = "/{oid}", method = {RequestMethod.DELETE})
    public ResponseEntity deleteSachbearbeiter(@PathVariable("oid") String oid) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("delete sachbearbeiter");
        }
        this.service.delete(oid);
        return ResponseEntity.ok().build();
    }

    /**
     * Liest die User eine Sachbearbeiter.
     *
     * @param oid
     * @return
     */
    @RolesAllowed({"PERM_readSachbearbeiterUser"})
    @RequestMapping(value = "/user/{oid}", method = {RequestMethod.GET})
    public ResponseEntity readSachbearbeiterUser(@PathVariable("oid") String oid) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("read Wohunung User");
        }
        User user = userService.read(this.service.read(oid).getUser().getOid());

        UserResource resources = userAssembler.toResource(user, HateoasUtil.REL_SELF, HateoasUtil.REL_NEW, HateoasUtil.REL_DELETE, HateoasUtil.REL_UPDATE, HateoasUtil.REL_COPY);
        return ResponseEntity.ok(resources);
    }

    /**
     * Assoziiert ein User mit einem Sachbearbeiter .
     *
     * @param sachOid
     * @param userOid
     * @return
     */
    @RolesAllowed({"PERM_addUserSachbearbeiter"})
    @RequestMapping(value = "add/sachbearbeiter/{sOid}/user/{uOid}", method = {RequestMethod.GET})
    public ResponseEntity addUserSachbearbeiter(@PathVariable("sOid") String sachOid, @PathVariable("uOid") String userOid) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Add User to sachbearbeiter");
        }

        User user = userService.read(userOid);
        Sachbearbeiter entity = service.read(sachOid);

        entity.setUser(user);
        this.service.update(entity);

        SachbearbeiterResource resource = this.assembler.assembleWithAllLinks(entity);
        return ResponseEntity.ok(resource);
    }

    /**
     * Erzeugt ein User und assoziiert ihm mit einem Sachbearbeiter .
     *
     * @param oid
     * @param request
     * @return
     */
    @RolesAllowed({"PERM_createUserSachbearbeiter"})
    @RequestMapping(value = "/create/user/{oid}", method = {RequestMethod.POST})
    public ResponseEntity createUserSachbearbeiter(@PathVariable("oid") String oid, @RequestBody UserResource request) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Create user sachbearbeiter");
        }
        User user = new User();
        this.userAssembler.fromResource(request, user);
        this.userService.save(user);
        Sachbearbeiter entity = service.read(oid);
        entity.setUser(user);
        this.service.update(entity);

        SachbearbeiterResource resource = this.assembler.assembleWithAllLinks(entity);
        return ResponseEntity.ok(resource);
    }

    /**
     * Assoziiert eine Buerger mit einem Sachbearbeiter .
     *
     * @param sachbearbeiterOid
     * @param buergerOid
     * @return
     */
    @RolesAllowed({"PERM_addBuergerSachbearbeiter"})
    @RequestMapping(value = "/add/sachbearbeiter/{bOid}/buerger/{wOid}", method = {RequestMethod.GET})
    public ResponseEntity addBuergerSachbearbeiter(@PathVariable("bOid") String sachbearbeiterOid, @PathVariable("wOid") String buergerOid) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Add buerger sachbearbeiter");
        }

        Buerger buerger = buergerService.read(buergerOid);
        Sachbearbeiter sachbearbeiter = service.read(sachbearbeiterOid);
        buerger.getSachbearbeiter().add(sachbearbeiter);
        sachbearbeiter.getBuerger().add(buerger);
        this.service.update(sachbearbeiter);
        this.buergerService.update(buerger);

        SachbearbeiterResource resource = this.assembler.assembleWithAllLinks(sachbearbeiter);
        resource.add(linkTo(methodOn(SachbearbeiterController.class).addBuergerSachbearbeiter(sachbearbeiterOid, buergerOid)).withSelfRel()); // add self link with params
        return ResponseEntity.ok(resource);
    }

    /**
     * Erzeugt eine Buerger und assoziiert ihm mit einem Sachbearbeiter .
     *
     * @param oid
     * @param request
     * @return
     */
    @RolesAllowed({"PERM_createBuergerSachbearbeiter"})
    @RequestMapping(value = "/create/buerger/{oid}", method = {RequestMethod.POST})
    public ResponseEntity createBuergerSachbearbeiter(@PathVariable("oid") String oid, @RequestBody BuergerResource request) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Create buerger sachbearbeiter");
        }
        Sachbearbeiter sachbearbeiter = service.read(oid);
        Buerger buerger = new Buerger();
        this.buergerAssembler.fromResource(request, buerger);
        buerger.getSachbearbeiter().add(sachbearbeiter);
        this.buergerService.save(buerger);
        sachbearbeiter.getBuerger().add(buerger);
        this.service.update(sachbearbeiter);

        SachbearbeiterResource resource = this.assembler.assembleWithAllLinks(sachbearbeiter);
        return ResponseEntity.ok(resource);
    }

    /**
     * Liest die Buerger einer Sachbearbeiter.
     *
     * @param oid
     * @return
     */
    @RolesAllowed({"PERM_readSachbearbeiterBuerger"})
    @RequestMapping(value = "/buerger/{oid}", method = {RequestMethod.GET})
    public ResponseEntity readSachbearbeiterBuerger(@PathVariable("oid") String oid) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("read sachbearbeiter Buerger");
        }
        Set<Buerger> buerger = this.service.read(oid).getBuerger();

        List<BuergerResource> resources;
        resources = buergerAssembler.toResource(buerger, HateoasUtil.REL_SELF);
        return ResponseEntity.ok(resources);
    }

    @RolesAllowed({"PERM_releaseSachbearbeiterBuerger"})
    @RequestMapping(value = "release/sachbearbeiter/{sOid}/buerger/{bOid}", method = {RequestMethod.GET})
    public ResponseEntity releaseSachbearbeiterBuerger(@PathVariable("sOid") String sachOid, @PathVariable("bOid") String buergerOid) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("release sachbearbeiter Buerger");
        }
        this.service.releaseSachbearbeiterBuerger(buergerOid, sachOid);
        return ResponseEntity.ok().build();
    }

    @RolesAllowed({"PERM_releaseSachbearbeiterAllBuerger"})
    @RequestMapping(value = "release/buerger/{sOid}", method = {RequestMethod.GET})
    public ResponseEntity releaseSachbearbeiterAllBuerger(@PathVariable("sOid") String sachOid) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("release sachbearbeiter All Buerger");
        }
        this.service.releaseSachbearbeiterAllBuerger(sachOid);
        return ResponseEntity.ok().build();
    }

    @RolesAllowed({"PERM_releaseSachbearbeiterUser"})
    @RequestMapping(value = "release/user/{sOid}/", method = {RequestMethod.GET})
    public ResponseEntity releaseSachbearbeiterUser(@PathVariable("sOid") String sachOid) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("release sachbearbeiter user");
        }
        this.service.releaseSachbearbeiterUser(sachOid);
        return ResponseEntity.ok().build();
    }
}
