package de.muenchen.demo.service.security;

import org.kohsuke.MetaInfServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;

import javax.sql.DataSource;

/**
 * Created by rene.zarwel on 21.08.15.
 */
@MetaInfServices
@Configuration
@Order(2)
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
                        + "JOIN USERS on USERS_AUTHORITYS.USER_ID = USERS.ID "
                        + "JOIN AUTHORITYS on USERS_AUTHORITYS.AUTHORITY_ID  = AUTHORITYS.ID "
                        + "JOIN AUTHORITYS_PERMISSIONS on AUTHORITYS_PERMISSIONS.AUTHORITY_ID = USERS_AUTHORITYS.AUTHORITY_ID "
                        + "JOIN PERMISSIONS ON AUTHORITYS_PERMISSIONS.PERMISSION_ID = PERMISSIONS.ID "
                        + "WHERE USERS.USER_USERNAME =  ?");

    }

    @Override
    public boolean accepts(String serviceTyp) {
        return (serviceTyp.equals("JDBC_Authentication"));
    }
}
