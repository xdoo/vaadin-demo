package de.muenchen.demo.service.rest;

import de.muenchen.demo.service.domain.Authority;
import de.muenchen.demo.service.rest.api.SearchResultResource;
import de.muenchen.demo.service.rest.api.AuthorityResource;
import de.muenchen.demo.service.rest.api.AuthorityResourceAssembler;
import de.muenchen.demo.service.services.AuthorityService;
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
@ExposesResourceFor(AuthorityController.class)
@RequestMapping("/authority")
public class AuthorityController {

    private static final Logger LOG = LoggerFactory.getLogger(AuthorityController.class);

    @Autowired
    EntityLinks entityLinks;
    @Autowired
    AuthorityService service;
    @Autowired
    AuthorityResourceAssembler assembler;



    /**
     * Alle Authority suchen.
     *
     * @return
     */
    @RolesAllowed({"PERM_queryAuthority"})
    @RequestMapping(value = "/query", method = {RequestMethod.GET})
    public ResponseEntity queryAuthority() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("query authoritys");
        }
        SearchResultResource<AuthorityResource> resource;
        resource = this.assembler.toResource(this.service.query());
        resource.add(linkTo(methodOn(AuthorityController.class).queryAuthority()).withSelfRel()); // add self link
        return ResponseEntity.ok(resource);
    }

    /**
     * Erzeugt ein neue Authority Objekt mit gefüllter OID. Das Objekt ist noch in
     * der DB gespeichert.
     *
     * @return
     */
    @RolesAllowed({"PERM_newAuthority"})
    @RequestMapping(value = "/new", method = {RequestMethod.GET})
    public ResponseEntity newAuthority() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("new authoritys");
        }
        Authority entity = this.service.create();
        AuthorityResource resource = this.assembler.toResource(entity, HateoasUtil.REL_NEW, HateoasUtil.REL_SAVE);
        return ResponseEntity.ok(resource);
    }


    /**
     * Liest eine Authority zur OID.
     *
     * @param oid
     * @return
     */
    @RolesAllowed({"PERM_readAuthority"})
    @RequestMapping(value = "/{oid}", method = {RequestMethod.GET})
    public ResponseEntity readAuthority(@PathVariable("oid") String oid) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("read authoritys");
        }
        Authority entity = this.service.read(oid);
        AuthorityResource resource = this.assembler.toResource(entity, HateoasUtil.REL_SELF, HateoasUtil.REL_NEW, HateoasUtil.REL_DELETE, HateoasUtil.REL_UPDATE, HateoasUtil.REL_COPY);
        return ResponseEntity.ok(resource);
    }

    /**
     * Speichert Änderungen an einer bereits vorhandenen Authority.
     *
     * @param oid
     * @param request
     * @return
     */
        @RolesAllowed({"PERM_updateAuthority"})
    @RequestMapping(value = "/{oid}", method = {RequestMethod.POST})
    public ResponseEntity updateAuthority(@PathVariable("oid") String oid, @RequestBody AuthorityResource request) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("update authoritys");
        }
        Authority entity = service.read(request.getOid());
        LOG.info("davor > " + entity.toString());
        this.assembler.fromResource(request, entity);
        LOG.info("danach > " + entity.toString());
        this.service.update(entity);
        AuthorityResource resource = this.assembler.toResource(entity, HateoasUtil.REL_SELF, HateoasUtil.REL_NEW, HateoasUtil.REL_DELETE, HateoasUtil.REL_UPDATE, HateoasUtil.REL_COPY);
        return ResponseEntity.ok(resource);
    }

    /**
     * Speichert eine neue Authority.
     *
     * @param request
     * @return
     */
        @RolesAllowed({"PERM_saveAuthority"})
    @RequestMapping(value = "/save", method = {RequestMethod.POST})
    public ResponseEntity saveAuthority(@RequestBody AuthorityResource request) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("save authoritys");
        }
        Authority entity = new Authority();
        this.assembler.fromResource(request, entity);
        this.service.save(entity);
        AuthorityResource resource = this.assembler.toResource(entity, HateoasUtil.REL_SELF, HateoasUtil.REL_NEW, HateoasUtil.REL_DELETE, HateoasUtil.REL_UPDATE, HateoasUtil.REL_COPY);
        return ResponseEntity.ok(resource);
    }

    /**
     * Löscht eine Authority.
     *
     * @param oid
     * @return
     */
    @RolesAllowed({"PERM_deleteAuthority"})
    @RequestMapping(value = "/{oid}", method = {RequestMethod.DELETE})
    public ResponseEntity deleteAuthority(@PathVariable("oid") String oid) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("delete authoritys");
        }
        this.service.delete(oid);
        return ResponseEntity.ok().build();
    }
    /**
     * Macht eine Kopie eines Büergers. Diese Kopie wird bei Erstellung in der
     * DB gespeichert.
     *
     * @param oid
     * @return
     */
    @RolesAllowed({"PERM_copyAuthority"})
    @RequestMapping(value = "/copy/{oid}", method = {RequestMethod.GET})
    public ResponseEntity copyAuthority(@PathVariable String oid) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("copy buerger");
        }
        Authority entity = this.service.copy(oid);
        AuthorityResource resource = this.assembler.toResource(entity, HateoasUtil.REL_SELF, HateoasUtil.REL_NEW, HateoasUtil.REL_DELETE, HateoasUtil.REL_UPDATE, HateoasUtil.REL_COPY);
        return ResponseEntity.ok(resource);
    }
}
