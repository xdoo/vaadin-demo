package de.muenchen.demo.test.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.muenchen.demo.service.Application;
import de.muenchen.demo.service.domain.AdresseExterneRepository;
import de.muenchen.demo.service.domain.AdresseInterneRepository;
import de.muenchen.demo.service.domain.AdresseReferenceRepository;
import de.muenchen.demo.service.domain.AuthorityPermissionRepository;
import de.muenchen.demo.service.domain.AuthorityRepository;
import de.muenchen.demo.service.domain.Buerger;
import de.muenchen.demo.service.domain.BuergerRepository;
import de.muenchen.demo.service.domain.MandantRepository;
import de.muenchen.demo.service.domain.PassRepository;
import de.muenchen.demo.service.domain.PermissionRepository;
import de.muenchen.demo.service.domain.StaatsangehoerigkeitReferenceRepository;
import de.muenchen.demo.service.domain.UserAuthorityRepository;
import de.muenchen.demo.service.domain.UserRepository;
import de.muenchen.demo.service.domain.Sachbearbeiter;
import de.muenchen.demo.service.domain.SachbearbeiterRepository;
import de.muenchen.demo.service.domain.User;
import de.muenchen.vaadin.demo.api.rest.BuergerResource;
import de.muenchen.vaadin.demo.api.rest.SearchResultResource;
import de.muenchen.demo.service.rest.api.SachbearbeiterResource;
import de.muenchen.demo.service.rest.api.UserResource;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
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
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.hateoas.hal.Jackson2HalModule;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author praktikant.tmar
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest({"server.port=0", "management.port=0"})
public class SachbearbeiterTest {

    Sachbearbeiter sachbearbeiter = new Sachbearbeiter();
    Sachbearbeiter sachbearbeiter2 = new Sachbearbeiter();
    Buerger buerger = new Buerger();

    private RestTemplate restTemplate = new TestRestTemplate();
    @Value("${local.server.port}")
    private int port;

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
    StaatsangehoerigkeitReferenceRepository staatRepo;
    @Autowired
    BuergerRepository buergerRepo;
    @Autowired
    SachbearbeiterRepository wohnRepo;
    @Autowired
    PassRepository passRepo;
    @Autowired
    AdresseInterneRepository interneRepo;
    @Autowired
    AdresseExterneRepository externeRepo;
    @Autowired
    AdresseReferenceRepository referenceRepo;
    @Autowired
    MandantRepository mandantRepo;
    @Autowired
    SachbearbeiterRepository sachbearbeiterRepo;

    private String urlSave;
    private String urlBuergerSave;
    private String urlNew;
    private SachbearbeiterResource response;
    private SearchResultResource responseQuery;
    private List responseList;
    private String urlUserSave;

    @Before
    public void setUp() throws JsonProcessingException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {

        authPermRepo.deleteAll();
        userAuthRepo.deleteAll();
        usersRepo.deleteAll();
        authRepo.deleteAll();
        permRepo.deleteAll();
        buergerRepo.deleteAll();
        staatRepo.deleteAll();
        wohnRepo.deleteAll();
        passRepo.deleteAll();
        referenceRepo.deleteAll();
        interneRepo.deleteAll();
        externeRepo.deleteAll();
        sachbearbeiterRepo.deleteAll();

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
        urlSave = "http://localhost:" + port + "/sachbearbeiter/save";
        urlBuergerSave = "http://localhost:" + port + "/buerger/save";
        urlUserSave = "http://localhost:" + port + "/user/save";
        urlNew = "http://localhost:" + port + "/sachbearbeiter/new";

        sachbearbeiter.setFax("089214589");
        sachbearbeiter.setFunktion("employee");
        sachbearbeiter.setOid("10");
        sachbearbeiter.setFax("089214589");
        sachbearbeiter.setFunktion("employe");
        sachbearbeiter2.setOid("11");
        buerger.setOid("b");
        buerger.setNachname("hans");
        buerger.setVorname("max");
    }

    @Test
    public void createSachbearbeiterTest() throws JsonProcessingException {

        restTemplate.postForEntity(urlSave, sachbearbeiter, SachbearbeiterResource.class);
        String URL2 = "http://localhost:" + port + "/sachbearbeiter/10";
        response = restTemplate.getForEntity(URL2, SachbearbeiterResource.class).getBody();
        assertEquals("employe", response.getFunktion());
        assertNotNull(response.getLink("new"));
        assertNotNull(response.getLink("update"));
        assertNotNull(response.getLink("copy"));
        assertNotNull(response.getLink("self"));
        assertNotNull(response.getLink("delete"));

    }

    @Test
    public void updateSachbearbeiterTest() {

        Sachbearbeiter sachbearbeiterUpdate = new Sachbearbeiter();
        sachbearbeiterUpdate.setFunktion("employe");
        sachbearbeiterUpdate.setOid("10");

        restTemplate.postForEntity(urlSave, sachbearbeiter, SachbearbeiterResource.class).getBody();
        String URL2 = "http://localhost:" + port + "/sachbearbeiter/10";
        response = restTemplate.postForEntity(URL2, sachbearbeiterUpdate, SachbearbeiterResource.class).getBody();

        assertEquals("employe", response.getFunktion());
        assertNotNull(response.getLink("new"));
        assertNotNull(response.getLink("update"));
        assertNotNull(response.getLink("copy"));
        assertNotNull(response.getLink("self"));
        assertNotNull(response.getLink("delete"));

    }

