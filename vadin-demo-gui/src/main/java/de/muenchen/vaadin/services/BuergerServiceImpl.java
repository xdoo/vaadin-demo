package de.muenchen.vaadin.services;

import de.muenchen.vaadin.demo.api.domain.Buerger;
import de.muenchen.vaadin.demo.api.hateoas.HateoasUtil;
import de.muenchen.vaadin.demo.api.rest.BuergerRestClient;
import de.muenchen.vaadin.demo.api.services.SecurityService;
import com.google.common.collect.Lists;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author claus.straube
 */
@SpringComponent @UIScope
public class BuergerServiceImpl implements BuergerService, Serializable {
    
    private static final Logger LOG = LoggerFactory.getLogger(BuergerService.class);
    
    private BuergerRestClient client;
    private InfoService infoService;
    private RestTemplate template;

    @Autowired
    public BuergerServiceImpl(BuergerRestClient client, InfoService infoService, SecurityService securityService) {
        this.client = client;
        this.infoService = infoService;
        if(securityService.getRestTemplate().isPresent()) {
            this.template = securityService.getRestTemplate().get();
        } else {
            LOG.warn("Cannot acquire rest template from security service.");
        }
    }

    @Override
    public Buerger createBuerger() {
        Link link = this.infoService.getUrl("buerger_new");
        ArrayList<Link> links = Lists.newArrayList(link.withRel(HateoasUtil.REL_NEW));
        return client.newBuerger(links, this.template);
    }

    @Override
    public Buerger readBuerger(Buerger entity) {
        return client.readBuerger(entity.getLinks(), this.template);
    }

    @Override
    public Buerger updateBuerger(Buerger buerger) {
        return client.updateBuerger(buerger, this.template);
    }

    @Override
    public Buerger saveBuerger(Buerger entity) {
       return client.saveBuerger(entity, this.template);
    }
    
    @Override
    public void deleteBuerger(Buerger buerger) {
        client.deleteBuerger(buerger.getLinks(), this.template);
    }

    @Override
    public List<Buerger> queryBuerger() {
        Link link = this.infoService.getUrl("buerger_query");
        ArrayList<Link> links = Lists.newArrayList(link.withRel(HateoasUtil.REL_QUERY));
        return client.queryBuerger(links, this.template);
    }

    @Override
    public List<Buerger> queryBuerger(String query) {
        Link link = this.infoService.getUrl("buerger_query");
        ArrayList<Link> links = Lists.newArrayList(link.withRel(HateoasUtil.REL_QUERY));
        return client.queryBuerger(query, links, this.template);
    }
    
    @Override
    public Buerger copyBuerger(Buerger buerger) {
        return client.copyBuerger(buerger.getLinks(), this.template);
    }
  
}
