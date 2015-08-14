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
import de.muenchen.demo.service.domain.Authority;
import de.muenchen.demo.service.domain.AuthorityPermissionRepository;
import de.muenchen.demo.service.domain.AuthorityRepository;
import de.muenchen.demo.service.domain.MandantRepository;
import de.muenchen.demo.service.domain.PermissionRepository;
import de.muenchen.demo.service.domain.UserAuthorityRepository;
import de.muenchen.demo.service.domain.UserRepository;
import de.muenchen.demo.service.rest.api.AuthorityResource;
import de.muenchen.demo.service.rest.api.UserResource;
import de.muenchen.vaadin.demo.api.rest.SearchResultResource;
import de.muenchen.demo.service.util.IdService;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
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
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;
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
public class AuthorityTest {

    private RestTemplate restTemplate;
    @Value("${local.server.port}")
    private int port;
    private Authority authority;
    private Authority auth2;
    private Authority auth3;
    private AuthorityResource response;
    private String urlSave;
    private String urlNew;
    @JsonProperty("result")
    private SearchResultResource<UserResource> responseList;
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
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new Jackson2HalModule());

        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(objectMapper);
        restTemplate.setMessageConverters(Collections.<HttpMessageConverter<?>>singletonList(converter));

        authority = new Authority();
        authority.setAuthority("ADMIN");
        authority.setOid("60");
        auth2 = new Authority();
        auth2.setAuthority("USER");
        auth2.setOid(IdService.next());
        auth3 = new Authority();
        auth3.setAuthority("USER");
        auth3.setOid(IdService.next());
        urlSave = "http://localhost:" + port + "/authority/save";
        urlNew = "http://localhost:" + port + "/authority/new";

    }

    @Test
    public void saveAuthorityTest() {


        restTemplate.postForEntity(urlSave, authority, AuthorityResource.class);
        String URL4 = "http://localhost:" + port + "/authority/60";
        response = restTemplate.getForEntity(URL4, AuthorityResource.class).getBody();

        assertEquals("ADMIN", response.getAuthority());
        assertNotNull(response.getLink("new"));
        assertNotNull(response.getLink("update"));
        assertNotNull(response.getLink("copy"));
        assertNotNull(response.getLink("self"));
        assertNotNull(response.getLink("delete"));

    }

    @Test
    public void queryAuthorityTest() {
        restTemplate.postForEntity(urlSave, auth2, AuthorityResource.class);
        restTemplate.postForEntity(urlSave, auth3, AuthorityResource.class);

        String URL3 = "http://localhost:" + port + "/authority/query";
        responseList = restTemplate.getForEntity(URL3, SearchResultResource.class).getBody();

        assertEquals(3, responseList.getResult().size());
        assertNotNull(responseList.getLink("self"));
        assertNotNull(responseList.getLink("query"));

    }
    @Test
    public void updateAuthorityTest() {

        Authority authorityUpdate = new Authority();
        authorityUpdate.setAuthority("User");
        authorityUpdate.setOid("60");

        restTemplate.postForEntity(urlSave, authority, AuthorityResource.class).getBody();
        String URL2 = "http://localhost:" + port + "/authority/60";
        response = restTemplate.postForEntity(URL2, authorityUpdate, AuthorityResource.class).getBody();

        assertEquals("User", response.getAuthority());
        assertNotNull(response.getLink("new"));
        assertNotNull(response.getLink("update"));
        assertNotNull(response.getLink("copy"));
        assertNotNull(response.getLink("self"));
        assertNotNull(response.getLink("delete"));

    }


    @Test
    public void copyAuthorityTest() {

        restTemplate.postForEntity(urlSave, authority, AuthorityResource.class).getBody();
        String URL2 = "http://localhost:" + port + "/authority/copy/60";
        response = restTemplate.getForEntity(URL2, AuthorityResource.class).getBody();

        assertEquals("ADMIN", response.getAuthority());
        assertNotNull(response.getLink("new"));
        assertNotNull(response.getLink("update"));
        assertNotNull(response.getLink("copy"));
        assertNotNull(response.getLink("self"));
        assertNotNull(response.getLink("delete"));
    }

    @Test
    public void deleteAuthorityTest() {

        restTemplate.postForEntity(urlSave, authority, AuthorityResource.class).getBody();
        String URL2 = "http://localhost:" + port + "/authority/60";
        restTemplate.delete(URL2, authority);
        String URL3 = "http://localhost:" + port + "/authority/query";
        responseList = restTemplate.getForEntity(URL3, SearchResultResource.class).getBody();

        assertEquals(1, responseList.getResult().size());

    }

    @Test
    public void newAuthorityTest() throws JsonProcessingException {

        response = restTemplate.getForEntity(urlNew, AuthorityResource.class).getBody();
        assertNotNull(response.getLink("new"));
        assertNotNull(response.getLink("save"));
        assertNotNull(response.getOid());

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
