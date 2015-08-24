package de.muenchen.demo.service.security;

import de.muenchen.demo.service.domain.User;
import de.muenchen.demo.service.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.ldap.userdetails.LdapAuthoritiesPopulator;

import java.util.Collection;
import java.util.HashSet;

/**
 * Anbindung an die Rollenzuordnung, die in der DB steht.
 *
 * @author m.kurz
 */
@Configuration
@ComponentScan
@EnableWebSecurity
public class CustomLdapAuthoritiesPopulator implements LdapAuthoritiesPopulator {

    @Autowired
    private UserService userService;
    
    @Override
    public Collection<? extends GrantedAuthority> getGrantedAuthorities(
            DirContextOperations userData, String username) {
        Collection<GrantedAuthority> gas = new HashSet<GrantedAuthority>();
        // hier gehts weiter
        User benutzer = userService.readByUsername(username);

        //gas.add(new SimpleGrantedAuthority(benutzer.getRole()));
        gas.add(new SimpleGrantedAuthority("PERM_queryBuerger"));
        return gas;
    }
}
