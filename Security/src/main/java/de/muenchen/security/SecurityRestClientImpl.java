/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package de.muenchen.security;

import de.muenchen.security.entities.User;
import de.muenchen.security.repositories.UserRepository;
import de.muenchen.vaadin.demo.apilib.domain.Principal;
import de.muenchen.vaadin.demo.apilib.rest.SecurityRestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

/**
 * @author praktikant.tmar
 */
@Service
public class SecurityRestClientImpl implements SecurityRestClient {

    @Autowired
    UserRepository userRepository;

    @Override
    public Optional<Principal> getPrincipal(RestTemplate template) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Principal principal = new Principal();
        principal.setUsername(authentication.getName());

        User user = userRepository.findFirstByUsername(principal.getUsername());

        user.getAuthoritys()
                .stream()
                .peek(authority1 -> {
                    // Add Authorities
                    principal.getRoles().add(authority1.getAuthority());
                    // Add Permissions
                    authority1.getPermissions()
                            .forEach(permission11 -> principal.getPermissions().add(permission11.getPermission()));
                });
        return Optional.of(principal);
    }

}