    @Test
    public void querySachbearbeiterTest() {

        restTemplate.postForEntity(urlSave, sachbearbeiter, SachbearbeiterResource.class).getBody();
        String URL3 = "http://localhost:" + port + "/sachbearbeiter/query";
        responseQuery = restTemplate.getForEntity(URL3, SearchResultResource.class).getBody();

        assertEquals(1, responseQuery.getResult().size());
        assertNotNull(responseQuery.getLink("query"));
        assertNotNull(responseQuery.getLink("self"));
    }

    @Test
    public void copySachbearbeiterTest() {

        restTemplate.postForEntity(urlSave, sachbearbeiter, SachbearbeiterResource.class).getBody();
        String URL2 = "http://localhost:" + port + "/sachbearbeiter/copy/10";
        response = restTemplate.getForEntity(URL2, SachbearbeiterResource.class).getBody();

        assertEquals("employe", response.getFunktion());
        assertNotNull(response.getLink("new"));
        assertNotNull(response.getLink("update"));
        assertNotNull(response.getLink("copy"));
        assertNotNull(response.getLink("self"));
        assertNotNull(response.getLink("delete"));
    }

    @Test
    public void deleteSachbearbeiterTest() {

        restTemplate.postForEntity(urlSave, sachbearbeiter, SachbearbeiterResource.class).getBody();
        String URL2 = "http://localhost:" + port + "/sachbearbeiter/10";
        restTemplate.delete(URL2, sachbearbeiter);
        String URL3 = "http://localhost:" + port + "/sachbearbeiter/query";
        responseQuery = restTemplate.getForEntity(URL3, SearchResultResource.class).getBody();

        assertEquals(true, responseQuery.getResult().isEmpty());

    }

    @Test
    public void newSachbearbeiterTest() throws JsonProcessingException {

        response = restTemplate.getForEntity(urlNew, SachbearbeiterResource.class).getBody();
        assertNotNull(response.getLink("new"));
        assertNotNull(response.getLink("save"));
        assertNotNull(response.getOid());

    }

    @Test
    public void sachbearbeiterBuergerTest() {
        Buerger b = new Buerger();
        b.setOid("b2");
        b.setNachname("ali");
        restTemplate.postForEntity(urlSave, sachbearbeiter, SachbearbeiterResource.class).getBody();

        restTemplate.postForEntity(urlBuergerSave, b, BuergerResource.class);

        String URL3 = "http://localhost:" + port + "/sachbearbeiter/create/buerger/10";
        restTemplate.postForEntity(URL3, buerger, SachbearbeiterResource.class).getBody();

        String URL5 = "http://localhost:" + port + "/sachbearbeiter/add/sachbearbeiter/10/buerger/b2";
        restTemplate.getForEntity(URL5, SachbearbeiterResource.class).getBody();
        
        /*Test methode readSachbearbeiterBuerger*/
//        String urlReleaseSachbearbeiterBuerger = "http://localhost:" + port + "/sachbearbeiter/release/buerger/10";
//        restTemplate.getForEntity(urlReleaseSachbearbeiterBuerger, SachbearbeiterResource.class);
//        String urlReadSachbearbeiterBuerger = "http://localhost:" + port + "/sachbearbeiter/buerger/10";
//        responseList = restTemplate.getForEntity(urlReadSachbearbeiterBuerger, List.class).getBody();
//        assertEquals(0, responseList.size());
        /*Test Delete Buerger*/
//        String urlBuergerDelete = "http://localhost:" + port + "/buerger/b2";
//        restTemplate.delete(urlBuergerDelete);
        /*Test Delete Buerger*/
        String urlSachbearbeiterDelete = "http://localhost:" + port + "/sachbearbeiter/10";
        restTemplate.delete(urlSachbearbeiterDelete);
        
        String urlBuergerDelete2 = "http://localhost:" + port + "/buerger/b";
        restTemplate.delete(urlBuergerDelete2);

    }

    @Test
    public void sachbearbeiterUserTest() {
        User user = new User();
        user.setOid("user1");
        user.setUsername("hans");
        User user1 = new User();
        user1.setOid("user2");
        user1.setUsername("max");
        restTemplate.postForEntity(urlSave, sachbearbeiter, SachbearbeiterResource.class).getBody();

        restTemplate.postForEntity(urlUserSave, user, UserResource.class);
        //restTemplate.postForEntity(urlBuergerSave, buerger, BuergerResource.class);

        String URL3 = "http://localhost:" + port + "/sachbearbeiter/create/user/10";
        restTemplate.postForEntity(URL3, user1, SachbearbeiterResource.class).getBody();

        String URL5 = "http://localhost:" + port + "/sachbearbeiter/add/sachbearbeiter/10/user/user1";
        restTemplate.getForEntity(URL5, SachbearbeiterResource.class).getBody();


        /*Test methode readSachbearbeiterBuerger*/
        String urlReadSachbearbeiterBuerger = "http://localhost:" + port + "/sachbearbeiter/user/10";
        UserResource responseUser = restTemplate.getForEntity(urlReadSachbearbeiterBuerger, UserResource.class).getBody();
        assertEquals("hans", responseUser.getUsername());
        String urlUserDelete = "http://localhost:" + port + "/user/user1";
        restTemplate.delete(urlUserDelete);

    }

    @After
    public void TearDown() {
        sachbearbeiterRepo.deleteAll();
        authPermRepo.deleteAll();
        userAuthRepo.deleteAll();
        usersRepo.deleteAll();
        authRepo.deleteAll();
        permRepo.deleteAll();
        buergerRepo.deleteAll();
        staatRepo.deleteAll();
        wohnRepo.deleteAll();
        passRepo.deleteAll();
        referenceRepo.deleteAll();
        interneRepo.deleteAll();
        externeRepo.deleteAll();
        mandantRepo.deleteAll();

    }

}
