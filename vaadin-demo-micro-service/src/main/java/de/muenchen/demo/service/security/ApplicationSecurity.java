package de.muenchen.demo.service.security;

import org.springframework.boot.actuate.autoconfigure.ManagementServerProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Created by rene.zarwel on 02.09.15.
 */
@Configuration
/** Security must be more important than chache because of Mandant Feature! (order)*/
@EnableGlobalMethodSecurity(prePostEnabled = true, order = 1)
@Order(ManagementServerProperties.ACCESS_OVERRIDE_ORDER)
public class ApplicationSecurity extends
        WebSecurityConfigurerAdapter {


    @Override
    protected void configure(HttpSecurity http) throws Exception {
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

                // All other request need to be authenticated
                .anyRequest().authenticated()
                .and()

                .httpBasic()
                .and()

                .csrf().disable();

    }


}
