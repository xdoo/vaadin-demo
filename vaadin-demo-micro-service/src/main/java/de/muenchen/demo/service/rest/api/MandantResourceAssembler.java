package de.muenchen.demo.service.rest.api;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import de.muenchen.demo.service.domain.Mandant;
import de.muenchen.demo.service.rest.MandantController;
import de.muenchen.demo.service.services.MandantService;
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

/**
 *
 * @author claus.straube
 */
@Service
public class MandantResourceAssembler {

    private static final Logger LOG = LoggerFactory.getLogger(MandantResourceAssembler.class);

    @Autowired
    DozerBeanMapper dozer;
    @Autowired
    MandantService service;
    @Autowired
    EntityLinks entityLinks;

    /**
     * Mapping einer Liste von Entities auf eine List von Resource Objekten.
     *
     * @param mandant
     * @return
     */
    public SearchResultResource<MandantResource> toResource(final List<Mandant> mandant) {
        SearchResultResource<MandantResource> resource = new SearchResultResource<>();
        mandant.stream().forEach((b) -> {
            resource.add(this.toResource(b, HateoasUtil.REL_SELF, HateoasUtil.REL_NEW, HateoasUtil.REL_DELETE, HateoasUtil.REL_UPDATE));
        });
        // add query link
        resource.add(linkTo(methodOn(MandantController.class).queryMandant()).withRel(HateoasUtil.REL_QUERY));
        return resource;
    }

    /**
     * Mapping einer Entity auf eine Resource.
     *
     * @param mandant
     * @param r
     * @return
     */
    public MandantResource toResource(final Mandant mandant, String... r) {
        // map
        MandantResource resource = this.dozer.map(mandant, MandantResource.class);

        // add links
        ArrayList<String> relations = Lists.newArrayList(r);
        if (relations.contains(HateoasUtil.REL_NEW)) {
            resource.add(linkTo(methodOn(MandantController.class).newMandant()).withRel(HateoasUtil.REL_NEW));

        }

        if (relations.contains(HateoasUtil.REL_UPDATE)) {
            resource.add(linkTo(methodOn(MandantController.class).updateMandant(mandant.getOid(), null)).withRel(HateoasUtil.REL_UPDATE));
        }

        if (relations.contains(HateoasUtil.REL_SELF)) {
            resource.add(linkTo(methodOn(MandantController.class).readMandant(mandant.getOid())).withSelfRel());
        }

        if (relations.contains(HateoasUtil.REL_DELETE)) {
            resource.add(linkTo(methodOn(MandantController.class).deleteMandant(mandant.getOid())).withRel(HateoasUtil.REL_DELETE));
        }

        if (relations.contains(HateoasUtil.REL_SAVE)) {
            resource.add(linkTo(methodOn(MandantController.class).saveMandant(null)).withRel(HateoasUtil.REL_SAVE));
        }

        if (relations.contains(HateoasUtil.REL_COPY)) {
            resource.add(linkTo(methodOn(MandantController.class).copyMandant(mandant.getOid())).withRel(HateoasUtil.REL_COPY));
        }


        return resource;
    }

    /**
     * Mapping einer Resource auf eine Entity
     *
     * @param resource
     * @param entity
     */
    public void fromResource(final MandantResource resource, final Mandant entity) {
        if (!Strings.isNullOrEmpty(resource.getOid())) {
//            this.dozer.map(resource, entity);
            entity.setOid(resource.getOid());
            // start field mapping
        entity.setMid(resource.getMid());
            // end field mapping
        } else {
            LOG.error(resource.toString());
            throw new IllegalArgumentException("The object id (oid) field must be filled.");
        }
    }

    public List<MandantResource> toResource(Set<Mandant> kinder, String hateoasRelations) {

        List<MandantResource> resource = new ArrayList<>();
        kinder.stream().forEach((b) -> {
            resource.add(this.toResource(b, HateoasUtil.REL_SELF, HateoasUtil.REL_NEW, HateoasUtil.REL_DELETE, HateoasUtil.REL_UPDATE));
        });
        return resource;
    }

}
