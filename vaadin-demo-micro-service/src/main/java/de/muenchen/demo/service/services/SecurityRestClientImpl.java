/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.demo.service.services;

import de.muenchen.demo.service.domain.UserAuthority;
import de.muenchen.vaadin.demo.api.domain.Principal;
import de.muenchen.vaadin.demo.api.rest.SecurityRestClient;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author praktikant.tmar
 */
@Service
public class SecurityRestClientImpl implements SecurityRestClient {

    @Autowired
    UserAuthorityService userAuthoriyService;
    @Autowired
    AuthorityPermissionService authorityPermissionService;
    @Override
    public Optional<Principal> getPrincipal(RestTemplate template) {
        Principal principal = new Principal();
        ArrayList list =new ArrayList();
        principal.setRoles(list);
        ArrayList list2 =new ArrayList();
        principal.setPermissions(list2);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();
        principal.setUsername(name);

        List<UserAuthority> userAuthority = userAuthoriyService.readByUsername(name);
        userAuthority.stream().map((userAuthority1) -> {
            principal.getRoles().add(userAuthority1.getId().getAuthority().getAuthority());
            return userAuthority1;
        }).map((userAuthority1) -> authorityPermissionService.readByAuthority(userAuthority1.getId().getAuthority().getAuthority())).forEach((authPerm) -> {
            authPerm.stream().forEach((authPerm1) -> {
                principal.getPermissions().add(authPerm1.getId().getPermission().getPermission());
            });
        });

        return Optional.of(principal);
    }

}
