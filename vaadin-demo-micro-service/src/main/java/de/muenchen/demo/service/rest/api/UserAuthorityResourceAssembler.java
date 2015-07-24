package de.muenchen.demo.service.rest.api;

import de.muenchen.demo.service.domain.UserAuthId;
import de.muenchen.demo.service.domain.UserAuthority;
import de.muenchen.demo.service.rest.UserAuthorityController;
import de.muenchen.demo.service.services.UserAuthorityService;
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
public class UserAuthorityResourceAssembler {

    private static final Logger LOG = LoggerFactory.getLogger(UserAuthorityResourceAssembler.class);

    @Autowired
    DozerBeanMapper dozer;
    @Autowired
    UserAuthorityService service;
    @Autowired
    EntityLinks entityLinks;

    /**
     * Mapping einer Liste von Entities auf eine List von Resource Objekten.
     *
     * @param usersAuthoritys
     * @return
     */
    public SearchResultResource<UserAuthorityResource> toResource(final List<UserAuthority> usersAuthoritys) {
        SearchResultResource<UserAuthorityResource> resource = new SearchResultResource<>();
        usersAuthoritys.stream().forEach((b) -> {
            resource.add(this.toResource(b, HateoasRelations.SELF, HateoasRelations.NEW, HateoasRelations.DELETE, HateoasRelations.UPDATE));
        });
        // add query link
        resource.add(linkTo(methodOn(UserAuthorityController.class).queryUserAuthority()).withRel(HateoasUtil.QUERY));
        return resource;
    }

    /**
     * Mapping einer Entity auf eine Resource.
     *
     * @param usersAuthoritys
     * @param r
     * @return
     */
    public UserAuthorityResource toResource(final UserAuthority usersAuthoritys, HateoasRelations... r) {
        // map
        UserAuthorityResource resource = this.dozer.map(usersAuthoritys, UserAuthorityResource.class);

        // add links
//        ArrayList<HateoasRelations> relations = Lists.newArrayList(r);
//        if (relations.contains(HateoasRelations.NEW)) {
//            resource.add(linkTo(methodOn(UserAuthorityController.class).newUsersAuthoritys()).withRel(HateoasUtil.NEW));
//
//        }
//
//        if (relations.contains(HateoasRelations.SAVE)) {
//            resource.add(linkTo(methodOn(UserAuthorityController.class).saveUserAuthority(null)).withRel(HateoasUtil.SAVE));
//        }

        return resource;
    }

    /**
     * Mapping einer Resource auf eine Entity
     *
     * @param resource
     * @param entity
     */
    public void fromResource(final UserAuthorityResource resource, final UserAuthority entity) {
        
        if (!resource.equals(null)) {
            entity.setId(new UserAuthId(resource.getId().getUser(), resource.getId().getAuthority()));
        } else {
            LOG.error(resource.toString());
            throw new IllegalArgumentException("The object id (oid) field must be filled.");
        }
    }

    public List<UserAuthorityResource> toResource(Set<UserAuthority> usersAuthoritys, HateoasRelations hateoasRelations) {

        List<UserAuthorityResource> resource = new ArrayList<>();
        usersAuthoritys.stream().forEach((b) -> {
            resource.add(this.toResource(b, HateoasRelations.SELF));
        });
        return resource;
    }

}
