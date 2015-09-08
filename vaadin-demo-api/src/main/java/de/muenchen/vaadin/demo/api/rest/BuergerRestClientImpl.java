package de.muenchen.vaadin.demo.api.rest;

import de.muenchen.vaadin.demo.api.domain.Buerger;
import de.muenchen.vaadin.demo.api.hateoas.HateoasUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

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
        return this.requestMultiSource(HttpMethod.GET, HateoasUtil.REL_QUERY, links, restTemplate);
    }
    
    @Override
    public List<Buerger> queryBuerger(String query, List<Link> links, RestTemplate restTemplate) {
        return this.requestMultiSource(HttpMethod.POST, HateoasUtil.REL_QUERY, links, restTemplate, new HttpEntity(query));
    }
    
    @Override
    public Buerger updateBuerger(Buerger buerger, RestTemplate restTemplate) {
        return this.updateSingleSource(HateoasUtil.REL_UPDATE, buerger, restTemplate);
    }
    
    @Override
    public Buerger saveBuerger(Buerger buerger, RestTemplate restTemplate) {
        return this.writeSingleSource(HateoasUtil.REL_SAVE, buerger, restTemplate);
    }
    
    @Override
    public void deleteBuerger(List<Link> links, RestTemplate restTemplate) {
        Optional<Link> link = HateoasUtil.findLinkForRel(HateoasUtil.REL_DELETE, links);
        if (link.isPresent()) {
            ResponseEntity<BuergerResource> exchange = restTemplate.exchange(link.get().getHref(), HttpMethod.DELETE, null, new ParameterizedTypeReference<BuergerResource>() {});
            LOG.debug(exchange.toString());
        }
    }
    
    @Override
    public List<Buerger> queryKinder(List<Link> links, RestTemplate restTemplate) {
        return this.requestMultiSource(HttpMethod.GET, "kinder", links, restTemplate);
    }

    @Override
    public List<Buerger> queryHistory(List<Link> links, RestTemplate restTemplate) {
        return this.requestMultiSource(HttpMethod.GET, "history", links, restTemplate);
    }

    @Override
    public List<Buerger> queryPartner(List<Link> links, RestTemplate restTemplate) {
        return this.requestMultiSource(HttpMethod.GET, "partner", links, restTemplate);
    }
    
    @Override
    public Buerger saveBuergerKind(Buerger buerger, Buerger kind, RestTemplate restTemplate) {
        Optional<Link> link = HateoasUtil.findLinkForRel(BuergerResource.SAVE_KIND, buerger.getLinks());
        LOG.warn("used Link: "+link.get().toString());
        return this.writeSingleSource(link, kind, restTemplate);
    }

    @Override
    public Buerger saveBuergerPartner(Buerger buerger, Buerger partner, RestTemplate restTemplate) {
        Optional<Link> link = HateoasUtil.findLinkForRel(BuergerResource.SAVE_PARTNER, buerger.getLinks());
        LOG.warn("used Link: "+link.get().toString());
        return this.writeSingleSource(link, partner, restTemplate);
    }
    
    
    @Override
    public Buerger addBuergerKind(Buerger buerger, Buerger kind, RestTemplate restTemplate) {
        Optional<Link> link = HateoasUtil.findLinkForRel(BuergerResource.ADD_KIND, buerger.getLinks());
        LOG.warn("used Link: "+link.get().toString());
        return this.writeSingleSource(link, kind.getOid(), restTemplate);
    }
    @Override
    public Buerger addBuergerPartner(Buerger buerger, Buerger partner, RestTemplate restTemplate) {
        Optional<Link> link =
                HateoasUtil.findLinkForRel(BuergerResource.ADD_PARTNER, buerger.getLinks());
        LOG.warn("used Link: "+link.get().toString());
        return this.writeSingleSource(link, partner.getOid(), restTemplate);
    }

    @Override
    public Buerger releaseBuergerElternteil(Buerger elternteil, Buerger kind, RestTemplate restTemplate) {
        Optional<Link> link = HateoasUtil.findLinkForRel(BuergerResource.RELEASE_ELTERNTEIL, kind.getLinks());
        if(link.isPresent()) {
            LOG.warn("used Link: " + link.get().toString());
            return this.writeSingleSource(link, elternteil.getOid(), restTemplate);
        }else
        return null;
    }

    @Override
    public Buerger releaseBuergerPartner(Buerger buerger, Buerger partner, RestTemplate restTemplate) {
        Optional<Link> link = HateoasUtil.findLinkForRel(BuergerResource.RELEASE_PARTNER,partner.getLinks());
        LOG.error(link.toString());
        if(link.isPresent()) {
            LOG.warn("used Link: " + link.get().toString());
            return this.writeSingleSource(link, buerger.getOid(), restTemplate);
        }else
            return null;
    }

    public Buerger writeSingleSource(String rel, Buerger buerger, RestTemplate restTemplate) {
        Optional<Link> link = HateoasUtil.findLinkForRel(rel, buerger.getLinks());
        return this.writeSingleSource(link, buerger, restTemplate);
    }
    
    public Buerger writeSingleSource(Optional<Link> link, Object buerger, RestTemplate restTemplate) {
        if(link.isPresent()) {
            ResponseEntity<BuergerResource> resource = restTemplate.postForEntity(link.get().getHref(), buerger, BuergerResource.class);
            return BuergerAssembler.fromResource(resource.getBody());
        }
        LOG.warn("Found no link to self.");
        return null;
    }

    public Buerger updateSingleSource(String rel, Buerger buerger, RestTemplate restTemplate) {
        Optional<Link> link = HateoasUtil.findLinkForRel(rel, buerger.getLinks());
        return this.updateSingleSource(link, buerger, restTemplate);
    }

    public Buerger updateSingleSource(Optional<Link> link, Buerger buerger, RestTemplate restTemplate) {
        if(link.isPresent()) {
            restTemplate.put(link.get().getHref(), buerger, BuergerResource.class);
            return buerger;
        }
        LOG.warn("Found no link to self.");
        return null;
    }
    
    public Buerger readSingleSource(String rel, List<Link> links, RestTemplate restTemplate) {
        Optional<Link> link = HateoasUtil.findLinkForRel(rel, links);
        if (link.isPresent()) {
            ResponseEntity<BuergerResource> exchange = restTemplate.exchange(link.get().getHref(), HttpMethod.GET, null, new ParameterizedTypeReference<BuergerResource>() {});
            LOG.debug(exchange.toString());
            return BuergerAssembler.fromResource(exchange.getBody());
        }
        LOG.warn("Found no link.");
        return null;
    }
    
    
    
    public List<Buerger> requestMultiSource(HttpMethod httpMethod, String rel, List<Link> links, RestTemplate restTemplate) {
        return this.requestMultiSource(httpMethod, rel, links, restTemplate, HttpEntity.EMPTY);
    }
    
    public List<Buerger> requestMultiSource(HttpMethod httpMethod, String rel, List<Link> links, RestTemplate restTemplate, HttpEntity payload) {
        Optional<Link> link = HateoasUtil.findLinkForRel(rel, links);
        if (link.isPresent()) {
            ResponseEntity<SearchResultResource<BuergerResource>> exchange = restTemplate.exchange(link.get().getHref(), httpMethod, payload, new ParameterizedTypeReference<SearchResultResource<BuergerResource>>() {});
            LOG.debug(exchange.toString());
            return BuergerAssembler.fromResources(exchange.getBody());
        }
        LOG.warn("Found no link. Link: " + link.toString());
        return null;
    }
    
}
