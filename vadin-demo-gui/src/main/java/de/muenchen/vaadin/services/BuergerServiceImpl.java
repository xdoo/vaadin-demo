package de.muenchen.vaadin.services;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import de.muenchen.vaadin.demo.api.local.Buerger;
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
import java.util.List;
import java.util.Optional;

/**
 * @author claus.straube
 */
@SpringComponent
@UIScope
public class BuergerServiceImpl implements BuergerService, Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(BuergerService.class);

    private BuergerRestClient client;
    private InfoService infoService;
    private RestTemplate template;
    private SecurityService securityService;

    @Autowired
    public BuergerServiceImpl(InfoService infoService, SecurityService securityService) {
        this.infoService = infoService;
        this.securityService = securityService;

        //TODO
        this.client = new BuergerRestClientImpl(getTemplate(), URI.create("http://localhost:8080"));
    }

    @Override
    public Buerger create(Buerger buerger) {
        return client.create(buerger);
    }

    @Override
    public Buerger update(Buerger buerger) {
        return client.update(buerger);
    }

    @Override
    public void delete(Link link) {
        client.delete(link);
    }

    @Override
    public Buerger copy(Link link) {
        Optional<Buerger> original = findOne(link);
        if (!original.isPresent())
            return null;

        return create(original.get());
    }

    @Override
    public List<Buerger> findAll() {
        return client.findAll();
    }

    @Override
    public List<Buerger> findAll(Link relation) {
        return client.findAll(relation);
    }

    @Override
    public Optional<Buerger> findOne(Link link) {
        return client.findOne(link);
    }

    @Override
    public List<Buerger> queryBuerger(String query) {
        return client.findFullTextFuzzy(query);
    }

    @Override
    public void setRelations(Link link, List<Link> links) {
        client.setRelations(link, links);
    }

    @Override
    public void setRelation(Link link, Link relation) {
        client.setRelation(link, relation);
    }

    /**
     * Gets the resttemplate from the security if not present
     *
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
