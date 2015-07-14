package de.muenchen.demo.service;

import de.muenchen.demo.service.domain.AuthPermId;
import de.muenchen.demo.service.domain.UserAuthId;
import de.muenchen.demo.service.domain.Authority;
import de.muenchen.demo.service.domain.AuthorityPermission;
import de.muenchen.demo.service.domain.AuthorityPermissionRepository;
import de.muenchen.demo.service.domain.AuthorityRepository;
import de.muenchen.demo.service.config.InitApplication;
import de.muenchen.demo.service.domain.Permission;
import de.muenchen.demo.service.domain.PermissionRepository;
import de.muenchen.demo.service.domain.UserAuthorityRepository;
import de.muenchen.demo.service.domain.User;
import de.muenchen.demo.service.domain.UserAuthority;
import de.muenchen.demo.service.domain.UserRepository;
import de.muenchen.demo.service.rest.AccountController;
import de.muenchen.demo.service.rest.AdresseController;
import de.muenchen.demo.service.rest.AuthorityController;
import de.muenchen.demo.service.rest.AuthorityPermissionController;
import de.muenchen.demo.service.rest.BuergerController;
import de.muenchen.demo.service.rest.CompanyBaseInfoController;
import de.muenchen.demo.service.rest.PermissionController;
import de.muenchen.demo.service.rest.StaatsangehoerigkeitController;
import de.muenchen.demo.service.rest.UserAuthorityController;
import de.muenchen.demo.service.rest.UserController;
import de.muenchen.demo.service.rest.WohnungController;
import de.muenchen.demo.service.util.IdService;
import java.io.IOException;
import static java.lang.Boolean.TRUE;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.boot.actuate.autoconfigure.ManagementServerProperties;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
@EnableAutoConfiguration
@EnableGlobalMethodSecurity(jsr250Enabled = true)
public class Application {

    private static final Logger LOG
            = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    CommandLineRunner init(UserRepository usersRepo, AuthorityRepository authRepo, PermissionRepository permRepo, UserAuthorityRepository userAuthRepo, AuthorityPermissionRepository authPermRepo) {

        return new CommandLineRunner() {

            public void run(String... arg0) {

                InitApplication initApplication = new InitApplication(usersRepo, authRepo, permRepo, userAuthRepo, authPermRepo);
                initApplication.init();
            }

        };

    }

//    Dozer
    @Bean
    public Mapper dozerBeanMapper() {
        return new DozerBeanMapper();
    }

//    Security
    @Bean
    public ApplicationSecurity applicationSecurity() {
        LOG.info("creating application security...");
        return new ApplicationSecurity();
    }

    @Bean
    public AuthenticationManagerConfiguration authenticationSecurity() {
        return new AuthenticationManagerConfiguration();
    }

    @Order(ManagementServerProperties.ACCESS_OVERRIDE_ORDER)
    protected static class ApplicationSecurity extends
            WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.authorizeRequests().anyRequest().authenticated().and().httpBasic().and().csrf().disable();
//                .csrf().csrfTokenRepository(csrfTokenRepository()).and()
//                .addFilterAfter(csrfHeaderFilter(), CsrfFilter.class);

        }

        private Filter csrfHeaderFilter() {
            return new OncePerRequestFilter() {
                @Override
                protected void doFilterInternal(HttpServletRequest request,
                        HttpServletResponse response, FilterChain filterChain)
                        throws ServletException, IOException {
                    CsrfToken csrf = (CsrfToken) request.getAttribute(CsrfToken.class
                            .getName());
                    if (csrf != null) {
                        Cookie cookie = WebUtils.getCookie(request,
                                "XSRF-TOKEN");
                        String token = csrf.getToken();
                        if (cookie == null || token != null
                                && !token.equals(cookie.getValue())) {
                            cookie = new Cookie("XSRF-TOKEN", token);
                            cookie.setPath("/");
                            response.addCookie(cookie);
                        }
                    }
                    filterChain.doFilter(request, response);
                }
            };
        }

        private CsrfTokenRepository csrfTokenRepository() {
            HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
            repository.setHeaderName("X-XSRF-TOKEN");
            return repository;
        }
    }

    @Order(Ordered.HIGHEST_PRECEDENCE + 10)
    protected static class AuthenticationManagerConfiguration extends
            GlobalAuthenticationConfigurerAdapter {

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
    }

}
