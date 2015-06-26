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
    public Buerger readBuerger(Long id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Buerger updateBuerger(Buerger buerger) {
        return client.updateBuerger(buerger);
    }

    @Override
    public void deleteBuerger(String oid) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Buerger> queryBuerger() {
        Link link = this.infoService.getUrl("buerger_query");
        ArrayList<Link> links = Lists.newArrayList(link.withRel(HateoasUtil.REL_QUERY));
        return client.queryBuerger(links);
    }

    @Override
    public Buerger copyBuerger(String oid) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
