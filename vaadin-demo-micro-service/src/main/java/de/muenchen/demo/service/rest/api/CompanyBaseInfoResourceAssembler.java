package de.muenchen.demo.service.rest.api;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import de.muenchen.demo.service.domain.CompanyBaseInfo;
import de.muenchen.demo.service.rest.CompanyBaseInfoController;
import de.muenchen.demo.service.services.CompanyBaseInfoService;
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
            resource.add(this.toResource(b, HateoasUtil.REL_SELF, HateoasUtil.REL_NEW, HateoasUtil.REL_DELETE, HateoasUtil.REL_UPDATE));
        });
        // add query link
        resource.add(linkTo(methodOn(CompanyBaseInfoController.class).queryCompanyBaseInfo()).withRel(HateoasUtil.REL_QUERY));
        return resource;
    }

    /**
     * Mapping einer Entity auf eine Resource.
     *
     * @param companyBaseInfos
     * @param r
     * @return
     */
    public CompanyBaseInfoResource toResource(final CompanyBaseInfo companyBaseInfos, String... r) {
        // map
        CompanyBaseInfoResource resource = this.dozer.map(companyBaseInfos, CompanyBaseInfoResource.class);

        // add links
        ArrayList<String> relations = Lists.newArrayList(r);
        if (relations.contains(HateoasUtil.REL_NEW)) {
            resource.add(linkTo(methodOn(CompanyBaseInfoController.class).newCompanyBaseInfo()).withRel(HateoasUtil.REL_NEW));

        }

        if (relations.contains(HateoasUtil.REL_UPDATE)) {
            resource.add(linkTo(methodOn(CompanyBaseInfoController.class).updateCompanyBaseInfo(companyBaseInfos.getOid(), null)).withRel(HateoasUtil.REL_UPDATE));
        }

        if (relations.contains(HateoasUtil.REL_SELF)) {
            resource.add(linkTo(methodOn(CompanyBaseInfoController.class).readCompanyBaseInfo(companyBaseInfos.getOid())).withSelfRel());
        }

        if (relations.contains(HateoasUtil.REL_DELETE)) {
            resource.add(linkTo(methodOn(CompanyBaseInfoController.class).deleteCompanyBaseInfo(companyBaseInfos.getOid())).withRel(HateoasUtil.REL_DELETE));
        }

        if (relations.contains(HateoasUtil.REL_SAVE)) {
            resource.add(linkTo(methodOn(CompanyBaseInfoController.class).saveCompanyBaseInfo(null)).withRel(HateoasUtil.REL_SAVE));
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

    public List<CompanyBaseInfoResource> toResource(Set<CompanyBaseInfo> companyBaseInfos, String hateoasRelations) {

        List<CompanyBaseInfoResource> resource = new ArrayList<>();
        companyBaseInfos.stream().forEach((b) -> {
            resource.add(this.toResource(b, HateoasUtil.REL_SELF, HateoasUtil.REL_NEW, HateoasUtil.REL_DELETE, HateoasUtil.REL_UPDATE));
        });
        return resource;
    }

}
