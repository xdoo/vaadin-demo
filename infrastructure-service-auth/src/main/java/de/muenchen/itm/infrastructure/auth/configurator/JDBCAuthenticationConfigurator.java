package de.muenchen.itm.infrastructure.auth.configurator;

import org.kohsuke.MetaInfServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;

import javax.sql.DataSource;

/**
 * Created by rene.zarwel on 21.08.15.
 */
@MetaInfServices
@Order(2)
@Configuration
public class JDBCAuthenticationConfigurator extends GlobalAuthenticationConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Override
    public void init(AuthenticationManagerBuilder auth) throws
            Exception {
        auth
                .jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery("select USER_USERNAME, USER_PASSWORD, USER_ENABLED, mandant from _USERS where USER_USERNAME = ? AND USER_PASSWORD IS NOT NULL")
                .authoritiesByUsernameQuery(
                        "SELECT _USERS.USER_USERNAME, _AUTHORITIES.AUTH_AUTHORITY " +
                                "FROM _USERS_AUTHORITIES " +
                                "JOIN _USERS on _USERS_AUTHORITIES.USER_OID = _USERS.OID " +
                                "JOIN _AUTHORITIES on _USERS_AUTHORITIES.AUTHORITY_OID  = _AUTHORITIES.OID " +
                                "JOIN _AUTHORITIES_PERMISSIONS on _AUTHORITIES_PERMISSIONS.AUTHORITY_OID = _USERS_AUTHORITIES.AUTHORITY_OID " +
                                "JOIN _PERMISSIONS ON _AUTHORITIES_PERMISSIONS.PERMISSION_OID = _PERMISSIONS.OID " +
                                "WHERE _USERS.USER_USERNAME = ?");

    }

}

