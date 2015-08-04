package de.muenchen.demo.test.integration;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import de.muenchen.demo.service.Application;
import de.muenchen.demo.service.domain.AuthorityPermissionRepository;
import de.muenchen.demo.service.domain.AuthorityRepository;
import de.muenchen.demo.service.domain.Buerger;
import de.muenchen.demo.service.domain.BuergerRepository;
import de.muenchen.demo.service.domain.MandantRepository;
import de.muenchen.demo.service.domain.Pass;
import de.muenchen.demo.service.domain.PassRepository;
import de.muenchen.demo.service.domain.PermissionRepository;
import de.muenchen.demo.service.domain.Staatsangehoerigkeit;
import de.muenchen.demo.service.domain.StaatsangehoerigkeitReferenceRepository;
import de.muenchen.demo.service.domain.UserAuthorityRepository;
import de.muenchen.demo.service.domain.UserRepository;
import de.muenchen.demo.service.domain.Wohnung;
import de.muenchen.demo.service.domain.WohnungRepository;
import de.muenchen.demo.service.rest.api.BuergerResource;
import de.muenchen.demo.service.rest.api.PassResource;
import de.muenchen.demo.service.rest.api.SearchResultResource;
import de.muenchen.demo.service.rest.api.StaatsangehoerigkeitResource;
import de.muenchen.demo.service.services.BuergerService;
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
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Rule;
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
public class BuergerTest {

    private RestTemplate restTemplate;
    @Value("${local.server.port}")
    private int port;
    BuergerService service;
    private final Buerger buerger = new Buerger();
    private final Buerger buergerUpdate = new Buerger();
    private final Buerger kind = new Buerger();
    private final Buerger kind2 = new Buerger();
    private final Pass pass = new Pass();
    private final Wohnung wohnung = new Wohnung();
    private BuergerResource response;
    private List responseList;
    private String urlSave;
    private String urlNew;

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
    WohnungRepository wohnRepo;
    @Autowired
    PassRepository passRepo;

    @Autowired
    MandantRepository mandantRepo;
    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8089);

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
        ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally

        Staatsangehoerigkeit staat = new Staatsangehoerigkeit();
        staat.setCode("de");
        staat.setLand("Deutschland");
        staat.setSprache("Deutsch");
        staat.setReference("123");

        String json = mapper.writeValueAsString(staat);

        stubFor(get(urlEqualTo("/staat/123")).willReturn(
                aResponse().withHeader("Content-Type", "application/json")
                .withBody(json)
        ));

        urlSave = "http://localhost:" + port + "/buerger/save";
        urlNew = "http://localhost:" + port + "/buerger/new";

        buerger.setOid("b");
        buerger.setNachname("hans");
        buerger.setVorname("hans");

        buergerUpdate.setOid("b");
        buergerUpdate.setNachname("max");
        buergerUpdate.setVorname("hans");

        kind.setOid("bk");
        kind.setVorname("son");
        kind.setNachname("hans");
        kind2.setOid("bk2");
        kind2.setVorname("son");
        kind2.setNachname("hans");
        wohnung.setOid("bw");
        wohnung.setAusrichtung("West");
        wohnung.setStock("2");

        pass.setOid("bp");
        pass.setPassNummer("208040");
        pass.setKode("D");
    }

    @Test
    public void newBuergerTest() throws JsonProcessingException {

        response = restTemplate.getForEntity(urlNew, BuergerResource.class).getBody();
        assertNotEquals(null, response.getLink("new"));
        assertNotEquals(null, response.getLink("save"));
        assertNotEquals(null, response.getOid());

    }

    @Test
    public void updateBuergerTest() {

        restTemplate.postForEntity(urlSave, buerger, BuergerResource.class).getBody();
        String URL2 = "http://localhost:" + port + "/buerger/b";
        response = restTemplate.postForEntity(URL2, buergerUpdate, BuergerResource.class).getBody();

        assertEquals("max", response.getNachname());
        assertNotEquals(null, response.getLink("new"));
        assertNotEquals(null, response.getLink("update"));
        assertNotEquals(null, response.getLink("copy"));
        assertNotEquals(null, response.getLink("self"));
        assertNotEquals(null, response.getLink("delete"));
        assertNotEquals(null, response.getLink("pass"));
        assertNotEquals(null, response.getLink("wohnungen"));
        assertNotEquals(null, response.getLink("kinder"));
        assertNotEquals(null, response.getLink("staatsangehoerigkeit"));
    }

    @Test
    public void saveBuergerTest() throws JsonProcessingException {

        response = restTemplate.postForEntity(urlSave, buerger, BuergerResource.class).getBody();
        assertEquals(response.getNachname(), "hans");
        assertNotEquals(null, response.getLink("new"));
        assertNotEquals(null, response.getLink("update"));
        assertNotEquals(null, response.getLink("copy"));
        assertNotEquals(null, response.getLink("self"));
        assertNotEquals(null, response.getLink("delete"));
        assertNotEquals(null, response.getLink("pass"));
        assertNotEquals(null, response.getLink("wohnungen"));
        assertNotEquals(null, response.getLink("kinder"));
        assertNotEquals(null, response.getLink("staatsangehoerigkeit"));
    }

    @Test
    public void readBuergerTest() throws JsonProcessingException {

        response = restTemplate.postForEntity(urlSave, buerger, BuergerResource.class).getBody();
        String URL11 = "http://localhost:" + port + "/buerger/b";
        response = restTemplate.getForEntity(URL11, BuergerResource.class).getBody();
        assertEquals(response.getNachname(), "hans");
        assertNotEquals(null, response.getLink("new"));
        assertNotEquals(null, response.getLink("update"));
        assertNotEquals(null, response.getLink("copy"));
        assertNotEquals(null, response.getLink("self"));
        assertNotEquals(null, response.getLink("delete"));
        assertNotEquals(null, response.getLink("pass"));
        assertNotEquals(null, response.getLink("wohnungen"));
        assertNotEquals(null, response.getLink("kinder"));
        assertNotEquals(null, response.getLink("staatsangehoerigkeit"));

    }

    @Test
    public void queryBuergerTest() throws JsonProcessingException {

        restTemplate.postForEntity(urlSave, buerger, BuergerResource.class).getBody();

        String URL11 = "http://localhost:" + port + "/buerger/query";
        SearchResultResource queryResponse = restTemplate.getForEntity(URL11, SearchResultResource.class).getBody();
        assertEquals(1, queryResponse.getResult().size());
        assertNotEquals(null, queryResponse.getLink("self"));
        assertNotEquals(null, queryResponse.getLink("query"));

    }

