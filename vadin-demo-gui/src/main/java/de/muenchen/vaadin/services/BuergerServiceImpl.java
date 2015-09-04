package de.muenchen.vaadin.services;

import com.google.common.collect.Lists;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import de.muenchen.vaadin.demo.api.domain.Buerger;
import de.muenchen.vaadin.demo.api.hateoas.HateoasUtil;
import de.muenchen.vaadin.demo.api.rest.BuergerRestClient;
import de.muenchen.vaadin.demo.api.services.SecurityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.web.client.RestTemplate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
    private SecurityService securityService;

    @Autowired
    public BuergerServiceImpl(BuergerRestClient client, InfoService infoService, SecurityService securityService) {
        this.client = client;
        this.infoService = infoService;
        this.securityService=securityService;

        this.template = getTemplate();

    }

    @Override
    public Buerger createBuerger() {
        Link link = this.infoService.getUrl("buerger_new");
        ArrayList<Link> links = Lists.newArrayList(link.withRel(HateoasUtil.REL_NEW));
        return client.newBuerger(links, getTemplate());
    }

    @Override
    public Buerger readBuerger(Buerger entity) {
        return client.readBuerger(entity.getLinks(), getTemplate());
    }

    @Override
    public Buerger updateBuerger(Buerger buerger) {
        return client.updateBuerger(buerger, getTemplate());
    }

    @Override
    public Buerger saveBuerger(Buerger entity) {
       return client.saveBuerger(entity, getTemplate());
    }
    
    @Override
    public void deleteBuerger(Buerger buerger) {
        client.deleteBuerger(buerger.getLinks(), getTemplate());
    }

    @Override
    public List<Buerger> queryBuerger() {
        Link link = this.infoService.getUrl("buerger_query");
        ArrayList<Link> links = Lists.newArrayList(link.withRel(HateoasUtil.REL_QUERY));
        return client.queryBuerger(links, getTemplate());
    }

    @Override
    public List<Buerger> queryBuerger(String query) {
        Link link = this.infoService.getUrl("buerger_query");
        ArrayList<Link> links = Lists.newArrayList(link.withRel(HateoasUtil.REL_QUERY));
        return client.queryBuerger(query, links, getTemplate());
    }
    
    @Override
    public Buerger copyBuerger(Buerger entity) {
        return client.copyBuerger(entity.getLinks(), getTemplate());
    }

    @Override
    public List<Buerger> queryKinder(Buerger entity) {
        return client.queryKinder(entity.getLinks(), getTemplate());
    }

    @Override
    public List<Buerger> queryHistory(Buerger entity) {
        return client.queryHistory(entity.getLinks(), getTemplate());
    }

    @Override
    public List<Buerger> queryPartner(Buerger entity) {
        return client.queryPartner(entity.getLinks(), getTemplate());
    }

    @Override
    public Buerger saveKind(Buerger entity, Buerger kind) {
        return client.saveBuergerKind(entity, kind, getTemplate());
    }
    @Override
    public Buerger savePartner(Buerger entity, Buerger partner) {
        return client.saveBuergerPartner(entity, partner, getTemplate());
    }
    
    @Override
    public Buerger addKind(Buerger entity, Buerger kind) {
        return client.addBuergerKind(entity, kind, getTemplate());
    }

    @Override
    public Buerger addPartner(Buerger entity, Buerger partner) {
        return client.addBuergerPartner(entity, partner, getTemplate());
    }

    @Override
    public Buerger releaseElternteil(Buerger elternteil, Buerger kind){
        return client.releaseBuergerElternteil(elternteil, kind, getTemplate());
    }


    /**
     * Gets the resttemplate from the security if not present
     * @return resttemplate of this session
     */
    public RestTemplate getTemplate() {
        if (template != null) {
            return template;
        }
        if (securityService.getRestTemplate().isPresent()) {
            return securityService.getRestTemplate().get();
        }
        LOG.warn("Cannot acquire rest template from security service.");
        return null;

    }
  
}
