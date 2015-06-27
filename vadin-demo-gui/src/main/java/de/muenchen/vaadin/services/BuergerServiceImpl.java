package de.muenchen.vaadin.services;

import com.catify.vaadin.demo.api.domain.Buerger;
import com.catify.vaadin.demo.api.hateoas.HateoasUtil;
import com.catify.vaadin.demo.api.rest.BuergerRestClient;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Service;

/**
 *
 * @author claus.straube
 */
@Service
public class BuergerServiceImpl implements BuergerService {
    
    @Autowired BuergerRestClient client;
    @Autowired InfoService infoService;

    @Override
    public Buerger createBuerger() {
        Link link = this.infoService.getUrl("buerger_new");
        ArrayList<Link> links = Lists.newArrayList(link.withRel(HateoasUtil.REL_NEW));
        return client.newBuerger(links);
    }

    @Override
    public Buerger readBuerger(Buerger entity) {
        return client.readBuerger(entity.getLinks());
    }

    @Override
    public Buerger updateBuerger(Buerger buerger) {
        return client.updateBuerger(buerger);
    }

    @Override
    public void deleteBuerger(Buerger buerger) {
        client.deleteBuerger(buerger.getLinks());
    }

    @Override
    public List<Buerger> queryBuerger() {
        Link link = this.infoService.getUrl("buerger_query");
        ArrayList<Link> links = Lists.newArrayList(link.withRel(HateoasUtil.REL_QUERY));
        return client.queryBuerger(links);
    }

    @Override
    public Buerger copyBuerger(Buerger buerger) {
        return client.copyBuerger(buerger.getLinks());
    }
    
}
