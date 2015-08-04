/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.demo.test.integration;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.muenchen.demo.service.Application;
import de.muenchen.demo.service.domain.AuthPermId;
import de.muenchen.demo.service.domain.Authority;
import de.muenchen.demo.service.domain.AuthorityPermission;
import de.muenchen.demo.service.domain.AuthorityPermissionRepository;
import de.muenchen.demo.service.domain.AuthorityRepository;
import de.muenchen.demo.service.domain.MandantRepository;
import de.muenchen.demo.service.domain.Permission;
import de.muenchen.demo.service.domain.PermissionRepository;
import de.muenchen.demo.service.domain.User;
import de.muenchen.demo.service.domain.UserAuthId;
import de.muenchen.demo.service.domain.UserAuthority;
import de.muenchen.demo.service.domain.UserAuthorityRepository;
import de.muenchen.demo.service.domain.UserRepository;
import de.muenchen.demo.service.rest.AuthorityPermissionController;
import de.muenchen.demo.service.rest.UserAuthorityController;
import de.muenchen.demo.service.rest.api.AuthorityPermissionResource;
import de.muenchen.demo.service.rest.api.AuthorityResource;
import de.muenchen.demo.service.rest.api.PermissionResource;
import de.muenchen.demo.service.rest.api.SearchResultResource;
import de.muenchen.demo.service.rest.api.UserAuthorityResource;
import de.muenchen.demo.service.rest.api.UserResource;
import de.muenchen.demo.service.util.IdService;
import static java.lang.Boolean.TRUE;
import java.lang.reflect.Method;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
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
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.hateoas.hal.Jackson2HalModule;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author praktikant.tmar
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest({"server.port=0", "management.port=0"})
public class UserAuthorityPermissionTest {

    private RestTemplate restTemplate;
    @Value("${local.server.port}")
    private int port;
    @JsonProperty("result")
    private SearchResultResource response;
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

    @Autowired
    MandantRepository mandantRepo;

    @Before
    public void setUp() throws JsonProcessingException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {

        authPermRepo.deleteAll();
        userAuthRepo.deleteAll();
        usersRepo.deleteAll();
        authRepo.deleteAll();
        permRepo.deleteAll();
        mandantRepo.deleteAll();

        InitTest initTest = new InitTest(usersRepo, authRepo, permRepo, userAuthRepo, authPermRepo, mandantRepo);
        initTest.init();
        User user = new User();
        user.setEmail("hans2@muenchen.de");
        user.setPassword("test3");
        user.setUsername("hans3");
        user.setOid("U71");
        user.setEnabled(TRUE);
        usersRepo.save(user);

        Authority auth = new Authority();
        auth.setAuthority("ADMIN");
        auth.setOid("A71");
        authRepo.save(auth);

        List<String> permissions = new ArrayList();
        for (Method method : AuthorityPermissionController.class.getDeclaredMethods()) {
            String name = method.getName();
            permissions.add("PERM_" + name);
        }
        for (Method method : UserAuthorityController.class.getDeclaredMethods()) {
            String name = method.getName();
            permissions.add("PERM_" + name);
        }
        permissions.stream().map((permission1) -> {
            Permission permission = new Permission();
            permission.setPermision(permission1);
            return permission;
        }).map((permission) -> {
            permission.setOid(IdService.next());
            return permission;
        }).map((permission) -> {
            permRepo.save(permission);
            return permission;
        }).map((permission) -> {
            AuthorityPermission authPerm = new AuthorityPermission();
            AuthPermId idA = new AuthPermId(permission, auth);
            authPerm.setId(idA);
            return authPerm;
        }).forEach((authPerm) -> {
            authPermRepo.save(authPerm);
        });
        UserAuthority userAuth = new UserAuthority();
        UserAuthId id = new UserAuthId(user, auth);

        userAuth.setId(id);

        userAuthRepo.save(userAuth);

        User user2 = new User();
        user2.setEmail("hans2@muenchen.de");
        user2.setPassword("test4");
        user2.setUsername("hans4");
        user2.setOid("U73");
        user2.setEnabled(TRUE);
        usersRepo.save(user2);

        Authority auth2 = new Authority();
        auth2.setAuthority("USER");
        auth2.setOid("A73");
        authRepo.save(auth2);

        Permission permission = new Permission();
        permission.setPermision("PERM_readUser");
        permission.setOid("P73");
        permRepo.save(permission);

        SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, new TrustSelfSignedStrategy()).useTLS().build();
        SSLConnectionSocketFactory connectionFactory = new SSLConnectionSocketFactory(sslContext, new AllowAllHostnameVerifier());
        BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials("hans", "test"));

        HttpClient httpClient = HttpClientBuilder.create()
                .setSSLSocketFactory(connectionFactory)
                .setDefaultCredentialsProvider(credentialsProvider)
                .build();

        ClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        restTemplate = new RestTemplate(requestFactory);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new Jackson2HalModule());

        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(objectMapper);
        restTemplate.setMessageConverters(Collections.<HttpMessageConverter<?>>singletonList(converter));

    }

    @Test
    public void queryUserAuthorityPermissionTest() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {

        /*Test Authority Permission query*/
        String URL = "http://localhost:" + port + "/authorityPermission/query";
        response = restTemplate.getForEntity(URL, SearchResultResource.class).getBody();

        assertEquals(false, response.getResult().isEmpty());

        /*Test User Authority  query*/
        String URL2 = "http://localhost:" + port + "/userAuthority/query";
        response = restTemplate.getForEntity(URL2, SearchResultResource.class).getBody();
        assertEquals(3, response.getResult().size());
        assertNotEquals(null, response.getLink("self"));
        assertNotEquals(null, response.getLink("query"));

    }

    @Test
    public void createUserAuthorityPermissionTest() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {

        Authority auth3 = new Authority();
        auth3.setAuthority("USER");
        auth3.setOid("A74");

        Permission permission1 = new Permission();
        permission1.setPermision("PERM_readUser");
        permission1.setOid("P74");

        User user3 = new User();
        user3.setEmail("hans2@muenchen.de");
        user3.setPassword("test4");
        user3.setUsername("hans4");
        user3.setOid("U74");
        user3.setEnabled(TRUE);

        String URL22 = "http://localhost:" + port + "/permission/save";
        restTemplate.postForEntity(URL22, permission1, PermissionResource.class);

        String URL24 = "http://localhost:" + port + "/user/save";
        restTemplate.postForEntity(URL24, user3, UserResource.class);

        String URL23 = "http://localhost:" + port + "/authority/save";
        restTemplate.postForEntity(URL23, auth3, AuthorityResource.class);

        /*Test save Authority Permission */
        String URL = "http://localhost:" + port + "/authorityPermission/save/P74/A74";
        AuthorityPermissionResource response4 = restTemplate.getForEntity(URL, AuthorityPermissionResource.class).getBody();
        assertEquals("USER", response4.getId().getAuthority().getAuthority());

        /*Test save User Authority  */
        String URL2 = "http://localhost:" + port + "/userAuthority/save/U74/A74";
        UserAuthorityResource response5 = restTemplate.getForEntity(URL2, UserAuthorityResource.class).getBody();
        assertEquals("USER", response5.getId().getAuthority().getAuthority());

    }

    @After
    public void TearDown() {
        authPermRepo.deleteAll();
        userAuthRepo.deleteAll();
        usersRepo.deleteAll();
        authRepo.deleteAll();
        permRepo.deleteAll();
        mandantRepo.deleteAll();
    }

}
