/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package de.muenchen.service.security;

import de.muenchen.vaadin.demo.apilib.domain.Principal;
import de.muenchen.vaadin.demo.apilib.rest.SecurityRestClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

/**
 * @author praktikant.tmar
 */
@Service
public class SecurityRestClientImpl implements SecurityRestClient {

    @Override
    public Optional<Principal> getPrincipal(RestTemplate template) {
        Principal principal = new Principal();
        return Optional.of(principal);
    }

}