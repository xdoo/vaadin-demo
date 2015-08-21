package de.muenchen.vaadin.demo.api.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.ldap.DefaultSpringSecurityContextSource;
import org.springframework.security.ldap.authentication.LdapAuthenticationProvider;
import org.springframework.security.ldap.search.FilterBasedLdapUserSearch;
import org.springframework.security.ldap.userdetails.DefaultLdapAuthoritiesPopulator;

import java.util.Set;

/**
 * Created by rene.zarwel on 21.08.15.
 */
public class LDAPAuthenticationConfigurator extends
        AuthenticationConfiguratorAdapter {

    private final static String GROUP_SEARCH_BASE = "";

    private DefaultSpringSecurityContextSource contextSource;

    private DefaultLdapAuthoritiesPopulator authoritiesPopulator = new DefaultLdapAuthoritiesPopulator(contextSource, GROUP_SEARCH_BASE){
        @Override
        protected Set<GrantedAuthority> getAdditionalRoles(DirContextOperations user, String username) {

            //TODO Insert Magic here


            return super.getAdditionalRoles(user, username);
        }
    };

    @Override
    public void init(AuthenticationManagerBuilder auth) throws
            Exception {

        //TODO Configure LDAP Authentication

        auth
                .ldapAuthentication()
                .contextSource(contextSource)
                ;


    }

    @Override
    public boolean accepts(String serviceTyp) {
        return (serviceTyp.equals("LDAP_Authentication"));
    }
}

