package de.muenchen.demo.service.rest.api;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import de.muenchen.demo.service.domain.Adresse;
import de.muenchen.demo.service.rest.AdresseController;
import de.muenchen.demo.service.services.AdresseService;
import de.muenchen.vaadin.demo.api.hateoas.HateoasUtil;
import de.muenchen.vaadin.demo.api.rest.SearchResultResource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.dozer.DozerBeanMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.stereotype.Service;
import de.muenchen.vaadin.demo.api.rest.AdresseResource;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

/**
 *
 * @author praktikant.tmar
 */
@Service
public class AdresseResourceAssembler {

    private static final Logger LOG = LoggerFactory.getLogger(AdresseResourceAssembler.class);

    @Autowired
    DozerBeanMapper dozer;
    @Autowired
    AdresseService service;
    @Autowired
    EntityLinks entityLinks;

    /**
     * Mapping einer Liste von Entities auf eine List von Resource Objekten.
     *
     * @param adresse
     * @return
     */

    public SearchResultResource<AdresseResource> toResource(final List<Adresse> adresse) {
        SearchResultResource<AdresseResource> resource = new SearchResultResource<>();
        adresse.stream().forEach((b) -> {
            resource.add(this.toResource(b, HateoasUtil.REL_SELF, HateoasUtil.REL_NEW, HateoasUtil.REL_DELETE, HateoasUtil.REL_UPDATE));
        });
        // add query link
        resource.add(linkTo(methodOn(AdresseController.class).queryAdresse()).withRel(HateoasUtil.REL_QUERY));
        List<AdresseResource> liste = resource.getResult();
        return resource;
    }

    /**
     * Mapping einer Entity auf eine Resource.
     *
     * @param adresse
     * @param r
     * @return
     */
    public AdresseResource toResource(final Adresse adresse, String... r) {
        // map
        AdresseResource resource = this.dozer.map(adresse, AdresseResource.class);

        // add links
        ArrayList<String> relations = Lists.newArrayList(r);
        if (relations.contains(HateoasUtil.REL_NEW)) {
            resource.add(linkTo(methodOn(AdresseController.class).newAdresse()).withRel(HateoasUtil.REL_NEW));

        }

        if (relations.contains(HateoasUtil.REL_UPDATE)) {
            resource.add(linkTo(methodOn(AdresseController.class).updateAdresse(adresse.getOid(), null)).withRel(HateoasUtil.REL_UPDATE));
        }

        if (relations.contains(HateoasUtil.REL_SELF)) {
            resource.add(linkTo(methodOn(AdresseController.class).readAdresse(adresse.getOid())).withSelfRel());

        }

        if (relations.contains(HateoasUtil.REL_DELETE)) {
            resource.add(linkTo(methodOn(AdresseController.class).deleteAdresse(adresse.getOid())).withRel(HateoasUtil.REL_DELETE));
        }

        if (relations.contains(HateoasUtil.REL_SAVE)) {
            resource.add(linkTo(methodOn(AdresseController.class).saveAdresse(null)).withRel(HateoasUtil.REL_SAVE));
        }

        if (relations.contains(HateoasUtil.REL_COPY)) {
            resource.add(linkTo(methodOn(AdresseController.class).copyAdresse(adresse.getOid())).withRel(HateoasUtil.REL_COPY));
        }

        return resource;
    }

    /**
     * Mapping einer Resource auf eine Entity
     *
     * @param resource
     * @param entity
     */
    public void fromResource(final AdresseResource resource, final Adresse entity) {
        if (!Strings.isNullOrEmpty(resource.getOid())) {
            this.dozer.map(resource, entity);
            entity.setOid(resource.getOid());
            // start field mapping
            entity.setStrasse(resource.getStrasse());
            entity.setHausnummer(resource.getHausnummer());
            entity.setPlz(resource.getPlz());
            entity.setStadt(resource.getStadt());
            // end field mapping
        } else {
            LOG.error(resource.toString());
            throw new IllegalArgumentException("The object id (oid) field must be filled.");
        }
    }

    /**
     * Mapping liste Resource auf liste Entity
     *
     * @param adresse
     * @param hateoasRelations
     * @return
     */
    public List<AdresseResource> toResource(Set<Adresse> adresse, String hateoasRelations) {

        List<AdresseResource> resource = new ArrayList<>();
        adresse.stream().forEach((b) -> {
            resource.add(this.toResource(b, HateoasUtil.REL_SELF, HateoasUtil.REL_NEW, HateoasUtil.REL_DELETE, HateoasUtil.REL_UPDATE));
        });
        return resource;
    }

}
