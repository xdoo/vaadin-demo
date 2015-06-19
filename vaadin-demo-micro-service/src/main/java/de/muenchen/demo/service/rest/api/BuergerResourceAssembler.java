package de.muenchen.demo.service.rest.api;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import de.muenchen.demo.service.domain.Buerger;
import de.muenchen.demo.service.rest.BuergerController;
import de.muenchen.demo.service.services.BuergerService;
import de.muenchen.demo.service.util.HateoasRelations;
import de.muenchen.demo.service.util.HateoasUtil;
import java.util.ArrayList;
import java.util.List;
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
public class BuergerResourceAssembler {

    private static final Logger LOG = LoggerFactory.getLogger(BuergerResourceAssembler.class);
    
    @Autowired DozerBeanMapper dozer;
    @Autowired BuergerService service;
    @Autowired EntityLinks entityLinks;
    
    /**
     * Mapping einer Liste von Entities auf eine List von Resource Objekten.
     * 
     * @param buerger
     * @return 
     */
    public SearchResultResource<BuergerResource> toResource(final List<Buerger> buerger) {
        SearchResultResource<BuergerResource> resource = new SearchResultResource<>();
        buerger.stream().forEach((b) -> {
            resource.add(this.toResource(b, HateoasRelations.SELF, HateoasRelations.NEW, HateoasRelations.DELETE, HateoasRelations.UPDATE));
        }); 
        // add query link
        resource.add(linkTo(methodOn(BuergerController.class).queryBuerger()).withRel(HateoasUtil.QUERY));
        return resource;
    }
    
    /**
     * Mapping einer Entity auf eine Resource.
     * 
     * @param buerger
     * @param r
     * @return 
     */
    public BuergerResource toResource(final Buerger buerger, HateoasRelations... r) {
        // map
        BuergerResource resource = this.dozer.map(buerger, BuergerResource.class);

        
        // add links
        ArrayList<HateoasRelations> relations = Lists.newArrayList(r);
        if(relations.contains(HateoasRelations.NEW)) {
           resource.add(linkTo(methodOn(BuergerController.class).newBuerger()).withRel(HateoasUtil.NEW)); 
        } 
        
        if(relations.contains(HateoasRelations.UPDATE)) {
            resource.add(linkTo(methodOn(BuergerController.class).updateBuerger(buerger.getOid(), null)).withRel(HateoasUtil.UPDATE));
        }
        
        if(relations.contains(HateoasRelations.SELF)) {
            resource.add(linkTo(methodOn(BuergerController.class).readBuerger(buerger.getOid())).withSelfRel());
        }
        
        if(relations.contains(HateoasRelations.DELETE)) {
            resource.add(linkTo(methodOn(BuergerController.class).deleteBuerger(buerger.getOid())).withRel(HateoasUtil.DELETE));
        }
        
        if(relations.contains(HateoasRelations.SAVE)) {
            resource.add(linkTo(methodOn(BuergerController.class).saveBuerger(null)).withRel(HateoasUtil.SAVE));
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
    public void fromResource(final BuergerResource resource, final Buerger entity) {
        if(!Strings.isNullOrEmpty(resource.getOid())) {
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

}
