package de.muenchen.demo.service.rest;

import com.google.gwt.thirdparty.guava.common.collect.Lists;
import de.muenchen.demo.service.domain.Buerger;
import de.muenchen.demo.service.domain.Pass;
import de.muenchen.demo.service.domain.Staatsangehoerigkeit;
import de.muenchen.demo.service.domain.User;
import de.muenchen.demo.service.domain.Wohnung;
import de.muenchen.demo.service.rest.api.BuergerResource;
import de.muenchen.demo.service.rest.api.BuergerResourceAssembler;
import de.muenchen.demo.service.rest.api.PassResource;
import de.muenchen.demo.service.rest.api.PassResourceAssembler;
import de.muenchen.demo.service.rest.api.SearchResultResource;
import de.muenchen.demo.service.rest.api.WohnungResource;
import de.muenchen.demo.service.rest.api.WohnungResourceAssembler;
import de.muenchen.demo.service.services.BuergerService;
import de.muenchen.demo.service.services.PassService;
import de.muenchen.demo.service.services.StaatsangehoerigkeitService;
import de.muenchen.demo.service.services.UserService;
import de.muenchen.demo.service.services.WohnungService;
import de.muenchen.vaadin.demo.api.hateoas.HateoasUtil;
import java.util.List;
import java.util.Set;
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
import org.springframework.http.HttpStatus;
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

    @Autowired
    EntityLinks entityLinks;
    @Autowired
    BuergerService service;
    @Autowired
    WohnungService wohnungService;
    @Autowired
    PassService passService;
    @Autowired
    StaatsangehoerigkeitService staatsService;
    @Autowired
    BuergerResourceAssembler assembler;
    @Autowired
    WohnungResourceAssembler wohnungAssembler;
    @Autowired
    PassResourceAssembler passAssembler;
    @Value("${URL}")
    private String URL;
    @Autowired
    UserService  userService;

    /**
     * Alle Bürger suchen.
     *
     * @return
     */
    @RolesAllowed({"PERM_queryBuerger"})
    @RequestMapping(value = "/query", method = {RequestMethod.GET})
    public ResponseEntity queryBuerger() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("query buerger");
        }
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
    @RolesAllowed({"PERM_queryBuerger"})
    @RequestMapping(value = "/query", method = {RequestMethod.POST})
    public ResponseEntity queryBuerger(@RequestBody String query) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("query buerger with query > " + query);
        }
        List<Buerger> result = this.service.query(query);
        SearchResultResource<BuergerResource> resource = this.assembler.toResource(result);
        resource.add(linkTo(methodOn(BuergerController.class).queryBuerger(query)).withSelfRel()); // add self link with params
        return ResponseEntity.ok(resource);
    }

    /**
     * Erzeugt ein neues Bürger Objekt mit gefüllter OID. Das Objekt ist noch in
     * der DB gespeichert.
     *
     * @return
     */
    @RolesAllowed({"PERM_newBuerger"})
    @RequestMapping(value = "/new", method = {RequestMethod.GET})
    public ResponseEntity newBuerger() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("new buerger");
        }
        System.out.println(URL);
        Buerger entity = this.service.create();
        BuergerResource resource = this.assembler.toResource(entity, HateoasUtil.REL_NEW, HateoasUtil.REL_SAVE);
        return ResponseEntity.ok(resource);
    }

    /**
     * Macht eine Kopie eines Büergers. Diese Kopie wird bei Erstellung in der
     * DB gespeichert.
     *
     * @param oid
     * @return
     */
    @RolesAllowed({"PERM_copyBuerger"})
    @RequestMapping(value = "/copy/{oid}", method = {RequestMethod.GET})
    public ResponseEntity copyBuerger(@PathVariable String oid) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("copy buerger");
        }
        Buerger entity = this.service.copy(oid);
        BuergerResource resource = this.assembler.toResource(entity, HateoasUtil.REL_SELF, HateoasUtil.REL_NEW, HateoasUtil.REL_DELETE, HateoasUtil.REL_UPDATE, HateoasUtil.REL_COPY);
        return ResponseEntity.ok(resource);
    }

    /**
     * Liest einen Bürger zur OID.
     *
     * @param oid
     * @return
     */
    @RolesAllowed({"PERM_readBuerger"})
    @RequestMapping(value = "/{oid}", method = {RequestMethod.GET})
    public ResponseEntity readBuerger(@PathVariable("oid") String oid) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("read buerger");
        }
        Buerger entity = this.service.read(oid);
        BuergerResource resource = this.assembler.toResource(entity, HateoasUtil.REL_SELF, HateoasUtil.REL_NEW, HateoasUtil.REL_DELETE, HateoasUtil.REL_UPDATE, HateoasUtil.REL_COPY);
        return ResponseEntity.ok(resource);
    }

    /**
     * Speichert Änderungen an einem bereits vorhandenen Bürger.
     *
     * @param oid
     * @param request
     * @return
     */
    @RolesAllowed({"PERM_updateBuerger"})
    @RequestMapping(value = "/{oid}", method = {RequestMethod.POST})
    public ResponseEntity updateBuerger(@PathVariable("oid") String oid, @RequestBody BuergerResource request) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("update buerger");
        }
        
        if(!request.getOid().equals(oid)) {
            LOG.warn(String.format("Provided two different oids: '%s' != '%s'", oid, request.getOid()));
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        
        Buerger entity = service.read(request.getOid());
        if (entity != null) {
            this.assembler.fromResource(request, entity);
            this.service.update(entity);
            BuergerResource resource = this.assembler.assembleWithAllLinks(entity);
            return ResponseEntity.ok(resource);
        } else {
            LOG.warn(String.format("Could not find resource with id '%s'.", oid));
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Speichert einen neuen Bürger.
     *
     * @param request
     * @return
     */
    @RolesAllowed({"PERM_saveBuerger"})
    @RequestMapping(value = "/save", method = {RequestMethod.POST})
    public ResponseEntity saveBuerger(@RequestBody BuergerResource request) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("save buerger");
        }
        Buerger entity = new Buerger();
        this.assembler.fromResource(request, entity);
        this.service.save(entity);
        BuergerResource resource = this.assembler.assembleWithAllLinks(entity);
        return ResponseEntity.ok(resource);
    }

    /**
     * Löscht einen Bürger.
     *
     * @param oid
     * @return
     */
    @RolesAllowed({"PERM_deleteBuerger"})
    @RequestMapping(value = "/{oid}", method = {RequestMethod.DELETE})
    public ResponseEntity deleteBuerger(@PathVariable("oid") String oid) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("delete buerger");
        }
        this.service.delete(oid);
        return ResponseEntity.ok().build();
    }

    /**
     * Liest die Wohnunugen einer Bürger.
     *
     * @param oid
     * @return
     */
    @RolesAllowed({"PERM_readBuergerWohnungen"})
    @RequestMapping(value = "/wohnungen/{oid}", method = {RequestMethod.GET})
    public ResponseEntity readBuergerWohnungen(@PathVariable("oid") String oid) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("read buerger Wohunungen");
        }
        Set<Wohnung> wohnungen = this.service.read(oid).getWohnungen();

        List<WohnungResource> resources = wohnungAssembler.toResource(wohnungen, HateoasUtil.REL_SELF);
        return ResponseEntity.ok(resources);
    }

    /**
     * Liest die Kinder einer Bürger.
     *
     * @param oid
     * @return
     */
    @RolesAllowed({"PERM_readBuergerKinder"})
    @RequestMapping(value = "/kinder/{oid}", method = {RequestMethod.GET})
    public ResponseEntity readBuergerKinder(@PathVariable("oid") String oid) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("read buerger  Kinder");
        }
        Set<Buerger> kinder = this.service.read(oid).getKinder();
        SearchResultResource<BuergerResource> resource = this.assembler.toResource(Lists.newArrayList(kinder));
        resource.add(linkTo(methodOn(BuergerController.class).readBuergerKinder(oid)).withSelfRel());
        return ResponseEntity.ok(resource);
    }

    /**
     * Erzeugt ein Kind als Buerger und assoziiert ihm mit einem Buerger .
     *
     * @param oid
     * @param request
     * @return
     */
    @RolesAllowed({"PERM_createKindBuerger"})
    @RequestMapping(value = "/create/kind/{oid}", method = {RequestMethod.POST})
    public ResponseEntity createKindBuerger(@PathVariable("oid") String oid, @RequestBody BuergerResource request) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Create Kind buerger");
        }
        Buerger kind = new Buerger();
        this.assembler.fromResource(request, kind);
        this.service.save(kind);
        Buerger entity = service.read(oid);
        entity.getKinder().add(kind);
        this.service.update(entity);

        BuergerResource resource = this.assembler.assembleWithAllLinks(entity);
        return ResponseEntity.ok(resource);
    }

    /**
     * Assoziiert ein Kind mit einem Buerger .
     *
     * @param buergerOid
     * @param kindOid
     * @return
     */
    @RolesAllowed({"PERM_addKindBuerger"})
    @RequestMapping(value = "add/buerger/{bOid}/kind/{kOid}", method = {RequestMethod.GET})
    public ResponseEntity addKindBuerger(@PathVariable("bOid") String buergerOid, @PathVariable("kOid") String kindOid) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Add Kind buerger");
        }

        Buerger kind = service.read(kindOid);
        Buerger entity = service.read(buergerOid);

        entity.getKinder().add(kind);
        this.service.update(entity);

        BuergerResource resource = this.assembler.assembleWithAllLinks(entity);
        return ResponseEntity.ok(resource);
    }

    /**
     * Assoziiert eine Wohnung mit einem Buerger .
     *
     * @param buergerOid
     * @param wohnungOid
     * @return
     */
    @RolesAllowed({"PERM_addWohnungBuerger"})
    @RequestMapping(value = "add/buerger/{bOid}/wohnung/{wOid}", method = {RequestMethod.GET})
    public ResponseEntity addWohnungBuerger(@PathVariable("bOid") String buergerOid, @PathVariable("wOid") String wohnungOid) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Add wohnung buerger");
        }

        Wohnung wohnung = wohnungService.read(wohnungOid);
        Buerger entity = service.read(buergerOid);

        entity.getWohnungen().add(wohnung);
        this.service.update(entity);

        BuergerResource resource = this.assembler.assembleWithAllLinks(entity);
        return ResponseEntity.ok(resource);
    }

    /**
     * Erzeugt eine Wohnung und assoziiert ihm mit einem Buerger .
     *
     * @param oid
     * @param request
     * @return
     */
    @RolesAllowed({"PERM_createWhonungBuerger"})
    @RequestMapping(value = "create/wohnung/{oid}", method = {RequestMethod.POST})
    public ResponseEntity createWhonungBuerger(@PathVariable("oid") String oid, @RequestBody WohnungResource request) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Create wohnung buerger");
        }
        Wohnung wohnung = new Wohnung();
        this.wohnungAssembler.fromResource(request, wohnung);
        this.wohnungService.save(wohnung);
        Buerger entity = service.read(oid);
        entity.getWohnungen().add(wohnung);
        this.service.update(entity);

        BuergerResource resource = this.assembler.assembleWithAllLinks(entity);
        return ResponseEntity.ok(resource);
    }

    /**
     * Assoziiert ein Staatsangehoerigkeit mit einem Buerger .
     *
     * @param buergerOid
     * @param staatsOid
     * @return
     */
    @RolesAllowed({"PERM_addStaatangehoerigkeitBuerger"})
    @RequestMapping(value = "add/buerger/{bOid}/staats/{sOid}", method = {RequestMethod.GET})
    public ResponseEntity addStaatangehoerigkeitBuerger(@PathVariable("bOid") String buergerOid, @PathVariable("sOid") String staatsOid) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Add staatsangehoerigkeit buerger");
        }

        Staatsangehoerigkeit staats = staatsService.read(staatsOid);
        Buerger entity = service.read(buergerOid);

        entity.getStaatsangehoerigkeiten().add(staats);
        this.service.update(entity);

        BuergerResource resource = this.assembler.assembleWithAllLinks(entity);
        return ResponseEntity.ok(resource);
    }

    /**
     * Assoziiert ein Pass mit einem Buerger .
     *
     * @param buergerOid
     * @param passOid
     * @return
     */
    @RolesAllowed({"PERM_addPassBuerger"})
    @RequestMapping(value = "add/buerger/{bOid}/pass/{pOid}", method = {RequestMethod.GET})
    public ResponseEntity addPassBuerger(@PathVariable("bOid") String buergerOid, @PathVariable("pOid") String passOid) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Add Pass buerger");
        }

        Pass pass = passService.read(passOid);
        Buerger entity = service.read(buergerOid);

        entity.getPass().add(pass);
        this.service.update(entity);

        BuergerResource resource = this.assembler.assembleWithAllLinks(entity);
        return ResponseEntity.ok(resource);
    }

    /**
     * Liest die Pass einer Bürger.
     *
     * @param oid
     * @return
     */
    @RolesAllowed({"PERM_readBuergerPass"})
    @RequestMapping(value = "/pass/{oid}", method = {RequestMethod.GET})
    public ResponseEntity readBuergerPass(@PathVariable("oid") String oid) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("read buerger Pass");
        }
        Set<Pass> pass = this.service.read(oid).getPass();

        List<PassResource> resources = passAssembler.toResource(pass, HateoasUtil.REL_SELF);
        return ResponseEntity.ok(resources);
    }
    /**
     * Assoziiert ein Sachbearbeiter mit einem Buerger .
     *
     * @param buergerOid
     * @param sachbearbeiterOid
     * @return
     */
    @RolesAllowed({"PERM_addSachbearbeiterBuerger"})
    @RequestMapping(value = "add/buerger/{bOid}/sachbearbeiter/{sOid}", method = {RequestMethod.GET})
    public ResponseEntity addSachbearbeiterBuerger(@PathVariable("bOid") String buergerOid, @PathVariable("sOid") String sachbearbeiterOid) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Add Pass buerger");
        }

        User sachbearbeiter = userService.read(sachbearbeiterOid);
        Buerger entity = service.read(buergerOid);

        entity.setSachbearbeiter(sachbearbeiter);
        this.service.update(entity);

        BuergerResource resource = this.assembler.assembleWithAllLinks(entity);
        return ResponseEntity.ok(resource);
    }
    

}
