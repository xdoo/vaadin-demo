package de.muenchen.vaadin.services;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import de.muenchen.vaadin.demo.api.local.LocalBuerger;
import de.muenchen.vaadin.demo.api.rest.BuergerRestClient;
import de.muenchen.vaadin.demo.api.rest.BuergerRestClientImpl;
import de.muenchen.vaadin.demo.apilib.services.SecurityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.web.client.RestTemplate;

import java.io.Serializable;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    public BuergerServiceImpl(InfoService infoService, SecurityService securityService) {
        this.infoService = infoService;
        this.securityService=securityService;

        //TODO
        this.client = new BuergerRestClientImpl(getTemplate(), URI.create("http://localhost:8080"));
    }

    @Override
    public LocalBuerger create(LocalBuerger buerger) {
        return client.create(buerger);
    }

    @Override
    public LocalBuerger update(LocalBuerger buerger) {
        return client.update(buerger);
    }

    @Override
    public void delete(Link link) {
        client.delete(link);
    }

    @Override
    public LocalBuerger copy(Link link) {
        Optional<LocalBuerger> original = findOne(link);
        if (!original.isPresent())
            return null;

        return create(original.get());
    }

    @Override
    public List<LocalBuerger> findAll() {
        return client.findAll();
    }

    @Override
    public List<LocalBuerger> findAll(Link relation) {
        return client.findAll(relation);
    }

    @Override
    public Optional<LocalBuerger> findOne(Link link) {
        return client.findOne(link);
    }

    @Override
    public List<LocalBuerger> queryBuerger(String query) {
        //    Link link = this.infoService.getUrl("buerger_query");
        //    ArrayList<Link> links = Lists.newArrayList(link.withRel(HateoasUtil.REL_QUERY));
        //    return client.queryBuerger(query, links, getTemplate());
        return new ArrayList<>();
    }

    @Override
    public void setRelations(Link link, List<Link> kinder) {
        client.setRelations(link, kinder);
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
