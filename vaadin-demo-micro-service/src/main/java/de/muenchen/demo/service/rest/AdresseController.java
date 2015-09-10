package de.muenchen.demo.service.rest;

import de.muenchen.demo.service.domain.Adresse;
import de.muenchen.vaadin.demo.apilib.rest.SearchResultResource;
import de.muenchen.vaadin.demo.api.rest.AdresseResource;
import de.muenchen.demo.service.rest.api.AdresseResourceAssembler;
import de.muenchen.demo.service.services.AdresseService;
import de.muenchen.vaadin.demo.apilib.hateoas.HateoasUtil;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
    @Secured({"PERM_queryAdresse"})
    @RequestMapping(value = "/query", method = {RequestMethod.GET})
    public ResponseEntity queryAdresse() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("query adresse");
        }
        List<Adresse> result = this.service.query();
        SearchResultResource<AdresseResource> resource = this.assembler.toResource(result);
        resource.add(linkTo(methodOn(AdresseController.class).queryAdresse()).withSelfRel()); // add self link
        return ResponseEntity.ok(resource);
    }

    /**
     * Erzeugt ein neue Adresse Objekt mit gefüllter OID. Das Objekt ist noch in
     * der DB gespeichert.
     *
     * @return
     */
    @Secured({"PERM_newAdresse"})
    @RequestMapping(value = "/new", method = {RequestMethod.GET})
    public ResponseEntity newAdresse() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("new adresse");
        }
        Adresse entity = this.service.create();
        AdresseResource resource = this.assembler.toResource(entity, HateoasUtil.REL_NEW);
        return ResponseEntity.ok(resource);
    }

    /**
     * Liest eine Adresse zur OID.
     *
     * @param oid
     * @return
     */
    @Secured({"PERM_readAdresse"})
    @RequestMapping(value = "/{oid}", method = {RequestMethod.GET})
    public ResponseEntity readAdresse(@PathVariable("oid") String oid) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("read adresse");
        }
        Adresse entity = this.service.read(oid);
        AdresseResource resource = this.assembler.assembleWithAllLinks(entity);
        return ResponseEntity.ok(resource);
    }

    /**
     * Adressen mit Parametern suchen.
     *
     * @param adresse
     * @return
     */
    //@Secured({"PERM_sucheAdresse"})
    @RequestMapping(value = "/suche", method = {RequestMethod.POST})
    public ResponseEntity sucheAdresse(@RequestBody AdresseResource adresse) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("suche Adresse with query > " + adresse.toString());
        }
        Adresse entity = new Adresse();
        this.assembler.fromResource(adresse, entity);
        List<Adresse> result = this.service.suche(entity);
        Set<Adresse> set = new HashSet<>(result);
        List resource = this.assembler.toResource(set, HateoasUtil.REL_NEW);
        return ResponseEntity.ok(resource);
    }

}
