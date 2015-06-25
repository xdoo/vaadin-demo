package com.catify.vaadin.demo.api.rest;

import com.catify.vaadin.demo.api.domain.Buerger;
import com.catify.vaadin.demo.api.hateoas.HateoasUtil;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author claus
 */
@Service
public class BuergerRestClient {
    
    protected static final Logger LOG = LoggerFactory.getLogger(BuergerRestClient.class);
    
    RestTemplate restTemplate = new RestTemplate();
    
    public Buerger newBuerger(List<Link> links) {
        return this.readSingleSource(HateoasUtil.REL_NEW, links);
    }
    
    public Buerger readBuerger(List<Link> links) {
        return this.readSingleSource(Link.REL_SELF, links);
    }
    
    public Buerger copyBuerger(List<Link> links) {
        return this.readSingleSource(HateoasUtil.REL_COPY, links);
    }
    
    public List<Buerger> queryBuerger(List<Link> links) {
        return this.readMultiSource(HateoasUtil.REL_QUERY, links);
    }
    
    public Buerger updateBuerger(Buerger buerger) {
        return this.writeSingleSource(HateoasUtil.REL_UPDATE, buerger);
    }
    
    public void deleteBuerger(List<Link> links) {
        this.readSingleSource(HateoasUtil.REL_DELETE, links);
    }
    
    public Buerger writeSingleSource(String rel, Buerger buerger) {
        Optional<Link> link = HateoasUtil.findLinkForRel(rel, buerger.getLinks());
        if(link.isPresent()) {
            ResponseEntity<BuergerResource> resource = restTemplate.postForEntity(link.get().getHref(), buerger, BuergerResource.class);
            return BuergerAssembler.fromResource(resource.getBody());
        }
        LOG.warn("Found no link to self.");
        return null;
    }
    
    public Buerger readSingleSource(String rel, List<Link> links) {
        Optional<Link> link = HateoasUtil.findLinkForRel(rel, links);
        if (link.isPresent()) {
            ResponseEntity<BuergerResource> resource = restTemplate.getForEntity(link.get().getHref(), BuergerResource.class);
            return BuergerAssembler.fromResource(resource.getBody());
        }
        LOG.warn("Found no link.");
        return null;
    }
    
    public List<Buerger> readMultiSource(String rel, List<Link> links) {
        Optional<Link> link = HateoasUtil.findLinkForRel(rel, links);
        if (link.isPresent()) {
            ResponseEntity<SearchResultResource> resource = this.restTemplate.getForEntity(link.get().getHref(), SearchResultResource.class);
            return BuergerAssembler.fromResources(resource.getBody().getResult());
        }
        LOG.warn("Found no link.");
        return null;
    }
    
}
