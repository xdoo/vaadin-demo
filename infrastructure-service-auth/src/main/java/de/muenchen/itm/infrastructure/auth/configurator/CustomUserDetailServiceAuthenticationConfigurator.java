package de.muenchen.itm.infrastructure.auth.configurator;

import de.muenchen.itm.infrastructure.auth.services.UserDetailsService;
import org.kohsuke.MetaInfServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;

/**
 * Configuration to add the custom UserDetailsService as authentication mechanism.
 * Created by huningd on 17.12.15.
 */
@MetaInfServices
@Order(3)
@Configuration
public class CustomUserDetailServiceAuthenticationConfigurator extends GlobalAuthenticationConfigurerAdapter {

    @Autowired
    private UserDetailsService uds;

    @Override
    public void init(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(uds);
    }
}

