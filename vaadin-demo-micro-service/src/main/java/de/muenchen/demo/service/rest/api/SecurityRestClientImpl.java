/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.demo.service.rest.api;

import de.muenchen.vaadin.demo.api.domain.Principal;
import de.muenchen.vaadin.demo.api.rest.SecurityRestClient;
import java.util.LinkedHashMap;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
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
    @Value("${info.url}")
    private String URL;
    @Override
    public Optional<Principal> getPrincipal(RestTemplate template) {
        Principal principal = new Principal();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();
        principal.setUsername(name);

        String URL21 = URL+"/userAuthority/user/" + name;
        SearchResultResource response2;
        response2 = template.getForEntity(URL21, SearchResultResource.class).getBody();

        for (Object userAuth : response2.getResult()) {
            LinkedHashMap userAuth2 = (LinkedHashMap) userAuth;

            for (Object a1 : userAuth2.values()) {
                LinkedHashMap userAuth3 = (LinkedHashMap) a1;

                for (Object z2 : userAuth3.values()) {
                    LinkedHashMap userAuth4 = (LinkedHashMap) z2;

                    String authority = (String) userAuth4.get("authority");
                    if (authority != null&& !principal.getRoles().contains(authority)) {
                        principal.getRoles().add(authority);
                        String URL31 = URL+"/authorityPermission/authority/" + authority;
                        SearchResultResource<AuthorityPermissionResource> response3;
                        response3 = template.getForEntity(URL31, SearchResultResource.class).getBody();

                        for (Object permission1 : response3.getResult()) {
                            LinkedHashMap perm1 = (LinkedHashMap) permission1;

                            for (Object permission2 : perm1.values()) {
                                LinkedHashMap perm2 = (LinkedHashMap) permission2;

                                for (Object permission3 : perm2.values()) {
                                    LinkedHashMap perm3 = (LinkedHashMap) permission3;

                                    String permission = (String) perm3.get("permision");
                                    if (permission != null && !principal.getPermissions().contains(permission)) {
                                        principal.getPermissions().add(permission);
                                    }
                                }

                            }

                        }
                    }

                }

            }

        }

        return Optional.of(principal);
    }

}
