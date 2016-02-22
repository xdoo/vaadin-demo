package de.muenchen.itm.infrastructure.auth;

import de.muenchen.itm.infrastructure.auth.configurator.JDBCAuthenticationConfigurator;
import de.muenchen.itm.infrastructure.auth.configurator.LDAPAuthenticationConfigurator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Set;
import java.util.stream.Collectors;

@SpringBootApplication
@Controller
@SessionAttributes("authorizationRequest")
@EnableResourceServer
@EnableEurekaClient
@Configuration
@EntityScan(basePackages = {"de.muenchen.itm.infrastructure.auth"})
@EnableJpaRepositories(basePackages = {"de.muenchen.itm.infrastructure.auth"})
@EnableAutoConfiguration
@ComponentScan(basePackages = {"de.muenchen.service", "de.muenchen.itm.infrastructure.auth"})
public class AuthserverApplication extends WebMvcConfigurerAdapter {


    public static void main(String[] args) {
        SpringApplication.run(AuthserverApplication.class, args);
    }

    @RequestMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        SecurityContextHolder.getContext().setAuthentication(null);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
    }

    // Database-Security
    @Bean
    public GlobalAuthenticationConfigurerAdapter configurationAdapter1() {
        return new JDBCAuthenticationConfigurator();
    }

    // LDAP-Security
    @Bean
    public GlobalAuthenticationConfigurerAdapter configurationAdapter2() {
        return new LDAPAuthenticationConfigurator();
    }

    @Configuration
    @Order(-20)
    protected static class LoginConfig extends WebSecurityConfigurerAdapter {

        @Autowired
        private AuthenticationManager authenticationManager;

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            // @formatter:off
            http
                    .formLogin().loginPage("/login").permitAll()
                    .and()
                    //Enable httpSecurity for urls match
                    .requestMatchers().antMatchers("/login", "/logout", "/oauth/authorize", "/oauth/confirm_access")
                    .and()
                    .authorizeRequests().anyRequest().authenticated();
            // @formatter:on
        }

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.parentAuthenticationManager(authenticationManager);
        }
    }

    @Configuration
    @EnableAuthorizationServer
    protected static class OAuth2AuthorizationConfig extends
            AuthorizationServerConfigurerAdapter {

        @Value("${security.oauth2.client.access-token-validity-seconds:43200}")
        private int tokenValiditySeconds;

        @Value("${security.oauth2.client.id:acme}")
        private String clientID;

        @Value("${security.oauth2.client.scope:defaultScope}")
        private Set<String> scopes;

        @Value("${security.oauth2.jwt.signingKey:secret}")
        private String signingKey;

        @Autowired
        private AuthenticationManager authenticationManager;

        @Bean
        public JwtAccessTokenConverter jwtAccessTokenConverter() {
            JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
            DefaultAccessTokenConverter accessTokenConverter = new DefaultAccessTokenConverter();
            accessTokenConverter.setUserTokenConverter(new DefaultUserAuthenticationConverter());
            converter.setAccessTokenConverter(accessTokenConverter);
            converter.setSigningKey(signingKey);
            return converter;
        }

        @Override
        public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
            clients.inMemory()
                    .withClient(clientID)
                    .authorizedGrantTypes("authorization_code", "refresh_token", "password")
                    .scopes(scopes.stream().collect(Collectors.joining(", ")))
                    .autoApprove(true);
        }

        @Override
        public void configure(AuthorizationServerEndpointsConfigurer endpoints)
                throws Exception {
            endpoints.authenticationManager(authenticationManager).accessTokenConverter(
                    jwtAccessTokenConverter());
        }

        @Override
        public void configure(AuthorizationServerSecurityConfigurer oauthServer)
                throws Exception {
            oauthServer.tokenKeyAccess("permitAll()").checkTokenAccess(
                    "isAuthenticated()");
        }
    }
}
