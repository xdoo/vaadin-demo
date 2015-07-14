package de.muenchen.demo.service.rest;

import de.muenchen.demo.service.domain.Authority;
import de.muenchen.demo.service.domain.User;
import de.muenchen.demo.service.domain.UserAuthId;
import de.muenchen.demo.service.domain.UserAuthority;
import de.muenchen.demo.service.rest.api.SearchResultResource;
import de.muenchen.demo.service.rest.api.UserAuthorityResource;
import de.muenchen.demo.service.rest.api.UserAuthorityResourceAssembler;
import de.muenchen.demo.service.services.AuthorityService;
import de.muenchen.demo.service.services.UserAuthorityService;
import de.muenchen.demo.service.services.UserService;
import de.muenchen.demo.service.util.HateoasRelations;
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
@ExposesResourceFor(UserAuthorityController.class)
@RequestMapping("/userAuthority")
public class UserAuthorityController {

    private static final Logger LOG = LoggerFactory.getLogger(UserAuthorityController.class);

    @Autowired
    EntityLinks entityLinks;
    @Autowired
    UserAuthorityService service;
    @Autowired
    UserAuthorityResourceAssembler assembler;
    @Autowired
    UserService serviceUser;
    @Autowired
    AuthorityService authService;

    /**
     * Alle UserAuthority suchen.
     *
     * @return
     */
    @RolesAllowed({"PERM_queryUserAuthority"})
    @RequestMapping(value = "/query", method = {RequestMethod.GET})
    public ResponseEntity queryUserAuthority() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("query usersAuthoritys");
        }
        SearchResultResource<UserAuthorityResource> resource;
        resource = this.assembler.toResource(this.service.query());
        resource.add(linkTo(methodOn(UserAuthorityController.class).queryUserAuthority()).withSelfRel()); // add self link
        return ResponseEntity.ok(resource);
    }

    /**
     * Liest eine UserAuthority zur OID.
     *
     * @param oid
     * @return
     */
    @RolesAllowed({"PERM_readUserAuthority"})
    @RequestMapping(value = "/{uoid}/{aoid}", method = {RequestMethod.GET})
    public ResponseEntity readUserAuthority(@PathVariable("uoid") String uoid, @PathVariable("aoid") String aoid) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("read usersAuthoritys");
        }

        User user = this.serviceUser.read(uoid);
        Authority auth = this.authService.read(aoid);
        UserAuthId id = new UserAuthId(user, auth);

        UserAuthority entity = this.service.read(id);
        UserAuthorityResource resource = this.assembler.toResource(entity, HateoasRelations.SELF, HateoasRelations.NEW, HateoasRelations.DELETE, HateoasRelations.UPDATE);
        return ResponseEntity.ok(resource);
    }

    /**
     * Speichert eine neue UserAuthority.
     *
     * @param request
     * @return
     */
    @RolesAllowed({"PERM_saveUserAuthority"})
    @RequestMapping(value = "/save/{uoid}/{aoid}", method = {RequestMethod.GET})
    public ResponseEntity saveUserAuthority(@PathVariable("uoid") String uoid, @PathVariable("aoid") String aoid) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("save usersAuthoritys");
        }
        UserAuthority entity = new UserAuthority();
        User user = this.serviceUser.read(uoid);
        Authority auth = this.authService.read(aoid);
        entity.setId(new UserAuthId(user, auth));
        this.service.save(entity);
        UserAuthorityResource resource = this.assembler.toResource(entity, HateoasRelations.SELF, HateoasRelations.NEW, HateoasRelations.DELETE, HateoasRelations.UPDATE);
        return ResponseEntity.ok(resource);
    }

    /**
     * Löscht eine UserAuthority.
     *
     * @param oid
     * @return
     */
    @RolesAllowed({"PERM_deleteUserAuthority"})
    @RequestMapping(value = "/{uoid}/{aoid}", method = {RequestMethod.DELETE})
    public ResponseEntity deleteUserAuthority(@PathVariable("uoid") String uoid, @PathVariable("aoid") String aoid) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("delete usersAuthoritys");
        }

        User user = this.serviceUser.read(uoid);
        Authority auth = this.authService.read(aoid);
        UserAuthId id = new UserAuthId(user, auth);
        this.service.delete(id);
        return ResponseEntity.ok().build();
    }
}
