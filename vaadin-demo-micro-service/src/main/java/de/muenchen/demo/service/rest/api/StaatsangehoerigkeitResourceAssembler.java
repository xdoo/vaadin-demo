package de.muenchen.demo.service.rest.api;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import de.muenchen.demo.service.domain.Staatsangehoerigkeit;
import de.muenchen.demo.service.rest.StaatsangehoerigkeitController;
import de.muenchen.demo.service.services.StaatsangehoerigkeitService;
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
 * @author praktikant.tmar
 */
@Service
public class StaatsangehoerigkeitResourceAssembler {

    private static final Logger LOG = LoggerFactory.getLogger(StaatsangehoerigkeitResourceAssembler.class);

    @Autowired
    DozerBeanMapper dozer;
    @Autowired
    StaatsangehoerigkeitService service;
    @Autowired
    EntityLinks entityLinks;

    /**
     * Mapping einer Liste von Entities auf eine List von Resource Objekten.
     *
     * @param staatsangehoerigkeit
     * @return
     */

    public SearchResultResource<StaatsangehoerigkeitResource> toResource(final List<Staatsangehoerigkeit> staatsangehoerigkeit) {
        SearchResultResource<StaatsangehoerigkeitResource> resource = new SearchResultResource<>();
        staatsangehoerigkeit.stream().forEach((b) -> {
            resource.add(this.toResource(b, HateoasUtil.REL_SELF, HateoasUtil.REL_NEW, HateoasUtil.REL_DELETE, HateoasUtil.REL_UPDATE));
        });
        // add query link
        resource.add(linkTo(methodOn(StaatsangehoerigkeitController.class).queryStaatsangehoerigkeit()).withRel(HateoasUtil.REL_QUERY));
        List<StaatsangehoerigkeitResource> liste = resource.getResult();
        return resource;
    }

    /**
     * Mapping einer Entity auf eine Resource.
     *
     * @param staatsangehoerigkeit
     * @param r
     * @return
     */
    public StaatsangehoerigkeitResource toResource(final Staatsangehoerigkeit staatsangehoerigkeit, String... r) {
        // map
        StaatsangehoerigkeitResource resource = this.dozer.map(staatsangehoerigkeit, StaatsangehoerigkeitResource.class);

        // add links
        ArrayList<String> relations = Lists.newArrayList(r);
      


        if (relations.contains(HateoasUtil.REL_SELF)) {
            resource.add(linkTo(methodOn(StaatsangehoerigkeitController.class).readStaatsangehoerigkeit(staatsangehoerigkeit.getReference())).withSelfRel());

        }

        if (relations.contains(HateoasUtil.REL_DELETE)) {
            resource.add(linkTo(methodOn(StaatsangehoerigkeitController.class).deleteStaatsangehoerigkeit(staatsangehoerigkeit.getReference())).withRel(HateoasUtil.REL_DELETE));
        }


        return resource;
    }

    /**
     * Mapping einer Resource auf eine Entity
     *
     * @param resource
     * @param entity
     */
    public void fromResource(final StaatsangehoerigkeitResource resource, final Staatsangehoerigkeit entity) {
        if (!Strings.isNullOrEmpty(resource.getOid())) {
            this.dozer.map(resource, entity);
            entity.setReference(resource.getReference());
            // start field mapping
            entity.setCode(resource.getCode());
            entity.setLand(resource.getLand());
            entity.setSprache(resource.getSprache());
            // end field mapping
        } else {
            LOG.error(resource.toString());
            throw new IllegalArgumentException("The object id (oid) field must be filled.");
        }
    }

    /**
     * Mapping liste Resource auf liste Entity
     *
     * @param staatsangehoerigkeit
     * @param hateoasRelations
     * @return
     */
    public List<StaatsangehoerigkeitResource> toResource(Set<Staatsangehoerigkeit> staatsangehoerigkeit, String hateoasRelations) {

        List<StaatsangehoerigkeitResource> resource = new ArrayList<>();
        staatsangehoerigkeit.stream().forEach((b) -> {
            resource.add(this.toResource(b, HateoasUtil.REL_SELF, HateoasUtil.REL_NEW, HateoasUtil.REL_DELETE, HateoasUtil.REL_UPDATE));
        });
        return resource;
    }

}
