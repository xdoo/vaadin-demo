///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package de.muenchen.demo.test.integration;
//
//import com.fasterxml.jackson.annotation.JsonProperty;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import de.muenchen.demo.service.Application;
//import de.muenchen.demo.service.domain.AdresseExterneRepository;
//import de.muenchen.demo.service.domain.AdresseInterneRepository;
//import de.muenchen.demo.service.domain.AdresseReferenceRepository;
//import de.muenchen.demo.service.domain.AuthorityPermissionRepository;
//import de.muenchen.demo.service.domain.AuthorityRepository;
//import de.muenchen.demo.service.domain.BuergerRepository;
//import de.muenchen.demo.service.domain.MandantRepository;
//import de.muenchen.demo.service.domain.PassRepository;
//import de.muenchen.demo.service.domain.PermissionRepository;
//import de.muenchen.demo.service.domain.StaatsangehoerigkeitReferenceRepository;
//import de.muenchen.demo.service.domain.User;
//import de.muenchen.demo.service.domain.UserAuthorityRepository;
//import de.muenchen.demo.service.domain.UserRepository;
//import de.muenchen.demo.service.domain.WohnungRepository;
//import de.muenchen.demo.service.rest.api.UserResource;
//import de.muenchen.vaadin.demo.api.rest.SearchResultResource;
//import de.muenchen.demo.service.util.IdService;
//import static java.lang.Boolean.TRUE;
//import java.security.KeyManagementException;
//import java.security.KeyStoreException;
//import java.security.NoSuchAlgorithmException;
//import java.util.Collections;
//import javax.net.ssl.SSLContext;
//import org.apache.http.auth.AuthScope;
//import org.apache.http.auth.UsernamePasswordCredentials;
//import org.apache.http.client.HttpClient;
//import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
//import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
//import org.apache.http.conn.ssl.SSLContexts;
//import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
//import org.apache.http.impl.client.BasicCredentialsProvider;
//import org.apache.http.impl.client.HttpClientBuilder;
//import org.junit.After;
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertNotNull;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.test.SpringApplicationConfiguration;
//import org.springframework.boot.test.WebIntegrationTest;
//import org.springframework.hateoas.hal.Jackson2HalModule;
//import org.springframework.http.client.ClientHttpRequestFactory;
//import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
//import org.springframework.http.converter.HttpMessageConverter;
//import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
//
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import org.springframework.web.client.RestTemplate;
//
///**
// *
// * @author praktikant.tmar
// */
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringApplicationConfiguration(classes = Application.class)
//@WebIntegrationTest({"server.port=0", "management.port=0"})
//public class UserTest {
//
//    private RestTemplate restTemplate;
//    @Value("${local.server.port}")
//    private int port;
//    private User user;
//    private User user2;
//    private User user3;
//    @JsonProperty("result")
//    private SearchResultResource<UserResource> responseQuery;
//
//    @Autowired
//    UserRepository usersRepo;
//    @Autowired
//    AuthorityRepository authRepo;
//    @Autowired
//    PermissionRepository permRepo;
//    @Autowired
//    UserAuthorityRepository userAuthRepo;
//    @Autowired
//    AuthorityPermissionRepository authPermRepo;
//    @Autowired
//    StaatsangehoerigkeitReferenceRepository staatRepo;
//    @Autowired
//    BuergerRepository buergerRepo;
//    @Autowired
//    WohnungRepository wohnRepo;
//    @Autowired
//    PassRepository passRepo;
//    @Autowired
//    AdresseInterneRepository interneRepo;
//    @Autowired
//    AdresseExterneRepository externeRepo;
//    @Autowired
//    AdresseReferenceRepository referenceRepo;
//    @Autowired
//    MandantRepository mandantRepo;
//
//    private String urlSave;
//    private String urlNew;
//    private UserResource response;
//
//    @Before
//    public void setUp() throws JsonProcessingException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
//
//        authPermRepo.deleteAll();
//        userAuthRepo.deleteAll();
//        usersRepo.deleteAll();
//        authRepo.deleteAll();
//        permRepo.deleteAll();
//        buergerRepo.deleteAll();
//        staatRepo.deleteAll();
//        wohnRepo.deleteAll();
//        passRepo.deleteAll();
//        referenceRepo.deleteAll();
//        interneRepo.deleteAll();
//        externeRepo.deleteAll();
//        mandantRepo.deleteAll();
//
//        InitTest initTest = new InitTest(usersRepo, authRepo, permRepo, userAuthRepo, authPermRepo, mandantRepo);
//        initTest.init();
//        SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, new TrustSelfSignedStrategy()).useTLS().build();
//        SSLConnectionSocketFactory connectionFactory = new SSLConnectionSocketFactory(sslContext, new AllowAllHostnameVerifier());
//        BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
//        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials("hans", "test"));
//
//        HttpClient httpClient = HttpClientBuilder.create()
//                .setSSLSocketFactory(connectionFactory)
//                .setDefaultCredentialsProvider(credentialsProvider)
//                .build();
//
//        ClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
//        restTemplate = new RestTemplate(requestFactory);
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.registerModule(new Jackson2HalModule());
//
//        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
//        converter.setObjectMapper(objectMapper);
//        restTemplate.setMessageConverters(Collections.<HttpMessageConverter<?>>singletonList(converter));
//
//        user = new User();
//        user.setEmail("hans2@muenchen.de");
//        user.setPassword("test2");
//        user.setUsername("hans2");
//        user.setOid("51");
//        user.setEnabled(TRUE);
//        user2 = new User();
//        user2.setEmail("hans3@muenchen.de");
//        user2.setPassword("test3");
//        user2.setUsername("hans3");
//        user2.setOid(IdService.next());
//        user2.setEnabled(TRUE);
//        user3 = new User();
//        user3.setEmail("hans4@muenchen.de");
//        user3.setPassword("test4");
//        user3.setUsername("hans4");
//        user3.setOid(IdService.next());
//        user3.setEnabled(TRUE);
//        urlSave = "http://localhost:" + port + "/user/save";
//        urlNew = "http://localhost:" + port + "/user/new";
//
//    }
//
//    @Test
//    public void saveUserTest() {
//
//
//        restTemplate.postForEntity(urlSave, user, UserResource.class);
//        String URL4 = "http://localhost:" + port + "/user/51";
//        response = restTemplate.getForEntity(URL4, UserResource.class).getBody();
//
//        assertEquals("test2", response.getPassword());
//        assertNotNull(response.getLink("new"));
//        assertNotNull(response.getLink("update"));
//        assertNotNull(response.getLink("copy"));
//        assertNotNull(response.getLink("self"));
//        assertNotNull(response.getLink("delete"));
//
//    }
//
//    @Test
//    public void queryUserTest() {
//        restTemplate.postForEntity(urlSave, user2, UserResource.class);
//        restTemplate.postForEntity(urlSave, user3, UserResource.class);
//
//        String URL3 = "http://localhost:" + port + "/user/query";
//        responseQuery = restTemplate.getForEntity(URL3, SearchResultResource.class).getBody();
//
//        assertEquals(4, responseQuery.getResult().size());
//        assertNotNull(responseQuery.getLink("self"));
//        assertNotNull(responseQuery.getLink("query"));
//
//    }
//    @Test
//    public void userUpdateTest() {
//
//        User userUpdate = new User();
//        userUpdate.setPassword("test22");
//        userUpdate.setUsername("hans22");
//        userUpdate.setOid("51");
//
//        restTemplate.postForEntity(urlSave, user, UserResource.class).getBody();
//        String URL2 = "http://localhost:" + port + "/user/51";
//        response = restTemplate.postForEntity(URL2, userUpdate, UserResource.class).getBody();
//
//        assertEquals("test22", response.getPassword());
//        assertNotNull(response.getLink("new"));
//        assertNotNull(response.getLink("update"));
//        assertNotNull(response.getLink("copy"));
//        assertNotNull(response.getLink("self"));
//        assertNotNull(response.getLink("delete"));
//
//    }
//
//
//    @Test
//    public void copyUserTest() {
//
//        restTemplate.postForEntity(urlSave, user, UserResource.class).getBody();
//        String URL2 = "http://localhost:" + port + "/user/copy/51";
//        response = restTemplate.getForEntity(URL2, UserResource.class).getBody();
//
//        assertEquals("test2", response.getPassword());
//        assertNotNull(response.getLink("new"));
//        assertNotNull(response.getLink("update"));
//        assertNotNull(response.getLink("copy"));
//        assertNotNull(response.getLink("self"));
//        assertNotNull(response.getLink("delete"));
//    }
//
//    @Test
//    public void deleteUserTest() {
//
//        restTemplate.postForEntity(urlSave, user, UserResource.class).getBody();
//        String URL2 = "http://localhost:" + port + "/user/51";
//        restTemplate.delete(URL2, user);
//        String URL3 = "http://localhost:" + port + "/user/query";
//        responseQuery = restTemplate.getForEntity(URL3, SearchResultResource.class).getBody();
//
//        assertEquals(false, responseQuery.getResult().isEmpty());
//
//    }
//
//    @Test
//    public void newUserTest() throws JsonProcessingException {
//
//        response = restTemplate.getForEntity(urlNew, UserResource.class).getBody();
//        assertNotNull(response.getLink("new"));
//        assertNotNull(response.getLink("save"));
//        assertNotNull(response.getOid());
//
//    }
//    @After
//    public void TearDown() {
//        authPermRepo.deleteAll();
//        userAuthRepo.deleteAll();
//        usersRepo.deleteAll();
//        authRepo.deleteAll();
//        permRepo.deleteAll();
//        buergerRepo.deleteAll();
//        staatRepo.deleteAll();
//        wohnRepo.deleteAll();
//        passRepo.deleteAll();
//        referenceRepo.deleteAll();
//        interneRepo.deleteAll();
//        externeRepo.deleteAll();
//        mandantRepo.deleteAll();
//
//    }
//}
