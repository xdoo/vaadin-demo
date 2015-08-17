package de.muenchen.demo.service.rest.api;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import de.muenchen.demo.service.domain.Pass;
import de.muenchen.demo.service.rest.PassController;
import de.muenchen.demo.service.services.PassService;
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
            resource.add(this.toResource(b, HateoasUtil.REL_SELF, HateoasUtil.REL_NEW, HateoasUtil.REL_DELETE, HateoasUtil.REL_UPDATE));
        });
        // add query link
        resource.add(linkTo(methodOn(PassController.class).queryPass()).withRel(HateoasUtil.REL_QUERY));
        return resource;
    }

    /**
     * Mapping einer Entity auf eine Resource.
     *
     * @param pass
     * @param r
     * @return
     */
    public PassResource toResource(final Pass pass, String... r) {
        // map
        PassResource resource = this.dozer.map(pass, PassResource.class);

        // add links
        ArrayList<String> relations = Lists.newArrayList(r);
        if (relations.contains(HateoasUtil.REL_NEW)) {
            resource.add(linkTo(methodOn(PassController.class).newPass()).withRel(HateoasUtil.REL_NEW));

        }

        if (relations.contains(HateoasUtil.REL_UPDATE)) {
            resource.add(linkTo(methodOn(PassController.class).updatePass(pass.getOid(), null)).withRel(HateoasUtil.REL_UPDATE));
        }

        if (relations.contains(HateoasUtil.REL_SELF)) {
            resource.add(linkTo(methodOn(PassController.class).readPass(pass.getOid())).withSelfRel());
        }

        if (relations.contains(HateoasUtil.REL_DELETE)) {
            resource.add(linkTo(methodOn(PassController.class).deletePass(pass.getOid())).withRel(HateoasUtil.REL_DELETE));
        }

        if (relations.contains(HateoasUtil.REL_SAVE)) {
            resource.add(linkTo(methodOn(PassController.class).savePass(null)).withRel(HateoasUtil.REL_SAVE));
        }

        if (relations.contains(HateoasUtil.REL_COPY)) {
            resource.add(linkTo(methodOn(PassController.class).copyPass(pass.getOid())).withRel(HateoasUtil.REL_COPY));
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

    public List<PassResource> toResource(Set<Pass> kinder, String hateoasRelations) {

        List<PassResource> resource = new ArrayList<>();
        kinder.stream().forEach((b) -> {
            resource.add(this.toResource(b, HateoasUtil.REL_SELF, HateoasUtil.REL_NEW, HateoasUtil.REL_DELETE, HateoasUtil.REL_UPDATE));
        });
        return resource;
    }

}
