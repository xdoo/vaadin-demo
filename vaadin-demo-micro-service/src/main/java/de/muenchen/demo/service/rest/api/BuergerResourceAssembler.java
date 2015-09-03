package de.muenchen.demo.service.rest.api;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import de.muenchen.demo.service.domain.Buerger;
import de.muenchen.demo.service.rest.BuergerController;
import de.muenchen.demo.service.services.BuergerService;
import de.muenchen.vaadin.demo.api.hateoas.HateoasUtil;
import de.muenchen.vaadin.demo.api.rest.BuergerResource;
import de.muenchen.vaadin.demo.api.rest.SearchResultResource;
import org.dozer.DozerBeanMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 *
 * @author claus.straube
 */
@Service
public class BuergerResourceAssembler {

    private static final Logger LOG = LoggerFactory.getLogger(BuergerResourceAssembler.class);

    @Autowired
    DozerBeanMapper dozer;
    @Autowired
    BuergerService service;
    @Autowired
    EntityLinks entityLinks;

    /**
     * Mapping einer Liste von Entities auf eine List von Resource Objekten.
     *
     * @param buerger
     * @return
     */
    public SearchResultResource<BuergerResource> toResource(final List<Buerger> buerger) {
        SearchResultResource<BuergerResource> resource = new SearchResultResource<>();
        buerger.stream().forEach((e) -> {
            resource.add(this.assembleWithAllLinks(e));
        });
        // add query link
        resource.add(linkTo(methodOn(BuergerController.class).queryBuerger()).withRel(HateoasUtil.REL_QUERY));
        return resource;
    }

