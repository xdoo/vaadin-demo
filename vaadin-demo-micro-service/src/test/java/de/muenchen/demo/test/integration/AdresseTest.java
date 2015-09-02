/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.demo.test.integration;

import aj.org.objectweb.asm.TypeReference;
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
import de.muenchen.demo.service.domain.BuergerRepository;
import de.muenchen.demo.service.domain.MandantRepository;
import de.muenchen.demo.service.domain.PassRepository;
import de.muenchen.demo.service.domain.PermissionRepository;
import de.muenchen.demo.service.domain.StaatsangehoerigkeitReferenceRepository;
import de.muenchen.demo.service.domain.UserAuthorityRepository;
import de.muenchen.demo.service.domain.UserRepository;
import de.muenchen.demo.service.domain.WohnungRepository;
import de.muenchen.vaadin.demo.api.rest.AdresseResource;
import de.muenchen.vaadin.demo.api.rest.SearchResultResource;
import de.muenchen.vaadin.demo.api.rest.WohnungResource;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
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
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.hal.Jackson2HalModule;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
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
    StaatsangehoerigkeitReferenceRepository staatRepo;
    @Autowired
    BuergerRepository buergerRepo;
    @Autowired
    WohnungRepository wohnRepo;
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
    private String urlSave;
    private String urlNew;
    private String urlRead1;
    private String urlRead0;
    private AdresseResource response;
    ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally

    @Before
    public void setUp() throws JsonProcessingException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {

        List adresses = new ArrayList();

        adresse.setOid("123");
        adresse.setStrasse("Donau-Schwaben-Str");
        adresse.setHausnummer("18");
        adresse.setStadt("Passau");
        adresse.setPlz(96034);

        adresse2.setStrasseReference("AM80331");
        adresse2.setStrasse("ampfing strasse");
        adresse2.setHausnummer("15");
        adresse2.setStadt("München");
        adresse2.setPlz(80331);
        adresses.add(adresse2);

        adresse3.setStrasse("ampfing strasse");
        adresse3.setHausnummer("16");
        adresse3.setStadt("München");
        adresse3.setPlz(80331);
        adresse3.setStrasseReference("AM80331");
        adresses.add(adresse3);

        adresse4.setStrasse("goethe strasse");
        adresse4.setStrasseReference("GO80331");
        adresse4.setStadt("München");
        adresse4.setHausnummer("26");
        adresse4.setPlz(80331);
        adresses.add(adresse4);
        List adresseListGO80331;
        adresseListGO80331 = new ArrayList();
        adresseListGO80331.add(adresse4);
        adresse5.setOid("aa");
        adresse5.setStrasse("ampfing strasse");
        adresse5.setHausnummer("11");
        adresse5.setStadt("München");
        adresse5.setPlz(81671);
        List adresseList;
        adresseList = new ArrayList();
        adresseList.add(adresse5);
        Adresse adresse6 = new Adresse();
        adresse6.setOid("aa");
        adresse6.setStrasse("ampfing strasse");
        adresse6.setHausnummer("10");
        adresse6.setStadt("München");
        adresse6.setPlz(81671);
        adresseList.add(adresse6);

        String json80331 = mapper.writeValueAsString(adresses);
        String jsonAdresse = mapper.writeValueAsString(adresseListGO80331);
        String jsonAM80331 = mapper.writeValueAsString(adresseList);

        stubFor(get(urlEqualTo("/adresse/80331")).willReturn(
                aResponse().withHeader("Content-Type", "application/json")
                .withBody(json80331)
        ));
        stubFor(get(urlEqualTo("/adresse/AM80331")).willReturn(
                aResponse().withHeader("Content-Type", "application/json")
                .withBody(jsonAM80331)
        ));
        stubFor(get(urlEqualTo("/adresse/GO80331")).willReturn(
                aResponse().withHeader("Content-Type", "application/json")
                .withBody(jsonAdresse)
        ));

        BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials("hans", "test"));

        HttpClient httpClient = HttpClientBuilder.create()
                .setDefaultCredentialsProvider(credentialsProvider)
                .build();

        ClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        restTemplate = new RestTemplate(requestFactory);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new Jackson2HalModule());

        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(objectMapper);
