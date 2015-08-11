package de.muenchen.demo.service.rest.api;

import de.muenchen.demo.service.domain.AuthPermId;
import de.muenchen.demo.service.domain.AuthorityPermission;
import de.muenchen.demo.service.rest.AuthorityPermissionController;
import de.muenchen.demo.service.services.AuthorityPermissionService;
import de.muenchen.vaadin.demo.api.hateoas.HateoasUtil;
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
            resource.add(this.toResource(b, HateoasUtil.REL_SELF, HateoasUtil.REL_NEW, HateoasUtil.REL_DELETE, HateoasUtil.REL_UPDATE));
        });
        // add query link
        resource.add(linkTo(methodOn(AuthorityPermissionController.class).queryAuthorityPermission()).withRel(HateoasUtil.REL_QUERY));
        return resource;
    }

    /**
     * Mapping einer Entity auf eine Resource.
     *
     * @param authoritysPermissions
     * @param r
     * @return
     */
    public AuthorityPermissionResource toResource(final AuthorityPermission authoritysPermissions, String... r) {
        // map
        AuthorityPermissionResource resource = this.dozer.map(authoritysPermissions, AuthorityPermissionResource.class);

        return resource;
    }

    /**
     * Mapping einer Resource auf eine Entity
     *
     * @param resource
     * @param entity
     */
    public void fromResource(final AuthorityPermissionResource resource, final AuthorityPermission entity) {
        
        if (!resource.equals(null)) {
            entity.setId(new AuthPermId(resource.getId().getPermission(), resource.getId().getAuthority()));
        } else {
            LOG.error(resource.toString());
            throw new IllegalArgumentException("The object id (oid) field must be filled.");
        }
    }

    public List<AuthorityPermissionResource> toResource(Set<AuthorityPermission> authoritysPermissions, String hateoasRelations) {

        List<AuthorityPermissionResource> resource = new ArrayList<>();
        authoritysPermissions.stream().forEach((b) -> {
            resource.add(this.toResource(b, HateoasUtil.REL_SELF));
        });
        return resource;
    }

}
