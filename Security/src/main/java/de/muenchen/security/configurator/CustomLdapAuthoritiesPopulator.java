package de.muenchen.security.configurator;

import de.muenchen.security.entities.User;
import de.muenchen.security.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.ldap.userdetails.LdapAuthoritiesPopulator;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.HashSet;

/**
 * Anbindung an die Permissions, die in der DB stehen.
 *
 * @author rene.zarwel
 */
@Configuration
@ComponentScan
@EnableWebSecurity
public class CustomLdapAuthoritiesPopulator implements LdapAuthoritiesPopulator {

    private static final Logger LOG = LoggerFactory.getLogger(CustomLdapAuthoritiesPopulator.class);


    @Autowired
    private UserRepository userRepository;

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

        User user = userRepository.findFirstByUsername(username);

        user.getAuthoritys().stream().forEach(userAuthority -> {

            String authorityName = userAuthority.getAuthority();

            userAuthority.getPermissions().stream().forEach(authorityPermission -> {

                String authName = authorityPermission.getPermission();

                gas.add(new SimpleGrantedAuthority(authName));

            });


        });

        LOG.info("User " + username + " got Permissions:"+ gas.toString());

        return gas;
    }
}
