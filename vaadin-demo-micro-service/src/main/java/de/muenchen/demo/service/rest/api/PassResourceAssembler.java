package de.muenchen.demo.service.rest.api;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import de.muenchen.demo.service.domain.Pass;
import de.muenchen.demo.service.rest.PassController;
import de.muenchen.demo.service.services.PassService;
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
public class PassResourceAssembler {

    private static final Logger LOG = LoggerFactory.getLogger(PassResourceAssembler.class);

    @Autowired
    DozerBeanMapper dozer;
    @Autowired
    PassService service;
    @Autowired
    EntityLinks entityLinks;

    /**
     * Mapping einer Liste von Entities auf eine List von Resource Objekten.
     *
     * @param pass
     * @return
     */
    public SearchResultResource<PassResource> toResource(final List<Pass> pass) {
        SearchResultResource<PassResource> resource = new SearchResultResource<>();
        pass.stream().forEach((b) -> {
            resource.add(this.toResource(b, HateoasRelations.SELF, HateoasRelations.NEW, HateoasRelations.DELETE, HateoasRelations.UPDATE));
        });
        // add query link
        resource.add(linkTo(methodOn(PassController.class).queryPass()).withRel(HateoasUtil.QUERY));
        return resource;
    }

    /**
     * Mapping einer Entity auf eine Resource.
     *
     * @param pass
     * @param r
     * @return
     */
    public PassResource toResource(final Pass pass, HateoasRelations... r) {
        // map
        PassResource resource = this.dozer.map(pass, PassResource.class);

        // add links
        ArrayList<HateoasRelations> relations = Lists.newArrayList(r);
        if (relations.contains(HateoasRelations.NEW)) {
            resource.add(linkTo(methodOn(PassController.class).newPass()).withRel(HateoasUtil.NEW));

        }

        if (relations.contains(HateoasRelations.UPDATE)) {
            resource.add(linkTo(methodOn(PassController.class).updatePass(pass.getOid(), null)).withRel(HateoasUtil.UPDATE));
        }

        if (relations.contains(HateoasRelations.SELF)) {
            resource.add(linkTo(methodOn(PassController.class).readPass(pass.getOid())).withSelfRel());
        }

        if (relations.contains(HateoasRelations.DELETE)) {
            resource.add(linkTo(methodOn(PassController.class).deletePass(pass.getOid())).withRel(HateoasUtil.DELETE));
        }

        if (relations.contains(HateoasRelations.SAVE)) {
            resource.add(linkTo(methodOn(PassController.class).savePass(null)).withRel(HateoasUtil.SAVE));
        }

        if (relations.contains(HateoasRelations.COPY)) {
            resource.add(linkTo(methodOn(PassController.class).copyPass(pass.getOid())).withRel(HateoasUtil.COPY));
        }

        return resource;
    }

    /**
     * Mapping einer Resource auf eine Entity
     *
     * @param resource
     * @param entity
     */
    public void fromResource(final PassResource resource, final Pass entity) {
        if (!Strings.isNullOrEmpty(resource.getOid())) {
//            this.dozer.map(resource, entity);
            entity.setOid(resource.getOid());
            // start field mapping
            entity.setAustellungsdatum(resource.getAustellungsdatum());
            entity.setBehoerde(resource.getBehoerde());
            entity.setGueltigBis(resource.getGueltigBis());
            entity.setKode(resource.getKode());
            entity.setTyp(resource.getTyp());
            entity.setPassNummer(resource.getPassNummer());
            entity.setStaatsangehoerigkeit(resource.getStaatsangehoerigkeit());
            entity.setGroesse(resource.getGroesse());
            entity.setAugenFarbe(resource.getAugenFarbe());
            entity.setStaatsangehoerigkeitReference(resource.getStaatsangehoerigkeitReference());
            // end field mapping
        } else {
            LOG.error(resource.toString());
            throw new IllegalArgumentException("The object id (oid) field must be filled.");
        }
    }

    public List<PassResource> toResource(Set<Pass> kinder, HateoasRelations hateoasRelations) {

        List<PassResource> resource = new ArrayList<>();
        kinder.stream().forEach((b) -> {
            resource.add(this.toResource(b, HateoasRelations.SELF, HateoasRelations.NEW, HateoasRelations.DELETE, HateoasRelations.UPDATE));
        });
        return resource;
    }

}
