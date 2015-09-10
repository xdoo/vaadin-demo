package de.muenchen.demo.service.rest.api;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import de.muenchen.demo.service.domain.Sachbearbeiter;
import de.muenchen.demo.service.rest.SachbearbeiterController;
import de.muenchen.demo.service.services.SachbearbeiterService;
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
 * @author claus.straube
 */
@Service
public class SachbearbeiterResourceAssembler {

    private static final Logger LOG = LoggerFactory.getLogger(SachbearbeiterResourceAssembler.class);

    @Autowired
    DozerBeanMapper dozer;
    @Autowired
    SachbearbeiterService service;
    @Autowired
    EntityLinks entityLinks;

    /**
     * Mapping einer Liste von Entities auf eine List von Resource Objekten.
     *
     * @param Sachbearbeiter
     * @return
     */
    public SearchResultResource<SachbearbeiterResource> toResource(final List<Sachbearbeiter> Sachbearbeiter) {
        SearchResultResource<SachbearbeiterResource> resource = new SearchResultResource<>();
        Sachbearbeiter.stream().forEach((b) -> {
            resource.add(this.toResource(b, HateoasUtil.REL_SELF, HateoasUtil.REL_NEW, HateoasUtil.REL_DELETE, HateoasUtil.REL_UPDATE));
        });
        // add query link
        resource.add(linkTo(methodOn(SachbearbeiterController.class).querySachbearbeiter()).withRel(HateoasUtil.REL_QUERY));
        return resource;
    }

    /**
     * Mapping einer Entity auf eine Resource.
     *
     * @param Sachbearbeiter
     * @param r
     * @return
     */
    public SachbearbeiterResource toResource(final Sachbearbeiter Sachbearbeiter, String... r) {
        // map
        SachbearbeiterResource resource = this.dozer.map(Sachbearbeiter, SachbearbeiterResource.class);

        // add links
        ArrayList<String> relations = Lists.newArrayList(r);
        if (relations.contains(HateoasUtil.REL_NEW)) {
            resource.add(linkTo(methodOn(SachbearbeiterController.class).newSachbearbeiter()).withRel(HateoasUtil.REL_NEW));

        }

        if (relations.contains(HateoasUtil.REL_UPDATE)) {
            resource.add(linkTo(methodOn(SachbearbeiterController.class).updateSachbearbeiter(Sachbearbeiter.getOid(), null)).withRel(HateoasUtil.REL_UPDATE));
        }

        if (relations.contains(HateoasUtil.REL_SELF)) {
            resource.add(linkTo(methodOn(SachbearbeiterController.class).readSachbearbeiter(Sachbearbeiter.getOid())).withSelfRel());

        }

        if (relations.contains(HateoasUtil.REL_DELETE)) {
            resource.add(linkTo(methodOn(SachbearbeiterController.class).deleteSachbearbeiter(Sachbearbeiter.getOid())).withRel(HateoasUtil.REL_DELETE));
        }

        if (relations.contains(HateoasUtil.REL_SAVE)) {
            resource.add(linkTo(methodOn(SachbearbeiterController.class).saveSachbearbeiter(null)).withRel(HateoasUtil.REL_SAVE));
        }

        if (relations.contains(HateoasUtil.REL_COPY)) {
            resource.add(linkTo(methodOn(SachbearbeiterController.class).copySachbearbeiter(Sachbearbeiter.getOid())).withRel(HateoasUtil.REL_COPY));
        }
        if (relations.contains(SachbearbeiterResource.BUERGER)) {
            resource.add(linkTo(methodOn(SachbearbeiterController.class).readSachbearbeiterBuerger(Sachbearbeiter.getOid())).withRel(SachbearbeiterResource.BUERGER));
        }

        if (relations.contains(SachbearbeiterResource.SAVE_BUERGER)) {
            resource.add(linkTo(methodOn(SachbearbeiterController.class).createBuergerSachbearbeiter(Sachbearbeiter.getOid(), null)).withRel(SachbearbeiterResource.SAVE_BUERGER));
        }
        if (relations.contains(SachbearbeiterResource.RELEASE_BUERGER)) {
            resource.add(linkTo(methodOn(SachbearbeiterController.class).releaseSachbearbeiterAllBuerger(Sachbearbeiter.getOid())).withRel(SachbearbeiterResource.RELEASE_BUERGER));
        }
        if (relations.contains(SachbearbeiterResource.USER)) {
            resource.add(linkTo(methodOn(SachbearbeiterController.class).readSachbearbeiterUser(Sachbearbeiter.getOid())).withRel(SachbearbeiterResource.USER));
        }

        if (relations.contains(SachbearbeiterResource.SAVE_USER)) {
            resource.add(linkTo(methodOn(SachbearbeiterController.class).createUserSachbearbeiter(Sachbearbeiter.getOid(), null)).withRel(SachbearbeiterResource.SAVE_USER));
        }

        if (relations.contains(SachbearbeiterResource.RELEASE_USER)) {
            resource.add(linkTo(methodOn(SachbearbeiterController.class).releaseSachbearbeiterUser(Sachbearbeiter.getOid())).withRel(SachbearbeiterResource.RELEASE_USER));
        }

        return resource;
    }

    /**
     * Mapping einer Resource auf eine Entity
     *
     * @param resource
     * @param entity
     */
    public void fromResource(final SachbearbeiterResource resource, final Sachbearbeiter entity) {
        if (!Strings.isNullOrEmpty(resource.getOid())) {
//            this.dozer.map(resource, entity);
            entity.setOid(resource.getOid());
            // start field mapping
            entity.setFax(resource.getFax());
            entity.setFunktion(resource.getFunktion());
            entity.setTelephone(resource.getTelephone());
            entity.setOrganisationseinheit(resource.getOrganisationseinheit());
            // end field mapping
        } else {
            LOG.error(resource.toString());
            throw new IllegalArgumentException("The object id (oid) field must be filled.");
        }
    }

    /**
     * Mapping liste Resource auf liste Entity
     *
     * @param Sachbearbeiter
     * @param hateoasRelations
     * @return
     */
    public List<SachbearbeiterResource> toResource(Set<Sachbearbeiter> Sachbearbeiter, String hateoasRelations) {

        List<SachbearbeiterResource> resource = new ArrayList<>();
        Sachbearbeiter.stream().forEach((b) -> {
            resource.add(this.toResource(b, HateoasUtil.REL_SELF, HateoasUtil.REL_NEW, HateoasUtil.REL_DELETE, HateoasUtil.REL_UPDATE));
        });
        return resource;
    }

    public SachbearbeiterResource assembleWithAllLinks(Sachbearbeiter entity) {
        return this.toResource(entity,
                HateoasUtil.REL_SELF,
                HateoasUtil.REL_NEW,
                HateoasUtil.REL_DELETE,
                HateoasUtil.REL_UPDATE,
                HateoasUtil.REL_COPY,
                SachbearbeiterResource.BUERGER,
                SachbearbeiterResource.SAVE_BUERGER,
                SachbearbeiterResource.RELEASE_BUERGER,
                SachbearbeiterResource.USER,
                SachbearbeiterResource.SAVE_USER,
                SachbearbeiterResource.RELEASE_USER
        );
    }

}
