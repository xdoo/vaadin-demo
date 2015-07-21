package de.muenchen.demo.test;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import de.muenchen.demo.service.rest.api.BuergerResource;
import de.muenchen.demo.service.rest.api.SearchResultResource;
import de.muenchen.demo.service.rest.api.WohnungResource;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
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
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
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
    }

    @Test
    public void createWohnungTest() throws JsonProcessingException {

        Wohnung w = new Wohnung();
        w.setAusrichtung("Nord");
        w.setStock("2");
        w.setOid("123");

        String URL = "http://localhost:" + port + "/wohnung/save";
        restTemplate.postForEntity(URL, w, WohnungResource.class);
        String URL2 = "http://localhost:" + port + "/wohnung/123";
        WohnungResource wohnung = restTemplate.getForEntity(URL2, WohnungResource.class).getBody();
        assertEquals("2", wohnung.getStock());
    }

    @Test
    public void wohnungBuergerTest() {

        Buerger buerger = new Buerger();
        buerger.setOid("123");
        buerger.setNachname("hans");
        buerger.setVorname("vater");
        Date dV = new Date();

        Wohnung wohnung = new Wohnung();
        wohnung.setAusrichtung("Nord");
        wohnung.setStock("2");
        wohnung.setOid("1");

        String URL = "http://localhost:" + port + "/buerger/save";
        ResponseEntity<BuergerResource> response = restTemplate.postForEntity(URL, buerger, BuergerResource.class);
        Object body = response.getBody();

        String URL2 = "http://localhost:" + port + "/buerger/123";
        Buerger wo = restTemplate.getForEntity(URL2, Buerger.class).getBody();
        assertEquals("hans", wo.getNachname());

        /* Test methode createWohnungBuerger*/
        String URL3 = "http://localhost:" + port + "/buerger/create/wohnung/123";
        ResponseEntity<BuergerResource> response2 = restTemplate.postForEntity(URL3, wohnung, BuergerResource.class);
        Buerger w = restTemplate.getForEntity(URL2, Buerger.class).getBody();
        assertEquals(false, w.getWohnungen().isEmpty());

        /* Test methode addWohnungBuerger*/
        String URL5 = "http://localhost:" + port + "/buerger/add/buerger/123/wohnung/1";
        BuergerResource response4 = restTemplate.getForEntity(URL5, BuergerResource.class).getBody();
        assertEquals(1, response4.getWohnungen().size());

    }

    @Test
    public void wohnungAdresseTest() {

        Adresse adresse = new Adresse();
        adresse.setHausnummer("12");
        adresse.setOid("10");
        adresse.setPlz(94032);
        adresse.setStrasse("MarinenStr");
        adresse.setStadt("Passau");

        Wohnung wohnung = new Wohnung();
        wohnung.setAusrichtung("Nord");
        wohnung.setStock("2");
        wohnung.setOid("10");
        String URL = "http://localhost:" + port + "/wohnung/save";
        WohnungResource response = restTemplate.postForEntity(URL, wohnung, WohnungResource.class).getBody();

        String URL2 = "http://localhost:" + port + "/adresse/save";
        ResponseEntity<AdresseResource> response2;
        response2 = restTemplate.postForEntity(URL2, adresse, AdresseResource.class);

        /* Test methode addAdresseWohnung*/
        String URL5 = "http://localhost:" + port + "/wohnung/add/wohnung/10/adresse/10";
        WohnungResource response4 = restTemplate.getForEntity(URL5, WohnungResource.class).getBody();
        assertEquals(94032, response4.getAdresse().getAdresseExterne().getPlz());

    }

    @Test
    public void wohnungUpdateTest() {

        Wohnung wohnung = new Wohnung();
        wohnung.setAusrichtung("Nord");
        wohnung.setStock("2");
        wohnung.setOid("10");
        Wohnung wohnungUpdate = new Wohnung();
        wohnungUpdate.setAusrichtung("West");
        wohnungUpdate.setStock("3");
        wohnungUpdate.setOid("10");
        String URL = "http://localhost:" + port + "/wohnung/save";
        restTemplate.postForEntity(URL, wohnung, WohnungResource.class).getBody();
        String URL2 = "http://localhost:" + port + "/wohnung/10";
        WohnungResource response2 = restTemplate.postForEntity(URL2, wohnungUpdate, WohnungResource.class).getBody();

        assertEquals("3", response2.getStock());

    }
    
    @Test
    public void wohnungQueryTest() {

        Wohnung wohnung = new Wohnung();
        wohnung.setAusrichtung("Nord");
        wohnung.setStock("2");
        wohnung.setOid("10");
        Wohnung wohnung2 = new Wohnung();
        wohnung2.setAusrichtung("West");
        wohnung2.setStock("3");
        wohnung2.setOid("11");
        String URL = "http://localhost:" + port + "/wohnung/save";
        restTemplate.postForEntity(URL, wohnung, WohnungResource.class).getBody();
        restTemplate.postForEntity(URL, wohnung2, WohnungResource.class).getBody();
        String URL3 = "http://localhost:" + port + "/wohnung/query";
        SearchResultResource response = restTemplate.getForEntity(URL3, SearchResultResource.class).getBody();

        assertEquals(2, response.getResult().size());

    }
    
    @Test
    public void wohnungCopyTest() {

        Wohnung wohnung = new Wohnung();
        wohnung.setAusrichtung("Nord");
        wohnung.setStock("2");
        wohnung.setOid("10");
        String URL = "http://localhost:" + port + "/wohnung/save";
        restTemplate.postForEntity(URL, wohnung, WohnungResource.class).getBody();
        String URL2 = "http://localhost:" + port + "/wohnung/copy/10";
        WohnungResource response2 = restTemplate.getForEntity(URL2, WohnungResource.class).getBody();

        assertNotEquals("10", response2.getOid());

    }
    @Test
    public void wohnungDeleteTest() {

        Wohnung wohnung = new Wohnung();
        wohnung.setAusrichtung("Nord");
        wohnung.setStock("2");
        wohnung.setOid("10");
        String URL = "http://localhost:" + port + "/wohnung/save";
        restTemplate.postForEntity(URL, wohnung, WohnungResource.class).getBody();
        String URL2 = "http://localhost:" + port + "/wohnung/10";
        restTemplate.delete(URL2,wohnung);
        String URL3 = "http://localhost:" + port + "/wohnung/query";
        SearchResultResource response = restTemplate.getForEntity(URL3, SearchResultResource.class).getBody();

        assertEquals(true, response.getResult().isEmpty());

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
