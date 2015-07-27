package de.muenchen.vaadin.demo.api.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import org.springframework.hateoas.Link;

/**
 *
 * @author claus.straube
 */
public class BaseEntity {
    
    /**
     * Ressource Identifier
     */
    @JsonIgnore private String id;
    private String oid;
    @JsonIgnore private List<Link> links;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }
    
}
