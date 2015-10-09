/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package de.muenchen.security;

import de.muenchen.security.entities.User;
import de.muenchen.security.repositories.PermissionRepository;
import de.muenchen.security.repositories.UserRepository;
import de.muenchen.vaadin.demo.apilib.domain.Principal;
import de.muenchen.vaadin.demo.apilib.rest.SecurityRestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Optional;

/**
 * @author praktikant.tmar
 */
@Service
public class SecurityRestClientImpl implements SecurityRestClient {

    @Autowired
    UserRepository userRepository;
    @Autowired
    PermissionRepository permissionRepository;

    @Override
    public Optional<Principal> getPrincipal(RestTemplate template) {
        Principal principal = new Principal();
        ArrayList list = new ArrayList();
        principal.setRoles(list);
        ArrayList list2 = new ArrayList();
        principal.setPermissions(list2);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();
        principal.setUsername(name);

        User user = userRepository.findFirstByUsername(name);
        user.getAuthorities()
                .stream()
                .map((userAuthority1) -> {
                    principal.getRoles().add(userAuthority1.getAuthority());
                    return userAuthority1;
                })
                .map(authority -> authority.getPermissions().stream().map(
                        permission -> {
                            principal.getPermissions().add(permission.getPermission());
                            return permission;
                            }));
        return Optional.of(principal);
    }
}