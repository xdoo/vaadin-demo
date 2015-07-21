/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.demo.test;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import de.muenchen.demo.service.Application;
import de.muenchen.demo.service.domain.AuthorityPermissionRepository;
import de.muenchen.demo.service.domain.AuthorityRepository;
import de.muenchen.demo.service.domain.MandantRepository;
import de.muenchen.demo.service.domain.PermissionRepository;
import de.muenchen.demo.service.domain.User;
import de.muenchen.demo.service.domain.UserAuthorityRepository;
import de.muenchen.demo.service.domain.UserRepository;
import de.muenchen.demo.service.rest.api.UserResource;
import de.muenchen.demo.service.rest.api.SearchResultResource;
import de.muenchen.demo.service.util.IdService;
import static java.lang.Boolean.TRUE;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
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
import org.junit.Before;
import org.junit.Test;
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
public class UserTest {

    private RestTemplate restTemplate;
    @Value("${local.server.port}")
    private int port;
    private User user1;
    private User user2;
    private User user3;
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

        user1 = new User();
        user1.setEmail("hans2@muenchen.de");
        user1.setPassword("test2");
        user1.setUsername("hans2");
        user1.setOid("51");
        user1.setEnabled(TRUE);
        user2 = new User();
        user2.setEmail("hans3@muenchen.de");
        user2.setPassword("test3");
        user2.setUsername("hans3");
        user2.setOid(IdService.next());
        user2.setEnabled(TRUE);
        user3 = new User();
        user3.setEmail("hans4@muenchen.de");
        user3.setPassword("test4");
        user3.setUsername("hans4");
        user3.setOid(IdService.next());
        user3.setEnabled(TRUE);
    }

    @Test
    public void saveUserTest() {

        String URL = "http://localhost:" + port + "/user/save";

        restTemplate.postForEntity(URL, user1, UserResource.class);
        String URL4 = "http://localhost:" + port + "/user/51";
        UserResource response;
        response = restTemplate.getForEntity(URL4, UserResource.class).getBody();

        assertEquals("test2", response.getPassword());

    }

    @Test
    public void queryUserTest() {
        String URL = "http://localhost:" + port + "/user/save";
        restTemplate.postForEntity(URL, user2, UserResource.class);
        restTemplate.postForEntity(URL, user3, UserResource.class);

        String URL3 = "http://localhost:" + port + "/user/query";
        response = restTemplate.getForEntity(URL3, SearchResultResource.class).getBody();

        assertEquals(4, response.getResult().size());

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
