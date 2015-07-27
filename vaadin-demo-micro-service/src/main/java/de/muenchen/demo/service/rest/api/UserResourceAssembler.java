package de.muenchen.demo.service.rest.api;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import de.muenchen.demo.service.domain.User;
import de.muenchen.demo.service.rest.UserController;
import de.muenchen.demo.service.services.UserService;
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
            resource.add(this.toResource(b, HateoasRelations.SELF, HateoasRelations.NEW, HateoasRelations.DELETE, HateoasRelations.UPDATE));
        });
        // add query link
        resource.add(linkTo(methodOn(UserController.class).queryUser()).withRel(HateoasUtil.QUERY));
        return resource;
    }

    /**
     * Mapping einer Entity auf eine Resource.
     *
     * @param user
     * @param r
     * @return
     */
    public UserResource toResource(final User user, HateoasRelations... r) {
        // map
        UserResource resource = this.dozer.map(user, UserResource.class);

        // add links
        ArrayList<HateoasRelations> relations = Lists.newArrayList(r);
        if (relations.contains(HateoasRelations.NEW)) {
            resource.add(linkTo(methodOn(UserController.class).newUser()).withRel(HateoasUtil.NEW));

        }

        if (relations.contains(HateoasRelations.UPDATE)) {
            resource.add(linkTo(methodOn(UserController.class).updateUser(user.getOid(), null)).withRel(HateoasUtil.UPDATE));
        }

        if (relations.contains(HateoasRelations.SELF)) {
            resource.add(linkTo(methodOn(UserController.class).readUser(user.getOid())).withSelfRel());
        }

        if (relations.contains(HateoasRelations.DELETE)) {
            resource.add(linkTo(methodOn(UserController.class).deleteUser(user.getOid())).withRel(HateoasUtil.DELETE));
        }

        if (relations.contains(HateoasRelations.SAVE)) {
            resource.add(linkTo(methodOn(UserController.class).saveUser(null)).withRel(HateoasUtil.SAVE));
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

    public List<UserResource> toResource(Set<User> user, HateoasRelations hateoasRelations) {

        List<UserResource> resource = new ArrayList<>();
        user.stream().forEach((b) -> {
            resource.add(this.toResource(b, HateoasRelations.SELF, HateoasRelations.NEW, HateoasRelations.DELETE, HateoasRelations.UPDATE));
        });
        return resource;
    }

}
