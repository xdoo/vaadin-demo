package de.muenchen.demo.service.rest;

import de.muenchen.demo.service.domain.Mandant;
import de.muenchen.demo.service.domain.User;
import de.muenchen.demo.service.rest.api.SearchResultResource;
import de.muenchen.demo.service.rest.api.UserResource;
import de.muenchen.demo.service.rest.api.UserResourceAssembler;
import de.muenchen.demo.service.services.MandantService;
import de.muenchen.demo.service.services.UserService;
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
@ExposesResourceFor(UserController.class)
@RequestMapping("/user")
public class UserController {

    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

    @Autowired
    EntityLinks entityLinks;
    @Autowired
    UserService service;
    @Autowired
    UserResourceAssembler assembler;
    @Autowired
    MandantService madantService;



    /**
     * Alle User suchen.
     *
     * @return
     */
    @RolesAllowed({"PERM_queryUser"})
    @RequestMapping(value = "/query", method = {RequestMethod.GET})
    public ResponseEntity queryUser() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("query users");
        }
        SearchResultResource<UserResource> resource;
        resource = this.assembler.toResource(this.service.query());
        resource.add(linkTo(methodOn(UserController.class).queryUser()).withSelfRel()); // add self link
        return ResponseEntity.ok(resource);
    }

    /**
     * Erzeugt ein neue User Objekt mit gefüllter OID. Das Objekt ist noch in
     * der DB gespeichert.
     *
     * @return
     */
@RolesAllowed({"PERM_newUser"})
@RequestMapping(value = "/new", method = {RequestMethod.GET})
    public ResponseEntity newUser() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("new user");
        }
        User entity = this.service.create();
        UserResource resource = this.assembler.toResource(entity, HateoasRelations.NEW, HateoasRelations.SAVE);
        return ResponseEntity.ok(resource);
    }


    /**
     * Liest eine User zur OID.
     *
     * @param oid
     * @return
     */
    @RolesAllowed({"PERM_readUser"})
    @RequestMapping(value = "/{oid}", method = {RequestMethod.GET})
    public ResponseEntity readUser(@PathVariable("oid") String oid) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("read users");
        }
        User entity = this.service.read(oid);
        UserResource resource = this.assembler.toResource(entity, HateoasRelations.SELF, HateoasRelations.NEW, HateoasRelations.DELETE, HateoasRelations.UPDATE);
        return ResponseEntity.ok(resource);
    }
    
    /**
     * Liest eine User zur OID.
     *
     * @param name
     * @return
     */
    @RolesAllowed({"PERM_readUsername"})
    @RequestMapping(value = "/name/{name}", method = {RequestMethod.GET})
    public ResponseEntity readUsername(@PathVariable("name") String name) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("read users");
        }
        User entity = this.service.readByUsername(name);
        UserResource resource = this.assembler.toResource(entity, HateoasRelations.SELF, HateoasRelations.NEW, HateoasRelations.DELETE, HateoasRelations.UPDATE);
        return ResponseEntity.ok(resource);
    }

    /**
     * Speichert Änderungen an einer bereits vorhandenen User.
     *
     * @param oid
     * @param request
     * @return
     */
    @RolesAllowed({"PERM_updateUser"})
    @RequestMapping(value = "/{oid}", method = {RequestMethod.POST})
    public ResponseEntity updateUser(@PathVariable("oid") String oid, @RequestBody UserResource request) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("update users");
        }
        User entity = service.read(request.getOid());
        LOG.info("davor > " + entity.toString());
        this.assembler.fromResource(request, entity);
        LOG.info("danach > " + entity.toString());
        this.service.update(entity);
        UserResource resource = this.assembler.toResource(entity, HateoasRelations.SELF, HateoasRelations.NEW, HateoasRelations.DELETE, HateoasRelations.UPDATE);
        return ResponseEntity.ok(resource);
    }

    /**
     * Speichert eine neue User.
     *
     * @param request
     * @return
     */
    @RolesAllowed({"PERM_saveUser"})
    @RequestMapping(value = "/save", method = {RequestMethod.POST})
    public ResponseEntity saveUser(@RequestBody UserResource request) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("save users");
        }
        User entity = new User();
        this.assembler.fromResource(request, entity);
        this.service.save(entity);
        UserResource resource = this.assembler.toResource(entity, HateoasRelations.SELF, HateoasRelations.NEW, HateoasRelations.DELETE, HateoasRelations.UPDATE);
        return ResponseEntity.ok(resource);
    }

    /**
     * Löscht eine User.
     *
     * @param oid
     * @return
     */
    @RolesAllowed({"PERM_deleteUser"})
    @RequestMapping(value = "/{oid}", method = {RequestMethod.DELETE})
    public ResponseEntity deleteUser(@PathVariable("oid") String oid) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("delete users");
        }
        this.service.delete(oid);
        return ResponseEntity.ok().build();
    }
    /**
     * Assoziiert ein Staatsangehoerigkeit mit einem Buerger .
     *
     * @param buergerOid
     * @param staatsOid
     * @return
     */
    @RolesAllowed({"PERM_addMandantUser"})
    @RequestMapping(value = "add/user/{uOid}/mandant/{mOid}", method = {RequestMethod.GET})
    public ResponseEntity addMandantUser(@PathVariable("uOid") String userOid, @PathVariable("mOid") String mandantOid) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Add staatsangehoerigkeit buerger");
        }

        Mandant mandant = madantService.read(mandantOid);
        User entity = service.read(userOid);

        entity.setMandant(mandant);
        this.service.update(entity);

        UserResource resource = this.assembler.toResource(entity, HateoasRelations.SELF, HateoasRelations.NEW, HateoasRelations.DELETE, HateoasRelations.UPDATE, HateoasRelations.COPY, HateoasRelations.WOHNUNGEN, HateoasRelations.KINDER);
        return ResponseEntity.ok(resource);
    }
}
