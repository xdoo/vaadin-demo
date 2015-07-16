/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.demo.test;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import de.muenchen.demo.service.Application;
import de.muenchen.demo.service.config.InitApplication;
import de.muenchen.demo.service.domain.AuthorityPermissionRepository;
import de.muenchen.demo.service.domain.AuthorityRepository;
import de.muenchen.demo.service.domain.Buerger;
import de.muenchen.demo.service.domain.BuergerRepository;
import de.muenchen.demo.service.domain.Pass;
import de.muenchen.demo.service.domain.PassRepository;
import de.muenchen.demo.service.domain.PermissionRepository;
import de.muenchen.demo.service.domain.Staatsangehoerigkeit;
import de.muenchen.demo.service.domain.StaatsangehoerigkeitReferenceRepository;
import de.muenchen.demo.service.domain.UserAuthorityRepository;
import de.muenchen.demo.service.domain.UserRepository;
import de.muenchen.demo.service.rest.api.BuergerResource;
import de.muenchen.demo.service.rest.api.PassResource;
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

/**
 *
 * @author praktikant.tmar
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest({"server.port=0", "management.port=0"})
public class PassTest {

    private RestTemplate restTemplate = new TestRestTemplate();
    @Value("${local.server.port}")
    private int port;
    private final Buerger buerger = new Buerger();
    private final Pass pass = new Pass();
    private final Pass pass2 = new Pass();
    @JsonProperty("result")
    private SearchResultResource queryResponse;
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
    PassRepository passRepo;

    @Before
    public void setUp() throws JsonProcessingException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {

        authPermRepo.deleteAll();
        userAuthRepo.deleteAll();
        usersRepo.deleteAll();
        authRepo.deleteAll();
        permRepo.deleteAll();
        buergerRepo.deleteAll();
        staatRepo.deleteAll();
        passRepo.deleteAll();

        InitApplication initApplication = new InitApplication(usersRepo, authRepo, permRepo, userAuthRepo, authPermRepo);
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

        buerger.setOid("90");
        buerger.setNachname("hans");
        buerger.setVorname("max");

        pass.setOid("90");
        pass.setPassNummer("0215485226");
        pass.setAustellungsdatum(new Date(1220227200L * 1000));
        pass.setGueltigBis(new Date(1220227200L * 1000));
        pass.setBehoerde("München");
        pass.setKode("De");
        pass.setTyp("D");

        pass2.setOid("91");
        pass2.setPassNummer("0215485226");
        pass2.setAustellungsdatum(new Date(1220227200L * 1000));
        pass2.setGueltigBis(new Date(1220227200L * 1000));
        pass2.setBehoerde("München");
        pass2.setKode("De");
        pass2.setTyp("D");
    }

    @Test
    public void addPassStaatsangehoerigkeitTest() throws JsonProcessingException {

        String URL2 = "http://localhost:" + port + "/staat/create/123";
        Staatsangehoerigkeit staat = restTemplate.getForEntity(URL2, Staatsangehoerigkeit.class).getBody();
        assertEquals("de", staat.getCode());

        String URL10 = "http://localhost:" + port + "/pass/save";
        PassResource response = restTemplate.postForEntity(URL10, pass, PassResource.class).getBody();
        assertEquals(response.getBehoerde(), "München");

        String URL12 = "http://localhost:" + port + "/pass/add/pass/90/staats/123";
        ResponseEntity response4 = restTemplate.getForEntity(URL12, PassResource.class);
        PassResource response9 = (PassResource) response4.getBody();
        assertEquals("de", response9.getStaatsangehoerigkeit().getCode());
    }

    @Test
    public void savePassTest() throws JsonProcessingException {

        String URL10 = "http://localhost:" + port + "/pass/save";
        PassResource response = restTemplate.postForEntity(URL10, pass, PassResource.class).getBody();
        assertEquals(response.getBehoerde(), "München");

    }

    @Test
    public void readPassTest() throws JsonProcessingException {

        String URL10 = "http://localhost:" + port + "/pass/save";
        PassResource response = restTemplate.postForEntity(URL10, pass, PassResource.class).getBody();
        assertEquals(response.getBehoerde(), "München");
        String URL11 = "http://localhost:" + port + "/pass/90";
        PassResource response2 = restTemplate.getForEntity(URL11, PassResource.class).getBody();
        assertEquals(response2, response);

    }

    @Test
    public void queryPassTest() throws JsonProcessingException {

        String URL10 = "http://localhost:" + port + "/pass/save";
        restTemplate.postForEntity(URL10, pass, PassResource.class).getBody();
        restTemplate.postForEntity(URL10, pass2, PassResource.class).getBody();

        String URL11 = "http://localhost:" + port + "/pass/query";
        queryResponse = restTemplate.getForEntity(URL11, SearchResultResource.class).getBody();
        assertEquals(2, queryResponse.getResult().size());

    }

    @Test
    public void addBuergerPassTest() throws JsonProcessingException {

        String URL2 = "http://localhost:" + port + "/staat/create/123";
        Staatsangehoerigkeit staat = restTemplate.getForEntity(URL2, Staatsangehoerigkeit.class).getBody();

        String URL10 = "http://localhost:" + port + "/buerger/save";
        ResponseEntity<BuergerResource> response = restTemplate.postForEntity(URL10, buerger, BuergerResource.class);
        
        String URL13 = "http://localhost:" + port + "/pass/save";
        restTemplate.postForEntity(URL13, pass, PassResource.class).getBody();

        String URL12 = "http://localhost:" + port + "/buerger/add/buerger/90/pass/90";
        ResponseEntity response4 = restTemplate.getForEntity(URL12, BuergerResource.class);
        BuergerResource response9 = (BuergerResource) response4.getBody();
        assertEquals(1, response9.getPass().size());
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
    }
}
