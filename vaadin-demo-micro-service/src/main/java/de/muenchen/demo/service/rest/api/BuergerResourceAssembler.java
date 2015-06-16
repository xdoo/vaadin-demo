package de.muenchen.demo.service.rest.api;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import de.muenchen.demo.service.domain.Buerger;
import de.muenchen.demo.service.services.BuergerService;
import java.util.List;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author claus.straube
 */
@Service
public class BuergerResourceAssembler {

    @Autowired DozerBeanMapper dozer;
    @Autowired BuergerService service;
    
    /**
     * 
     * @param buerger
     * @return 
     */
    public List<BuergerResource> toResource(List<Buerger> buerger) {
        List<BuergerResource> result = Lists.newArrayList();
        buerger.stream().forEach((b) -> {
            result.add(this.toResource(b));
        });
        return result;
    }
    
    /**
     * 
     * @param buerger
     * @return 
     */
    public BuergerResource toResource(Buerger buerger) {
        BuergerResource resource = this.dozer.map(buerger, BuergerResource.class);
        return resource;
    }
    
    /**
     * 
     * @param resource
     * @return 
     */
    public List<Buerger> fromResource(List<BuergerResource> resource) {
        List<Buerger> result = Lists.newArrayList();
        resource.stream().forEach((r) -> {
            result.add(this.fromResource(r));
        });
        return result;
    }

    /**
     * 
     * 
     * @param resource
     * @return 
     */
    public Buerger fromResource(BuergerResource resource) {
        if(!Strings.isNullOrEmpty(resource.getOid())) {
            Buerger entity = service.read(resource.getOid());
            this.dozer.map(resource, entity);
            return entity;
        } else {
            throw new IllegalArgumentException("The object id (oid) field must be filled.");
        }
    }

}
