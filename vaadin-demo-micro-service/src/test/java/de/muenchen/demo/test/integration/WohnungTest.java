package de.muenchen.demo.test.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.muenchen.demo.service.Application;
import de.muenchen.demo.service.domain.Adresse;
import de.muenchen.demo.service.domain.AdresseExterneRepository;
import de.muenchen.demo.service.domain.AdresseInterneRepository;
import de.muenchen.demo.service.domain.AdresseReferenceRepository;
import de.muenchen.demo.service.domain.AuthorityPermissionRepository;
import de.muenchen.demo.service.domain.AuthorityRepository;
import de.muenchen.demo.service.domain.Buerger;
import de.muenchen.demo.service.domain.BuergerRepository;
import de.muenchen.demo.service.domain.MandantRepository;
import de.muenchen.demo.service.domain.PermissionRepository;
import de.muenchen.demo.service.domain.UserAuthorityRepository;
import de.muenchen.demo.service.domain.UserRepository;
import de.muenchen.demo.service.domain.Wohnung;
import de.muenchen.demo.service.domain.WohnungRepository;
import de.muenchen.demo.service.rest.api.AdresseResource;
import de.muenchen.demo.service.rest.api.SearchResultResource;
import de.muenchen.demo.service.rest.api.WohnungResource;
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
public class WohnungTest {

    Wohnung wohnung = new Wohnung();
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
    WohnungRepository wohnRepo;
    @Autowired
    BuergerRepository buergerRepo;
    @Autowired
    AdresseExterneRepository adresseExtRepo;
    @Autowired
    AdresseInterneRepository adresseIntRepo;
    @Autowired
    AdresseReferenceRepository adresseRefRepo;

    @Autowired
    MandantRepository mandantRepo;
    private String urlSave;
    private String urlNew;
    private WohnungResource response;
    private SearchResultResource responseQuery;
    private List responseList;

    @Before
    public void setUp() throws JsonProcessingException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {

        authPermRepo.deleteAll();
        userAuthRepo.deleteAll();
        usersRepo.deleteAll();
        authRepo.deleteAll();
        permRepo.deleteAll();
        mandantRepo.deleteAll();
        buergerRepo.deleteAll();
        wohnRepo.deleteAll();
        adresseRefRepo.deleteAll();
        adresseExtRepo.deleteAll();
        adresseIntRepo.deleteAll();

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
        urlSave = "http://localhost:" + port + "/wohnung/save";
        urlNew = "http://localhost:" + port + "/wohnung/new";

        wohnung.setAusrichtung("Nord");
        wohnung.setStock("2");
        wohnung.setOid("10");

        buerger.setOid("123");
        buerger.setNachname("hans");
        buerger.setVorname("vater");
    }

    @Test
    public void createWohnungTest() throws JsonProcessingException {

        restTemplate.postForEntity(urlSave, wohnung, WohnungResource.class);
        String URL2 = "http://localhost:" + port + "/wohnung/10";
        response = restTemplate.getForEntity(URL2, WohnungResource.class).getBody();
        assertEquals("2", response.getStock());
        assertNotNull(response.getLink("new"));
        assertNotNull(response.getLink("update"));
        assertNotNull(response.getLink("copy"));
        assertNotNull(response.getLink("self"));
        assertNotNull(response.getLink("delete"));
        assertNotNull(response.getLink("adresse"));

    }

    @Test
    public void updateWohnungTest() {

        Wohnung wohnungUpdate = new Wohnung();
        wohnungUpdate.setAusrichtung("West");
        wohnungUpdate.setStock("3");
        wohnungUpdate.setOid("10");

        restTemplate.postForEntity(urlSave, wohnung, WohnungResource.class).getBody();
        String URL2 = "http://localhost:" + port + "/wohnung/10";
        response = restTemplate.postForEntity(URL2, wohnungUpdate, WohnungResource.class).getBody();

        assertEquals("3", response.getStock());
        assertNotNull(response.getLink("new"));
        assertNotNull(response.getLink("update"));
        assertNotNull(response.getLink("copy"));
        assertNotNull(response.getLink("self"));
        assertNotNull(response.getLink("delete"));
        assertNotNull(response.getLink("adresse"));

    }

    @Test
    public void queryWohnungTest() {

        restTemplate.postForEntity(urlSave, wohnung, WohnungResource.class).getBody();
        String URL3 = "http://localhost:" + port + "/wohnung/query";
        responseQuery = restTemplate.getForEntity(URL3, SearchResultResource.class).getBody();

        assertEquals(1, responseQuery.getResult().size());
        assertNotNull(responseQuery.getLink("query"));
        assertNotNull(responseQuery.getLink("self"));
    }

    @Test
    public void copyWohnungTest() {

        restTemplate.postForEntity(urlSave, wohnung, WohnungResource.class).getBody();
        String URL2 = "http://localhost:" + port + "/wohnung/copy/10";
        response = restTemplate.getForEntity(URL2, WohnungResource.class).getBody();

        assertEquals("2", response.getStock());
        assertNotNull(response.getLink("new"));
        assertNotNull(response.getLink("update"));
        assertNotNull(response.getLink("copy"));
        assertNotNull(response.getLink("self"));
        assertNotNull(response.getLink("delete"));
    }

    @Test
    public void deleteWohnungTest() {

        restTemplate.postForEntity(urlSave, wohnung, WohnungResource.class).getBody();
        String URL2 = "http://localhost:" + port + "/wohnung/10";
        restTemplate.delete(URL2, wohnung);
        String URL3 = "http://localhost:" + port + "/wohnung/query";
        responseQuery = restTemplate.getForEntity(URL3, SearchResultResource.class).getBody();

        assertEquals(true, responseQuery.getResult().isEmpty());

    }

    @Test
    public void newWohnungTest() throws JsonProcessingException {

        response = restTemplate.getForEntity(urlNew, WohnungResource.class).getBody();
        assertNotNull(response.getLink("new"));
        assertNotNull(response.getLink("save"));
        assertNotNull(response.getOid());

    }

    @Test
    public void wohnungAdresseTest() {

        Adresse adresse = new Adresse();
        adresse.setHausnummer("12");
        adresse.setOid("10");
        adresse.setPlz(94032);
        adresse.setStrasse("MarinenStr");
        adresse.setStadt("Passau");

        restTemplate.postForEntity(urlSave, wohnung, WohnungResource.class).getBody();

        String URL2 = "http://localhost:" + port + "/adresse/save";
        restTemplate.postForEntity(URL2, adresse, AdresseResource.class);

        /* Test methode addAdresseWohnung*/
        String URL5 = "http://localhost:" + port + "/wohnung/add/wohnung/10/adresse/10";
        WohnungResource response4 = restTemplate.getForEntity(URL5, WohnungResource.class).getBody();
        assertNotNull(response4.getOid());
        
        /*Test methode readAdresseWohnung*/
        String URL1 = "http://localhost:" + port + "/wohnung/adresse/10";
        AdresseResource responseAdresse = restTemplate.getForEntity(URL1, AdresseResource.class).getBody();
        assertEquals("Passau", responseAdresse.getStadt());

    }

    @After
    public void TearDown() {
        authPermRepo.deleteAll();
        userAuthRepo.deleteAll();
        usersRepo.deleteAll();
        authRepo.deleteAll();
        permRepo.deleteAll();
        buergerRepo.deleteAll();
        wohnRepo.deleteAll();
        adresseRefRepo.deleteAll();
        adresseExtRepo.deleteAll();
        adresseIntRepo.deleteAll();

    }

}
