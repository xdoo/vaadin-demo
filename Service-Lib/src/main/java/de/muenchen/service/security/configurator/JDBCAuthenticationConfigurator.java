package de.muenchen.service.security.configurator;

import org.kohsuke.MetaInfServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;

import javax.sql.DataSource;

/**
 * Created by rene.zarwel on 21.08.15.
 */
@MetaInfServices
@Order(1)
public class JDBCAuthenticationConfigurator extends GlobalAuthenticationConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Override
    public void init(AuthenticationManagerBuilder auth) throws
            Exception {
        auth
                .jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery("select USER_USERNAME, USER_PASSWORD, USER_ENABLED from USERS where USER_USERNAME = ? AND USER_PASSWORD IS NOT NULL")
                .authoritiesByUsernameQuery(
                        "SELECT USERS.USER_USERNAME, PERMISSIONS.PERM_PERMISSION " +
                                "FROM USERS_AUTHORITIES " +
                                "JOIN USERS on USERS_AUTHORITIES.USER_OID = USERS.OID " +
                                "JOIN AUTHORITIES on USERS_AUTHORITIES.AUTHORITY_OID  = AUTHORITIES.OID " +
                                "JOIN AUTHORITIES_PERMISSIONS on AUTHORITIES_PERMISSIONS.AUTHORITY_OID = USERS_AUTHORITIES.AUTHORITY_OID " +
                                "JOIN PERMISSIONS ON AUTHORITIES_PERMISSIONS.PERMISSION_OID = PERMISSION.OID " +
                                "WHERE USERS.USER_USERNAME = ?");

    }
}
