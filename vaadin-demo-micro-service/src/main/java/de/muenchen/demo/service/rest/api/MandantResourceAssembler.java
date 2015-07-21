package de.muenchen.demo.service.rest.api;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import de.muenchen.demo.service.domain.Mandant;
import de.muenchen.demo.service.rest.MandantController;
import de.muenchen.demo.service.services.MandantService;
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
            resource.add(this.toResource(b, HateoasRelations.SELF, HateoasRelations.NEW, HateoasRelations.DELETE, HateoasRelations.UPDATE));
        });
        // add query link
        resource.add(linkTo(methodOn(MandantController.class).queryMandant()).withRel(HateoasUtil.QUERY));
        return resource;
    }

    /**
     * Mapping einer Entity auf eine Resource.
     *
     * @param mandant
     * @param r
     * @return
     */
    public MandantResource toResource(final Mandant mandant, HateoasRelations... r) {
        // map
        MandantResource resource = this.dozer.map(mandant, MandantResource.class);

        // add links
        ArrayList<HateoasRelations> relations = Lists.newArrayList(r);
        if (relations.contains(HateoasRelations.NEW)) {
            resource.add(linkTo(methodOn(MandantController.class).newMandant()).withRel(HateoasUtil.NEW));

        }

        if (relations.contains(HateoasRelations.UPDATE)) {
            resource.add(linkTo(methodOn(MandantController.class).updateMandant(mandant.getOid(), null)).withRel(HateoasUtil.UPDATE));
        }

        if (relations.contains(HateoasRelations.SELF)) {
            resource.add(linkTo(methodOn(MandantController.class).readMandant(mandant.getOid())).withSelfRel());
        }

        if (relations.contains(HateoasRelations.DELETE)) {
            resource.add(linkTo(methodOn(MandantController.class).deleteMandant(mandant.getOid())).withRel(HateoasUtil.DELETE));
        }

        if (relations.contains(HateoasRelations.SAVE)) {
            resource.add(linkTo(methodOn(MandantController.class).saveMandant(null)).withRel(HateoasUtil.SAVE));
        }

        if (relations.contains(HateoasRelations.COPY)) {
            resource.add(linkTo(methodOn(MandantController.class).copyMandant(mandant.getOid())).withRel(HateoasUtil.COPY));
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

    public List<MandantResource> toResource(Set<Mandant> kinder, HateoasRelations hateoasRelations) {

        List<MandantResource> resource = new ArrayList<>();
        kinder.stream().forEach((b) -> {
            resource.add(this.toResource(b, HateoasRelations.SELF, HateoasRelations.NEW, HateoasRelations.DELETE, HateoasRelations.UPDATE));
        });
        return resource;
    }

}
