package de.muenchen.demo.service.rest.api;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import de.muenchen.demo.service.domain.CompanyBaseInfo;
import de.muenchen.demo.service.rest.CompanyBaseInfoController;
import de.muenchen.demo.service.services.CompanyBaseInfoService;
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
public class CompanyBaseInfoResourceAssembler {

    private static final Logger LOG = LoggerFactory.getLogger(CompanyBaseInfoResourceAssembler.class);

    @Autowired
    DozerBeanMapper dozer;
    @Autowired
    CompanyBaseInfoService service;
    @Autowired
    EntityLinks entityLinks;

    /**
     * Mapping einer Liste von Entities auf eine List von Resource Objekten.
     *
     * @param companyBaseInfos
     * @return
     */
    public SearchResultResource<CompanyBaseInfoResource> toResource(final List<CompanyBaseInfo> companyBaseInfos) {
        SearchResultResource<CompanyBaseInfoResource> resource = new SearchResultResource<>();
        companyBaseInfos.stream().forEach((b) -> {
            resource.add(this.toResource(b, HateoasRelations.SELF, HateoasRelations.NEW, HateoasRelations.DELETE, HateoasRelations.UPDATE));
        });
        // add query link
        resource.add(linkTo(methodOn(CompanyBaseInfoController.class).queryCompanyBaseInfo()).withRel(HateoasUtil.QUERY));
        return resource;
    }

    /**
     * Mapping einer Entity auf eine Resource.
     *
     * @param companyBaseInfos
     * @param r
     * @return
     */
    public CompanyBaseInfoResource toResource(final CompanyBaseInfo companyBaseInfos, HateoasRelations... r) {
        // map
        CompanyBaseInfoResource resource = this.dozer.map(companyBaseInfos, CompanyBaseInfoResource.class);

        // add links
        ArrayList<HateoasRelations> relations = Lists.newArrayList(r);
        if (relations.contains(HateoasRelations.NEW)) {
            resource.add(linkTo(methodOn(CompanyBaseInfoController.class).newCompanyBaseInfo()).withRel(HateoasUtil.NEW));

        }

        if (relations.contains(HateoasRelations.UPDATE)) {
            resource.add(linkTo(methodOn(CompanyBaseInfoController.class).updateCompanyBaseInfo(companyBaseInfos.getOid(), null)).withRel(HateoasUtil.UPDATE));
        }

        if (relations.contains(HateoasRelations.SELF)) {
            resource.add(linkTo(methodOn(CompanyBaseInfoController.class).readCompanyBaseInfo(companyBaseInfos.getOid())).withSelfRel());
        }

        if (relations.contains(HateoasRelations.DELETE)) {
            resource.add(linkTo(methodOn(CompanyBaseInfoController.class).deleteCompanyBaseInfo(companyBaseInfos.getOid())).withRel(HateoasUtil.DELETE));
        }

        if (relations.contains(HateoasRelations.SAVE)) {
            resource.add(linkTo(methodOn(CompanyBaseInfoController.class).saveCompanyBaseInfo(null)).withRel(HateoasUtil.SAVE));
        }

        return resource;
    }

    /**
     * Mapping einer Resource auf eine Entity
     *
     * @param resource
     * @param entity
     */
    public void fromResource(final CompanyBaseInfoResource resource, final CompanyBaseInfo entity) {
        if (!Strings.isNullOrEmpty(resource.getOid())) {
//            this.dozer.map(resource, entity);
            entity.setOid(resource.getOid());
            // start field mapping
            entity.setName(resource.getName());
            entity.setAdresse(resource.getAdresse());
            entity.setAccounts(resource.getAccounts());
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

    public List<CompanyBaseInfoResource> toResource(Set<CompanyBaseInfo> companyBaseInfos, HateoasRelations hateoasRelations) {

        List<CompanyBaseInfoResource> resource = new ArrayList<>();
        companyBaseInfos.stream().forEach((b) -> {
            resource.add(this.toResource(b, HateoasRelations.SELF, HateoasRelations.NEW, HateoasRelations.DELETE, HateoasRelations.UPDATE));
        });
        return resource;
    }

}
