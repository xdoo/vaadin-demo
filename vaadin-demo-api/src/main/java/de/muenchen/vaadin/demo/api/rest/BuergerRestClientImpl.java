package de.muenchen.vaadin.demo.api.rest;

import de.muenchen.vaadin.demo.api.domain.Buerger;
import de.muenchen.vaadin.demo.api.hateoas.HateoasUtil;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author claus
 */
@Service
public class BuergerRestClientImpl implements BuergerRestClient {
    
    protected static final Logger LOG = LoggerFactory.getLogger(BuergerRestClientImpl.class);
    
    @Override
    public Buerger newBuerger(List<Link> links, RestTemplate restTemplate) {
        return this.readSingleSource(HateoasUtil.REL_NEW, links, restTemplate);
    }
    
    @Override
    public Buerger readBuerger(List<Link> links, RestTemplate restTemplate) {
        return this.readSingleSource(Link.REL_SELF, links, restTemplate);
    }
    
    @Override
    public Buerger copyBuerger(List<Link> links, RestTemplate restTemplate) {
        return this.readSingleSource(HateoasUtil.REL_COPY, links, restTemplate);
    }
    
    @Override
    public List<Buerger> queryBuerger(List<Link> links, RestTemplate restTemplate) {
        return this.readMultiSource(HateoasUtil.REL_QUERY, links, restTemplate);
    }
    
    @Override
    public Buerger updateBuerger(Buerger buerger, RestTemplate restTemplate) {
        return this.writeSingleSource(HateoasUtil.REL_UPDATE, buerger, restTemplate);
    }
    
    @Override
    public void deleteBuerger(List<Link> links, RestTemplate restTemplate) {
        this.readSingleSource(HateoasUtil.REL_DELETE, links, restTemplate);
    }
    
    public Buerger writeSingleSource(String rel, Buerger buerger, RestTemplate restTemplate) {
        Optional<Link> link = HateoasUtil.findLinkForRel(rel, buerger.getLinks());
        if(link.isPresent()) {
            ResponseEntity<BuergerResource> resource = restTemplate.postForEntity(link.get().getHref(), buerger, BuergerResource.class);
            return BuergerAssembler.fromResource(resource.getBody());
        }
        LOG.warn("Found no link to self.");
        return null;
    }
    
    public Buerger readSingleSource(String rel, List<Link> links, RestTemplate restTemplate) {
        Optional<Link> link = HateoasUtil.findLinkForRel(rel, links);
        if (link.isPresent()) {
            ResponseEntity<Resource<BuergerResource>> exchange = restTemplate.exchange(link.get().getHref(), HttpMethod.GET, null, new ParameterizedTypeReference<Resource<BuergerResource>>() {});
//            ResponseEntity<BuergerResource> resource = restTemplate.getForEntity(link.get().getHref(), BuergerResource.class);
            return BuergerAssembler.fromResource(exchange.getBody());
        }
        LOG.warn("Found no link.");
        return null;
    }
    
    public List<Buerger> readMultiSource(String rel, List<Link> links, RestTemplate restTemplate) {
        Optional<Link> link = HateoasUtil.findLinkForRel(rel, links);
        if (link.isPresent()) {
            ResponseEntity<SearchResultResource> resource = restTemplate.getForEntity(link.get().getHref(), SearchResultResource.class);
            return BuergerAssembler.fromResources(resource.getBody().getResult());
        }
        LOG.warn("Found no link.");
        return null;
    }
    
}
