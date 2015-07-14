package de.muenchen.demo.service.rest.api;

import de.muenchen.demo.service.domain.AuthPermId;
import de.muenchen.demo.service.domain.AuthorityPermission;
import de.muenchen.demo.service.rest.AuthorityPermissionController;
import de.muenchen.demo.service.services.AuthorityPermissionService;
import de.muenchen.demo.service.util.HateoasRelations;
import de.muenchen.demo.service.util.HateoasUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.dozer.DozerBeanMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.stereotype.Service;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

/**
 *
 * @author praktikant.tmar
 */
@Service
public class AuthorityPermissionResourceAssembler {

    private static final Logger LOG = LoggerFactory.getLogger(AuthorityPermissionResourceAssembler.class);

    @Autowired
    DozerBeanMapper dozer;
    @Autowired
    AuthorityPermissionService service;
    @Autowired
    EntityLinks entityLinks;

    /**
     * Mapping einer Liste von Entities auf eine List von Resource Objekten.
     *
     * @param authoritysPermissions
     * @return
     */
    public SearchResultResource<AuthorityPermissionResource> toResource(final List<AuthorityPermission> authoritysPermissions) {
        SearchResultResource<AuthorityPermissionResource> resource = new SearchResultResource<>();
        authoritysPermissions.stream().forEach((b) -> {
            resource.add(this.toResource(b, HateoasRelations.SELF, HateoasRelations.NEW, HateoasRelations.DELETE, HateoasRelations.UPDATE));
        });
        // add query link
        resource.add(linkTo(methodOn(AuthorityPermissionController.class).queryAuthorityPermission()).withRel(HateoasUtil.QUERY));
        return resource;
    }

    /**
     * Mapping einer Entity auf eine Resource.
     *
     * @param authoritysPermissions
     * @param r
     * @return
     */
    public AuthorityPermissionResource toResource(final AuthorityPermission authoritysPermissions, HateoasRelations... r) {
        // map
        AuthorityPermissionResource resource = this.dozer.map(authoritysPermissions, AuthorityPermissionResource.class);

        // add links
//        ArrayList<HateoasRelations> relations = Lists.newArrayList(r);
//        if (relations.contains(HateoasRelations.NEW)) {
//            resource.add(linkTo(methodOn(AuthorityPermissionController.class).newAuthoritysPermissions()).withRel(HateoasUtil.NEW));
//
//        }
//
//        if (relations.contains(HateoasRelations.SAVE)) {
//            resource.add(linkTo(methodOn(AuthorityPermissionController.class).saveAuthorityPermission(null)).withRel(HateoasUtil.SAVE));
//        }

        return resource;
    }

    /**
     * Mapping einer Resource auf eine Entity
     *
     * @param resource
     * @param entity
     * @return
     */
    public void fromResource(final AuthorityPermissionResource resource, final AuthorityPermission entity) {
        
        if (!resource.equals(null)) {
            entity.setId(new AuthPermId(resource.getId().getPermission(), resource.getId().getAuthority()));
        } else {
            LOG.error(resource.toString());
            throw new IllegalArgumentException("The object id (oid) field must be filled.");
        }
    }

    public List<AuthorityPermissionResource> toResource(Set<AuthorityPermission> authoritysPermissions, HateoasRelations hateoasRelations) {

        List<AuthorityPermissionResource> resource = new ArrayList<>();
        authoritysPermissions.stream().forEach((b) -> {
            resource.add(this.toResource(b, HateoasRelations.SELF));
        });
        return resource;
    }

}
