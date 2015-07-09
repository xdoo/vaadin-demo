package de.muenchen.vaadin.services;

import com.google.common.collect.Maps;
import de.muenchen.vaadin.demo.api.hateoas.HateoasUtil;
import de.muenchen.vaadin.demo.api.rest.ServiceInfoRestClient;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Service;

/**
 *
 * @author claus.straube
 */
@Service
public class InfoServiceImpl implements InfoService {

    private static final Logger LOG = LoggerFactory.getLogger(InfoService.class);
    
    private final ServiceInfoRestClient client;

    @Autowired
    public InfoServiceImpl(ServiceInfoRestClient client) {
        this.client = client;
    }
    
    
    
    private Map<String, Link> links;
    
    /**
     * Lädt die Liste der Service Links initial.
     */
    @PostConstruct
    public void init() {
        this.loadLinks();
    }
    
    @Override
    public void reload() {
        this.loadLinks();
    }

    @Override
    public Link getUrl(String rel) {
        if(this.links.containsKey(rel)) {
            return this.links.get(rel);
        }
        LOG.warn(String.format("Cannot find relation '%s' in list of loaded links.%s", rel, HateoasUtil.links(links)));
        return null;
    }
    
    /**
     * Lädt die Liste der Service Links.
     */
    public void loadLinks() {
//        TODO > fix in service
        this.links = client.getServiceInfo().getEntityLinks();
//        this.links = new HashMap<>();
    }
    
}