    /**
     * Mapping einer Entity auf eine Resource.
     *
     * @param buerger
     * @param r
     * @return
     */
    public BuergerResource toResource(final Buerger buerger, String... r) {
        // map
        BuergerResource resource = this.dozer.map(buerger, BuergerResource.class);

        // add links
        ArrayList<String> relations = Lists.newArrayList(r);
        if (relations.contains(HateoasUtil.REL_NEW)) {
            resource.add(linkTo(methodOn(BuergerController.class).newBuerger()).withRel(HateoasUtil.REL_NEW));

        }

        if (relations.contains(HateoasUtil.REL_UPDATE)) {
            resource.add(linkTo(methodOn(BuergerController.class).updateBuerger(buerger.getOid(), null)).withRel(HateoasUtil.REL_UPDATE));
        }

        if (relations.contains(HateoasUtil.REL_SELF)) {
            resource.add(linkTo(methodOn(BuergerController.class).readBuerger(buerger.getOid())).withSelfRel());
        }

        if (relations.contains(HateoasUtil.REL_DELETE)) {
            resource.add(linkTo(methodOn(BuergerController.class).deleteBuerger(buerger.getOid())).withRel(HateoasUtil.REL_DELETE));
        }

        if (relations.contains(HateoasUtil.REL_SAVE)) {
            resource.add(linkTo(methodOn(BuergerController.class).saveBuerger(null)).withRel(HateoasUtil.REL_SAVE));
        }

        if (relations.contains(HateoasUtil.REL_COPY)) {
            resource.add(linkTo(methodOn(BuergerController.class).copyBuerger(buerger.getOid())).withRel(HateoasUtil.REL_COPY));
        }
        if (relations.contains(HateoasUtil.REL_COPY_LIST)) {
            resource.add(linkTo(methodOn(BuergerController.class).copyListBuerger(null)).withRel(HateoasUtil.REL_COPY_LIST));
        }
        if (relations.contains(HateoasUtil.REL_DELETE_LIST)) {
            resource.add(linkTo(methodOn(BuergerController.class).deleteListBuerger(null)).withRel(HateoasUtil.REL_DELETE_LIST));
        }

        if (relations.contains(de.muenchen.vaadin.demo.api.rest.BuergerResource.WOHNUNGEN)) {
            resource.add(linkTo(methodOn(BuergerController.class).readBuergerWohnungen(buerger.getOid())).withRel(de.muenchen.vaadin.demo.api.rest.BuergerResource.WOHNUNGEN));
        }

        if (relations.contains(de.muenchen.vaadin.demo.api.rest.BuergerResource.SAVE_WOHNUNG)) {
            resource.add(linkTo(methodOn(BuergerController.class).createWohnungBuerger(buerger.getOid(), null)).withRel(de.muenchen.vaadin.demo.api.rest.BuergerResource.SAVE_WOHNUNG));
        }

        if (relations.contains(de.muenchen.vaadin.demo.api.rest.BuergerResource.SACHBEARBEITER)) {
            resource.add(linkTo(methodOn(BuergerController.class).readBuergerSachbearbeiter(buerger.getOid())).withRel(de.muenchen.vaadin.demo.api.rest.BuergerResource.SACHBEARBEITER));
        }
        if (relations.contains(de.muenchen.vaadin.demo.api.rest.BuergerResource.SAVE_SACHBEARBEITER)) {
            resource.add(linkTo(methodOn(BuergerController.class).createSachbearbeiterBuerger(buerger.getOid(), null)).withRel(de.muenchen.vaadin.demo.api.rest.BuergerResource.SAVE_SACHBEARBEITER));
        }
        if (relations.contains(de.muenchen.vaadin.demo.api.rest.BuergerResource.PAESSE)) {
            resource.add(linkTo(methodOn(BuergerController.class).readBuergerPass(buerger.getOid())).withRel(de.muenchen.vaadin.demo.api.rest.BuergerResource.PAESSE));
        }

        if (relations.contains(de.muenchen.vaadin.demo.api.rest.BuergerResource.SAVE_PASS)) {
            resource.add(linkTo(methodOn(BuergerController.class).createPassBuerger(buerger.getOid(), null)).withRel(de.muenchen.vaadin.demo.api.rest.BuergerResource.SAVE_PASS));
        }

        if (relations.contains(de.muenchen.vaadin.demo.api.rest.BuergerResource.KINDER)) {
            resource.add(linkTo(methodOn(BuergerController.class).readBuergerKinder(buerger.getOid())).withRel(de.muenchen.vaadin.demo.api.rest.BuergerResource.KINDER));
        }

        if (relations.contains(de.muenchen.vaadin.demo.api.rest.BuergerResource.SAVE_KIND)) {
            resource.add(linkTo(methodOn(BuergerController.class).createKindBuerger(buerger.getOid(), null)).withRel(de.muenchen.vaadin.demo.api.rest.BuergerResource.SAVE_KIND));
        }

        if (relations.contains(BuergerResource.SAVE_PARTNER)) {
            resource.add(linkTo(methodOn(BuergerController.class).createPartnerBuerger(buerger.getOid(), null)).withRel(de.muenchen.vaadin.demo.api.rest.BuergerResource.SAVE_PARTNER));
        }

        if (relations.contains(de.muenchen.vaadin.demo.api.rest.BuergerResource.STAATSANGEHOERIGKEITEN)) {
            resource.add(linkTo(methodOn(BuergerController.class).readBuergerStaatsangehoerigkeiten(buerger.getOid())).withRel(de.muenchen.vaadin.demo.api.rest.BuergerResource.STAATSANGEHOERIGKEITEN));
        }
        if (relations.contains(de.muenchen.vaadin.demo.api.rest.BuergerResource.ELTERN)) {
            resource.add(linkTo(methodOn(BuergerController.class).readEltern(buerger.getOid())).withRel(de.muenchen.vaadin.demo.api.rest.BuergerResource.ELTERN));
        }
        if (relations.contains(de.muenchen.vaadin.demo.api.rest.BuergerResource.RELEASE_ELTERN)) {
            resource.add(linkTo(methodOn(BuergerController.class).releaseBuergerEltern(buerger.getOid())).withRel(de.muenchen.vaadin.demo.api.rest.BuergerResource.RELEASE_ELTERN));
        }
        if (relations.contains(de.muenchen.vaadin.demo.api.rest.BuergerResource.RELEASE_ELTERNTEIL)) {
            resource.add(linkTo(methodOn(BuergerController.class).releaseBuergerElternteil(buerger.getOid(), null)).withRel(de.muenchen.vaadin.demo.api.rest.BuergerResource.RELEASE_ELTERNTEIL));
        }
        if (relations.contains(de.muenchen.vaadin.demo.api.rest.BuergerResource.RELEASE_KINDER)) {
            resource.add(linkTo(methodOn(BuergerController.class).releaseBuergerKinder(buerger.getOid())).withRel(de.muenchen.vaadin.demo.api.rest.BuergerResource.RELEASE_KINDER));
        }
        if (relations.contains(de.muenchen.vaadin.demo.api.rest.BuergerResource.RELEASE_SACHBEARBEITER)) {
            resource.add(linkTo(methodOn(BuergerController.class).releaseBuergerAllSachbearbeiter(buerger.getOid())).withRel(de.muenchen.vaadin.demo.api.rest.BuergerResource.RELEASE_SACHBEARBEITER));
        }
        if (relations.contains(de.muenchen.vaadin.demo.api.rest.BuergerResource.RELEASE_WOHNUNGEN)) {
            resource.add(linkTo(methodOn(BuergerController.class).releaseBuergerWohnungen(buerger.getOid())).withRel(de.muenchen.vaadin.demo.api.rest.BuergerResource.RELEASE_WOHNUNGEN));
        }
        if (relations.contains(de.muenchen.vaadin.demo.api.rest.BuergerResource.RELEASE_WOHNUNG)) {
            resource.add(linkTo(methodOn(BuergerController.class).releaseWohnungBuerger(buerger.getOid(), null)).withRel(de.muenchen.vaadin.demo.api.rest.BuergerResource.RELEASE_WOHNUNG));
        }
        if (relations.contains(de.muenchen.vaadin.demo.api.rest.BuergerResource.RELEASE_PAESSE)) {
            resource.add(linkTo(methodOn(BuergerController.class).releaseBuergerPaesse(buerger.getOid())).withRel(de.muenchen.vaadin.demo.api.rest.BuergerResource.RELEASE_PAESSE));
        }
        if (relations.contains(de.muenchen.vaadin.demo.api.rest.BuergerResource.ADD_WOHNUNG)) {
            resource.add(linkTo(methodOn(BuergerController.class).addWohnungBuerger(buerger.getOid(), null)).withRel(de.muenchen.vaadin.demo.api.rest.BuergerResource.ADD_WOHNUNG));
        }
        if (relations.contains(de.muenchen.vaadin.demo.api.rest.BuergerResource.ADD_SACHBEARBEITER)) {
            resource.add(linkTo(methodOn(BuergerController.class).addSachbearbeiterBuerger(buerger.getOid(), null)).withRel(de.muenchen.vaadin.demo.api.rest.BuergerResource.ADD_SACHBEARBEITER));
        }
        if (relations.contains(de.muenchen.vaadin.demo.api.rest.BuergerResource.ADD_PASS)) {
            resource.add(linkTo(methodOn(BuergerController.class).addPassBuerger(buerger.getOid(), null)).withRel(de.muenchen.vaadin.demo.api.rest.BuergerResource.ADD_PASS));
        }
        if (relations.contains(de.muenchen.vaadin.demo.api.rest.BuergerResource.ADD_KIND)) {
            resource.add(linkTo(methodOn(BuergerController.class).addKindBuerger(buerger.getOid(), null)).withRel(de.muenchen.vaadin.demo.api.rest.BuergerResource.ADD_KIND));
        }
        if (relations.contains(de.muenchen.vaadin.demo.api.rest.BuergerResource.ADD_PARTNER)) {
            resource.add(new Link("/add/buerger/"+buerger.getOid()+"/partner/").withRel(de.muenchen.vaadin.demo.api.rest.BuergerResource.ADD_PARTNER));

        }
        if (relations.contains(de.muenchen.vaadin.demo.api.rest.BuergerResource.PARTNER)) {
            resource.add(linkTo(methodOn(BuergerController.class).readBuergerPartner(buerger.getOid())).withRel(de.muenchen.vaadin.demo.api.rest.BuergerResource.PARTNER));
        }
        
        if (relations.contains(de.muenchen.vaadin.demo.api.rest.BuergerResource.ADD_STAATSANGEHOERIGKEITEN)) {
            resource.add(linkTo(methodOn(BuergerController.class).addStaatangehoerigkeitBuerger(buerger.getOid(), null)).withRel(de.muenchen.vaadin.demo.api.rest.BuergerResource.ADD_STAATSANGEHOERIGKEITEN));
        }
        return resource;
    }

