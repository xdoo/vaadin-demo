package de.muenchen.demo.service.security;

import de.muenchen.demo.service.domain.AuthorityPermission;
import de.muenchen.demo.service.domain.AuthorityPermissionRepository;
import de.muenchen.demo.service.domain.UserAuthority;
import de.muenchen.demo.service.domain.UserAuthorityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.ldap.userdetails.LdapAuthoritiesPopulator;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * Anbindung an die Permissions, die in der DB stehen.
 *
 * @author rene.zarwel
 */
@Configuration
@ComponentScan
public class CustomLdapAuthoritiesPopulator implements LdapAuthoritiesPopulator {

    private static final Logger LOG = LoggerFactory.getLogger(CustomLdapAuthoritiesPopulator.class);

    @Autowired
    private UserAuthorityRepository userAuthRepository;

    @Autowired
    private AuthorityPermissionRepository authorityPermissionRepository;

    @Override
    @Transactional(readOnly = true)
    public Collection<? extends GrantedAuthority> getGrantedAuthorities(
            DirContextOperations userData, String username) {
        Collection<GrantedAuthority> gas = new HashSet<>();

        /*
        Gets a list of all authorities of this user
        and with this authorities all permissions of this user.

        Afterwards returns a list of all permissions of this user.
         */

        List<UserAuthority> authorities = userAuthRepository.findByIdUserUsername(username);

        authorities.stream().forEach(userAuthority -> {

            String authorityName = userAuthority.getId().getAuthority().getAuthority();

            List<AuthorityPermission> authorityPermissionsList = authorityPermissionRepository.findByIdAuthorityAuthority(authorityName);

            authorityPermissionsList.stream().forEach(authorityPermission -> {

                String authName = authorityPermission.getId().getPermission().getPermission();

                gas.add(new SimpleGrantedAuthority(authName));

            });


        });

        LOG.info("User " + username + " got Permissions:"+ gas.toString());

        return gas;
    }
}
