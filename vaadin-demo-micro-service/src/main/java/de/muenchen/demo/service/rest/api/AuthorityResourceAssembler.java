package de.muenchen.demo.service.rest.api;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import de.muenchen.demo.service.domain.Authority;
import de.muenchen.demo.service.rest.AuthorityController;
import de.muenchen.demo.service.services.AuthorityService;
import de.muenchen.demo.service.util.HateoasRelations;
import de.muenchen.demo.service.util.HateoasUtil;
import java.sql.Date;
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
public class AuthorityResourceAssembler {

    private static final Logger LOG = LoggerFactory.getLogger(AuthorityResourceAssembler.class);

    @Autowired
    DozerBeanMapper dozer;
    @Autowired
    AuthorityService service;
    @Autowired
    EntityLinks entityLinks;

    /**
     * Mapping einer Liste von Entities auf eine List von Resource Objekten.
     *
     * @param authoritys
     * @return
     */
    public SearchResultResource<AuthorityResource> toResource(final List<Authority> authoritys) {
        SearchResultResource<AuthorityResource> resource = new SearchResultResource<>();
        authoritys.stream().forEach((b) -> {
            resource.add(this.toResource(b, HateoasRelations.SELF, HateoasRelations.NEW, HateoasRelations.DELETE, HateoasRelations.UPDATE));
        });
        // add query link
        resource.add(linkTo(methodOn(AuthorityController.class).queryAuthority()).withRel(HateoasUtil.QUERY));
        return resource;
    }

    /**
     * Mapping einer Entity auf eine Resource.
     *
     * @param authoritys
     * @param r
     * @return
     */
    public AuthorityResource toResource(final Authority authoritys, HateoasRelations... r) {
        // map
        AuthorityResource resource = this.dozer.map(authoritys, AuthorityResource.class);

        // add links
        ArrayList<HateoasRelations> relations = Lists.newArrayList(r);
        if (relations.contains(HateoasRelations.NEW)) {
            resource.add(linkTo(methodOn(AuthorityController.class).newAuthority()).withRel(HateoasUtil.NEW));

        }

        if (relations.contains(HateoasRelations.UPDATE)) {
            resource.add(linkTo(methodOn(AuthorityController.class).updateAuthority(authoritys.getOid(), null)).withRel(HateoasUtil.UPDATE));
        }

        if (relations.contains(HateoasRelations.SELF)) {
            resource.add(linkTo(methodOn(AuthorityController.class).readAuthority(authoritys.getOid())).withSelfRel());
        }

        if (relations.contains(HateoasRelations.DELETE)) {
            resource.add(linkTo(methodOn(AuthorityController.class).deleteAuthority(authoritys.getOid())).withRel(HateoasUtil.DELETE));
        }

        if (relations.contains(HateoasRelations.SAVE)) {
            resource.add(linkTo(methodOn(AuthorityController.class).saveAuthority(null)).withRel(HateoasUtil.SAVE));
        }

        return resource;
    }

    /**
     * Mapping einer Resource auf eine Entity
     *
     * @param resource
     * @param entity
     * @return
     */
    public void fromResource(final AuthorityResource resource, final Authority entity) {
        if (!Strings.isNullOrEmpty(resource.getOid())) {
//            this.dozer.map(resource, entity);
            entity.setOid(resource.getOid());
            // start field mapping
            entity.setAuthority(resource.getAuthority());
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

    public List<AuthorityResource> toResource(Set<Authority> authoritys, HateoasRelations hateoasRelations) {

        List<AuthorityResource> resource = new ArrayList<>();
        authoritys.stream().forEach((b) -> {
            resource.add(this.toResource(b, HateoasRelations.SELF, HateoasRelations.NEW, HateoasRelations.DELETE, HateoasRelations.UPDATE));
        });
        return resource;
    }

}
