package de.muenchen.demo.service.rest.api;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import de.muenchen.demo.service.domain.Account;
import de.muenchen.demo.service.rest.AccountController;
import de.muenchen.demo.service.services.AccountService;
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
public class AccountResourceAssembler {

    private static final Logger LOG = LoggerFactory.getLogger(AccountResourceAssembler.class);

    @Autowired
    DozerBeanMapper dozer;
    @Autowired
    AccountService service;
    @Autowired
    EntityLinks entityLinks;

    /**
     * Mapping einer Liste von Entities auf eine List von Resource Objekten.
     *
     * @param accounts
     * @return
     */
    public SearchResultResource<AccountResource> toResource(final List<Account> accounts) {
        SearchResultResource<AccountResource> resource = new SearchResultResource<>();
        accounts.stream().forEach((b) -> {
            resource.add(this.toResource(b, HateoasRelations.SELF, HateoasRelations.NEW, HateoasRelations.DELETE, HateoasRelations.UPDATE));
        });
        // add query link
        resource.add(linkTo(methodOn(AccountController.class).queryAccount()).withRel(HateoasUtil.QUERY));
        return resource;
    }

    /**
     * Mapping einer Entity auf eine Resource.
     *
     * @param accounts
     * @param r
     * @return
     */
    public AccountResource toResource(final Account accounts, HateoasRelations... r) {
        // map
        AccountResource resource = this.dozer.map(accounts, AccountResource.class);

        // add links
        ArrayList<HateoasRelations> relations = Lists.newArrayList(r);
        if (relations.contains(HateoasRelations.NEW)) {
            resource.add(linkTo(methodOn(AccountController.class).newAccount()).withRel(HateoasUtil.NEW));

        }

        if (relations.contains(HateoasRelations.UPDATE)) {
            resource.add(linkTo(methodOn(AccountController.class).updateAccount(accounts.getOid(), null)).withRel(HateoasUtil.UPDATE));
        }

        if (relations.contains(HateoasRelations.SELF)) {
            resource.add(linkTo(methodOn(AccountController.class).readAccount(accounts.getOid())).withSelfRel());
        }

        if (relations.contains(HateoasRelations.DELETE)) {
            resource.add(linkTo(methodOn(AccountController.class).deleteAccount(accounts.getOid())).withRel(HateoasUtil.DELETE));
        }

        if (relations.contains(HateoasRelations.SAVE)) {
            resource.add(linkTo(methodOn(AccountController.class).saveAccount(null)).withRel(HateoasUtil.SAVE));
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
    public void fromResource(final AccountResource resource, final Account entity) {
        if (!Strings.isNullOrEmpty(resource.getOid())) {
//            this.dozer.map(resource, entity);
            entity.setOid(resource.getOid());
            // start field mapping
       
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

    public List<AccountResource> toResource(Set<Account> accounts, HateoasRelations hateoasRelations) {

        List<AccountResource> resource = new ArrayList<>();
        accounts.stream().forEach((b) -> {
            resource.add(this.toResource(b, HateoasRelations.SELF, HateoasRelations.NEW, HateoasRelations.DELETE, HateoasRelations.UPDATE));
        });
        return resource;
    }

}
