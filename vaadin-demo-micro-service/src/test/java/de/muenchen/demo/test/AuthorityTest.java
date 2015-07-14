/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.demo.test;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import de.muenchen.demo.service.Application;
import de.muenchen.demo.service.domain.Authority;
import de.muenchen.demo.service.domain.AuthorityPermissionRepository;
import de.muenchen.demo.service.domain.AuthorityRepository;
import de.muenchen.demo.service.config.InitApplication;
import de.muenchen.demo.service.domain.PermissionRepository;
import de.muenchen.demo.service.domain.UserAuthorityRepository;
import de.muenchen.demo.service.domain.UserRepository;
import de.muenchen.demo.service.rest.api.AuthorityResource;
import de.muenchen.demo.service.rest.api.UserResource;
import de.muenchen.demo.service.rest.api.SearchResultResource;
import de.muenchen.demo.service.util.IdService;
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
public class AuthorityTest {

    private RestTemplate restTemplate;
    @Value("${local.server.port}")
    private int port;
    private Authority auth1;
    private Authority auth2;
    private Authority auth3;
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

    @Before
    public void setUp() throws JsonProcessingException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {

        authPermRepo.deleteAll();
        userAuthRepo.deleteAll();
        usersRepo.deleteAll();
        authRepo.deleteAll();
        permRepo.deleteAll();

        InitApplication initApplication = new InitApplication( usersRepo,  authRepo,  permRepo,  userAuthRepo,  authPermRepo);
        initApplication.init();

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

        auth1 = new Authority();
        auth1.setAuthority("ADMIN");
        auth1.setOid("60");
        auth2 = new Authority();
        auth2.setAuthority("USER");
        auth2.setOid(IdService.next());
        auth3 = new Authority();
        auth3.setAuthority("USER");
        auth3.setOid(IdService.next());
    }

    @Test
    public void saveAuthorityTest() {

        String URL = "http://localhost:" + port + "/authority/save";

        restTemplate.postForEntity(URL, auth1, AuthorityResource.class);
        String URL4 = "http://localhost:" + port + "/authority/60";
        AuthorityResource response2;
        response2 = restTemplate.getForEntity(URL4, AuthorityResource.class).getBody();

        assertEquals("ADMIN", response2.getAuthority());

    }

    @Test
    public void queryAuthorityTest() {
        String URL = "http://localhost:" + port + "/authority/save";
        restTemplate.postForEntity(URL, auth2, AuthorityResource.class);
        restTemplate.postForEntity(URL, auth3, AuthorityResource.class);

        String URL3 = "http://localhost:" + port + "/authority/query";
        response = restTemplate.getForEntity(URL3, SearchResultResource.class).getBody();

        assertEquals(3, response.getResult().size());

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
