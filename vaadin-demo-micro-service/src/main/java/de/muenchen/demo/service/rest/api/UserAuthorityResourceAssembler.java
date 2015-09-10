package de.muenchen.demo.service.rest.api;

import de.muenchen.demo.service.domain.UserAuthId;
import de.muenchen.demo.service.domain.UserAuthority;
import de.muenchen.demo.service.rest.UserAuthorityController;
import de.muenchen.demo.service.services.UserAuthorityService;
import de.muenchen.vaadin.demo.apilib.hateoas.HateoasUtil;
import de.muenchen.vaadin.demo.apilib.rest.SearchResultResource;
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
            resource.add(this.toResource(b, HateoasUtil.REL_SELF, HateoasUtil.REL_NEW, HateoasUtil.REL_DELETE, HateoasUtil.REL_UPDATE));
        });
        // add query link
        resource.add(linkTo(methodOn(UserAuthorityController.class).queryUserAuthority()).withRel(HateoasUtil.REL_QUERY));
        return resource;
    }

    /**
     * Mapping einer Entity auf eine Resource.
     *
     * @param usersAuthoritys
     * @param r
     * @return
     */
    public UserAuthorityResource toResource(final UserAuthority usersAuthoritys, String... r) {
        // map
        UserAuthorityResource resource = this.dozer.map(usersAuthoritys, UserAuthorityResource.class);

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

    public List<UserAuthorityResource> toResource(Set<UserAuthority> usersAuthoritys, String hateoasRelations) {

        List<UserAuthorityResource> resource = new ArrayList<>();
        usersAuthoritys.stream().forEach((b) -> {
            resource.add(this.toResource(b, HateoasUtil.REL_SELF));
        });
        return resource;
    }

}
