/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.demo.test;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import de.muenchen.demo.service.Application;
import de.muenchen.demo.service.config.InitApplication;
import de.muenchen.demo.service.domain.AuthPermId;
import de.muenchen.demo.service.domain.Authority;
import de.muenchen.demo.service.domain.AuthorityPermission;
import de.muenchen.demo.service.domain.AuthorityPermissionRepository;
import de.muenchen.demo.service.domain.AuthorityRepository;
import de.muenchen.demo.service.domain.Permission;
import de.muenchen.demo.service.domain.PermissionRepository;
import de.muenchen.demo.service.domain.User;
import de.muenchen.demo.service.domain.UserAuthId;
import de.muenchen.demo.service.domain.UserAuthority;
import de.muenchen.demo.service.domain.UserAuthorityRepository;
import de.muenchen.demo.service.domain.UserRepository;
import de.muenchen.demo.service.rest.BuergerController;
import de.muenchen.demo.service.rest.api.SearchResultResource;
import de.muenchen.demo.service.rest.api.UserResource;
import de.muenchen.demo.service.util.IdService;
import static java.lang.Boolean.TRUE;
import java.lang.reflect.Method;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import javax.net.ssl.SSLContext;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import static org.hamcrest.CoreMatchers.equalTo;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author praktikant.tmar
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest({"server.port=0", "management.port=0"})
public class SecurityTest {
    
    private RestTemplate restTemplate;
    @Value("${local.server.port}")
    private int port;
    @JsonProperty("result")
    private SearchResultResource<UserResource> response;
    @Autowired
    UserRepository usersRepo;
    @Autowired
    AuthorityRepository authRepo;
    @Autowired
    PermissionRepository permRepo;
    @Autowired
    UserAuthorityRepository userAuthRepo;
    @Autowired
    AuthorityPermissionRepository authPermRepo;

    @Rule
    public ExpectedException thrown = ExpectedException.none();
    @Before
    public void setUp() throws JsonProcessingException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {

        authPermRepo.deleteAll();
        userAuthRepo.deleteAll();
        usersRepo.deleteAll();
        authRepo.deleteAll();
        permRepo.deleteAll();

        InitApplication initApplication = new InitApplication( usersRepo,  authRepo,  permRepo,  userAuthRepo,  authPermRepo);
        initApplication.init();
    }
     @Test
    public void acessDeniedTest() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {

        User user = new User();
        user.setEmail("hans2@muenchen.de");
        user.setPassword("test5");
        user.setUsername("hans5");
        user.setOid("U80");
        user.setEnabled(TRUE);
        usersRepo.save(user);

        Authority auth = new Authority();
        auth.setAuthority("USER");
        auth.setOid("A80");
        authRepo.save(auth);

        List<String> list = new ArrayList();
        for (Method method : BuergerController.class.getDeclaredMethods()) {
            String name = method.getName();
            list.add("PERM_" + name);
        }
        for (String list1 : list) {
            Permission permission = new Permission();
            permission.setPermision(list1);
            permission.setOid(IdService.next());
            permRepo.save(permission);

            AuthorityPermission authPerm = new AuthorityPermission();
            AuthPermId idA = new AuthPermId(permission, auth);
            authPerm.setId(idA);
            authPermRepo.save(authPerm);

        }
        UserAuthority userAuth = new UserAuthority();
        UserAuthId id = new UserAuthId(user, auth);

        userAuth.setId(id);

        userAuthRepo.save(userAuth);

        SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, new TrustSelfSignedStrategy()).useTLS().build();
        SSLConnectionSocketFactory connectionFactory = new SSLConnectionSocketFactory(sslContext, new AllowAllHostnameVerifier());
        BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials("hans5", "test5"));

        HttpClient httpClient = HttpClientBuilder.create()
                .setSSLSocketFactory(connectionFactory)
                .setDefaultCredentialsProvider(credentialsProvider)
                .build();

        ClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        restTemplate = new RestTemplate(requestFactory);

        thrown.expect(org.springframework.web.client.HttpClientErrorException.class);
        thrown.expectMessage(equalTo("403 Forbidden"));
        String URL2 = "http://localhost:" + port + "/userAuthority/query";
        response = restTemplate.getForEntity(URL2, SearchResultResource.class).getBody();
    }
        @After
    public void TearDown() {
        authPermRepo.deleteAll();
        userAuthRepo.deleteAll();
        usersRepo.deleteAll();
        authRepo.deleteAll();
        permRepo.deleteAll();
    }
}
