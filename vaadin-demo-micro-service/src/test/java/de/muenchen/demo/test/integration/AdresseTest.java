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
import de.muenchen.demo.service.domain.Adresse;
import de.muenchen.demo.service.domain.AdresseExterneRepository;
import de.muenchen.demo.service.domain.AdresseInterneRepository;
import de.muenchen.demo.service.domain.AdresseReferenceRepository;
import de.muenchen.demo.service.domain.AuthorityPermissionRepository;
import de.muenchen.demo.service.domain.AuthorityRepository;
import de.muenchen.demo.service.domain.MandantRepository;
import de.muenchen.demo.service.domain.PermissionRepository;
import de.muenchen.demo.service.domain.UserAuthorityRepository;
import de.muenchen.demo.service.domain.UserRepository;
import de.muenchen.demo.service.rest.api.AdresseResource;
import de.muenchen.demo.service.rest.api.SearchResultResource;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
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
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Rule;
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
public class AdresseTest {

    private RestTemplate restTemplate;
    @Value("${local.server.port}")
    private int port;
    private final Adresse adresse2 = new Adresse();
    private final Adresse adresse = new Adresse();
    private final Adresse adresse3 = new Adresse();
    private final Adresse adresse4 = new Adresse();
    private final Adresse adresse5 = new Adresse();

    @JsonProperty("result")
    private SearchResultResource<AdresseResource> responseQuery;
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
    MandantRepository mandantRepo;
    @Autowired
    AdresseInterneRepository interneRepo;
    @Autowired
    AdresseExterneRepository externeRepo;
    @Autowired
    AdresseReferenceRepository referenceRepo;
    private String urlSave;
    private String urlNew;
    private AdresseResource response;

    @Before
    public void setUp() throws JsonProcessingException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {

        authPermRepo.deleteAll();
        userAuthRepo.deleteAll();
        usersRepo.deleteAll();
        authRepo.deleteAll();
        permRepo.deleteAll();
        referenceRepo.deleteAll();
        interneRepo.deleteAll();
        externeRepo.deleteAll();
        mandantRepo.deleteAll();

        InitTest initTest = new InitTest(usersRepo, authRepo, permRepo, userAuthRepo, authPermRepo, mandantRepo);
        initTest.init();

        ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally

        List adresses = new ArrayList();

        adresse.setOid("123");
        adresse.setStrasse("Donau-Schwaben-Str");
        adresse.setHausnummer("18");
        adresse.setStadt("Passau");
        adresse.setPlz(96034);

        adresse2.setOid("12");
        adresse2.setStrasse("ampfing strasse");
        adresse2.setHausnummer("15");
        adresse2.setStadt("Passau");
        adresse2.setPlz(80331);
        adresses.add(adresse2);

        adresse3.setOid("1234");
        adresse3.setStrasse("ampfing strasse");
        adresse3.setHausnummer("16");
        adresse3.setStadt("Passau");
        adresse3.setPlz(80331);
        adresses.add(adresse3);

        adresse5.setOid("aa");
        adresse5.setStrasse("ampfing strasse");
        adresse5.setHausnummer("16");
        adresse5.setStadt("Passau");
        adresse5.setPlz(96452);

        adresse4.setStrasse("goethe strasse");
        adresse4.setStrasseReference("goeth2500");
        adresse4.setStadt("München");
        adresse4.setPlz(80331);
        adresses.add(adresse4);

        String json80331 = mapper.writeValueAsString(adresses);
        String jsonAdresse = mapper.writeValueAsString(adresse4);
        adresse4.setHausnummer("16");
        adresse4.setOid("12345");

        stubFor(get(urlEqualTo("/adresse/80331")).willReturn(
                aResponse().withHeader("Content-Type", "application/json")
                .withBody(json80331)
        ));
        stubFor(get(urlEqualTo("/adresse/goeth2500")).willReturn(
                aResponse().withHeader("Content-Type", "application/json")
                .withBody(jsonAdresse)
        ));

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
        urlSave = "http://localhost:" + port + "/adresse/save";
        urlNew = "http://localhost:" + port + "/adresse/new";

    }

