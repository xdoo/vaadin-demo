package de.muenchen.demo.test.integration;

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
import de.muenchen.demo.service.rest.api.StaatsangehoerigkeitResource;
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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.hateoas.hal.Jackson2HalModule;
import org.springframework.http.ResponseEntity;
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
public class StaatsangehoerigkeitTest {

    private RestTemplate restTemplate = new TestRestTemplate();
    @Value("${local.server.port}")
    private int port;
    private final Buerger buerger = new Buerger();

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

        buerger.setOid("30");
        buerger.setNachname("hans");
        buerger.setVorname("vater");
    }

    @Test
    public void createReadStaatsangehoerigkeitTest() throws JsonProcessingException {

        String URL2 = "http://localhost:" + port + "/staat/create/123";
        StaatsangehoerigkeitResource staat = restTemplate.getForEntity(URL2, StaatsangehoerigkeitResource.class).getBody();
        assertEquals("de", staat.getCode());

        String URL = "http://localhost:" + port + "/staat/123";
        StaatsangehoerigkeitResource staat2 = restTemplate.getForEntity(URL, StaatsangehoerigkeitResource.class).getBody();
        assertEquals("de", staat2.getCode());
        assertNotEquals(null, staat2.getLink("self"));
        assertNotEquals(null, staat2.getLink("delete"));

    }

    
    @Test
    public void queryStaatsangehoerigkeitTest() throws JsonProcessingException {

        String URL2 = "http://localhost:" + port + "/staat/create/123";
        StaatsangehoerigkeitResource staat = restTemplate.getForEntity(URL2, StaatsangehoerigkeitResource.class).getBody();
        assertEquals("de", staat.getCode());

        String URL = "http://localhost:" + port + "/staat/query";
        SearchResultResource response = restTemplate.getForEntity(URL, SearchResultResource.class).getBody();
        assertEquals(1, response.getResult().size());
        assertNotEquals(null, response.getLink("self"));
        assertNotEquals(null, response.getLink("query"));

    }

    @Test
    public void deleteStaatsangehoerigkeitTest() {

        String URL2 = "http://localhost:" + port + "/staat/create/123";
        StaatsangehoerigkeitResource staat = restTemplate.getForEntity(URL2, StaatsangehoerigkeitResource.class).getBody();
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
