package de.muenchen.vaadin.guilib.services;

import de.muenchen.vaadin.demo.apilib.domain.DomainService;

import java.net.URI;

/**
 *
 * @author claus.straube
 */
public interface InfoService {
    
   public URI getBaseUri(DomainService service);
    
}
