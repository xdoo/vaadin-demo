package de.muenchen.demo.service.security;

import de.muenchen.vaadin.demo.apilib.domain.Principal;
import de.muenchen.vaadin.demo.apilib.rest.SecurityRestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

/**
 * @author praktikant.tmar
 */
@Controller
@ExposesResourceFor(SecurityRestClientController.class)
@RequestMapping("/principal")
public class SecurityRestClientController {
    private static final Logger LOG = LoggerFactory.getLogger(SecurityRestClientController.class);

    @Autowired
    SecurityRestClient service;

    @PreAuthorize("hasRole('ROLE_READ_Principal')")
    @RequestMapping(method = {RequestMethod.GET})
    public ResponseEntity getPrincipal() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("update users");
        }
        RestTemplate rest = new RestTemplate();
        Optional<Principal> resource = service.getPrincipal(rest);
        if (resource.isPresent()) {
            return ResponseEntity.ok(resource.get());
        }
        return null;
    }
}