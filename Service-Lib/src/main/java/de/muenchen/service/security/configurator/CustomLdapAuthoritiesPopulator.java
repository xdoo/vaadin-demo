package de.muenchen.service.security.configurator;

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

    private static final Logger LOG = LoggerFactory.getLogger(CustomLdapAuthoritiesPopulator.class);
    public static final String QUERY =
            "SELECT PERMISSIONS.PERM_PERMISSION " +
            "FROM USERS_AUTHORITIES " +
            "JOIN USERS on USERS_AUTHORITIES.USER_OID = USERS.OID " +
            "JOIN AUTHORITIES on USERS_AUTHORITIES.AUTHORITY_OID  = AUTHORITIES.OID " +
            "JOIN AUTHORITIES_PERMISSIONS on AUTHORITIES_PERMISSIONS.AUTHORITY_OID = USERS_AUTHORITIES.AUTHORITY_OID " +
            "JOIN PERMISSIONS ON AUTHORITIES_PERMISSIONS.PERMISSION_OID = PERMISSIONS.OID WHERE USERS.USER_USERNAME = \'%s\'";

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

        LOG.info("User " + username + " got Permissions:"+ resultList.toString());

        return resultList.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
    }
}
