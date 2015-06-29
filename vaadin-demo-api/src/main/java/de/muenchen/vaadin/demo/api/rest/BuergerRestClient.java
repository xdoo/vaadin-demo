package de.muenchen.vaadin.demo.api.rest;

import de.muenchen.vaadin.demo.api.domain.Buerger;
import java.util.List;
import org.springframework.hateoas.Link;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author claus.straube
 */
public interface BuergerRestClient {
    
    public Buerger newBuerger(List<Link> links, RestTemplate restTemplate);
    
    public Buerger readBuerger(List<Link> links, RestTemplate restTemplate);
    
    public Buerger copyBuerger(List<Link> links, RestTemplate restTemplate);
    
    public List<Buerger> queryBuerger(List<Link> links, RestTemplate restTemplate);
    
    public Buerger updateBuerger(Buerger buerger, RestTemplate restTemplate);
    
    public void deleteBuerger(List<Link> links, RestTemplate restTemplate);
    
}