    /**
     *
     * @throws JsonProcessingException
     */
    @Test
    @SuppressWarnings("empty-statement")
    public void createExterneAdresseTest() throws JsonProcessingException, Exception {

        String URL = "http://localhost:" + port + "/adresse/save";

        restTemplate.postForEntity(URL, adresse, AdresseResource.class);
        String URL4 = "http://localhost:" + port + "/adresse/123";
        AdresseResource responseAdresse = restTemplate.getForEntity(URL4, AdresseResource.class).getBody();

        assertEquals(96034, responseAdresse.getPlz());
        assertNotNull(responseAdresse.getLink("new"));
        assertNotNull(responseAdresse.getLink("update"));
        assertNotNull(responseAdresse.getLink("copy"));
        assertNotNull(responseAdresse.getLink("self"));
        assertNotNull(responseAdresse.getLink("delete"));

    }

    @Test
    public void CreateMuenchenerAdresseTest() throws JsonProcessingException {
        String URL = "http://localhost:" + port + "/adresse/save";

        AdresseResource responseAdresse = restTemplate.postForEntity(URL, adresse4, AdresseResource.class).getBody();
        assertEquals("goeth2500", responseAdresse.getStrasseReference());

        String URL4 = "http://localhost:" + port + "/adresse/12345";
        AdresseResource response2 = restTemplate.getForEntity(URL4, AdresseResource.class).getBody();

        assertEquals(80331, response2.getPlz());
        assertNotNull(response2.getLink("new"));
        assertNotNull(response2.getLink("update"));
        assertNotNull(response2.getLink("copy"));
        assertNotNull(response2.getLink("self"));
        assertNotNull(response2.getLink("delete"));
    }

    @Test
    public void queryAdresseTest() throws JsonProcessingException {
        String URL = "http://localhost:" + port + "/adresse/save";
        restTemplate.postForEntity(URL, adresse2, AdresseResource.class);
        restTemplate.postForEntity(URL, adresse3, AdresseResource.class);

        String URL3 = "http://localhost:" + port + "/adresse/query";
        responseQuery = restTemplate.getForEntity(URL3, SearchResultResource.class).getBody();

        assertEquals(2, responseQuery.getResult().size());
        assertNotNull(responseQuery.getLink("self"));
        assertNotNull(responseQuery.getLink("query"));
    }

    @Test
    public void newAdresseTest() throws JsonProcessingException {

        response = restTemplate.getForEntity(urlNew, AdresseResource.class).getBody();
        assertNotNull(response.getLink("new"));
        assertNotNull(response.getLink("save"));
        assertNotNull(response.getOid());

    }

    @Test
    public void adresseUpdateTest() {

        Adresse adresseUpdate = new Adresse();
        adresseUpdate.setOid("123");
        adresseUpdate.setStrasse("Panorama Str");
        adresseUpdate.setHausnummer("30");
        adresseUpdate.setStadt("Heidelberg");
        adresseUpdate.setPlz(96034);

        restTemplate.postForEntity(urlSave, adresse, AdresseResource.class).getBody();
        String URL2 = "http://localhost:" + port + "/adresse/123";
        response = restTemplate.postForEntity(URL2, adresseUpdate, AdresseResource.class).getBody();

        assertEquals("30", response.getHausnummer());
        assertNotNull(response.getLink("new"));
        assertNotNull(response.getLink("update"));
        assertNotNull(response.getLink("copy"));
        assertNotNull(response.getLink("self"));
        assertNotNull(response.getLink("delete"));

    }

    @Test
    public void copyAdresseTest() {

        restTemplate.postForEntity(urlSave, adresse, AdresseResource.class).getBody();
        String URL2 = "http://localhost:" + port + "/adresse/copy/123";
        response = restTemplate.getForEntity(URL2, AdresseResource.class).getBody();

        assertNotEquals("123", response.getOid());
        assertNotNull(response.getLink("new"));
        assertNotNull(response.getLink("update"));
        assertNotNull(response.getLink("copy"));
        assertNotNull(response.getLink("self"));
        assertNotNull(response.getLink("delete"));

    }

    @Test
    public void adresseDeleteTest() {

        restTemplate.postForEntity(urlSave, adresse, AdresseResource.class).getBody();
        String URL2 = "http://localhost:" + port + "/adresse/123";
        restTemplate.delete(URL2, adresse);
        String URL3 = "http://localhost:" + port + "/adresse/query";
        responseQuery = restTemplate.getForEntity(URL3, SearchResultResource.class).getBody();

        assertEquals(true, responseQuery.getResult().isEmpty());

    }

    @After
    public void TearDown() {
        authPermRepo.deleteAll();
        userAuthRepo.deleteAll();
        usersRepo.deleteAll();
        authRepo.deleteAll();
        permRepo.deleteAll();
        referenceRepo.deleteAll();
        interneRepo.deleteAll();
        externeRepo.deleteAll();
        mandantRepo.deleteAll();
    }
}
