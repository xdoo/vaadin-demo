/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.demo.test.integration;

import com.fasterxml.jackson.annotation.JsonProperty;
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
import de.muenchen.demo.service.rest.api.PassResource;
import de.muenchen.demo.service.rest.api.SearchResultResource;
import de.muenchen.demo.service.rest.api.StaatsangehoerigkeitResource;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
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
import static org.junit.Assert.assertNotEquals;
import org.junit.Before;
import org.junit.Rule;
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

    @Autowired
    MandantRepository mandantRepo;
    private String urlNew;
    private String urlSave;
    private PassResource response;
    private SearchResultResource responseQuery;

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
        mandantRepo.deleteAll();

        InitTest initTest = new InitTest(usersRepo, authRepo, permRepo, userAuthRepo, authPermRepo, mandantRepo);
        initTest.init();

        ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally


        
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

        urlSave = "http://localhost:" + port + "/pass/save";
        urlNew = "http://localhost:" + port + "/pass/new";

    }

    @Test
    public void savePassTest() throws JsonProcessingException {

        response = restTemplate.postForEntity(urlSave, pass, PassResource.class).getBody();
        assertEquals(response.getBehoerde(), "München");
        assertNotEquals(null, response.getLink("new"));
        assertNotEquals(null, response.getLink("update"));
        assertNotEquals(null, response.getLink("copy"));
        assertNotEquals(null, response.getLink("self"));
        assertNotEquals(null, response.getLink("delete"));

    }

    @Test
    public void readPassTest() throws JsonProcessingException {

        response = restTemplate.postForEntity(urlSave, pass, PassResource.class).getBody();

        String URL11 = "http://localhost:" + port + "/pass/90";
        response = restTemplate.getForEntity(URL11, PassResource.class).getBody();
        assertEquals(response, response);
        assertNotEquals(null, response.getLink("new"));
        assertNotEquals(null, response.getLink("update"));
        assertNotEquals(null, response.getLink("copy"));
        assertNotEquals(null, response.getLink("self"));
        assertNotEquals(null, response.getLink("delete"));

    }

    @Test
    public void queryPassTest() throws JsonProcessingException {

        restTemplate.postForEntity(urlSave, pass, PassResource.class).getBody();
        restTemplate.postForEntity(urlSave, pass2, PassResource.class).getBody();

        String URL11 = "http://localhost:" + port + "/pass/query";
        queryResponse = restTemplate.getForEntity(URL11, SearchResultResource.class).getBody();
        assertEquals(2, queryResponse.getResult().size());
        assertNotEquals(null, queryResponse.getLink("self"));
        assertNotEquals(null, queryResponse.getLink("query"));

    }

    @Test
    public void copyPassTest() {

        restTemplate.postForEntity(urlSave, pass, PassResource.class).getBody();
        String URL2 = "http://localhost:" + port + "/pass/copy/90";
        PassResource response2 = restTemplate.getForEntity(URL2, PassResource.class).getBody();

        assertNotEquals("90", response2.getOid());
        assertNotEquals(null, response2.getLink("new"));
        assertNotEquals(null, response2.getLink("update"));
        assertNotEquals(null, response2.getLink("copy"));
        assertNotEquals(null, response2.getLink("self"));
        assertNotEquals(null, response2.getLink("delete"));

    }

    @Test
    public void PassUpdateTest() {

        Pass passUpdate = new Pass();
        passUpdate.setPassNummer("204");
        passUpdate.setOid("90");

        restTemplate.postForEntity(urlSave, pass, PassResource.class).getBody();
        String URL2 = "http://localhost:" + port + "/pass/90";
        response = restTemplate.postForEntity(URL2, passUpdate, PassResource.class).getBody();

        assertEquals("204", response.getPassNummer());
        assertNotEquals(null, response.getLink("new"));
        assertNotEquals(null, response.getLink("update"));
        assertNotEquals(null, response.getLink("copy"));
        assertNotEquals(null, response.getLink("self"));
        assertNotEquals(null, response.getLink("delete"));

    }

    @Test
    public void deletePassTest() {

        restTemplate.postForEntity(urlSave, pass, PassResource.class).getBody();
        String URL2 = "http://localhost:" + port + "/pass/90";
        restTemplate.delete(URL2, pass);
        String URL3 = "http://localhost:" + port + "/pass/query";
        responseQuery = restTemplate.getForEntity(URL3, SearchResultResource.class).getBody();

        assertEquals(true, responseQuery.getResult().isEmpty());

    }

    @Test
    public void staatsangehoerigkeitPassTest() throws JsonProcessingException {

        String URL2 = "http://localhost:" + port + "/staat/create/123";
        StaatsangehoerigkeitResource staat = restTemplate.getForEntity(URL2, StaatsangehoerigkeitResource.class).getBody();
        assertEquals("de", staat.getCode());

        response = restTemplate.postForEntity(urlSave, pass, PassResource.class).getBody();
        assertEquals(response.getBehoerde(), "München");

        /*Test methode addStaatsangehoerigkeitPass*/
        String URL12 = "http://localhost:" + port + "/pass/add/pass/90/staats/123";
        response = restTemplate.getForEntity(URL12, PassResource.class).getBody();
        assertEquals("de", response.getStaatsangehoerigkeit().getCode());

        /*Test methode readStaatsangehoerigkeitPass*/
        String URL1 = "http://localhost:" + port + "/pass/staat/90";
        StaatsangehoerigkeitResource responseStaat = restTemplate.getForEntity(URL1, StaatsangehoerigkeitResource.class).getBody();
        assertEquals("de", responseStaat.getCode());
    }

    @After
    public void TearDown() {
        authPermRepo.deleteAll();
        userAuthRepo.deleteAll();
        usersRepo.deleteAll();
        authRepo.deleteAll();
        permRepo.deleteAll();
        buergerRepo.deleteAll();
        passRepo.deleteAll();
        staatRepo.deleteAll();
        mandantRepo.deleteAll();

    }
}
