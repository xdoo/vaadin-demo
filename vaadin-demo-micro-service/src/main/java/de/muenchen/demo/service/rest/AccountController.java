package de.muenchen.demo.service.rest;

import de.muenchen.demo.service.domain.Account;
import de.muenchen.vaadin.demo.apilib.rest.SearchResultResource;
import de.muenchen.vaadin.demo.apilib.rest.AccountResource;
import de.muenchen.demo.service.rest.api.AccountResourceAssembler;
import de.muenchen.demo.service.services.AccountService;
import de.muenchen.vaadin.demo.apilib.hateoas.HateoasUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.ExposesResourceFor;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestBody;

/**
 *
 * @author praktikant.tmar
 */
@Controller
@ExposesResourceFor(AccountController.class)
@RequestMapping("/accounts")
public class AccountController {

    private static final Logger LOG = LoggerFactory.getLogger(AccountController.class);

    @Autowired
    EntityLinks entityLinks;
    @Autowired
    AccountService service;
    @Autowired
    AccountResourceAssembler assembler;



    /**
     * Alle Account suchen.
     *
     * @return
     */
    @Secured({"PERM_queryAccount"})
    @RequestMapping(value = "/query", method = {RequestMethod.GET})
    public ResponseEntity queryAccount() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("query accounts");
        }
        SearchResultResource<AccountResource> resource;
        resource = this.assembler.toResource(this.service.query());
        resource.add(linkTo(methodOn(AccountController.class).queryAccount()).withSelfRel()); // add self link
        return ResponseEntity.ok(resource);
    }

    /**
     * Erzeugt ein neue Account Objekt mit gefüllter OID. Das Objekt ist noch in
     * der DB gespeichert.
     *
     * @return
     */
@Secured({"PERM_newAccount"})
@RequestMapping(value = "/new", method = {RequestMethod.GET})
    public ResponseEntity newAccount() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("new accounts");
        }
        Account entity = this.service.create();
        AccountResource resource = this.assembler.toResource(entity, HateoasUtil.REL_NEW, HateoasUtil.REL_SAVE);
        return ResponseEntity.ok(resource);
    }


    /**
     * Liest eine Account zur OID.
     *
     * @param oid
     * @return
     */
    @Secured({"PERM_readAccount"})
    @RequestMapping(value = "/{oid}", method = {RequestMethod.GET})
    public ResponseEntity readAccount(@PathVariable("oid") String oid) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("read accounts");
        }
        Account entity = this.service.read(oid);
        AccountResource resource = this.assembler.toResource(entity, HateoasUtil.REL_SELF, HateoasUtil.REL_NEW, HateoasUtil.REL_DELETE, HateoasUtil.REL_UPDATE);
        return ResponseEntity.ok(resource);
    }

    /**
     * Speichert Änderungen an einer bereits vorhandenen Account.
     *
     * @param oid
     * @param request
     * @return
     */
    @Secured({"PERM_updateAccount"})
    @RequestMapping(value = "/{oid}", method = {RequestMethod.POST})
    public ResponseEntity updateAccount(@PathVariable("oid") String oid, @RequestBody AccountResource request) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("update accounts");
        }
        Account entity = service.read(request.getOid());
        LOG.info("davor > " + entity.toString());
        this.assembler.fromResource(request, entity);
        LOG.info("danach > " + entity.toString());
        this.service.update(entity);
        AccountResource resource = this.assembler.toResource(entity, HateoasUtil.REL_SELF, HateoasUtil.REL_NEW, HateoasUtil.REL_DELETE, HateoasUtil.REL_UPDATE);
        return ResponseEntity.ok(resource);
    }

    /**
     * Speichert eine neue Account.
     *
     * @param request
     * @return
     */
    @Secured({"PERM_saveAccount"})
    @RequestMapping(value = "/save", method = {RequestMethod.POST})
    public ResponseEntity saveAccount(@RequestBody AccountResource request) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("save accounts");
        }
        Account entity = new Account();
        this.assembler.fromResource(request, entity);
        this.service.save(entity);
        AccountResource resource = this.assembler.toResource(entity, HateoasUtil.REL_SELF, HateoasUtil.REL_NEW, HateoasUtil.REL_DELETE, HateoasUtil.REL_UPDATE);
        return ResponseEntity.ok(resource);
    }

    /**
     * Löscht eine Account.
     *
     * @param oid
     * @return
     */
    @Secured({"PERM_deleteAccount"})
    @RequestMapping(value = "/{oid}", method = {RequestMethod.DELETE})
    public ResponseEntity deleteAccount(@PathVariable("oid") String oid) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("delete accounts");
        }
        this.service.delete(oid);
        return ResponseEntity.ok().build();
    }
    
}
