package de.muenchen.demo.service.rest.api;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import de.muenchen.demo.service.domain.User;
import de.muenchen.demo.service.rest.UserController;
import de.muenchen.demo.service.services.UserService;
import de.muenchen.vaadin.demo.api.hateoas.HateoasUtil;
import de.muenchen.vaadin.demo.api.rest.SearchResultResource;
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
public class UserResourceAssembler {

    private static final Logger LOG = LoggerFactory.getLogger(UserResourceAssembler.class);

    @Autowired
    DozerBeanMapper dozer;
    @Autowired
    UserService service;
    @Autowired
    EntityLinks entityLinks;

    /**
     * Mapping einer Liste von Entities auf eine List von Resource Objekten.
     *
     * @param users
     * @return
     */
    public SearchResultResource<UserResource> toResource(final List<User> users) {
        SearchResultResource<UserResource> resource = new SearchResultResource<>();
        users.stream().forEach((b) -> {
            resource.add(this.toResource(b, HateoasUtil.REL_SELF, HateoasUtil.REL_NEW, HateoasUtil.REL_DELETE, HateoasUtil.REL_UPDATE));
        });
        // add query link
        resource.add(linkTo(methodOn(UserController.class).queryUser()).withRel(HateoasUtil.REL_QUERY));
        return resource;
    }

    /**
     * Mapping einer Entity auf eine Resource.
     *
     * @param user
     * @param r
     * @return
     */
    public UserResource toResource(final User user, String... r) {
        // map
        UserResource resource = this.dozer.map(user, UserResource.class);

        // add links
        ArrayList<String> relations = Lists.newArrayList(r);
        if (relations.contains(HateoasUtil.REL_NEW)) {
            resource.add(linkTo(methodOn(UserController.class).newUser()).withRel(HateoasUtil.REL_NEW));

        }

        if (relations.contains(HateoasUtil.REL_UPDATE)) {
            resource.add(linkTo(methodOn(UserController.class).updateUser(user.getOid(), null)).withRel(HateoasUtil.REL_UPDATE));
        }

        if (relations.contains(HateoasUtil.REL_SELF)) {
            resource.add(linkTo(methodOn(UserController.class).readUser(user.getOid())).withSelfRel());
        }

        if (relations.contains(HateoasUtil.REL_DELETE)) {
            resource.add(linkTo(methodOn(UserController.class).deleteUser(user.getOid())).withRel(HateoasUtil.REL_DELETE));
        }

        if (relations.contains(HateoasUtil.REL_SAVE)) {
            resource.add(linkTo(methodOn(UserController.class).saveUser(null)).withRel(HateoasUtil.REL_SAVE));
        }
        
        if (relations.contains(HateoasUtil.REL_COPY)) {
            resource.add(linkTo(methodOn(UserController.class).saveUser(null)).withRel(HateoasUtil.REL_COPY));
        }

        return resource;
    }

    /**
     * Mapping einer Resource auf eine Entity
     *
     * @param resource
     * @param entity
     */
    public void fromResource(final UserResource resource, final User entity) {
        if (!Strings.isNullOrEmpty(resource.getOid())) {
//            this.dozer.map(resource, entity);
            entity.setOid(resource.getOid());
            // start field mapping
            entity.setPassword(resource.getPassword());
            entity.setUsername(resource.getUsername());
            entity.setEmail(resource.getEmail());
            entity.setEnabled(resource.isEnabled());
            entity.setForname(resource.getForname());
            entity.setSurname(resource.getSurname());
            entity.setAccounts(resource.getAccounts());
            entity.setBirthdate(resource.getBirthdate());
            entity.setCreatedBy(resource.getCreatedBy());
            entity.setCreatedDate(resource.getCreatedDate());
            entity.setLastModBy(resource.getLastModBy());
            entity.setLastModDate(resource.getLastModDate());
            entity.setMandant(resource.getMandant());
// end field mapping
        } else {
            LOG.error(resource.toString());
            throw new IllegalArgumentException("The object id (oid) field must be filled.");
        }
    }

    public List<UserResource> toResource(Set<User> user, String hateoasRelations) {

        List<UserResource> resource = new ArrayList<>();
        user.stream().forEach((b) -> {
            resource.add(this.toResource(b, HateoasUtil.REL_SELF, HateoasUtil.REL_NEW, HateoasUtil.REL_DELETE, HateoasUtil.REL_UPDATE, HateoasUtil.REL_COPY));
        });
        return resource;
    }

    public UserResource assembleWithAllLinks(User entity) {
        return this.toResource(entity,
                HateoasUtil.REL_SELF,
                HateoasUtil.REL_NEW,
                HateoasUtil.REL_DELETE,
                HateoasUtil.REL_UPDATE,
                HateoasUtil.REL_COPY
        );
    }
}
