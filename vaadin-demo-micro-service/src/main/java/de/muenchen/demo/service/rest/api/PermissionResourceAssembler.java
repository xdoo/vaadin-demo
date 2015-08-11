package de.muenchen.demo.service.rest.api;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import de.muenchen.demo.service.domain.Permission;
import de.muenchen.demo.service.rest.PermissionController;
import de.muenchen.demo.service.services.PermissionService;
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
public class PermissionResourceAssembler {

    private static final Logger LOG = LoggerFactory.getLogger(PermissionResourceAssembler.class);

    @Autowired
    DozerBeanMapper dozer;
    @Autowired
    PermissionService service;
    @Autowired
    EntityLinks entityLinks;

    /**
     * Mapping einer Liste von Entities auf eine List von Resource Objekten.
     *
     * @param permissions
     * @return
     */
    public SearchResultResource<PermissionResource> toResource(final List<Permission> permissions) {
        SearchResultResource<PermissionResource> resource = new SearchResultResource<>();
        permissions.stream().forEach((b) -> {
            resource.add(this.toResource(b, HateoasUtil.REL_SELF, HateoasUtil.REL_NEW, HateoasUtil.REL_DELETE, HateoasUtil.REL_UPDATE));
        });
        // add query link
        resource.add(linkTo(methodOn(PermissionController.class).queryPermission()).withRel(HateoasUtil.REL_QUERY));
        return resource;
    }

    /**
     * Mapping einer Entity auf eine Resource.
     *
     * @param permissions
     * @param r
     * @return
     */
    public PermissionResource toResource(final Permission permissions, String... r) {
        // map
        PermissionResource resource = this.dozer.map(permissions, PermissionResource.class);

        // add links
        ArrayList<String> relations = Lists.newArrayList(r);

        if (relations.contains(HateoasUtil.REL_SELF)) {
            resource.add(linkTo(methodOn(PermissionController.class).readPermission(permissions.getOid())).withSelfRel());
        }

        if (relations.contains(HateoasUtil.REL_DELETE)) {
            resource.add(linkTo(methodOn(PermissionController.class).deletePermission(permissions.getOid())).withRel(HateoasUtil.REL_DELETE));
        }

        if (relations.contains(HateoasUtil.REL_SAVE)) {
            resource.add(linkTo(methodOn(PermissionController.class).savePermission(null)).withRel(HateoasUtil.REL_SAVE));
        }

        return resource;
    }

    /**
     * Mapping einer Resource auf eine Entity
     *
     * @param resource
     * @param entity
     */
    public void fromResource(final PermissionResource resource, final Permission entity) {
        if (!Strings.isNullOrEmpty(resource.getOid())) {
//            this.dozer.map(resource, entity);
            entity.setOid(resource.getOid());
            // start field mapping
            entity.setPermision(resource.getPermision());
            entity.setCreatedBy(resource.getCreatedBy());
            entity.setCreatedDate(resource.getCreatedDate());
            entity.setLastModBy(resource.getLastModBy());
            entity.setLastModDate(resource.getLastModDate());
// end field mapping
        } else {
            LOG.error(resource.toString());
            throw new IllegalArgumentException("The object id (oid) field must be filled.");
        }
    }

    public List<PermissionResource> toResource(Set<Permission> permissions, String hateoasRelations) {

        List<PermissionResource> resource = new ArrayList<>();
        permissions.stream().forEach((b) -> {
            resource.add(this.toResource(b, HateoasUtil.REL_SELF, HateoasUtil.REL_NEW, HateoasUtil.REL_DELETE, HateoasUtil.REL_UPDATE));
        });
        return resource;
    }

}
