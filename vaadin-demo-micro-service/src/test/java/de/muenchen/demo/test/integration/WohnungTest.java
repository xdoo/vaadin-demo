/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.demo.test.integration;

import static com.ctc.wstx.util.DataUtil.Integer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import de.muenchen.demo.service.Application;
import de.muenchen.demo.service.domain.Wohnung;
import de.muenchen.demo.service.domain.WohnungRepository;
import de.muenchen.demo.test.service.DomainConstants;
import de.muenchen.vaadin.demo.api.hateoas.HateoasUtil;
import de.muenchen.vaadin.demo.api.rest.SearchResultResource;
import de.muenchen.vaadin.demo.api.rest.WohnungResource;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
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
import org.springframework.http.converter.StringHttpMessageConverter;
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
public class WohnungTest {

    private RestTemplate restTemplate;
    @Value("${local.server.port}")
    private int port;
    private String urlSave;
    private String urlRead1;
    private String urlRead0;
    ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally+
    @Autowired
    WohnungRepository repo;
    @Rule
    public ExpectedException thrown = ExpectedException.none();

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
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

        urlSave = "http://localhost:" + port + "/wohnung/save";

    }

    @Test
    public void saveWohnungTest() throws JsonProcessingException {
        System.out.println("========== save Wohnung Test ==========");

//save Wohnung, die Adresse Interne hat
        WohnungResource w = new WohnungResource();
        w.setOid("OID0");
        w.setHausnummer(47);
        w.setStrasseReference(209);
        w.setAusrichtung("west");
        w.setStrasse("alleestr.");
        w.setStock("2");
        WohnungResource w1 = restTemplate.postForEntity(urlSave, w, WohnungResource.class).getBody();
        assertNotNull(w1);
        assertNotNull(w1.getStrasseReference());
        assertEquals("Ampfingstr.", w1.getStrasse());
        assertNotNull(w1.getLink(HateoasUtil.REL_NEW));
        assertNotNull(w1.getLink(HateoasUtil.REL_UPDATE));
        assertNotNull(w1.getLink(HateoasUtil.REL_COPY));
        assertNotNull(w1.getLink(HateoasUtil.REL_SELF));
        assertNotNull(w1.getLink(HateoasUtil.REL_DELETE));
        assertNotNull(w1.getLink(de.muenchen.vaadin.demo.api.rest.WohnungResource.BUERGER));
        assertNotNull(w1.getLink(de.muenchen.vaadin.demo.api.rest.WohnungResource.RELEASE_BUERGERS));
        System.out.println(String.format("Wohnung wurde mit Mandant '%s' in der DB gespeichert.", w1.getMandant().getOid()));

//save Wohnung,die Adresse externe hat
        WohnungResource w2 = new WohnungResource();
        w2.setOid("OID1");
        w2.setStrasse("panoramaStr.");
        w2.setPlz(69034);
        w2.setHausnummer(19);
        w2.setStadt("Passau");
        w2.setAusrichtung("west");
        w2.setStock("2");
        WohnungResource w3 = restTemplate.postForEntity(urlSave, w2, WohnungResource.class).getBody();
        assertNotNull(w3);
        assertNotNull(w3.getLink(HateoasUtil.REL_NEW));
        assertNotNull(w3.getLink(HateoasUtil.REL_UPDATE));
        assertNotNull(w3.getLink(HateoasUtil.REL_COPY));
        assertNotNull(w3.getLink(HateoasUtil.REL_SELF));
        assertNotNull(w3.getLink(HateoasUtil.REL_DELETE));
        assertNotNull(w3.getLink(de.muenchen.vaadin.demo.api.rest.WohnungResource.BUERGER));
        assertNotNull(w3.getLink(de.muenchen.vaadin.demo.api.rest.WohnungResource.RELEASE_BUERGERS));
        System.out.println(String.format("Wohnung wurde mit Mandant '%s' in der DB gespeichert.", w3.getMandant().getOid()));

    }

    @Test
    public void readWohnungTest() throws JsonProcessingException {
        System.out.println("========== read Wohnung Test ==========");
//read Wohnung,die Adresse Externe hat
        urlRead0 = "http://localhost:" + port + "/wohnung/" + DomainConstants.M2_W002;

        WohnungResource w4 = restTemplate.getForEntity(urlRead0, WohnungResource.class).getBody();
        assertNotNull(w4);
        assertNotNull(w4.getStrasse());
        assertNotNull(w4.getStadt());
        assertNull(w4.getStrasseReference());
        assertEquals("DonauStr.", w4.getStrasse());
        System.out.println(String.format("Wohnung konnte mit OID '%s' aus der DB gelesen werden.", w4.getOid()));

//read Wohnung, die Adresse Interne hat
        urlRead1 = "http://localhost:" + port + "/wohnung/" + DomainConstants.M2_W022;
        WohnungResource w5 = restTemplate.getForEntity(urlRead1, WohnungResource.class).getBody();
        assertNotNull(w5);
        assertNotNull(w5.getStrasse());
        assertNotNull(w5.getStadt());
        System.out.println(String.format("Wohnung konnte mit OID '%s' aus der DB gelesen werden.", w5.getOid()));

    }

    @Test
    public void updateWohnungTest() throws JsonProcessingException {
        System.out.println("========== update Wohnung Test ==========");
        urlRead1 = "http://localhost:" + port + "/wohnung/" + DomainConstants.M2_W003;
        urlRead0 = "http://localhost:" + port + "/wohnung/" + DomainConstants.M2_W024;

//update Wohnung,die Adresse Interne hat
        WohnungResource w6 = restTemplate.getForEntity(urlRead0, WohnungResource.class).getBody();
        assertNotNull(w6);
        w6.setHausnummer(20);
        w6.setAusrichtung("nord");
        WohnungResource w7 = restTemplate.postForEntity(urlRead0, w6, WohnungResource.class).getBody();
        assertNotNull(w7);
        assertEquals(Integer(20), w7.getHausnummer());
        assertEquals("nord", w7.getAusrichtung());
        assertNotNull(w7.getLink(HateoasUtil.REL_NEW));
        assertNotNull(w7.getLink(HateoasUtil.REL_UPDATE));
        assertNotNull(w7.getLink(HateoasUtil.REL_COPY));
        assertNotNull(w7.getLink(HateoasUtil.REL_SELF));
        assertNotNull(w7.getLink(HateoasUtil.REL_DELETE));
        assertNotNull(w7.getLink(de.muenchen.vaadin.demo.api.rest.WohnungResource.BUERGER));
        assertNotNull(w7.getLink(de.muenchen.vaadin.demo.api.rest.WohnungResource.RELEASE_BUERGERS));
        System.out.println("Wohnung wurde mit neuem Hausnummer und Ausrichtung in der DB gespeichert.");

//update Wohnung,die Adresse Externe hat
        WohnungResource w8 = restTemplate.getForEntity(urlRead1, WohnungResource.class).getBody();
        assertNotNull(w8);
        w8.setStock("4");
        w8.setStrasse("Goethestr.");
        WohnungResource w9 = restTemplate.postForEntity(urlRead1, w8, WohnungResource.class).getBody();
        assertNotNull(w9);
        assertEquals("4", w9.getStock());
        assertEquals("Goethestr.", w9.getStrasse());
        assertNotNull(w9.getLink(HateoasUtil.REL_NEW));
        assertNotNull(w9.getLink(HateoasUtil.REL_UPDATE));
        assertNotNull(w9.getLink(HateoasUtil.REL_COPY));
        assertNotNull(w9.getLink(HateoasUtil.REL_SELF));
        assertNotNull(w9.getLink(HateoasUtil.REL_DELETE));
        assertNotNull(w9.getLink(de.muenchen.vaadin.demo.api.rest.WohnungResource.BUERGER));
        assertNotNull(w9.getLink(de.muenchen.vaadin.demo.api.rest.WohnungResource.RELEASE_BUERGERS));
        System.out.println("Wohnung wurde mit neuem Stock und Strasse in der DB gespeichert.");

    }

    @Test
    public void copyWohnungTest() throws JsonProcessingException {
        System.out.println("========== copy Wohnung Test ==========");
        urlRead1 = "http://localhost:" + port + "/wohnung/" + DomainConstants.M2_W004;
        urlRead0 = "http://localhost:" + port + "/wohnung/" + DomainConstants.M2_W025;
//copy Wohnung, die Adresse Interne hat
        WohnungResource w10 = restTemplate.getForEntity(urlRead0, WohnungResource.class).getBody();
        String urlCopy = "http://localhost:" + port + "/wohnung/copy/" + w10.getOid();
        WohnungResource w11 = restTemplate.getForEntity(urlCopy, WohnungResource.class).getBody();
        assertNotNull(w11);
        assertEquals(w10.getStock(), w11.getStock());
        assertEquals(w10.getHausnummer(), w11.getHausnummer());
        assertNotNull(w11.getLink(HateoasUtil.REL_NEW));
        assertNotNull(w11.getLink(HateoasUtil.REL_UPDATE));
        assertNotNull(w11.getLink(HateoasUtil.REL_COPY));
        assertNotNull(w11.getLink(HateoasUtil.REL_SELF));
        assertNotNull(w11.getLink(HateoasUtil.REL_DELETE));
        assertNotNull(w11.getLink(de.muenchen.vaadin.demo.api.rest.WohnungResource.BUERGER));
        assertNotNull(w11.getLink(de.muenchen.vaadin.demo.api.rest.WohnungResource.RELEASE_BUERGERS));
        System.out.println(String.format("Objekt mit der OID '%s' konnte erfolgreich in Objekt '%s' kopiert (und in DB gespeichert) werden", w10.getOid(), w11.getOid()));

//copy Wohnung, die Adresse Externe hat
        WohnungResource w12 = restTemplate.getForEntity(urlRead1, WohnungResource.class).getBody();
        String urlCopy1 = "http://localhost:" + port + "/wohnung/copy/" + w12.getOid();
        WohnungResource w13 = restTemplate.getForEntity(urlCopy1, WohnungResource.class).getBody();
        assertNotNull(w13);
        assertEquals(w12.getStock(), w13.getStock());
        assertEquals(w12.getHausnummer(), w13.getHausnummer());
        assertNotNull(w12.getLink(HateoasUtil.REL_NEW));
        assertNotNull(w12.getLink(HateoasUtil.REL_UPDATE));
        assertNotNull(w12.getLink(HateoasUtil.REL_COPY));
        assertNotNull(w12.getLink(HateoasUtil.REL_SELF));
        assertNotNull(w12.getLink(HateoasUtil.REL_DELETE));
        assertNotNull(w12.getLink(de.muenchen.vaadin.demo.api.rest.WohnungResource.BUERGER));
        assertNotNull(w12.getLink(de.muenchen.vaadin.demo.api.rest.WohnungResource.RELEASE_BUERGERS));
        System.out.println(String.format("Objekt mit der OID '%s' konnte erfolgreich in Objekt '%s' kopiert (und in DB gespeichert) werden", w12.getOid(), w13.getOid()));

    }

    @Test
    public void copyListWohnungTest() {
        System.out.println("========== copy Liste Wohnung Test ==========");
        int x = this.count(DomainConstants.M2);
        String URL2 = "http://localhost:" + port + "/wohnung/copy";
        ArrayList<String> oids = new ArrayList();
        oids.add(DomainConstants.M2_W027);
        oids.add(DomainConstants.M2_W028);
        restTemplate.postForEntity(URL2, oids, WohnungResource.class);
        String URL11 = "http://localhost:" + port + "/wohnung/query";
        SearchResultResource queryResponse = restTemplate.getForEntity(URL11, SearchResultResource.class).getBody();
        assertEquals(x + 2, queryResponse.getResult().size());
        System.out.println(String.format("Objekte mit der OID '%s' und der OID '%s' konnte erfolgreich in Objekt  kopiert (und in DB gespeichert) werden", DomainConstants.M2_W025, DomainConstants.M2_W026));

    }

    @Test
    public void queryWohnungTest() throws JsonProcessingException {

        System.out.println("========== query Wohnung Test Mandant 2 ==========");
        int x = this.count(DomainConstants.M2);
        String URL11 = "http://localhost:" + port + "/wohnung/query";
        SearchResultResource queryResponse = restTemplate.getForEntity(URL11, SearchResultResource.class).getBody();
        assertEquals(x, queryResponse.getResult().size());
        assertNotNull(null, queryResponse.getLink("self"));
        assertNotNull(null, queryResponse.getLink("query"));
        System.out.println(String.format("Suche wurde erfolgreich durchgeführt. Einträge Mandant %s: %s | Ergebnis der Suche: %s", DomainConstants.M2, x, queryResponse.getResult().size()));

    }

    private int count(String mid) {
        ArrayList<Wohnung> all = Lists.newArrayList(repo.findAll());
        return all.stream().filter(b -> b.getMandant().getOid().equals(mid)).collect(Collectors.counting()).intValue();
    }

    @Test
    @WithMockUser(username = DomainConstants.M2_U001_NAME)
    public void wohnungDeleteTest() {
        System.out.println("========== delete Wohnung Test ==========");
        String URL11 = "http://localhost:" + port + "/wohnung/" + DomainConstants.M2_W013;
        WohnungResource response = restTemplate.getForEntity(URL11, WohnungResource.class).getBody();
        assertNotNull(response);
        restTemplate.delete(URL11);
        thrown.expect(org.springframework.web.client.HttpServerErrorException.class);
        thrown.expectMessage(equalTo("500 Internal Server Error"));
        restTemplate.getForEntity(URL11, WohnungResource.class).getBody();

        System.out.println(String.format("Wohnung konnte mit OID '%s' aus der DB (und dem Cache) gelöscht werden.", DomainConstants.M2_W004));

    }

    @Test
    public void wohnungDeleteListTest() {
        System.out.println("========== delete Wohnung Test ==========");

        String URL2 = "http://localhost:" + port + "/wohnung";
        ArrayList<String> oids = new ArrayList();
        oids.add(DomainConstants.M2_W030);
        oids.add(DomainConstants.M2_W031);
        restTemplate.postForEntity(URL2, oids, WohnungResource.class);
        String URL11 = "http://localhost:" + port + "/wohnung/" + DomainConstants.M2_W030;
        thrown.expect(org.springframework.web.client.HttpServerErrorException.class);
        thrown.expectMessage(equalTo("500 Internal Server Error"));
        WohnungResource response = restTemplate.getForEntity(URL11, WohnungResource.class).getBody();
        assertNull(response);
        System.out.println(String.format("Wohnung  mit OID '%s'und OID '%s' konnte aus der DB (und dem Cache) gelöscht werden.", DomainConstants.M2_W030, DomainConstants.M2_W031));

    }

    @Test
    public void readBuergerWohnungTest() {
        System.out.println("========== release Wohnung Buerger Test ==========");
        String URL1 = "http://localhost:" + port + "/wohnung/buerger/" + DomainConstants.M2_W007;
        SearchResultResource responseList2 = restTemplate.getForEntity(URL1, SearchResultResource.class).getBody();
        assertFalse(responseList2.getResult().isEmpty());
        System.out.println(String.format("die Buerger von der Wohnung mit OID '%s' konnten aus der DB gelesen werden.", DomainConstants.M2_W007));

    }

    @Test
    public void releaseWohnungAllBuergerTest() {
        System.out.println("========== release  Wohnung all Bürger Test ==========");
        String urlBuerger = "http://localhost:" + port + "/wohnung/buerger/" + DomainConstants.M2_W008;
        SearchResultResource responseListEltern = restTemplate.getForEntity(urlBuerger, SearchResultResource.class).getBody();
        assertFalse(responseListEltern.getResult().isEmpty());
        String urlReleaseBuerger = "http://localhost:" + port + "/wohnung/release/buerger/" + DomainConstants.M2_W008;
        restTemplate.getForEntity(urlReleaseBuerger, WohnungResource.class);
        SearchResultResource responseListEltern2 = restTemplate.getForEntity(urlBuerger, SearchResultResource.class).getBody();
        assertTrue(responseListEltern2.getResult().isEmpty());
        System.out.println(String.format("release operation für den Buerger einer Wohnung mit OID '%s' konnte aus der DB erfolgreich durchgeführt.", DomainConstants.M2_W008));
    }

}
