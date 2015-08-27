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
import com.google.common.collect.Lists;
import de.muenchen.demo.service.Application;
import de.muenchen.demo.service.domain.Buerger;
import de.muenchen.demo.service.domain.BuergerRepository;
import de.muenchen.demo.service.domain.Pass;
import de.muenchen.demo.service.domain.Sachbearbeiter;
import de.muenchen.demo.service.domain.Staatsangehoerigkeit;
import de.muenchen.demo.service.domain.Wohnung;
import de.muenchen.vaadin.demo.api.rest.BuergerResource;
import de.muenchen.demo.service.services.BuergerService;
import de.muenchen.demo.test.service.DomainConstants;
import de.muenchen.vaadin.demo.api.rest.SearchResultResource;
import de.muenchen.vaadin.demo.api.hateoas.HateoasUtil;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import static org.hamcrest.CoreMatchers.equalTo;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
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
import org.springframework.security.test.context.support.WithMockUser;
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

    private BuergerResource response;
    private List responseList;
    private String urlSave;
    private String urlNew;
    @Autowired
    private BuergerService service;
    @Autowired
    BuergerRepository repo;
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8089);

    @Before
    public void setUp() throws JsonProcessingException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
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
        restTemplate.setMessageConverters(Collections.<HttpMessageConverter<?>>singletonList(converter));

        ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally

        Staatsangehoerigkeit staat = new Staatsangehoerigkeit();
        staat.setCode("it");
        staat.setLand("Italien");
        staat.setSprache("Italienisch");
        staat.setReference("OIDM2_STAATS007");

        String json = mapper.writeValueAsString(staat);

        stubFor(get(urlEqualTo("/staat/OIDM2_STAATS007")).willReturn(
                aResponse().withHeader("Content-Type", "application/json")
                .withBody(json)
        ));

        Staatsangehoerigkeit staat2 = new Staatsangehoerigkeit();
        staat2.setCode("fr");
        staat2.setLand("Frankreich");
        staat2.setSprache("französisch");
        staat2.setReference("OIDM2_STAATS008");

        String json2 = mapper.writeValueAsString(staat2);

        stubFor(get(urlEqualTo("/staat/OIDM2_STAATS008")).willReturn(
                aResponse().withHeader("Content-Type", "application/json")
                .withBody(json2)
        ));

        Staatsangehoerigkeit staat3 = new Staatsangehoerigkeit();
        staat3.setCode("tn");
        staat3.setLand("Tunesien");
        staat3.setSprache("Arabisch");
        staat3.setReference("OIDM2_STAATS015");

        String json3 = mapper.writeValueAsString(staat3);

        stubFor(get(urlEqualTo("/staat/OIDM2_STAATS015")).willReturn(
                aResponse().withHeader("Content-Type", "application/json")
                .withBody(json3)
        ));

        Staatsangehoerigkeit staat4 = new Staatsangehoerigkeit();
        staat4.setCode("de");
        staat4.setLand("Deutschland");
        staat4.setSprache("Deutsch");
        staat4.setReference("OIDM2_STAATS017");

        String json4 = mapper.writeValueAsString(staat4);

        stubFor(get(urlEqualTo("/staat/OIDM2_STAATS017")).willReturn(
                aResponse().withHeader("Content-Type", "application/json")
                .withBody(json4)
        ));

        urlSave = "http://localhost:" + port + "/buerger/save";
        urlNew = "http://localhost:" + port + "/buerger/new";

    }

    @Test
    @WithMockUser(username = DomainConstants.M2_U001_NAME)

    public void newBuergerTest() throws JsonProcessingException {
        System.out.println("========== create Bürger Test ==========");

        response = restTemplate.getForEntity(urlNew, BuergerResource.class).getBody();
        assertNotNull(response.getLink("new"));
        assertNotNull(response.getLink("save"));
        assertNotNull(response.getOid());
        assertNull(repo.findFirstByOidAndMandantOid(response.getOid(), DomainConstants.M2));
        System.out.println(String.format("Bürger wurde mit neuer OID '%s' erstellt (und nicht in der DB gespeichert)", response.getOid()));

    }

    private Buerger createBuerger(String oid) {
        Buerger buerger = new Buerger();
        buerger.setOid(oid);
        buerger.setNachname("hans");
        buerger.setVorname("peter");
        return buerger;
    }

    @Test
    public void saveBuergerTest() throws JsonProcessingException {
        System.out.println("========== save Bürger Test ==========");
        String oid = "OIDTEST";
        response = restTemplate.postForEntity(urlSave, this.createBuerger(oid), BuergerResource.class).getBody();
        assertEquals(response.getNachname(), "hans");
        assertEquals(DomainConstants.M2, response.getMandant().getOid());
        assertNotNull(response.getLink(HateoasUtil.REL_NEW));
        assertNotNull(response.getLink(HateoasUtil.REL_UPDATE));
        assertNotNull(response.getLink(HateoasUtil.REL_COPY));
        assertNotNull(response.getLink(HateoasUtil.REL_SELF));
        assertNotNull(response.getLink(HateoasUtil.REL_DELETE));
        assertNotNull(response.getLink(de.muenchen.vaadin.demo.api.rest.BuergerResource.PAESSE));
        assertNotNull(response.getLink(de.muenchen.vaadin.demo.api.rest.BuergerResource.WOHNUNGEN));
        assertNotNull(response.getLink(de.muenchen.vaadin.demo.api.rest.BuergerResource.KINDER));
        assertNotNull(response.getLink(de.muenchen.vaadin.demo.api.rest.BuergerResource.SAVE_WOHNUNG));
        assertNotNull(response.getLink(de.muenchen.vaadin.demo.api.rest.BuergerResource.ELTERN));
        assertNotNull(response.getLink(de.muenchen.vaadin.demo.api.rest.BuergerResource.RELEASE_ELTERN));
        assertNotNull(response.getLink(de.muenchen.vaadin.demo.api.rest.BuergerResource.STAATSANGEHOERIGKEITEN));
        assertNotNull(response.getLink(de.muenchen.vaadin.demo.api.rest.BuergerResource.SAVE_KIND));
        assertNotNull(response.getLink(de.muenchen.vaadin.demo.api.rest.BuergerResource.SAVE_PASS));
        assertNotNull(response.getLink(de.muenchen.vaadin.demo.api.rest.BuergerResource.SACHBEARBEITER));
        assertNotNull(response.getLink(de.muenchen.vaadin.demo.api.rest.BuergerResource.SAVE_SACHBEARBEITER));
        assertNotNull(response.getLink(de.muenchen.vaadin.demo.api.rest.BuergerResource.RELEASE_ELTERNTEIL));
        assertNotNull(response.getLink(de.muenchen.vaadin.demo.api.rest.BuergerResource.RELEASE_KINDER));
        assertNotNull(response.getLink(de.muenchen.vaadin.demo.api.rest.BuergerResource.RELEASE_PAESSE));
        assertNotNull(response.getLink(de.muenchen.vaadin.demo.api.rest.BuergerResource.RELEASE_SACHBEARBEITER));
        assertNotNull(response.getLink(de.muenchen.vaadin.demo.api.rest.BuergerResource.RELEASE_WOHNUNG));
        assertNotNull(response.getLink(de.muenchen.vaadin.demo.api.rest.BuergerResource.RELEASE_WOHNUNGEN));

        System.out.println(String.format("Bürger wurde mit Mandant '%s' in der DB gespeichert.", response.getMandant().getOid()));
    }

    @Test
    public void updateBuergerTest() throws JsonProcessingException {
        System.out.println("========== update Bürger Test ==========");
        String oid = "OIDUPDATE";
        Buerger b1 = this.createBuerger(oid);
        response = restTemplate.postForEntity(urlSave, b1, BuergerResource.class).getBody();
        b1.setVorname("philip");
        String urlUpdate = "http://localhost:" + port + "/buerger/" + oid;
        restTemplate.put(urlUpdate, b1);
        String URL11 = "http://localhost:" + port + "/buerger/" + oid;
        response = restTemplate.getForEntity(URL11, BuergerResource.class).getBody();
        assertEquals(response.getVorname(), "philip");
        assertEquals(DomainConstants.M2, response.getMandant().getOid());

        System.out.println("Bürger wurde mit neuem Vornamen in der DB gespeichert.");
    }

    @Test
    public void readBuergerTest() throws JsonProcessingException {

        System.out.println("========== read Bürger Test ==========");
        String URL11 = "http://localhost:" + port + "/buerger/" + DomainConstants.M2_B003;
        response = restTemplate.getForEntity(URL11, BuergerResource.class).getBody();
        assertNotNull(response);
        assertNotNull(response.getLink(HateoasUtil.REL_NEW));
        assertNotNull(response.getLink(HateoasUtil.REL_UPDATE));
        assertNotNull(response.getLink(HateoasUtil.REL_COPY));
        assertNotNull(response.getLink(HateoasUtil.REL_SELF));
        assertNotNull(response.getLink(HateoasUtil.REL_DELETE));
        assertNotNull(response.getLink(de.muenchen.vaadin.demo.api.rest.BuergerResource.PAESSE));
        assertNotNull(response.getLink(de.muenchen.vaadin.demo.api.rest.BuergerResource.WOHNUNGEN));
        assertNotNull(response.getLink(de.muenchen.vaadin.demo.api.rest.BuergerResource.KINDER));
        assertNotNull(response.getLink(de.muenchen.vaadin.demo.api.rest.BuergerResource.SAVE_WOHNUNG));
        assertNotNull(response.getLink(de.muenchen.vaadin.demo.api.rest.BuergerResource.ELTERN));
        assertNotNull(response.getLink(de.muenchen.vaadin.demo.api.rest.BuergerResource.RELEASE_ELTERN));
        assertNotNull(response.getLink(de.muenchen.vaadin.demo.api.rest.BuergerResource.STAATSANGEHOERIGKEITEN));
        assertNotNull(response.getLink(de.muenchen.vaadin.demo.api.rest.BuergerResource.SAVE_KIND));
        assertNotNull(response.getLink(de.muenchen.vaadin.demo.api.rest.BuergerResource.SAVE_PASS));
        assertNotNull(response.getLink(de.muenchen.vaadin.demo.api.rest.BuergerResource.SACHBEARBEITER));
        assertNotNull(response.getLink(de.muenchen.vaadin.demo.api.rest.BuergerResource.SAVE_SACHBEARBEITER));
        assertNotNull(response.getLink(de.muenchen.vaadin.demo.api.rest.BuergerResource.RELEASE_ELTERNTEIL));
        assertNotNull(response.getLink(de.muenchen.vaadin.demo.api.rest.BuergerResource.RELEASE_KINDER));
        assertNotNull(response.getLink(de.muenchen.vaadin.demo.api.rest.BuergerResource.RELEASE_PAESSE));
        assertNotNull(response.getLink(de.muenchen.vaadin.demo.api.rest.BuergerResource.RELEASE_SACHBEARBEITER));
        assertNotNull(response.getLink(de.muenchen.vaadin.demo.api.rest.BuergerResource.RELEASE_WOHNUNG));
        assertNotNull(response.getLink(de.muenchen.vaadin.demo.api.rest.BuergerResource.RELEASE_WOHNUNGEN));

        System.out.println(String.format("Bürger konnte mit OID '%s' aus der DB gelesen werden.", response.getOid()));
    }

    @Test
    public void copyBuergerTest() {
        System.out.println("========== copy Bürger Test ==========");

        String URL2 = "http://localhost:" + port + "/buerger/copy/" + DomainConstants.M2_B005;
        response = restTemplate.getForEntity(URL2, BuergerResource.class).getBody();

        assertNotNull(response);
        assertNotNull(response.getLink(HateoasUtil.REL_NEW));
        assertNotNull(response.getLink(HateoasUtil.REL_UPDATE));
        assertNotNull(response.getLink(HateoasUtil.REL_COPY));
        assertNotNull(response.getLink(HateoasUtil.REL_SELF));
        assertNotNull(response.getLink(HateoasUtil.REL_DELETE));
        assertNotNull(response.getLink(de.muenchen.vaadin.demo.api.rest.BuergerResource.PAESSE));
        assertNotNull(response.getLink(de.muenchen.vaadin.demo.api.rest.BuergerResource.WOHNUNGEN));
        assertNotNull(response.getLink(de.muenchen.vaadin.demo.api.rest.BuergerResource.KINDER));
        assertNotNull(response.getLink(de.muenchen.vaadin.demo.api.rest.BuergerResource.SAVE_WOHNUNG));
        assertNotNull(response.getLink(de.muenchen.vaadin.demo.api.rest.BuergerResource.ELTERN));
        assertNotNull(response.getLink(de.muenchen.vaadin.demo.api.rest.BuergerResource.RELEASE_ELTERN));
        assertNotNull(response.getLink(de.muenchen.vaadin.demo.api.rest.BuergerResource.STAATSANGEHOERIGKEITEN));
        assertNotNull(response.getLink(de.muenchen.vaadin.demo.api.rest.BuergerResource.SAVE_KIND));
        assertNotNull(response.getLink(de.muenchen.vaadin.demo.api.rest.BuergerResource.SAVE_PASS));
        assertNotNull(response.getLink(de.muenchen.vaadin.demo.api.rest.BuergerResource.SACHBEARBEITER));
        assertNotNull(response.getLink(de.muenchen.vaadin.demo.api.rest.BuergerResource.SAVE_SACHBEARBEITER));
        assertNotNull(response.getLink(de.muenchen.vaadin.demo.api.rest.BuergerResource.RELEASE_ELTERNTEIL));
        assertNotNull(response.getLink(de.muenchen.vaadin.demo.api.rest.BuergerResource.RELEASE_KINDER));
        assertNotNull(response.getLink(de.muenchen.vaadin.demo.api.rest.BuergerResource.RELEASE_PAESSE));
        assertNotNull(response.getLink(de.muenchen.vaadin.demo.api.rest.BuergerResource.RELEASE_SACHBEARBEITER));
        assertNotNull(response.getLink(de.muenchen.vaadin.demo.api.rest.BuergerResource.RELEASE_WOHNUNG));
        assertNotNull(response.getLink(de.muenchen.vaadin.demo.api.rest.BuergerResource.RELEASE_WOHNUNGEN));

        System.out.println(String.format("Objekt mit der OID '%s' konnte erfolgreich in Objekt '%s' kopiert (und in DB gespeichert) werden", DomainConstants.M2_B005, response.getOid()));

    }

    @Test
    public void copyListBuergerTest() {
        System.out.println("========== copy Liste Bürger Test ==========");
        int x = this.count(DomainConstants.M2);
        String URL2 = "http://localhost:" + port + "/buerger/copy";
        ArrayList<String> oids = new ArrayList();
        oids.add(DomainConstants.M2_B025);
        oids.add(DomainConstants.M2_B026);
        restTemplate.postForEntity(URL2, oids, BuergerResource.class);
        String URL11 = "http://localhost:" + port + "/buerger/query";
        SearchResultResource queryResponse = restTemplate.getForEntity(URL11, SearchResultResource.class).getBody();
        assertEquals(x + 2, queryResponse.getResult().size());
        System.out.println(String.format("Objekte mit der OID '%s' und der OID '%s' konnte erfolgreich in Objekt  kopiert (und in DB gespeichert) werden", DomainConstants.M2_B025, DomainConstants.M2_B026));

    }

    @Test
    @WithMockUser(username = DomainConstants.M2_U001_NAME)
    public void buergerDeleteTest() {
        System.out.println("========== delete Bürger Test ==========");
        String URL11 = "http://localhost:" + port + "/buerger/" + DomainConstants.M2_B013;
        response = restTemplate.getForEntity(URL11, BuergerResource.class).getBody();
        assertNotNull(response);
        restTemplate.delete(URL11);
        thrown.expect(org.springframework.web.client.HttpServerErrorException.class);
        thrown.expectMessage(equalTo("500 Internal Server Error"));
        response = restTemplate.getForEntity(URL11, BuergerResource.class).getBody();

        System.out.println(String.format("Bürger konnte mit OID '%s' aus der DB (und dem Cache) gelöscht werden.", DomainConstants.M2_B004));

    }

    @Test
    public void buergerDeleteListTest() {
        System.out.println("========== delete Bürger Test ==========");

        String URL2 = "http://localhost:" + port + "/buerger";
        ArrayList<String> oids = new ArrayList();
        oids.add(DomainConstants.M2_B020);
        oids.add(DomainConstants.M2_B021);
        restTemplate.postForEntity(URL2, oids, BuergerResource.class);
        String URL11 = "http://localhost:" + port + "/buerger/" + DomainConstants.M2_B020;
        thrown.expect(org.springframework.web.client.HttpServerErrorException.class);
        thrown.expectMessage(equalTo("500 Internal Server Error"));
        response = restTemplate.getForEntity(URL11, BuergerResource.class).getBody();
        assertNull(response);
        System.out.println(String.format("Bürger  mit OID '%s'und OID '%s' konnte aus der DB (und dem Cache) gelöscht werden.", DomainConstants.M2_B020, DomainConstants.M2_B021));

    }

    @Test
    public void queryBuergerTest() throws JsonProcessingException {

        System.out.println("========== query Bürger Test Mandant 2 ==========");
        int x = this.count(DomainConstants.M2);
        String URL11 = "http://localhost:" + port + "/buerger/query";
        SearchResultResource queryResponse = restTemplate.getForEntity(URL11, SearchResultResource.class).getBody();
        assertEquals(x, queryResponse.getResult().size());
        assertNotNull(null, queryResponse.getLink("self"));
        assertNotNull(null, queryResponse.getLink("query"));
        System.out.println(String.format("Suche wurde erfolgreich durchgeführt. Einträge Mandant %s: %s | Ergebnis der Suche: %s", DomainConstants.M2, x, queryResponse.getResult().size()));

    }

    private int count(String mid) {
        ArrayList<Buerger> all = Lists.newArrayList(repo.findAll());
        return all.stream().filter(b -> b.getMandant().getOid().equals(mid)).collect(Collectors.counting()).intValue();
    }

    @Test
    public void readElternTest() {
        System.out.println("========== read Eltern Test ==========");
        String urlEltern = "http://localhost:" + port + "/buerger/eltern/" + DomainConstants.M2_B003;
        SearchResultResource responseListEltern = restTemplate.getForEntity(urlEltern, SearchResultResource.class).getBody();
        assertNotEquals(0, responseListEltern.getResult().size());
        System.out.println(String.format("die Eltern von dem Bürger mit OID '%s' konnten aus der DB gelesen werden.", DomainConstants.M2_B003));

    }

    @Test
    public void releaseBuergerElternTest() {
        System.out.println("========== release Bürger Eltern Test ==========");
        String urlEltern = "http://localhost:" + port + "/buerger/eltern/" + DomainConstants.M2_B014;
        SearchResultResource responseListEltern = restTemplate.getForEntity(urlEltern, SearchResultResource.class).getBody();
        assertFalse(responseListEltern.getResult().isEmpty());
        String urlReleaseEltern = "http://localhost:" + port + "/buerger/release/eltern/" + DomainConstants.M2_B014;
        restTemplate.getForEntity(urlReleaseEltern, BuergerResource.class);
        SearchResultResource responseListEltern2 = restTemplate.getForEntity(urlEltern, SearchResultResource.class).getBody();
        assertTrue(responseListEltern2.getResult().isEmpty());
        System.out.println(String.format("release operation für den Eltern eines Bürgers mit OID '%s' konnte aus der DB erfolgreich durchgeführt.", DomainConstants.M2_B012));
    }

    @Test
    @WithMockUser(username = DomainConstants.M2_U001_NAME)
    public void releaseBuergerElternteilTest() {
        System.out.println("========== release Bürger Elternteil Test ==========");

        assertEquals(1, this.checkChild(DomainConstants.M2_B015, DomainConstants.M2_B010));
        String urlReleasevater = "http://localhost:" + port + "/buerger/release/elternteil/" + DomainConstants.M2_B015;
        restTemplate.postForEntity(urlReleasevater, DomainConstants.M2_B010, BuergerResource.class);
        assertEquals(0, this.checkChild(DomainConstants.M2_B015, DomainConstants.M2_B010));
        System.out.println(String.format("release operation für den Bürger mit OID '%s' und den elternteil mit OID '%s' konnte aus der DB erfolgreich durchgeführt.", DomainConstants.M2_B011, DomainConstants.M2_B001));
    }

    private long checkChild(String kindOid, String elternOid) {
        return service.read(elternOid).getKinder().stream().filter(k -> k.getOid().equals(kindOid)).count();
    }

    @Test
    public void readBuergerKinderTest() {
        System.out.println("========== release Bürger Kinder Test ==========");
        String URL1 = "http://localhost:" + port + "/buerger/kinder/" + DomainConstants.M2_B007;
        SearchResultResource responseList2 = restTemplate.getForEntity(URL1, SearchResultResource.class).getBody();
        assertFalse(responseList2.getResult().isEmpty());
        System.out.println(String.format("die Kinder von dem Bürger mit OID '%s' konnten aus der DB gelesen werden.", DomainConstants.M2_B007));

    }

    @Test
    @WithMockUser(username = DomainConstants.M2_U001_NAME)
    public void AddBuergerKindTest() {
        System.out.println("========== Add Bürger Kind Test ==========");
        String URL2 = "http://localhost:" + port + "/buerger/add/" + DomainConstants.M2_B007 + "/kind/";
        restTemplate.postForEntity(URL2, DomainConstants.M2_B008, BuergerResource.class).getBody();
        assertEquals(1, this.checkChild(DomainConstants.M2_B008, DomainConstants.M2_B007));
        System.out.println(String.format("das Kind mit OID '%s' könnte zu dem Bürger mit OID '%s' hinzufügt werden.", DomainConstants.M2_B008, DomainConstants.M2_B007));

    }

    @Test
    @WithMockUser(username = DomainConstants.M2_U001_NAME)
    public void CreateBuergerKindTest() {
        System.out.println("========== Save Bürger Kind Test ==========");
        String URL1 = "http://localhost:" + port + "/buerger/create/kind/" + DomainConstants.M2_B007;
        String oid = "oidkind";
        restTemplate.postForEntity(URL1, this.createBuerger(oid), BuergerResource.class).getBody();
        assertEquals(1, this.checkChild("oidkind", DomainConstants.M2_B007));
        System.out.println(String.format("ein Kind könnte erstellt und zu dem Bürger mit OID '%s' hinzufügt werden.", DomainConstants.M2_B007));

    }

    @Test
    public void readBuergerWohnungenTest() {
        System.out.println("========== read Bürger Wohnungen Test ==========");
        String URL1 = "http://localhost:" + port + "/buerger/wohnungen/" + DomainConstants.M2_B007;
        responseList = restTemplate.getForEntity(URL1, List.class).getBody();
        assertFalse(responseList.isEmpty());
        System.out.println(String.format("die Wohnungen von dem Bürger mit OID '%s' konnten aus der DB gelesen werden.", DomainConstants.M2_B007));

    }

    @Test
    @WithMockUser(username = DomainConstants.M2_U001_NAME)
    public void releaseBuergerWohnungenTest() {
        System.out.println("========== release Bürger Wohnung Test ==========");
        String urlWohnung = "http://localhost:" + port + "/buerger/wohnungen/" + DomainConstants.M2_B015;
        responseList = restTemplate.getForEntity(urlWohnung, List.class).getBody();
        assertFalse(responseList.isEmpty());
        String urlReleaseWohnung = "http://localhost:" + port + "/buerger/release/wohnungen/" + DomainConstants.M2_B015;
        restTemplate.getForEntity(urlReleaseWohnung, BuergerResource.class);
        responseList = restTemplate.getForEntity(urlWohnung, List.class).getBody();
        assertTrue(responseList.isEmpty());
        System.out.println(String.format("release operation für den Wohnung eines Bürgers mit OID '%s' konnte aus der DB erfolgreich durchgeführt.", DomainConstants.M2_B015));
    }

    @Test
    @WithMockUser(username = DomainConstants.M2_U001_NAME)
    public void releaseBuergerWohnungTest() {
        System.out.println("========== release Wohnung Bürger Test ==========");

        assertEquals(1, this.checkWohnung(DomainConstants.M2_B015, DomainConstants.M2_W015));
        String urlReleasevater = "http://localhost:" + port + "/buerger/release/wohnung/" + DomainConstants.M2_B015;
        restTemplate.postForEntity(urlReleasevater, DomainConstants.M2_W015, BuergerResource.class);
        assertEquals(0, this.checkWohnung(DomainConstants.M2_B015, DomainConstants.M2_W015));
        System.out.println(String.format("release operation für den Bürger mit OID '%s' und den Wohnung mit OID '%s' konnte aus der DB erfolgreich durchgeführt.", DomainConstants.M2_B015, DomainConstants.M2_W015));
    }

    private long checkWohnung(String buergerOid, String wohnungOid) {
        return service.read(buergerOid).getWohnungen().stream().filter(k -> k.getOid().equals(wohnungOid)).count();
    }

    @Test
    @WithMockUser(username = DomainConstants.M2_U001_NAME)
    public void AddBuergerWohnungTest() {
        System.out.println("========== Add Bürger Wohnung Test ==========");
        assertEquals(0, this.checkWohnung(DomainConstants.M2_B007, DomainConstants.M2_W008));
        String URL2 = "http://localhost:" + port + "/buerger/add/" + DomainConstants.M2_B007 + "/wohnung/";
        restTemplate.postForEntity(URL2, DomainConstants.M2_W008, BuergerResource.class).getBody();
        assertEquals(1, this.checkWohnung(DomainConstants.M2_B007, DomainConstants.M2_W008));
        System.out.println(String.format("die Wohnung mit OID '%s' könnte zu dem Bürger mit OID '%s' hinzufügt werden.", DomainConstants.M2_W008, DomainConstants.M2_B007));

    }

    private Wohnung createWohnung(String oid) {
        Wohnung wohnung = new Wohnung();
        wohnung.setOid(oid);
        wohnung.setAusrichtung("nord");
        wohnung.setStock("4");
        return wohnung;
    }

    @Test
    @WithMockUser(username = DomainConstants.M2_U001_NAME)
    public void CreateBuergerWohnungTest() {
        System.out.println("========== Save Bürger Wohnung Test ==========");
        String URL1 = "http://localhost:" + port + "/buerger/create/wohnung/" + DomainConstants.M2_B007;
        String oid = "oidWohnung";
        restTemplate.postForEntity(URL1, this.createWohnung(oid), BuergerResource.class).getBody();
        assertEquals(1, this.checkWohnung(DomainConstants.M2_B007, "oidWohnung"));
        System.out.println(String.format("eine Wohnung könnte erstellt und zu dem Bürger mit OID '%s' hinzufügt werden.", DomainConstants.M2_B007));

    }

    @Test
    public void readBuergerPaesseTest() {
        System.out.println("========== read Bürger Paesse Test ==========");
        String URL1 = "http://localhost:" + port + "/buerger/pass/" + DomainConstants.M2_B007;
        responseList = restTemplate.getForEntity(URL1, List.class).getBody();
        assertFalse(responseList.isEmpty());
        System.out.println(String.format("die Paesse von dem Bürger mit OID '%s' konnten aus der DB gelesen werden.", DomainConstants.M2_B007));

    }

    @Test
    @WithMockUser(username = DomainConstants.M2_U001_NAME)
    public void releaseBuergerPassTest() {
        System.out.println("========== release Pass Bürger Test ==========");

        assertEquals(1, this.checkPass(DomainConstants.M2_B015, DomainConstants.M2_P015));
        String urlReleasevater = "http://localhost:" + port + "/buerger/release/paesse/" + DomainConstants.M2_B015;
        restTemplate.getForEntity(urlReleasevater, BuergerResource.class);
        assertEquals(0, this.checkPass(DomainConstants.M2_B015, DomainConstants.M2_P015));
        System.out.println(String.format("release operation für den Bürger mit OID '%s' und den Wohnung mit OID '%s' konnte aus der DB erfolgreich durchgeführt.", DomainConstants.M2_B015, DomainConstants.M2_P015));
    }

    private long checkPass(String buergerOid, String passOid) {
        return service.read(buergerOid).getPass().stream().filter(k -> k.getOid().equals(passOid)).count();
    }

    @Test
    @WithMockUser(username = DomainConstants.M2_U001_NAME)
    public void AddBuergerPassTest() {
        System.out.println("========== Add Bürger Pass Test ==========");
        assertEquals(0, this.checkPass(DomainConstants.M2_B007, DomainConstants.M2_P008));
        String URL2 = "http://localhost:" + port + "/buerger/add/" + DomainConstants.M2_B007 + "/pass/";
        restTemplate.postForEntity(URL2, DomainConstants.M2_P008, BuergerResource.class).getBody();
        assertEquals(1, this.checkPass(DomainConstants.M2_B007, DomainConstants.M2_P008));
        System.out.println(String.format("der Pass mit OID '%s' könnte zu dem Bürger mit OID '%s' hinzufügt werden.", DomainConstants.M2_P008, DomainConstants.M2_B007));

    }

    private Pass createPass(String oid) {
        Pass pass = new Pass();
        pass.setOid(oid);
        pass.setPassNummer("9000");
        pass.setBehoerde("M0021");
        return pass;
    }

    @Test
    @WithMockUser(username = DomainConstants.M2_U001_NAME)
    public void CreateBuergerPassTest() {
        System.out.println("========== Save Bürger Pass Test ==========");
        String URL1 = "http://localhost:" + port + "/buerger/create/pass/" + DomainConstants.M2_B007;
        String oid = "oidPass";
        restTemplate.postForEntity(URL1, this.createPass(oid), BuergerResource.class).getBody();
        assertEquals(1, this.checkPass(DomainConstants.M2_B007, "oidPass"));
        System.out.println(String.format("ein Pass könnte erstellt und zu dem Bürger mit OID '%s' hinzufügt werden.", DomainConstants.M2_B007));

    }

    @Test
    public void readBuergerStaatsangehoerigkeitTest() {
        System.out.println("========== read Bürger Staatsangehoerigkeit Test ==========");
        String URL1 = "http://localhost:" + port + "/buerger/staats/" + DomainConstants.M2_B007;
        responseList = restTemplate.getForEntity(URL1, List.class).getBody();
        assertFalse(responseList.isEmpty());
        System.out.println(String.format("die Staatsangehoerigkeit von dem Bürger mit OID '%s' konnten aus der DB gelesen werden.", DomainConstants.M2_B007));

    }

    private long checkStaatsangehoerigkeit(String buergerOid, String StaatsangehoerigkeitOid) {
        return service.read(buergerOid).getStaatsangehoerigkeitReferences().stream().filter(k -> k.getReferencedOid().equals(StaatsangehoerigkeitOid)).count();
    }

    @Test
    @WithMockUser(username = DomainConstants.M2_U001_NAME)
    public void AddBuergerStaatsangehoerigkeitTest() {
        System.out.println("========== Add Bürger Staatsanghörigkeit Test ==========");
        assertEquals(0, this.checkStaatsangehoerigkeit(DomainConstants.M2_B007, DomainConstants.M2_S008));
        String URL2 = "http://localhost:" + port + "/buerger/add/" + DomainConstants.M2_B007 + "/staats/";
        restTemplate.postForEntity(URL2, DomainConstants.M2_S008, BuergerResource.class).getBody();
        assertEquals(1, this.checkStaatsangehoerigkeit(DomainConstants.M2_B007, DomainConstants.M2_S008));
        System.out.println(String.format("die Staatsangehoerigkeit mit OID '%s' könnte zu dem Bürger mit OID '%s' hinzufügt werden.", DomainConstants.M2_S008, DomainConstants.M2_B007));

    }

    @Test
    public void readBuergerSachbearbeiterTest() {
        System.out.println("========== read Bürger Sachbearbeiter Test ==========");
        String URL1 = "http://localhost:" + port + "/buerger/sachbearbeiter/" + DomainConstants.M2_B007;
        responseList = restTemplate.getForEntity(URL1, List.class).getBody();
        assertFalse(responseList.isEmpty());
        System.out.println(String.format("die Sachbearbeiter von dem Bürger mit OID '%s' konnten aus der DB gelesen werden.", DomainConstants.M2_B007));

    }

    @Test
    @WithMockUser(username = DomainConstants.M2_U001_NAME)
    public void releaseBuergerSachbearbeiterTest() {
        System.out.println("========== release Bürger Sachbearbeiter Test ==========");
        String urlSachbearbeiter = "http://localhost:" + port + "/buerger/sachbearbeiter/" + DomainConstants.M2_B015;
        responseList = restTemplate.getForEntity(urlSachbearbeiter, List.class).getBody();
        assertFalse(responseList.isEmpty());
        String urlReleaseSachbearbeiter = "http://localhost:" + port + "/buerger/release/sachbearbeiter/" + DomainConstants.M2_B015;
        restTemplate.getForEntity(urlReleaseSachbearbeiter, BuergerResource.class);
        responseList = restTemplate.getForEntity(urlSachbearbeiter, List.class).getBody();
        assertTrue(responseList.isEmpty());
        System.out.println(String.format("release operation für den Sachbearbeiter eines Bürgers mit OID '%s' konnte aus der DB erfolgreich durchgeführt.", DomainConstants.M2_B015));
    }

    private long checkSachbearbeiter(String buergerOid, String sachbearbeiterOid) {
        return service.read(buergerOid).getSachbearbeiter().stream().filter(k -> k.getOid().equals(sachbearbeiterOid)).count();
    }

    @Test
    @WithMockUser(username = DomainConstants.M2_U001_NAME)
    public void AddBuergerSachbearbeiterTest() {
        System.out.println("========== Add Bürger Sachbearbeiter Test ==========");
        assertEquals(0, this.checkSachbearbeiter(DomainConstants.M2_B007, DomainConstants.M2_SA008));
        String URL2 = "http://localhost:" + port + "/buerger/add/" + DomainConstants.M2_B007 + "/Sachbearbeiter/";
        restTemplate.postForEntity(URL2, DomainConstants.M2_SA008, BuergerResource.class).getBody();
        assertEquals(1, this.checkSachbearbeiter(DomainConstants.M2_B007, DomainConstants.M2_SA008));
        System.out.println(String.format("der Sachbearbeiter mit OID '%s' könnte zu dem Bürger mit OID '%s' hinzufügt werden.", DomainConstants.M2_SA008, DomainConstants.M2_B007));

    }

    private Sachbearbeiter createSachbearbeiter(String oid) {
        Sachbearbeiter sachbearbeiter = new Sachbearbeiter();
        sachbearbeiter.setOid(oid);
        sachbearbeiter.setFunktion("beamte");
        sachbearbeiter.setTelephone("415248523");
        return sachbearbeiter;
    }

    @Test
    @WithMockUser(username = DomainConstants.M2_U001_NAME)
    public void CreateBuergerSachbearbeiterTest() {
        System.out.println("========== Save Bürger SAchbearbeiter Test ==========");
        String URL1 = "http://localhost:" + port + "/buerger/create/sachbearbeiter/" + DomainConstants.M2_B007;
        String oid = "oidSachbearbeiter";
        restTemplate.postForEntity(URL1, this.createSachbearbeiter(oid), BuergerResource.class).getBody();
        assertEquals(1, this.checkSachbearbeiter(DomainConstants.M2_B007, "oidSachbearbeiter"));
        System.out.println(String.format("ein Sachbearbeiter  könnte erstellt und zu dem Bürger mit OID '%s' hinzufügt werden.", DomainConstants.M2_B007));

    }

}
