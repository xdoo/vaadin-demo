package de.muenchen.demo.service.rest;

import de.muenchen.demo.service.domain.AuthPermId;
import de.muenchen.demo.service.domain.Authority;
import de.muenchen.demo.service.domain.AuthorityPermission;
import de.muenchen.demo.service.domain.Permission;
import de.muenchen.vaadin.demo.api.rest.SearchResultResource;
import de.muenchen.demo.service.rest.api.AuthorityPermissionResource;
import de.muenchen.demo.service.rest.api.AuthorityPermissionResourceAssembler;
import de.muenchen.demo.service.services.AuthorityService;
import de.muenchen.demo.service.services.AuthorityPermissionService;
import de.muenchen.demo.service.services.PermissionService;
import de.muenchen.demo.service.services.UserService;
import de.muenchen.vaadin.demo.api.hateoas.HateoasUtil;
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

/**
 *
 * @author praktikant.tmar
 */
@Controller
@ExposesResourceFor(AuthorityPermissionController.class)
@RequestMapping("/authorityPermission")
public class AuthorityPermissionController {

    private static final Logger LOG = LoggerFactory.getLogger(AuthorityPermissionController.class);

    @Autowired
    EntityLinks entityLinks;
    @Autowired
    AuthorityPermissionService service;
    @Autowired
    AuthorityPermissionResourceAssembler assembler;
    @Autowired
    UserService serviceUser;
    @Autowired
    AuthorityService authService;
    @Autowired
    PermissionService servicePermission;

    /**
     * Alle AuthorityPermission suchen.
     *
     * @return
     */
    @Secured({"PERM_queryAuthorityPermission"})
    @RequestMapping(value = "/query", method = {RequestMethod.GET})
    public ResponseEntity queryAuthorityPermission() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("query authoritysPermissions");
        }
        SearchResultResource<AuthorityPermissionResource> resource;
        resource = this.assembler.toResource(this.service.query());
        resource.add(linkTo(methodOn(AuthorityPermissionController.class).queryAuthorityPermission()).withSelfRel()); // add self link
        return ResponseEntity.ok(resource);
    }

    /**
     * AuthorityPermission by Authority suchen.
     *
     * @param authority
     * @return
     */
    @Secured({"PERM_readByAuthorityAuthorityPermission"})
    @RequestMapping(value = "/authority/{authority}", method = {RequestMethod.GET})
    public ResponseEntity readByAuthorityAuthorityPermission(@PathVariable("authority") String authority) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("query authoritysPermissions");
        }
        SearchResultResource<AuthorityPermissionResource> resource;
        resource = this.assembler.toResource(this.service.readByAuthority(authority));
        resource.add(linkTo(methodOn(AuthorityPermissionController.class).queryAuthorityPermission()).withSelfRel()); // add self link
        return ResponseEntity.ok(resource);
    }

    /**
     * Liest eine AuthorityPermission zur OID.
     *
     * @param poid
     * @param aoid
     * @return
     */
    @Secured({"PERM_readAuthorityPermission"})
    @RequestMapping(value = "/{poid}/{aoid}", method = {RequestMethod.GET})
    public ResponseEntity readAuthorityPermission(@PathVariable("poid") String poid, @PathVariable("aoid") String aoid) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("read authoritysPermissions");
        }

        Permission permission = this.servicePermission.read(poid);
        Authority auth = this.authService.read(aoid);
        AuthPermId id = new AuthPermId(permission, auth);

        AuthorityPermission entity = this.service.read(id);
        AuthorityPermissionResource resource = this.assembler.toResource(entity, HateoasUtil.REL_SELF, HateoasUtil.REL_NEW, HateoasUtil.REL_DELETE, HateoasUtil.REL_UPDATE);
        return ResponseEntity.ok(resource);
    }

    /**
     * Speichert eine neue AuthorityPermission.
     *
     * @param poid
     * @param aoid
     * @return
     */
    @Secured({"PERM_saveAuthorityPermission"})
    @RequestMapping(value = "/save/{poid}/{aoid}", method = {RequestMethod.GET})
    public ResponseEntity saveAuthorityPermission(@PathVariable("poid") String poid, @PathVariable("aoid") String aoid) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("save authoritysPermissions");
        }
        AuthorityPermission entity = new AuthorityPermission();
        Permission permission = this.servicePermission.read(poid);
        Authority auth = this.authService.read(aoid);
        entity.setId(new AuthPermId(permission, auth));
        this.service.save(entity);
        AuthorityPermissionResource resource = this.assembler.toResource(entity, HateoasUtil.REL_SELF, HateoasUtil.REL_NEW, HateoasUtil.REL_DELETE, HateoasUtil.REL_UPDATE);
        return ResponseEntity.ok(resource);
    }

    /**
     * Löscht eine AuthorityPermission.
     *
     * @param poid
     * @param aoid
     * @return
     */
    @Secured({"PERM_deleteAuthorityPermission"})
    @RequestMapping(value = "/{poid}/{aoid}", method = {RequestMethod.DELETE})
    public ResponseEntity deleteAuthorityPermission(@PathVariable("poid") String poid, @PathVariable("aoid") String aoid) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("delete authoritysPermissions");
        }

        Permission permission = this.servicePermission.read(poid);
        Authority auth = this.authService.read(aoid);
        AuthPermId id = new AuthPermId(permission, auth);
        this.service.delete(id);
        return ResponseEntity.ok().build();
    }
}
