package de.muenchen.demo.service.rest;

import de.muenchen.demo.service.domain.Adresse;
import de.muenchen.vaadin.demo.api.rest.SearchResultResource;
import de.muenchen.demo.service.rest.api.AdresseResource;
import de.muenchen.demo.service.rest.api.AdresseResourceAssembler;
import de.muenchen.demo.service.services.AdresseService;
import de.muenchen.vaadin.demo.api.hateoas.HateoasUtil;
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
@ExposesResourceFor(AdresseController.class)
@RequestMapping("/adresse")
public class AdresseController {

    private static final Logger LOG = LoggerFactory.getLogger(AdresseController.class);

    @Autowired
    EntityLinks entityLinks;
    @Autowired
    AdresseService service;
    @Autowired
    AdresseResourceAssembler assembler;

    /**
     * Alle Adresseen suchen.
     *
     * @return
     */
    @RolesAllowed({"PERM_queryAdresse"})
    @RequestMapping(value = "/query", method = {RequestMethod.GET})
    public ResponseEntity queryAdresse() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("query adresse");
        }
        SearchResultResource<AdresseResource> resource;
        resource = this.assembler.toResource(this.service.query());
        resource.add(linkTo(methodOn(BuergerController.class).queryBuerger()).withSelfRel()); // add self link
        return ResponseEntity.ok(resource);
    }

    /**
     * Erzeugt ein neue Adresse Objekt mit gefüllter OID. Das Objekt ist noch in
     * der DB gespeichert.
     *
     * @return
     */
    @RolesAllowed({"PERM_newAdresse"})
    @RequestMapping(value = "/new", method = {RequestMethod.GET})
    public ResponseEntity newAdresse() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("new adresse");
        }
        Adresse entity = this.service.create();
        AdresseResource resource = this.assembler.toResource(entity, HateoasUtil.REL_NEW, HateoasUtil.REL_SAVE);
        return ResponseEntity.ok(resource);
    }

    /**
     * Macht eine Kopie einer Adresse. Diese Kopie wird bei Erstellung in der DB
     * gespeichert.
     *
     * @param oid
     * @return
     */
    @RolesAllowed({"PERM_copyAdresse"})
    @RequestMapping(value = "/copy/{oid}", method = {RequestMethod.GET})
    public ResponseEntity copyAdresse(@PathVariable String oid) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("copy adresse");
        }
        Adresse entity = this.service.copy(oid);
        AdresseResource resource = this.assembler.toResource(entity, HateoasUtil.REL_SELF, HateoasUtil.REL_NEW, HateoasUtil.REL_DELETE, HateoasUtil.REL_UPDATE, HateoasUtil.REL_COPY);
        return ResponseEntity.ok(resource);
    }

    /**
     * Liest eine Adresse zur OID.
     *
     * @param oid
     * @return
     */
    @RolesAllowed({"PERM_readAdresse"})
    @RequestMapping(value = "/{oid}", method = {RequestMethod.GET})
    public ResponseEntity readAdresse(@PathVariable("oid") String oid) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("read adresse");
        }
        Adresse entity = this.service.read(oid);
        AdresseResource resource = this.assembler.toResource(entity, HateoasUtil.REL_SELF, HateoasUtil.REL_NEW, HateoasUtil.REL_DELETE, HateoasUtil.REL_UPDATE, HateoasUtil.REL_COPY);
        return ResponseEntity.ok(resource);
    }

    /**
     * Speichert Änderungen an einer bereits vorhandenen Adresse.
     *
     * @param oid
     * @param request
     * @return
     */
    @RolesAllowed({"PERM_updateAdresse"})
    @RequestMapping(value = "/{oid}", method = {RequestMethod.POST})
    public ResponseEntity updateAdresse(@PathVariable("oid") String oid, @RequestBody AdresseResource request) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("update adresse");
        }
        Adresse entity = service.read(request.getOid());
        LOG.info("davor > " + entity.toString());
        this.assembler.fromResource(request, entity);
        LOG.info("danach > " + entity.toString());
        this.service.update(entity);
        AdresseResource resource = this.assembler.toResource(entity, HateoasUtil.REL_SELF, HateoasUtil.REL_NEW, HateoasUtil.REL_DELETE, HateoasUtil.REL_UPDATE, HateoasUtil.REL_COPY);
        return ResponseEntity.ok(resource);
    }

    /**
     * Speichert eine neue Adresse.
     *
     * @param request
     * @return
     */
    @RolesAllowed({"PERM_saveAdresse"})
    @RequestMapping(value = "/save", method = {RequestMethod.POST})
    public ResponseEntity saveAdresse(@RequestBody AdresseResource request) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("save adresse");
        }
        Adresse entity = new Adresse();
        this.assembler.fromResource(request, entity);

        this.service.save(entity);
        AdresseResource resource = this.assembler.toResource(entity, HateoasUtil.REL_SELF, HateoasUtil.REL_NEW, HateoasUtil.REL_DELETE, HateoasUtil.REL_UPDATE, HateoasUtil.REL_COPY);
        return ResponseEntity.ok(resource);

    }

    /**
     * Löscht eine Adresse.
     *
     * @param oid
     * @return
     */
    @RolesAllowed({"PERM_deleteAdresse"})
    @RequestMapping(value = "/{oid}", method = {RequestMethod.DELETE})
    public ResponseEntity deleteAdresse(@PathVariable("oid") String oid) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("delete adresse");
        }
        this.service.delete(oid);
        return ResponseEntity.ok().build();
    }
}
