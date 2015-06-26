package de.muenchen.demo.service.rest.api;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.springframework.hateoas.Link;


/**
 *
 * @author claus.straube
 */
public class ServiceInfoResource extends BaseResource {

    /**
     * Die OID ist in diesem Fall nicht gegenriert, sondern ist ein lesebares
     * Kürzel.
     * 
     * @param oid 
     * @param serviceName 
     * @param serviceVersion 
     */
    public ServiceInfoResource(String oid, String serviceName, String serviceVersion) {
        this.serviceName = serviceName;
        this.serviceVersion = serviceVersion;
        this.setOid(oid);
    }
    
    String serviceName;
    String serviceVersion;
    Map<String, Link> entityLinks = new HashMap<>();

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceVersion() {
        return serviceVersion;
    }

    public void setServiceVersion(String serviceVersion) {
        this.serviceVersion = serviceVersion;
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
