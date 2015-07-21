package de.muenchen.demo.test;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import de.muenchen.demo.service.Application;
import de.muenchen.demo.service.domain.AuthorityPermissionRepository;
import de.muenchen.demo.service.domain.AuthorityRepository;
import de.muenchen.demo.service.domain.Buerger;
import de.muenchen.demo.service.domain.BuergerRepository;
import de.muenchen.demo.service.domain.MandantRepository;
import de.muenchen.demo.service.domain.PermissionRepository;
import de.muenchen.demo.service.domain.Staatsangehoerigkeit;
import de.muenchen.demo.service.domain.StaatsangehoerigkeitReferenceRepository;
import de.muenchen.demo.service.domain.UserAuthorityRepository;
import de.muenchen.demo.service.domain.UserRepository;
import de.muenchen.demo.service.rest.api.BuergerResource;
import de.muenchen.demo.service.rest.api.SearchResultResource;
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
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Rule;
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

/**
 *
 * @author praktikant.tmar
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest({"server.port=0", "management.port=0"})
public class StaatsangehoerigkeitTest {

    private RestTemplate restTemplate = new TestRestTemplate();
    @Value("${local.server.port}")
    private int port;
    private final Buerger bVater = new Buerger();

    public StaatsangehoerigkeitTest() {
    }
    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8089);

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
        staatRepo.deleteAll();

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

        ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally

        Staatsangehoerigkeit s = new Staatsangehoerigkeit();
        s.setCode("de");
        s.setLand("Deutschland");
        s.setSprache("Deutsch");
        s.setReference("123");

        String json = mapper.writeValueAsString(s);

        stubFor(get(urlEqualTo("/staat/123")).willReturn(
                aResponse().withHeader("Content-Type", "application/json")
                .withBody(json)
        ));

        bVater.setOid("30");
        bVater.setNachname("hans");
        bVater.setVorname("vater");
        Date dV = new Date();
    }

    @Test
    public void createStaatsangehoerigkeitTest() throws JsonProcessingException {

        String URL2 = "http://localhost:" + port + "/staat/create/123";
        Staatsangehoerigkeit staat = restTemplate.getForEntity(URL2, Staatsangehoerigkeit.class).getBody();
        assertEquals("de", staat.getCode());

        String URL = "http://localhost:" + port + "/staat/123";
        Staatsangehoerigkeit staat2 = restTemplate.getForEntity(URL, Staatsangehoerigkeit.class).getBody();
        assertEquals("de", staat2.getCode());

    }

    @Test
    public void createStaatsangehoerigkeitBuergerTest() throws JsonProcessingException {

        String URL2 = "http://localhost:" + port + "/staat/create/123";
        Staatsangehoerigkeit staat = restTemplate.getForEntity(URL2, Staatsangehoerigkeit.class).getBody();
        assertEquals("de", staat.getCode());

        String URL10 = "http://localhost:" + port + "/buerger/save";
        ResponseEntity<BuergerResource> response = restTemplate.postForEntity(URL10, bVater, BuergerResource.class);

        String URL11 = "http://localhost:" + port + "/buerger/30";
        Buerger wo = restTemplate.getForEntity(URL11, Buerger.class).getBody();
        assertEquals("hans", wo.getNachname());

        String URL12 = "http://localhost:" + port + "/buerger/add/buerger/30/staats/123";
        ResponseEntity response4 = restTemplate.getForEntity(URL12, BuergerResource.class);
        BuergerResource response9 = (BuergerResource) response4.getBody();
        assertEquals(1, response9.getStaatsangehoerigkeiten().size());
    }

    @Test
    public void queryStaatsangehoerigkeitTest() throws JsonProcessingException {

        String URL2 = "http://localhost:" + port + "/staat/create/123";
        Staatsangehoerigkeit staat = restTemplate.getForEntity(URL2, Staatsangehoerigkeit.class).getBody();
        assertEquals("de", staat.getCode());

        String URL = "http://localhost:" + port + "/staat/query";
        SearchResultResource response = restTemplate.getForEntity(URL, SearchResultResource.class).getBody();
        assertEquals(1, response.getResult().size());

    }

    @Test
    public void staatsangehoerigkeitDeleteTest() {

        String URL2 = "http://localhost:" + port + "/staat/create/123";
        Staatsangehoerigkeit staat = restTemplate.getForEntity(URL2, Staatsangehoerigkeit.class).getBody();
        assertEquals("de", staat.getCode());
        String URL = "http://localhost:" + port + "/staat/123";
        restTemplate.delete(URL);
        String URL3 = "http://localhost:" + port + "/staat/query";
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
        staatRepo.deleteAll();
        mandantRepo.deleteAll();

    }
}
