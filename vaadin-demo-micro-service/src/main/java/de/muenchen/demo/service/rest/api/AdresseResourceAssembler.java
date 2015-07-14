package de.muenchen.demo.service.rest.api;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import de.muenchen.demo.service.domain.Adresse;
import de.muenchen.demo.service.rest.AdresseController;
import de.muenchen.demo.service.services.AdresseService;
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
            resource.add(this.toResource(b, HateoasRelations.SELF, HateoasRelations.NEW, HateoasRelations.DELETE, HateoasRelations.UPDATE));
        });
        // add query link
        resource.add(linkTo(methodOn(AdresseController.class).queryAdresse()).withRel(HateoasUtil.QUERY));
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
    public AdresseResource toResource(final Adresse adresse, HateoasRelations... r) {
        // map
        AdresseResource resource = this.dozer.map(adresse, AdresseResource.class);

        // add links
        ArrayList<HateoasRelations> relations = Lists.newArrayList(r);
        if (relations.contains(HateoasRelations.NEW)) {
            resource.add(linkTo(methodOn(AdresseController.class).newAdresse()).withRel(HateoasUtil.NEW));

        }

        if (relations.contains(HateoasRelations.UPDATE)) {
            resource.add(linkTo(methodOn(AdresseController.class).updateAdresse(adresse.getOid(), null)).withRel(HateoasUtil.UPDATE));
        }

        if (relations.contains(HateoasRelations.SELF)) {
            resource.add(linkTo(methodOn(AdresseController.class).readAdresse(adresse.getOid())).withSelfRel());

        }

        if (relations.contains(HateoasRelations.DELETE)) {
            resource.add(linkTo(methodOn(AdresseController.class).deleteAdresse(adresse.getOid())).withRel(HateoasUtil.DELETE));
        }

        if (relations.contains(HateoasRelations.SAVE)) {
            resource.add(linkTo(methodOn(AdresseController.class).saveAdresse(null)).withRel(HateoasUtil.SAVE));
        }

        if (relations.contains(HateoasRelations.COPY)) {
            resource.add(linkTo(methodOn(AdresseController.class).copyAdresse(adresse.getOid())).withRel(HateoasUtil.COPY));
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
     * @param resource
     * @param entity
     * @return
     */
    public List<AdresseResource> toResource(Set<Adresse> adresse, HateoasRelations hateoasRelations) {

        List<AdresseResource> resource = new ArrayList<>();
        adresse.stream().forEach((b) -> {
            resource.add(this.toResource(b, HateoasRelations.SELF, HateoasRelations.NEW, HateoasRelations.DELETE, HateoasRelations.UPDATE));
        });
        return resource;
    }

}
