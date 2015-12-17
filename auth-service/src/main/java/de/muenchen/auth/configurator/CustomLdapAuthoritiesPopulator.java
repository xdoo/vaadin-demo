package de.muenchen.auth.configurator;

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

import javax.persistence.EntityManager;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Anbindung an die Permissions, die in der DB stehen.
 *
 * @author rene.zarwel
 */
@Configuration
@ComponentScan
@EnableWebSecurity
public class CustomLdapAuthoritiesPopulator implements LdapAuthoritiesPopulator {

    public static final String QUERY =
            "SELECT _PERMISSIONS.PERM_PERMISSION " +
                    "FROM _USERS_AUTHORITIES " +
                    "JOIN _USERS on _USERS_AUTHORITIES.USER_OID = _USERS.OID " +
                    "JOIN _AUTHORITIES on _USERS_AUTHORITIES.AUTHORITY_OID  = _AUTHORITIES.OID " +
                    "JOIN _AUTHORITIES_PERMISSIONS on _AUTHORITIES_PERMISSIONS.AUTHORITY_OID = _USERS_AUTHORITIES.AUTHORITY_OID " +
                    "JOIN _PERMISSIONS ON _AUTHORITIES_PERMISSIONS.PERMISSION_OID = _PERMISSIONS.OID WHERE _USERS.USER_USERNAME = \'%s\'";
    private static final Logger LOG = LoggerFactory.getLogger(CustomLdapAuthoritiesPopulator.class);
    @Autowired
    private EntityManager entityManager;

    @Override
    @Transactional(readOnly = true)
    public Collection<? extends GrantedAuthority> getGrantedAuthorities(
            DirContextOperations userData, String username) {

        @SuppressWarnings("unchecked")
        final List<String> resultList = entityManager.createNativeQuery(
                String.format(QUERY, username)
        ).getResultList();

        LOG.info("User " + username + " got Permissions:" + resultList.toString());

        return resultList.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
    }
}
