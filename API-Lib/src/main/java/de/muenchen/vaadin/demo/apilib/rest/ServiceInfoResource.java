package de.muenchen.vaadin.demo.apilib.rest;

import java.util.HashMap;
import java.util.Map;
import org.springframework.hateoas.Link;

/**
 *
 * @author claus.straube
 */
public class ServiceInfoResource extends BaseResource {
    
    private String name;
    private String version;
    private Map<String, Link> entityLinks = new HashMap<>();

    public ServiceInfoResource() {
    }

    public ServiceInfoResource(String name, String version, String oid) {
        this.name = name;
        this.version = version;
        this.setOid(oid);
    }
    
    

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Map<String, Link> getEntityLinks() {
        return entityLinks;
    }

    public void setEntityLinks(Map<String, Link> entityLinks) {
        this.entityLinks = entityLinks;
    }
    
    public void addEntityLink(Link link) {
        this.entityLinks.put(link.getRel(), link);
    }
    
}
