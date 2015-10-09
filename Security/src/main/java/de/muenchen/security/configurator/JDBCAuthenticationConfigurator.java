package de.muenchen.security.configurator;

import org.kohsuke.MetaInfServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;

import javax.sql.DataSource;

/**
 * Created by rene.zarwel on 21.08.15.
 */
@MetaInfServices
@Order(1)
public class JDBCAuthenticationConfigurator extends
        AuthenticationConfiguratorAdapter {

    @Autowired
    private DataSource dataSource;

    @Override
    public void init(AuthenticationManagerBuilder auth) throws
            Exception {
        auth
                .jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery("select USER_USERNAME,USER_PASSWORD, USER_ENABLED from USERS where USER_USERNAME =  ?")
                .authoritiesByUsernameQuery("SELECT USERS.USER_USERNAME, PERMISSIONS.PERM_PERMISSION "
                        + "FROM USERS_AUTHORITYS "
                        + "JOIN USERS on USERS_AUTHORITYS.USER_OID = USERS.OID "
                        + "JOIN AUTHORITYS on USERS_AUTHORITYS.AUTHORITY_OID  = AUTHORITYS.OID "
                        + "JOIN AUTHORITYS_PERMISSIONS on AUTHORITYS_PERMISSIONS.AUTHORITY_OID = USERS_AUTHORITYS.AUTHORITY_OID "
                        + "JOIN PERMISSIONS ON AUTHORITYS_PERMISSIONS.PERMISSION_OID = PERMISSIONS.OID "
                        + "WHERE USERS.USER_USERNAME =  ?");

    }

    @Override
    public boolean accepts(String serviceTyp) {
        return (serviceTyp.equals("JDBC_Authentication"));
    }
}
