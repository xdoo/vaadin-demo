package de.muenchen.demo.service.rest;

import com.google.common.collect.Lists;
import de.muenchen.demo.service.domain.Buerger;
import de.muenchen.demo.service.domain.Pass;
import de.muenchen.demo.service.domain.Sachbearbeiter;
import de.muenchen.demo.service.domain.Staatsangehoerigkeit;
import de.muenchen.demo.service.domain.StaatsangehoerigkeitReference;
import de.muenchen.demo.service.domain.Wohnung;
import de.muenchen.vaadin.demo.api.rest.BuergerResource;
import de.muenchen.demo.service.rest.api.BuergerResourceAssembler;
import de.muenchen.demo.service.rest.api.PassResource;
import de.muenchen.demo.service.rest.api.PassResourceAssembler;
import de.muenchen.demo.service.rest.api.SachbearbeiterResource;
import de.muenchen.demo.service.rest.api.SachbearbeiterResourceAssembler;
import de.muenchen.vaadin.demo.api.rest.SearchResultResource;
import de.muenchen.vaadin.demo.api.rest.StaatsangehoerigkeitResource;
import de.muenchen.demo.service.rest.api.StaatsangehoerigkeitResourceAssembler;
import de.muenchen.vaadin.demo.api.rest.WohnungResource;
import de.muenchen.demo.service.rest.api.WohnungResourceAssembler;
import de.muenchen.demo.service.services.BuergerService;
import de.muenchen.demo.service.services.PassService;
import de.muenchen.demo.service.services.SachbearbeiterService;
import de.muenchen.demo.service.services.StaatsangehoerigkeitService;
import de.muenchen.demo.service.services.UserService;
import de.muenchen.demo.service.services.WohnungService;
import de.muenchen.vaadin.demo.api.hateoas.HateoasUtil;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.security.RolesAllowed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;

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
    SachbearbeiterService sachbearbeiterService;
    @Autowired
    PassService passService;
    @Autowired
    StaatsangehoerigkeitService staatsService;
    @Autowired
    BuergerResourceAssembler assembler;
    @Autowired
    WohnungResourceAssembler wohnungAssembler;
    @Autowired
    SachbearbeiterResourceAssembler sachbearbeiterAssembler;
    @Autowired
    PassResourceAssembler passAssembler;
    @Autowired
    UserService userService;
    @Autowired
    StaatsangehoerigkeitResourceAssembler staatsAssembler;

    /**
     * Alle Bürger suchen.
     *
     * @return
     */
    @Secured({"PERM_queryBuerger"})
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
     * @param query
     * @return
     */
    @Secured({"PERM_queryBuerger"})
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
    @Secured({"PERM_newBuerger"})
    @RequestMapping(value = "/new", method = {RequestMethod.GET})
    public ResponseEntity newBuerger() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("new buerger");
        }
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
    @Secured({"PERM_copyBuerger"})
    @RequestMapping(value = "/copy/{oid}", method = {RequestMethod.GET})
    public ResponseEntity copyBuerger(@PathVariable String oid) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("copy buerger");
        }
        Buerger entity = this.service.copy(oid);
        BuergerResource resource = this.assembler.assembleWithAllLinks(entity);
        return ResponseEntity.ok(resource);
    }

    /**
     * Löscht mehrere Bürger.
     *
     * @param oids
     * @return
     */
    @Secured({"PERM_deleteListBuerger"})
    @RequestMapping(value = "/delete", method = {RequestMethod.POST})
    public ResponseEntity deleteListBuerger(@RequestBody ArrayList<String> oids) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("delete buerger");
        }
        this.service.delete(oids);
        return ResponseEntity.ok().build();
    }

    /**
     * Macht eine Kopie mehreres Büergers. Diese Kopie wird bei Erstellung in
     * der DB gespeichert.
     *
     * @param oids
     * @return
     */
    @Secured({"PERM_copyListBuerger"})
    @RequestMapping(value = "/copy", method = {RequestMethod.POST})
    public ResponseEntity copyListBuerger(@RequestBody ArrayList<String> oids) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("copy buerger");
        }
        this.service.copy(oids);
        return ResponseEntity.ok().build();
    }

    /**
     * Liest einen Bürger zur OID.
     *
     * @param oid
     * @return
     */
    @Secured({"PERM_readBuerger"})
    @RequestMapping(value = "/{oid}", method = {RequestMethod.GET})
    public ResponseEntity readBuerger(@PathVariable("oid") String oid) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("read buerger");
        }
        Buerger entity = this.service.read(oid);
        BuergerResource resource = this.assembler.assembleWithAllLinks(entity);
        return ResponseEntity.ok(resource);
    }

    /**
     * Speichert Änderungen an einem bereits vorhandenen Bürger.
     *
     * @param oid
     * @param request
     * @return
     */
    @Secured({"PERM_updateBuerger"})
    @RequestMapping(value = "/{oid}", method = {RequestMethod.POST})
    public ResponseEntity updateBuerger(@PathVariable("oid") String oid, @RequestBody BuergerResource request) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("update buerger");
        }

        if (!request.getOid().equals(oid)) {
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
    @Secured({"PERM_saveBuerger"})
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
    @Secured({"PERM_deleteBuerger"})
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
    @Secured({"PERM_readBuergerWohnungen"})
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
    @Secured({"PERM_readBuergerKinder"})
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
     * Liest die Eltern einer Bürger.
     *
     * @param oid
     * @return
     */
    @Secured({"PERM_readEltern"})
    @RequestMapping(value = "/eltern/{oid}", method = {RequestMethod.GET})
    public ResponseEntity readEltern(@PathVariable("oid") String oid) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("read Eltern");
        }
        Iterable<Buerger> buerger = this.service.readEltern(oid);
        SearchResultResource<BuergerResource> resource = this.assembler.toResource(Lists.newArrayList(buerger));
        resource.add(linkTo(methodOn(BuergerController.class).readBuergerKinder(oid)).withSelfRel());
        return ResponseEntity.ok(resource);
    }

    /**
     * Entfernt die Beziehung zwischen einem Buerger und seinen Eltern.
     *
     * @param oid
     * @return
     */
    @Secured({"PERM_releaseBuergerEltern"})
    @RequestMapping(value = "/release/eltern/{oid}", method = {RequestMethod.GET})
    public ResponseEntity releaseBuergerEltern(@PathVariable("oid") String oid) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("release Buerger Eltern");
        }
        this.service.releaseBuergerEltern(oid);
        return ResponseEntity.ok().build();

    }

    /**
     * Entfernt die Beziehung zwischen einem Buerger und seinen Kinder.
     *
     * @param oid
     * @return
     */
    @Secured({"PERM_releaseBuergerKinder"})
    @RequestMapping(value = "/release/kinder/{oid}", method = {RequestMethod.GET})
    public ResponseEntity releaseBuergerKinder(@PathVariable("oid") String oid) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("release Buerger Kinder");
        }
        this.service.releaseBuergerKinder(oid);
        return ResponseEntity.ok().build();

    }

    /**
     * Entfernt die Beziehung zwischen einem Buerger und seinem Elternteil.
     *
     * @param oid
     * @param elternteilOid
     * @return
     */
    @Secured({"PERM_releaseBuergerElternteil"})
    @RequestMapping(value = "/release/elternteil/{oid}/{elternteilOid}", method = {RequestMethod.GET})
    public ResponseEntity releaseBuergerElternteil(@PathVariable("oid") String oid, @PathVariable("elternteilOid") String elternteilOid) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("release Buerger elternteil");
        }
        this.service.releaseBuergerElternteil(oid, elternteilOid);
        return ResponseEntity.ok().build();

    }

    /**
     * Erzeugt ein Kind als Buerger und assoziiert ihm mit einem Buerger .
     *
     * @param oid
     * @param request
     * @return
     */
    @Secured({"PERM_createKindBuerger"})
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

    @Secured({"PERM_addKindBuerger"})
    @RequestMapping(value = "/add/buerger/{bOid}/kind/{kOid}", method = {RequestMethod.POST})
    public ResponseEntity addKindBuerger(@PathVariable("bOid") String buergerOid, @PathVariable("kOid") String kindOid) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Add Kind buerger");
        }
        LOG.warn("restrequestrecieved");
        Buerger kind = service.read(kindOid);
        Buerger entity = service.read(buergerOid);

        entity.getKinder().add(kind);
        this.service.update(entity);

        BuergerResource resource = this.assembler.assembleWithAllLinks(entity);
        resource.add(linkTo(methodOn(BuergerController.class).addKindBuerger(buergerOid, kindOid)).withSelfRel()); // add self link with params
        return ResponseEntity.ok(resource);
    }
    
    

    /**
     * Assoziiert eine Wohnung mit einem Buerger .
     *
     * @param buergerOid
     * @param wohnungOid
     * @return
     */
    @Secured({"PERM_addWohnungBuerger"})
    @RequestMapping(value = "/add/buerger/{bOid}/wohnung/{wOid}", method = {RequestMethod.GET})
    public ResponseEntity addWohnungBuerger(@PathVariable("bOid") String buergerOid, @PathVariable("wOid") String wohnungOid) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Add wohnung buerger");
        }

        Wohnung wohnung = wohnungService.read(wohnungOid);
        Buerger entity = service.read(buergerOid);

        entity.getWohnungen().add(wohnung);
        this.service.update(entity);

        BuergerResource resource = this.assembler.assembleWithAllLinks(entity);
        resource.add(linkTo(methodOn(BuergerController.class).addWohnungBuerger(buergerOid, wohnungOid)).withSelfRel()); // add self link with params
        return ResponseEntity.ok(resource);
    }

    /**
     * Erzeugt eine Wohnung und assoziiert ihm mit einem Buerger .
     *
     * @param oid
     * @param request
     * @return
     */
    @Secured({"PERM_createWohnungBuerger"})
    @RequestMapping(value = "/create/wohnung/{oid}", method = {RequestMethod.POST})
    public ResponseEntity createWohnungBuerger(@PathVariable("oid") String oid, @RequestBody WohnungResource request) {
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
     * Entfernt die Beziehung zwischen einem Buerger und seinen Wohnungen.
     *
     * @param oid
     * @return
     */
    @Secured({"PERM_releaseBuergerWohnungen"})
    @RequestMapping(value = "/release/wohnungen/{oid}", method = {RequestMethod.GET})
    public ResponseEntity releaseBuergerWohnungen(@PathVariable("oid") String oid) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("release Buerger Wohnungen");
        }
        this.service.releaseBuergerWohnungen(oid);
        return ResponseEntity.ok().build();

    }

    /**
     * Liest die Staatsangehoerigkeiten einer Bürger.
     *
     * @param oid
     * @return
     */
    @Secured({"PERM_readBuergerStaatsangehoerigkeiten"})
    @RequestMapping(value = "/staats/{oid}", method = {RequestMethod.GET})
    @SuppressWarnings("empty-statement")
    public ResponseEntity readBuergerStaatsangehoerigkeiten(@PathVariable("oid") String oid) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("read Buerger Staatsangehoerigkeiten");
        }
        Set<Staatsangehoerigkeit> staats = new HashSet<>();
        Buerger buerger = this.service.read(oid);
        buerger.getStaatsangehoerigkeitReferences();
        this.service.read(oid).getStaatsangehoerigkeitReferences().stream().forEach((staat) -> {
            staats.add(staatsService.read(staat.getReferencedOid()));
        });
        List<StaatsangehoerigkeitResource> resources = staatsAssembler.toResource(staats, HateoasUtil.REL_SELF);
        return ResponseEntity.ok(resources);
    }

    /**
     * Assoziiert ein Staatsangehoerigkeit mit einem Buerger .
     *
     * @param buergerOid
     * @param staatsOid
     * @return
     */
    @Secured({"PERM_addStaatangehoerigkeitBuerger"})
    @RequestMapping(value = "/add/buerger/{bOid}/staats/{sOid}", method = {RequestMethod.GET})
    public ResponseEntity addStaatangehoerigkeitBuerger(@PathVariable("bOid") String buergerOid, @PathVariable("sOid") String staatsOid) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Add staatsangehoerigkeit buerger");
        }

        Staatsangehoerigkeit staats = staatsService.read(staatsOid);
        Buerger entity = service.read(buergerOid);

        entity.getStaatsangehoerigkeiten().add(staats);
        StaatsangehoerigkeitReference staatRef = staatsService.readReference(staatsOid);
        entity.getStaatsangehoerigkeitReferences().add(staatRef);
        this.service.update(entity);

        BuergerResource resource = this.assembler.assembleWithAllLinks(entity);
        resource.add(linkTo(methodOn(BuergerController.class).addStaatangehoerigkeitBuerger(buergerOid, staatsOid)).withSelfRel()); // add self link with params
        return ResponseEntity.ok(resource);
    }

    /**
     * Assoziiert ein Pass mit einem Buerger .
     *
     * @param buergerOid
     * @param passOid
     * @return
     */
    @Secured({"PERM_addPassBuerger"})
    @RequestMapping(value = "/add/buerger/{bOid}/pass/{pOid}", method = {RequestMethod.GET})
    public ResponseEntity addPassBuerger(@PathVariable("bOid") String buergerOid, @PathVariable("pOid") String passOid) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Add Pass buerger");
        }

        Pass pass = passService.read(passOid);
        Buerger entity = service.read(buergerOid);

        entity.getPass().add(pass);
        this.service.update(entity);

        BuergerResource resource = this.assembler.assembleWithAllLinks(entity);
        resource.add(linkTo(methodOn(BuergerController.class).addPassBuerger(buergerOid, passOid)).withSelfRel()); // add self link with params
        return ResponseEntity.ok(resource);
    }

    /**
     * Erzeugt eine Pass und assoziiert ihm mit einem Buerger .
     *
     * @param oid
     * @param request
     * @return
     */
    @Secured({"PERM_createPassBuerger"})
    @RequestMapping(value = "/create/pass/{oid}", method = {RequestMethod.POST})
    public ResponseEntity createPassBuerger(@PathVariable("oid") String oid, @RequestBody PassResource request) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Create pass buerger");
        }
        Pass pass = new Pass();
        this.passAssembler.fromResource(request, pass);
        this.passService.save(pass);
        Buerger entity = service.read(oid);
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
    @Secured({"PERM_readBuergerPass"})
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
     * Entfernt die Beziehung zwischen einem Buerger und seinen Pässe.
     *
     * @param oid
     * @return
     */
    @Secured({"PERM_releaseBuergerPaesse"})
    @RequestMapping(value = "/release/paesse/{oid}", method = {RequestMethod.GET})
    public ResponseEntity releaseBuergerPaesse(@PathVariable("oid") String oid) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("release Buerger Paesse");
        }
        this.service.releasePassBuerger(oid);
        return ResponseEntity.ok().build();

    }

    /**
     * Liest die Wohnunugen einer Bürger.
     *
     * @param oid
     * @return
     */
    @Secured({"PERM_readBuergerSachbearbeiter"})
    @RequestMapping(value = "/sachbearbeiter/{oid}", method = {RequestMethod.GET})
    public ResponseEntity readBuergerSachbearbeiter(@PathVariable("oid") String oid) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("read buerger Sachbearbeiter");
        }
        Set<Sachbearbeiter> Sachbearbeiter = this.service.read(oid).getSachbearbeiter();

        List<SachbearbeiterResource> resources = sachbearbeiterAssembler.toResource(Sachbearbeiter, HateoasUtil.REL_SELF);
        return ResponseEntity.ok(resources);
    }

    /**
     * Assoziiert eine Sachbearbeiter mit einem Buerger .
     *
     * @param buergerOid
     * @param sachbearbeiterOid
     * @return
     */
    @Secured({"PERM_addSachbearbeiterBuerger"})
    @RequestMapping(value = "/add/buerger/{bOid}/Sachbearbeiter/{sOid}", method = {RequestMethod.GET})
    public ResponseEntity addSachbearbeiterBuerger(@PathVariable("bOid") String buergerOid, @PathVariable("sOid") String sachbearbeiterOid) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Add Sachbearbeiter buerger");
        }

        Sachbearbeiter sachbearbeiter = sachbearbeiterService.read(sachbearbeiterOid);
        Buerger buerger = service.read(buergerOid);

        buerger.getSachbearbeiter().add(sachbearbeiter);
        sachbearbeiter.getBuerger().add(buerger);
        this.sachbearbeiterService.update(sachbearbeiter);
        this.service.update(buerger);

        BuergerResource resource = this.assembler.assembleWithAllLinks(buerger);
        resource.add(linkTo(methodOn(BuergerController.class).addSachbearbeiterBuerger(buergerOid, sachbearbeiterOid)).withSelfRel()); // add self link with params
        return ResponseEntity.ok(resource);
    }

    /**
     * Erzeugt eine Sachbearbeiter und assoziiert ihm mit einem Buerger .
     *
     * @param oid
     * @param request
     * @return
     */
    @Secured({"PERM_createSachbearbeiterBuerger"})
    @RequestMapping(value = "/create/sachbearbeiter/{oid}", method = {RequestMethod.POST})
    public ResponseEntity createSachbearbeiterBuerger(@PathVariable("oid") String oid, @RequestBody SachbearbeiterResource request) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Create Sachbearbeiter buerger");
        }
        Buerger buerger = service.read(oid);
        Sachbearbeiter sachbearbeiter = new Sachbearbeiter();
        this.sachbearbeiterAssembler.fromResource(request, sachbearbeiter);
        sachbearbeiter.getBuerger().add(buerger);
        this.sachbearbeiterService.save(sachbearbeiter);
        buerger.getSachbearbeiter().add(sachbearbeiter);
        this.service.update(buerger);

        BuergerResource resource = this.assembler.assembleWithAllLinks(buerger);
        return ResponseEntity.ok(resource);
    }

    /**
     * Entfernt die Beziehung zwischen einem Buerger und seinen
     * Sachbearbeiteren.
     *
     * @param oid
     * @return
     */
    @Secured({"PERM_releaseBuergerAllSachbearbeiter"})
    @RequestMapping(value = "/release/sachbearbeiter/{oid}", method = {RequestMethod.GET})
    public ResponseEntity releaseBuergerAllSachbearbeiter(@PathVariable("oid") String oid) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("release Buerger Sachbearbeiter");
        }
        this.service.releaseBuergerAllSachbearbeiter(oid);
        return ResponseEntity.ok().build();

    }
}
