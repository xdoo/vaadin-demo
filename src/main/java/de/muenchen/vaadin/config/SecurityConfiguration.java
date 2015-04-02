/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.vaadin.config;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.RequestCacheAwareFilter;
import org.vaadin.spring.security.VaadinSecurityContext;
import org.vaadin.spring.security.annotation.EnableVaadinSecurity;
import org.vaadin.spring.security.web.VaadinDefaultRedirectStrategy;
import org.vaadin.spring.security.web.VaadinRedirectStrategy;
import org.vaadin.spring.security.web.authentication.SavedRequestAwareVaadinAuthenticationSuccessHandler;
import org.vaadin.spring.security.web.authentication.VaadinAuthenticationSuccessHandler;

@Configuration
@ComponentScan
@EnableWebSecurity
public class SecurityConfiguration {
// TODO Spring-Boot-Actuator

    @Configuration
    @EnableVaadinSecurity
    public static class WebSecurityConfig extends WebSecurityConfigurerAdapter implements InitializingBean {

        @Autowired
        private VaadinSecurityContext vaadinSecurityContext;
        
        /*
         * (non-Javadoc)
         * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
         *
         * Configure the redirectSaveHandler bean as a VaadinAuthenticationSuccessHandler
         */
        @Override
        public void afterPropertiesSet() throws Exception {
            this.vaadinSecurityContext.addAuthenticationSuccessHandler(redirectSaveHandler());
        }

        @Bean(name = "authenticationManager")
        @Override
        public AuthenticationManager authenticationManagerBean() throws Exception {
            return super.authenticationManagerBean();
        }
        
        /*
         * The HttpSessionRequestCache is where the initial request before
         * redirect to the login is cached so it can be used after successful login
         */
        @Bean
        public RequestCache requestCache() {
            RequestCache requestCache = new HttpSessionRequestCache();
            return requestCache;
        }
        
        /*
         * The RequestCacheAwareFilter is responsible for storing the initial request
         */
        @Bean
        public RequestCacheAwareFilter requestCacheAwareFilter() {
            RequestCacheAwareFilter filter = new RequestCacheAwareFilter(requestCache());
            return filter;
        }
        
        /*
         * The VaadinRedirectStategy
         */
        @Bean
        public VaadinRedirectStrategy vaadinRedirectStrategy() {
            return new VaadinDefaultRedirectStrategy();
        }

        @Bean
        public VaadinAuthenticationSuccessHandler redirectSaveHandler() {
            SavedRequestAwareVaadinAuthenticationSuccessHandler handler = new SavedRequestAwareVaadinAuthenticationSuccessHandler();
            handler.setRedirectStrategy(vaadinRedirectStrategy());
            handler.setRequestCache(requestCache());
            handler.setDefaultTargetUrl("/");
            handler.setTargetUrlParameter("r");
            return handler;
        }
// TODO Disable SpringSecurityFilterChain DefaultFilters (/css, /jsm /images)

        @Override
        public void configure(WebSecurity web) throws Exception {
            web
                    .ignoring()
                    .antMatchers("/VAADIN/**");
        }

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth
                    .inMemoryAuthentication()
                    .withUser("user").password("user").roles("USER")
                    .and()
                    .withUser("admin").password("admin").roles("ADMIN");
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .authorizeRequests()
                    .antMatchers("/login/**").permitAll()
                    .antMatchers("/UIDL/**").permitAll()
                    .antMatchers("/HEARTBEAT/**").authenticated()
                    .antMatchers("/**").authenticated()
                    .anyRequest().authenticated()
                    .and()
                    .sessionManagement()
                    .sessionFixation()
                    .migrateSession()
                    .and()
                    .csrf().disable()
                    .headers()
                    .frameOptions().disable()
                    .exceptionHandling().authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login"));
        }
    }
}