//    @Test
//    public void copyBuergerTest() {
//
//        restTemplate.postForEntity(urlSave, buerger, BuergerResource.class).getBody();
//        String URL2 = "http://localhost:" + port + "/buerger/copy/b";
//        response = restTemplate.getForEntity(URL2, BuergerResource.class).getBody();
//
//        assertNotEquals("b", response.getOid());
//        assertNotEquals(null, response.getLink("new"));
//        assertNotEquals(null, response.getLink("update"));
//        assertNotEquals(null, response.getLink("copy"));
//        assertNotEquals(null, response.getLink("self"));
//        assertNotEquals(null, response.getLink("delete"));
//
//    }
    @Test
    public void buergerDeleteTest() {

        restTemplate.postForEntity(urlSave, buerger, BuergerResource.class).getBody();
        String URL2 = "http://localhost:" + port + "/buerger/b";
        restTemplate.delete(URL2, buerger);
        String URL3 = "http://localhost:" + port + "/buerger/query";
        SearchResultResource responseQuery = restTemplate.getForEntity(URL3, SearchResultResource.class).getBody();

        assertEquals(true, responseQuery.getResult().isEmpty());

    }

    @Test
    public void BuergerKindTest() {

        restTemplate.postForEntity(urlSave, buerger, BuergerResource.class);
        restTemplate.postForEntity(urlSave, kind, BuergerResource.class);

        String URL2 = "http://localhost:" + port + "/buerger/b";

        /* Test methode createKindBuerger*/
        String URL3 = "http://localhost:" + port + "/buerger/create/kind/b";
        restTemplate.postForEntity(URL3, kind2, BuergerResource.class);
        response = restTemplate.getForEntity(URL2, BuergerResource.class).getBody();
        assertEquals(false, response.getKinder().isEmpty());
        assertNotEquals(null, response.getLink("new"));
        assertNotEquals(null, response.getLink("update"));
        assertNotEquals(null, response.getLink("copy"));
        assertNotEquals(null, response.getLink("self"));
        assertNotEquals(null, response.getLink("delete"));
        assertNotEquals(null, response.getLink("pass"));
        assertNotEquals(null, response.getLink("wohnungen"));
        assertNotEquals(null, response.getLink("kinder"));
        assertNotEquals(null, response.getLink("staatsangehoerigkeit"));

        /* Test methode addKindBuerger*/
        String URL5 = "http://localhost:" + port + "/buerger/add/buerger/b/kind/bk";
        response = restTemplate.getForEntity(URL5, BuergerResource.class).getBody();
        assertEquals(2, response.getKinder().size());
        assertNotEquals(null, response.getLink("new"));
        assertNotEquals(null, response.getLink("update"));
        assertNotEquals(null, response.getLink("copy"));
        assertNotEquals(null, response.getLink("self"));
        assertNotEquals(null, response.getLink("delete"));
        assertNotEquals(null, response.getLink("pass"));
        assertNotEquals(null, response.getLink("wohnungen"));
        assertNotEquals(null, response.getLink("kinder"));
        assertNotEquals(null, response.getLink("staatsangehoerigkeit"));


        /*Test methode readKinderBuerger*/
        String URL1 = "http://localhost:" + port + "/buerger/kinder/b";
        responseList = restTemplate.getForEntity(URL1, List.class).getBody();
        assertEquals(2, responseList.size());
    }

    @Test
    public void wohnungBuergerTest() {

        restTemplate.postForEntity(urlSave, buerger, BuergerResource.class);

        String URL2 = "http://localhost:" + port + "/buerger/b";

        /* Test methode createWohnungBuerger*/
        String URL3 = "http://localhost:" + port + "/buerger/create/wohnung/b";
        restTemplate.postForEntity(URL3, wohnung, BuergerResource.class);
        response = restTemplate.getForEntity(URL2, BuergerResource.class).getBody();
        assertEquals(false, response.getWohnungen().isEmpty());
        assertNotEquals(null, response.getLink("new"));
        assertNotEquals(null, response.getLink("update"));
        assertNotEquals(null, response.getLink("copy"));
        assertNotEquals(null, response.getLink("self"));
        assertNotEquals(null, response.getLink("delete"));
        assertNotEquals(null, response.getLink("pass"));
        assertNotEquals(null, response.getLink("wohnungen"));
        assertNotEquals(null, response.getLink("kinder"));
        assertNotEquals(null, response.getLink("staatsangehoerigkeit"));

        /* Test methode addWohnungBuerger*/
        String URL5 = "http://localhost:" + port + "/buerger/add/buerger/b/wohnung/bw";
        response = restTemplate.getForEntity(URL5, BuergerResource.class).getBody();
        assertEquals(1, response.getWohnungen().size());
        assertNotEquals(null, response.getLink("new"));
        assertNotEquals(null, response.getLink("update"));
        assertNotEquals(null, response.getLink("copy"));
        assertNotEquals(null, response.getLink("self"));
        assertNotEquals(null, response.getLink("delete"));
        assertNotEquals(null, response.getLink("pass"));
        assertNotEquals(null, response.getLink("wohnungen"));
        assertNotEquals(null, response.getLink("kinder"));
        assertNotEquals(null, response.getLink("staatsangehoerigkeit"));

        /*Test methode readBuergerWohnungen*/
        String URL1 = "http://localhost:" + port + "/buerger/wohnungen/b";
        responseList = restTemplate.getForEntity(URL1, List.class).getBody();
        assertEquals(1, responseList.size());

    }

    @Test
    public void createStaatsangehoerigkeitBuergerTest() throws JsonProcessingException {

        String URL2 = "http://localhost:" + port + "/staat/create/123";
        StaatsangehoerigkeitResource staat = restTemplate.getForEntity(URL2, StaatsangehoerigkeitResource.class).getBody();
        assertEquals("de", staat.getCode());

        restTemplate.postForEntity(urlSave, buerger, BuergerResource.class);

        /* Test methode addStaatsangehoerigkeitBuerger*/
        String URL12 = "http://localhost:" + port + "/buerger/add/buerger/b/staats/123";
        response = restTemplate.getForEntity(URL12, BuergerResource.class).getBody();
        assertEquals(1, response.getStaatsangehoerigkeiten().size());
        assertNotEquals(null, response.getLink("new"));
        assertNotEquals(null, response.getLink("update"));
        assertNotEquals(null, response.getLink("copy"));
        assertNotEquals(null, response.getLink("self"));
        assertNotEquals(null, response.getLink("delete"));
        assertNotEquals(null, response.getLink("pass"));
        assertNotEquals(null, response.getLink("wohnungen"));
        assertNotEquals(null, response.getLink("kinder"));
        assertNotEquals(null, response.getLink("staatsangehoerigkeit"));

        /*Test methode readStaatsangehoerigkeitenBuerger*/
        String URL1 = "http://localhost:" + port + "/buerger/staats/b";
        responseList = restTemplate.getForEntity(URL1, List.class).getBody();
        assertEquals(1, responseList.size());
    }

    @Test
    public void buergerPassTest() throws JsonProcessingException {

        restTemplate.postForEntity(urlSave, buerger, BuergerResource.class);

        String URL13 = "http://localhost:" + port + "/pass/save";
        restTemplate.postForEntity(URL13, pass, PassResource.class).getBody();

        /* Test methode addPassBuerger*/
        String URL12 = "http://localhost:" + port + "/buerger/add/buerger/b/pass/bp";
        response = restTemplate.getForEntity(URL12, BuergerResource.class).getBody();
        assertEquals(1, response.getPass().size());
        assertNotEquals(null, response.getLink("new"));
        assertNotEquals(null, response.getLink("update"));
        assertNotEquals(null, response.getLink("copy"));
        assertNotEquals(null, response.getLink("self"));
        assertNotEquals(null, response.getLink("delete"));
        assertNotEquals(null, response.getLink("pass"));
        assertNotEquals(null, response.getLink("wohnungen"));
        assertNotEquals(null, response.getLink("kinder"));
        assertNotEquals(null, response.getLink("staatsangehoerigkeit"));

        /*Test methode readPassBuerger*/
        String URL1 = "http://localhost:" + port + "/buerger/pass/b";
        responseList = restTemplate.getForEntity(URL1, List.class).getBody();
        assertEquals(1, responseList.size());
    }

    @After
    public void TearDown() {
        authPermRepo.deleteAll();
        userAuthRepo.deleteAll();
        usersRepo.deleteAll();
        authRepo.deleteAll();
        permRepo.deleteAll();
        buergerRepo.deleteAll();
        staatRepo.deleteAll();
        passRepo.deleteAll();
        wohnRepo.deleteAll();
        mandantRepo.deleteAll();

    }

}
