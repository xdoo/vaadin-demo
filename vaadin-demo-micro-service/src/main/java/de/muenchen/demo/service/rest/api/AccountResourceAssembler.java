package de.muenchen.demo.service.rest.api;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import de.muenchen.demo.service.domain.Account;
import de.muenchen.demo.service.rest.AccountController;
import de.muenchen.demo.service.services.AccountService;
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
import de.muenchen.vaadin.demo.api.rest.SearchResultResource;
import de.muenchen.vaadin.demo.api.rest.SecurityResource;
import de.muenchen.vaadin.demo.api.rest.AccountResource;

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
            resource.add(this.toResource(b, HateoasUtil.REL_SAVE, HateoasUtil.REL_NEW, HateoasUtil.REL_DELETE, HateoasUtil.REL_UPDATE));
        });
        // add query link
        resource.add(linkTo(methodOn(AccountController.class).queryAccount()).withRel(HateoasUtil.REL_QUERY));
        return resource;
    }

    /**
     * Mapping einer Entity auf eine Resource.
     *
     * @param accounts
     * @param r
     * @return
     */
    public AccountResource toResource(final Account accounts, String... r) {
        // map
        AccountResource resource = this.dozer.map(accounts, AccountResource.class);

        // add links
        ArrayList<String> relations = Lists.newArrayList(r);
        if (relations.contains(HateoasUtil.REL_NEW)) {
            resource.add(linkTo(methodOn(AccountController.class).newAccount()).withRel(HateoasUtil.REL_NEW));

        }

        if (relations.contains(HateoasUtil.REL_UPDATE)) {
            resource.add(linkTo(methodOn(AccountController.class).updateAccount(accounts.getOid(), null)).withRel(HateoasUtil.REL_UPDATE));
        }

        if (relations.contains(HateoasUtil.REL_SELF)) {
            resource.add(linkTo(methodOn(AccountController.class).readAccount(accounts.getOid())).withSelfRel());
        }

        if (relations.contains(HateoasUtil.REL_DELETE)) {
            resource.add(linkTo(methodOn(AccountController.class).deleteAccount(accounts.getOid())).withRel(HateoasUtil.REL_DELETE));
        }

        if (relations.contains(HateoasUtil.REL_SAVE)) {
            resource.add(linkTo(methodOn(AccountController.class).saveAccount(null)).withRel(HateoasUtil.REL_SAVE));
        }

        return resource;
    }

    /**
     * Mapping einer Resource auf eine Entity
     *
     * @param resource
     * @param entity
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

    public List<AccountResource> toResource(Set<Account> accounts, String hateoasRelations) {

        List<AccountResource> resource = new ArrayList<>();
        accounts.stream().forEach((b) -> {
            resource.add(this.toResource(b, HateoasUtil.REL_SELF, HateoasUtil.REL_NEW, HateoasUtil.REL_DELETE, HateoasUtil.REL_UPDATE));
        });
        return resource;
    }

}
