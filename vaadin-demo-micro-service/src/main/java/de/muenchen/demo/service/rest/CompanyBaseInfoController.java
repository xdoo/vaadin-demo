package de.muenchen.demo.service.rest;

import de.muenchen.demo.service.domain.CompanyBaseInfo;
import de.muenchen.demo.service.rest.api.SearchResultResource;
import de.muenchen.demo.service.rest.api.CompanyBaseInfoResource;
import de.muenchen.demo.service.rest.api.CompanyBaseInfoResourceAssembler;
import de.muenchen.demo.service.services.CompanyBaseInfoService;
import de.muenchen.demo.service.util.HateoasRelations;
import java.util.List;
import java.util.Set;
import javax.annotation.security.RolesAllowed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.ExposesResourceFor;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import org.springframework.http.ResponseEntity;
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
@ExposesResourceFor(CompanyBaseInfoController.class)
@RequestMapping("/cbi")
public class CompanyBaseInfoController {

    private static final Logger LOG = LoggerFactory.getLogger(CompanyBaseInfoController.class);

    @Autowired
    EntityLinks entityLinks;
    @Autowired
    CompanyBaseInfoService service;
    @Autowired
    CompanyBaseInfoResourceAssembler assembler;



    /**
     * Alle CompanyBaseInfo suchen.
     *
     * @return
     */
        @RolesAllowed({"PERM_queryCompanyBaseInfo"})
    @RequestMapping(value = "/query", method = {RequestMethod.GET})
    public ResponseEntity queryCompanyBaseInfo() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("query companyBaseInfos");
        }
        SearchResultResource<CompanyBaseInfoResource> resource;
        resource = this.assembler.toResource(this.service.query());
        resource.add(linkTo(methodOn(CompanyBaseInfoController.class).queryCompanyBaseInfo()).withSelfRel()); // add self link
        return ResponseEntity.ok(resource);
    }

    /**
     * Erzeugt ein neue CompanyBaseInfo Objekt mit gefüllter OID. Das Objekt ist noch in
     * der DB gespeichert.
     *
     * @return
     */
    @RolesAllowed({"PERM_newCompanyBaseInfo"})
    @RequestMapping(value = "/new", method = {RequestMethod.GET})
    public ResponseEntity newCompanyBaseInfo() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("new companyBaseInfo");
        }
        CompanyBaseInfo entity = this.service.create();
        CompanyBaseInfoResource resource = this.assembler.toResource(entity, HateoasRelations.NEW, HateoasRelations.SAVE);
        return ResponseEntity.ok(resource);
    }


    /**
     * Liest eine CompanyBaseInfo zur OID.
     *
     * @param oid
     * @return
     */
        @RolesAllowed({"PERM_readCompanyBaseInfo"})
    @RequestMapping(value = "/{oid}", method = {RequestMethod.GET})
    public ResponseEntity readCompanyBaseInfo(@PathVariable("oid") String oid) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("read companyBaseInfos");
        }
        CompanyBaseInfo entity = this.service.read(oid);
        CompanyBaseInfoResource resource = this.assembler.toResource(entity, HateoasRelations.SELF, HateoasRelations.NEW, HateoasRelations.DELETE, HateoasRelations.UPDATE);
        return ResponseEntity.ok(resource);
    }

    /**
     * Speichert Änderungen an einer bereits vorhandenen CompanyBaseInfo.
     *
     * @param oid
     * @param request
     * @return
     */
       @RolesAllowed({"PERM_updateCompanyBaseInfo"})
 @RequestMapping(value = "/{oid}", method = {RequestMethod.POST})
    public ResponseEntity updateCompanyBaseInfo(@PathVariable("oid") String oid, @RequestBody CompanyBaseInfoResource request) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("update companyBaseInfo");
        }
        CompanyBaseInfo entity = service.read(request.getOid());
        LOG.info("davor > " + entity.toString());
        this.assembler.fromResource(request, entity);
        LOG.info("danach > " + entity.toString());
        this.service.update(entity);
        CompanyBaseInfoResource resource = this.assembler.toResource(entity, HateoasRelations.SELF, HateoasRelations.NEW, HateoasRelations.DELETE, HateoasRelations.UPDATE);
        return ResponseEntity.ok(resource);
    }

    /**
     * Speichert eine neue CompanyBaseInfo.
     *
     * @param request
     * @return
     */
       @RolesAllowed({"PERM_saveCompanyBaseInfo"})
 @RequestMapping(value = "/save", method = {RequestMethod.POST})
    public ResponseEntity saveCompanyBaseInfo(@RequestBody CompanyBaseInfoResource request) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("save companyBaseInfo");
        }
        CompanyBaseInfo entity = new CompanyBaseInfo();
        this.assembler.fromResource(request, entity);
        this.service.save(entity);
        CompanyBaseInfoResource resource = this.assembler.toResource(entity, HateoasRelations.SELF, HateoasRelations.NEW, HateoasRelations.DELETE, HateoasRelations.UPDATE);
        return ResponseEntity.ok(resource);
    }

    /**
     * Löscht eine CompanyBaseInfo.
     *
     * @param oid
     * @return
     */
      @RolesAllowed({"PERM_deleteCompanyBaseInfo"})
  @RequestMapping(value = "/{oid}", method = {RequestMethod.DELETE})
    public ResponseEntity deleteCompanyBaseInfo(@PathVariable("oid") String oid) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("delete companyBaseInfos");
        }
        this.service.delete(oid);
        return ResponseEntity.ok().build();
    }
    
}
