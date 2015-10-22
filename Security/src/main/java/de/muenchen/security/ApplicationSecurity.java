package de.muenchen.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

/**
 * Created by rene.zarwel on 02.09.15.
 */
@Configuration
/** Security must be more important than chache because of Mandant Feature! (order)*/
@EnableGlobalMethodSecurity(prePostEnabled = true, order = 1)
@Order(SecurityProperties.BASIC_AUTH_ORDER - 6)
public class ApplicationSecurity extends ResourceServerConfigurerAdapter {

    @Value("${security.basic.enabled:true}")
    private boolean securityEnabled;


    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                        // Allow anonymous resource requests
                .antMatchers("/").permitAll()
                .antMatchers("/favicon.ico").permitAll()
                .antMatchers("**/*.html").permitAll()
                .antMatchers("**/*.css").permitAll()
                .antMatchers("**/*.js").permitAll()

                // Allow anonymous service info request
                .antMatchers("/service_info").permitAll()
                .and()

                .csrf().disable();

        if(securityEnabled) {
            http.authorizeRequests()
                    // All other request need to be authenticated
                    .anyRequest().authenticated();
        }

    }


}
