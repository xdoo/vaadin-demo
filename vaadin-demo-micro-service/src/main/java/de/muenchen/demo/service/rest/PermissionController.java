package de.muenchen.demo.service.rest;

import de.muenchen.demo.service.domain.Permission;
import de.muenchen.demo.service.rest.api.SearchResultResource;
import de.muenchen.demo.service.rest.api.PermissionResource;
import de.muenchen.demo.service.rest.api.PermissionResourceAssembler;
import de.muenchen.demo.service.services.PermissionService;
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
@ExposesResourceFor(PermissionController.class)
@RequestMapping("/permission")
public class PermissionController {

    private static final Logger LOG = LoggerFactory.getLogger(PermissionController.class);

    @Autowired
    EntityLinks entityLinks;
    @Autowired
    PermissionService service;
    @Autowired
    PermissionResourceAssembler assembler;

    /**
     * Alle Permission suchen.
     *
     * @return
     */
    @RolesAllowed({"PERM_queryPermission"})
    @RequestMapping(value = "/query", method = {RequestMethod.GET})
    public ResponseEntity queryPermission() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("query permission");
        }
        SearchResultResource<PermissionResource> resource;
        resource = this.assembler.toResource(this.service.query());
        resource.add(linkTo(methodOn(PermissionController.class).queryPermission()).withSelfRel()); // add self link
        return ResponseEntity.ok(resource);
    }

    /**
     * Liest eine Permission zur OID.
     *
     * @param oid
     * @return
     */
    @RolesAllowed({"PERM_readPermission"})
    @RequestMapping(value = "/{oid}", method = {RequestMethod.GET})
    public ResponseEntity readPermission(@PathVariable("oid") String oid) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("read permissions");
        }
        Permission entity = this.service.read(oid);
        PermissionResource resource = this.assembler.toResource(entity, HateoasUtil.REL_SELF, HateoasUtil.REL_NEW, HateoasUtil.REL_DELETE, HateoasUtil.REL_UPDATE);
        return ResponseEntity.ok(resource);
    }

    /**
     * Speichert eine neue Permission.
     *
     * @param request
     * @return
     */
    @RolesAllowed({"PERM_savePermission"})
    @RequestMapping(value = "/save", method = {RequestMethod.POST})
    public ResponseEntity savePermission(@RequestBody PermissionResource request) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("save permissions");
        }
        Permission entity = new Permission();
        this.assembler.fromResource(request, entity);
        this.service.save(entity);
        PermissionResource resource = this.assembler.toResource(entity, HateoasUtil.REL_SELF, HateoasUtil.REL_NEW, HateoasUtil.REL_DELETE, HateoasUtil.REL_UPDATE);
        return ResponseEntity.ok(resource);
    }

    /**
     * Löscht eine Permission.
     *
     * @param oid
     * @return
     */
    @RolesAllowed({"PERM_deletePermission"})
    @RequestMapping(value = "/{oid}", method = {RequestMethod.DELETE})
    public ResponseEntity deletePermission(@PathVariable("oid") String oid) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("delete permissions");
        }
        this.service.delete(oid);
        return ResponseEntity.ok().build();
    }

}
