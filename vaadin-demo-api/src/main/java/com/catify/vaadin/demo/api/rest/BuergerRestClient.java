package com.catify.vaadin.demo.api.rest;

import com.catify.vaadin.demo.api.domain.Buerger;
import java.util.List;
import org.springframework.hateoas.Link;

/**
 *
 * @author claus.straube
 */
public interface BuergerRestClient {
    
    public Buerger newBuerger(List<Link> links);
    
    public Buerger readBuerger(List<Link> links);
    
    public Buerger copyBuerger(List<Link> links);
    
    public List<Buerger> queryBuerger(List<Link> links);
    
    public Buerger updateBuerger(Buerger buerger);
    
    public void deleteBuerger(List<Link> links);
    
}