    /**
     * Mapping einer Resource auf eine Entity
     *
     * @param resource
     * @param entity
     */
    public void fromResource(final BuergerResource resource, final Buerger entity) {
        if (!Strings.isNullOrEmpty(resource.getOid())) {
//            this.dozer.map(resource, entity);
            entity.setOid(resource.getOid());
            // start field mapping
            entity.setVorname(resource.getVorname());
            entity.setNachname(resource.getNachname());
            entity.setGeburtsdatum(resource.getGeburtsdatum());

            // end field mapping
        } else {
            LOG.error(resource.toString());
            throw new IllegalArgumentException("The object id (oid) field must be filled.");
        }
    }

    public List<BuergerResource> toResource(Set<Buerger> kinder, String hateoasRelations) {

        List<BuergerResource> resource = new ArrayList<>();
        kinder.stream().forEach((b) -> {
            resource.add(this.toResource(b, HateoasUtil.REL_SELF, HateoasUtil.REL_NEW, HateoasUtil.REL_DELETE, HateoasUtil.REL_UPDATE));
        });
        return resource;
    }

    /**
     *
     *
     * @param entity
     * @return
     */
    public BuergerResource assembleWithAllLinks(Buerger entity) {
        return this.toResource(entity,
                HateoasUtil.REL_SELF,
                HateoasUtil.REL_NEW,
                HateoasUtil.REL_DELETE,
                HateoasUtil.REL_UPDATE,
                HateoasUtil.REL_COPY,
                HateoasUtil.REL_COPY_LIST,
                HateoasUtil.REL_DELETE_LIST,
                // Relationen
                de.muenchen.vaadin.demo.api.rest.BuergerResource.WOHNUNGEN,
                de.muenchen.vaadin.demo.api.rest.BuergerResource.SAVE_WOHNUNG,
                de.muenchen.vaadin.demo.api.rest.BuergerResource.SACHBEARBEITER,
                de.muenchen.vaadin.demo.api.rest.BuergerResource.SAVE_SACHBEARBEITER,
                de.muenchen.vaadin.demo.api.rest.BuergerResource.KINDER,
                de.muenchen.vaadin.demo.api.rest.BuergerResource.SAVE_KIND,
                de.muenchen.vaadin.demo.api.rest.BuergerResource.ADD_KIND,
                de.muenchen.vaadin.demo.api.rest.BuergerResource.PAESSE,
                de.muenchen.vaadin.demo.api.rest.BuergerResource.SAVE_PASS,
                de.muenchen.vaadin.demo.api.rest.BuergerResource.STAATSANGEHOERIGKEITEN,
                de.muenchen.vaadin.demo.api.rest.BuergerResource.ELTERN,
                de.muenchen.vaadin.demo.api.rest.BuergerResource.RELEASE_ELTERN,
                de.muenchen.vaadin.demo.api.rest.BuergerResource.RELEASE_KINDER,
                de.muenchen.vaadin.demo.api.rest.BuergerResource.RELEASE_PAESSE,
                de.muenchen.vaadin.demo.api.rest.BuergerResource.RELEASE_WOHNUNGEN,
                de.muenchen.vaadin.demo.api.rest.BuergerResource.RELEASE_SACHBEARBEITER,
                de.muenchen.vaadin.demo.api.rest.BuergerResource.RELEASE_WOHNUNG,
                de.muenchen.vaadin.demo.api.rest.BuergerResource.RELEASE_ELTERNTEIL,
                de.muenchen.vaadin.demo.api.rest.BuergerResource.ADD_KIND,
                de.muenchen.vaadin.demo.api.rest.BuergerResource.ADD_PASS,
                de.muenchen.vaadin.demo.api.rest.BuergerResource.ADD_SACHBEARBEITER,
                de.muenchen.vaadin.demo.api.rest.BuergerResource.ADD_STAATSANGEHOERIGKEITEN,
                de.muenchen.vaadin.demo.api.rest.BuergerResource.ADD_WOHNUNG,
                de.muenchen.vaadin.demo.api.rest.BuergerResource.RELEASE_SACHBEARBEITER,
                de.muenchen.vaadin.demo.api.rest.BuergerResource.PARTNER,
                de.muenchen.vaadin.demo.api.rest.BuergerResource.ADD_PARTNER,
                de.muenchen.vaadin.demo.api.rest.BuergerResource.SAVE_PARTNER
        );
    }

}
