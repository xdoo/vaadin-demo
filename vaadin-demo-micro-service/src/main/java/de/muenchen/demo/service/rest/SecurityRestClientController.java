/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.demo.service.rest;

import de.muenchen.vaadin.demo.api.domain.Principal;
import de.muenchen.vaadin.demo.api.rest.SecurityRestClient;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author praktikant.tmar
 */
@Controller
@ExposesResourceFor(SecurityRestClientController.class)
@RequestMapping("/principal")
public class SecurityRestClientController {

    private static final Logger LOG = LoggerFactory.getLogger(SecurityRestClientController.class);
    @Autowired
    SecurityRestClient service;

    @Secured({"PERM_getPrincipal"})
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