//restTemplate.setMessageConverters(Collections.<HttpMessageConverter<?>>singletonList(converter));
        urlSave = "http://localhost:" + port + "/wohnung/save";
        urlRead1 = "http://localhost:" + port + "/wohnung/OIDM2_WOHNUNG001";
        urlRead0 = "http://localhost:" + port + "/wohnung/OIDM2_WOHNUNG004";


    }

    @Test
    public void sucheAdresseTest() throws JsonProcessingException {
        String URL = "http://localhost:" + port + "/adresse/suche";
        String plz = "80331";

//Suche adresse Interne mit Plz 80331 
        ResponseEntity<List> responseAdresse;
        responseAdresse = restTemplate.postForEntity(URL, plz, List.class);
        assertNotNull(responseAdresse);
        assertEquals(3, responseAdresse.getBody().size());
    }

    @Test
    public void saveWohnungTest() throws JsonProcessingException {

//save Adresse Interne
        WohnungResource w = new WohnungResource();
        w.setOid("OID0");
        w.setHausnummer("26");
        w.setStrasseReference("GO80331");
        w.setAusrichtung("west");
        w.setStrasse("alleestr.");
        w.setStock("2");
        WohnungResource w1 = restTemplate.postForEntity(urlSave, w, WohnungResource.class).getBody();
        assertNotNull(w1);
        assertNotNull(w1.getStrasseReference());
        assertEquals("goethe strasse", w1.getStrasse());

//save Adresse externe
        WohnungResource w2 = new WohnungResource();
        w2.setOid("OID1");
        w2.setStrasse("panoramaStr.");
        w2.setPlz(69034);
        w2.setHausnummer("19");
        w2.setStadt("Passau");
        w2.setAusrichtung("west");
        w2.setStock("2");
        WohnungResource w3 = restTemplate.postForEntity(urlSave, w2, WohnungResource.class).getBody();
        assertNotNull(w3);
    }

    @Test
    public void readWohnungTest() throws JsonProcessingException {

//read Adresse Interne
        urlRead0 = "http://localhost:" + port + "/wohnung/OIDM2_WOHNUNG004";

        WohnungResource w4 = restTemplate.getForEntity(urlRead0, WohnungResource.class).getBody();
        assertNotNull(w4);
        assertNotNull(w4.getStrasse());
        assertNotNull(w4.getStadt());
        //assertEquals("AM80331", w4.getStrasseReference());

//read Adresse Externe
        urlRead1 = "http://localhost:" + port + "/wohnung/OIDM2_WOHNUNG001";
        WohnungResource w5 = restTemplate.getForEntity(urlRead1, WohnungResource.class).getBody();
        assertNotNull(w5);
        assertNotNull(w5.getStrasse());
        assertNotNull(w5.getStadt());
        assertNull(w5.getStrasseReference());
    }

    @Test
    public void updateWohnungTest() throws JsonProcessingException {

//update Adresse Interne
        WohnungResource w6 = restTemplate.getForEntity(urlRead0, WohnungResource.class).getBody();
        assertNotNull(w6);
        w6.setHausnummer("20");
        w6.setAusrichtung("nord");
        WohnungResource w7 = restTemplate.postForEntity(urlRead0, w6, WohnungResource.class).getBody();
        assertNotNull(w7);
        assertEquals("20", w7.getHausnummer());
        assertEquals("nord", w7.getAusrichtung());

//update Adresse Externe
        WohnungResource w8 = restTemplate.getForEntity(urlRead1, WohnungResource.class).getBody();
        assertNotNull(w8);
        w8.setStock("4");
        w8.setStrasse("AmpfingStr.");
        WohnungResource w9 = restTemplate.postForEntity(urlRead1, w8, WohnungResource.class).getBody();
        assertNotNull(w9);
        assertEquals("4", w9.getStock());
        assertEquals("AmpfingStr.", w9.getStrasse());
    }

    @Test
    public void copyWohnungTest() throws JsonProcessingException {

//copy Adresse Interne
        WohnungResource w10 = restTemplate.getForEntity(urlRead0, WohnungResource.class).getBody();
        String urlCopy = "http://localhost:" + port + "/wohnung/copy/OIDM2_WOHNUNG004";
        WohnungResource w11 = restTemplate.getForEntity(urlCopy, WohnungResource.class).getBody();
        assertNotNull(w11);
        assertEquals(w10.getStock(), w11.getStock());
        assertEquals(w10.getHausnummer(), w11.getHausnummer());

//copy Adresse Externe
        WohnungResource w12 = restTemplate.getForEntity(urlRead1, WohnungResource.class).getBody();
        String urlCopy1 = "http://localhost:" + port + "/wohnung/copy/OIDM2_WOHNUNG001";
        WohnungResource w13 = restTemplate.getForEntity(urlCopy1, WohnungResource.class).getBody();
        assertNotNull(w13);
        assertEquals(w12.getStock(), w13.getStock());
        assertEquals(w12.getHausnummer(), w13.getHausnummer());

    }

}
