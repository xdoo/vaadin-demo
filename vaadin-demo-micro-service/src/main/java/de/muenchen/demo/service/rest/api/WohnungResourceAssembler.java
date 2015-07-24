package de.muenchen.demo.service.rest.api;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import de.muenchen.demo.service.domain.Wohnung;
import de.muenchen.demo.service.rest.WohnungController;
import de.muenchen.demo.service.services.WohnungService;
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
            resource.add(this.toResource(b, HateoasRelations.SELF, HateoasRelations.NEW, HateoasRelations.DELETE, HateoasRelations.UPDATE));
        });
        // add query link
        resource.add(linkTo(methodOn(WohnungController.class).queryWohnung()).withRel(HateoasUtil.QUERY));
        return resource;
    }

    /**
     * Mapping einer Entity auf eine Resource.
     *
     * @param wohnung
     * @param r
     * @return
     */
    public WohnungResource toResource(final Wohnung wohnung, HateoasRelations... r) {
        // map
        WohnungResource resource = this.dozer.map(wohnung, WohnungResource.class);

        // add links
        ArrayList<HateoasRelations> relations = Lists.newArrayList(r);
        if (relations.contains(HateoasRelations.NEW)) {
            resource.add(linkTo(methodOn(WohnungController.class).newWohnung()).withRel(HateoasUtil.NEW));

        }

        if (relations.contains(HateoasRelations.UPDATE)) {
            resource.add(linkTo(methodOn(WohnungController.class).updateWohnung(wohnung.getOid(), null)).withRel(HateoasUtil.UPDATE));
        }

        if (relations.contains(HateoasRelations.SELF)) {
            resource.add(linkTo(methodOn(WohnungController.class).readWohnung(wohnung.getOid())).withSelfRel());

        }

        if (relations.contains(HateoasRelations.DELETE)) {
            resource.add(linkTo(methodOn(WohnungController.class).deleteWohnung(wohnung.getOid())).withRel(HateoasUtil.DELETE));
        }

        if (relations.contains(HateoasRelations.SAVE)) {
            resource.add(linkTo(methodOn(WohnungController.class).saveWohnung(null)).withRel(HateoasUtil.SAVE));
        }

        if (relations.contains(HateoasRelations.COPY)) {
            resource.add(linkTo(methodOn(WohnungController.class).copyWohnung(wohnung.getOid())).withRel(HateoasUtil.COPY));
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
            entity.setAdresse(resource.getAdresse());
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
    public List<WohnungResource> toResource(Set<Wohnung> wohnung, HateoasRelations hateoasRelations) {

        List<WohnungResource> resource = new ArrayList<>();
        wohnung.stream().forEach((b) -> {
            resource.add(this.toResource(b, HateoasRelations.SELF, HateoasRelations.NEW, HateoasRelations.DELETE, HateoasRelations.UPDATE));
        });
        return resource;
    }

}
