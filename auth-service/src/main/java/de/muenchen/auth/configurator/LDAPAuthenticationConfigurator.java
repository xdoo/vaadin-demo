package de.muenchen.auth.configurator;

import org.kohsuke.MetaInfServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.ldap.DefaultSpringSecurityContextSource;

/**
 * Created by rene.zarwel on 21.08.15.
 */
@MetaInfServices
@Order(2)
public class LDAPAuthenticationConfigurator extends GlobalAuthenticationConfigurerAdapter {

    private final static String USER_SEARCH_BASE = "o=Landeshauptstadt München,c=de";

    private final static String USER_SEARCH_FILTER = "(&(objectClass=inetOrgPerson)(uid={0}))";

    private final static String CONTEXT_SOURCE = "ldap://ldap01.muenchen.de:389";

    @Autowired
    private CustomLdapAuthoritiesPopulator authPopulator;

    @Override
    public void init(AuthenticationManagerBuilder auth) throws
            Exception {

        auth.ldapAuthentication()
                .ldapAuthoritiesPopulator(authPopulator)
                .userSearchBase(USER_SEARCH_BASE)
                .userSearchFilter(USER_SEARCH_FILTER)
                .contextSource(ldapContextSource());


    }

    protected LdapContextSource ldapContextSource() {
        DefaultSpringSecurityContextSource contextSource
                = new DefaultSpringSecurityContextSource(CONTEXT_SOURCE);
        // Die beiden Properties müssen so gesetzt sein, sonst funktioniert die Authentifizierung nicht
        contextSource.setAnonymousReadOnly(true);
        contextSource.setCacheEnvironmentProperties(false);
        return contextSource;
    }
}

