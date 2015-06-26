package de.muenchen.vaadin.services;

import org.springframework.hateoas.Link;

/**
 *
 * @author claus.straube
 */
public interface InfoService {
    
    /**
     * Erneuert die Liste der Service Links.
     */
    public void reload();
    
    /**
     * Gibt die URL für eine bestimmte Relation zurück.
     * 
     * @param rel z.B. buerger_new
     * @return URL
     */
    public Link getUrl(String rel);
    
}
