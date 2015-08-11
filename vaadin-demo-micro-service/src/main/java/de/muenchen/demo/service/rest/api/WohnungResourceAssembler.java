package de.muenchen.demo.service.rest.api;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import de.muenchen.demo.service.domain.Wohnung;
import de.muenchen.demo.service.rest.WohnungController;
import de.muenchen.demo.service.services.WohnungService;
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
public class WohnungResourceAssembler {

    private static final Logger LOG = LoggerFactory.getLogger(WohnungResourceAssembler.class);

    @Autowired
    DozerBeanMapper dozer;
    @Autowired
    WohnungService service;
    @Autowired
    EntityLinks entityLinks;

    /**
     * Mapping einer Liste von Entities auf eine List von Resource Objekten.
     *
     * @param wohnung
     * @return
     */

    public SearchResultResource<WohnungResource> toResource(final List<Wohnung> wohnung) {
        SearchResultResource<WohnungResource> resource = new SearchResultResource<>();
        wohnung.stream().forEach((b) -> {
            resource.add(this.toResource(b, HateoasUtil.REL_SELF, HateoasUtil.REL_NEW, HateoasUtil.REL_DELETE, HateoasUtil.REL_UPDATE));
        });
        // add query link
        resource.add(linkTo(methodOn(WohnungController.class).queryWohnung()).withRel(HateoasUtil.REL_QUERY));
        return resource;
    }

    /**
     * Mapping einer Entity auf eine Resource.
     *
     * @param wohnung
     * @param r
     * @return
     */
    public WohnungResource toResource(final Wohnung wohnung, String... r) {
        // map
        WohnungResource resource = this.dozer.map(wohnung, WohnungResource.class);

        // add links
        ArrayList<String> relations = Lists.newArrayList(r);
        if (relations.contains(HateoasUtil.REL_NEW)) {
            resource.add(linkTo(methodOn(WohnungController.class).newWohnung()).withRel(HateoasUtil.REL_NEW));

        }

        if (relations.contains(HateoasUtil.REL_UPDATE)) {
            resource.add(linkTo(methodOn(WohnungController.class).updateWohnung(wohnung.getOid(), null)).withRel(HateoasUtil.REL_UPDATE));
        }

        if (relations.contains(HateoasUtil.REL_SELF)) {
            resource.add(linkTo(methodOn(WohnungController.class).readWohnung(wohnung.getOid())).withSelfRel());

        }

        if (relations.contains(HateoasUtil.REL_DELETE)) {
            resource.add(linkTo(methodOn(WohnungController.class).deleteWohnung(wohnung.getOid())).withRel(HateoasUtil.REL_DELETE));
        }

        if (relations.contains(HateoasUtil.REL_SAVE)) {
            resource.add(linkTo(methodOn(WohnungController.class).saveWohnung(null)).withRel(HateoasUtil.REL_SAVE));
        }
        
         if (relations.contains(HateoasRelations.ADRESSE)) {
            resource.add(linkTo(methodOn(WohnungController.class).readWohnungAdresse(wohnung.getOid())).withRel(HateoasUtil.ADRESSE));
        }

        if (relations.contains(HateoasUtil.REL_COPY)) {
            resource.add(linkTo(methodOn(WohnungController.class).copyWohnung(wohnung.getOid())).withRel(HateoasUtil.REL_COPY));
        }

        return resource;
    }

    /**
     * Mapping einer Resource auf eine Entity
     *
     * @param resource
     * @param entity
     */
    public void fromResource(final WohnungResource resource, final Wohnung entity) {
        if (!Strings.isNullOrEmpty(resource.getOid())) {
//            this.dozer.map(resource, entity);
            entity.setOid(resource.getOid());
            // start field mapping
            entity.setAusrichtung(resource.getAusrichtung());
            entity.setStock(resource.getStock());
            // end field mapping
        } else {
            LOG.error(resource.toString());
            throw new IllegalArgumentException("The object id (oid) field must be filled.");
        }
    }

    /**
     * Mapping liste Resource auf liste Entity
     *
     * @param wohnung
     * @param hateoasRelations
     * @return
     */
    public List<WohnungResource> toResource(Set<Wohnung> wohnung, String hateoasRelations) {

        List<WohnungResource> resource = new ArrayList<>();
        wohnung.stream().forEach((b) -> {
            resource.add(this.toResource(b, HateoasUtil.REL_SELF, HateoasUtil.REL_NEW, HateoasUtil.REL_DELETE, HateoasUtil.REL_UPDATE));
        });
        return resource;
    }

}
