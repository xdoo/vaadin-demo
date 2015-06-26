package com.catify.vaadin.demo.api.rest;

import com.catify.vaadin.demo.api.domain.Buerger;
import com.catify.vaadin.demo.api.domain.ServiceInfo;
import com.catify.vaadin.demo.api.hateoas.HateoasUtil;
import static com.catify.vaadin.demo.api.rest.BuergerRestClientImpl.LOG;
import java.util.List;
import java.util.Optional;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author claus.straube
 */
@Service
public class ServiceRestClientImpl implements ServiceInfoRestClient {

    RestTemplate restTemplate = new RestTemplate();
    
    @Override
    public ServiceInfo getServiceInfo() {
        return readSingleSource(Link.REL_SELF, null);
    }
    
    public ServiceInfo readSingleSource(String rel, List<Link> links) {
        Optional<Link> link = HateoasUtil.findLinkForRel(rel, links);
        if (link.isPresent()) {
            ResponseEntity<ServiceInfoResource> resource = restTemplate.getForEntity(link.get().getHref(), ServiceInfoResource.class);
            return ServiceInfoAssembler.fromResource(resource.getBody());
        }
        LOG.warn("Found no link.");
        return null;
    }
    
}
