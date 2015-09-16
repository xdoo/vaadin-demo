//package de.muenchen.demo.test.integration;
//
///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.github.tomakehurst.wiremock.junit.WireMockRule;
//import de.muenchen.demo.service.Application;
//import de.muenchen.demo.service.domain.Pass;
//import de.muenchen.demo.service.domain.PassRepository;
//import de.muenchen.demo.service.domain.Staatsangehoerigkeit;
//import de.muenchen.demo.test.service.DomainConstants;
//import de.muenchen.vaadin.demo.api.rest.BuergerResource;
//import de.muenchen.vaadin.demo.api.rest.StaatsangehoerigkeitResource;
//import org.apache.http.auth.AuthScope;
//import org.apache.http.auth.UsernamePasswordCredentials;
//import org.apache.http.client.HttpClient;
//import org.apache.http.impl.client.BasicCredentialsProvider;
//import org.apache.http.impl.client.HttpClientBuilder;
//import org.junit.Before;
//import org.junit.Rule;
//import org.junit.Test;
//import org.junit.rules.ExpectedException;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.test.SpringApplicationConfiguration;
//import org.springframework.boot.test.WebIntegrationTest;
//import org.springframework.hateoas.hal.Jackson2HalModule;
//import org.springframework.http.client.ClientHttpRequestFactory;
//import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
//import org.springframework.http.converter.HttpMessageConverter;
//import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import org.springframework.web.client.RestTemplate;
//
//import java.security.KeyManagementException;
//import java.security.KeyStoreException;
//import java.security.NoSuchAlgorithmException;
//import java.util.ArrayList;
//import java.util.Collections;
//
//import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
//import static com.github.tomakehurst.wiremock.client.WireMock.get;
//import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
//import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
//import static org.hamcrest.CoreMatchers.equalTo;
//import static org.junit.Assert.assertFalse;
//import static org.junit.Assert.assertNotNull;
//import static org.junit.Assert.assertNull;
//
///**
// *
// * @author praktikant.tmar
// */
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringApplicationConfiguration(classes = Application.class)
//@WebIntegrationTest({"server.port=0", "management.port=0"})
//public class PassTest {
//
//    @Rule
//    public ExpectedException thrown = ExpectedException.none();
//    @Rule
//    public WireMockRule wireMockRule = new WireMockRule(8089);
//    @Autowired
//    PassRepository repo;
//    private RestTemplate restTemplate;
//    @Value("${local.server.port}")
//    private int port;
//    private String urlSave;
//    private String urlNew;
//
//    @Before
//    public void setUp() throws JsonProcessingException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
//        BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
//        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials("hans", "test"));
//
//        HttpClient httpClient = HttpClientBuilder.create()
//                .setDefaultCredentialsProvider(credentialsProvider)
//                .build();
//
//        ClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
//        restTemplate = new RestTemplate(requestFactory);
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.registerModule(new Jackson2HalModule());
//
//        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
//        converter.setObjectMapper(objectMapper);
//        restTemplate.setMessageConverters(Collections.<HttpMessageConverter<?>>singletonList(converter));
//
//        ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally
//
//        Staatsangehoerigkeit staat = new Staatsangehoerigkeit();
//        staat.setCode("it");
//        staat.setLand("Italien");
//        staat.setSprache("Italienisch");
//        staat.setReference("OIDM2_STAATS007");
//
//        String json = mapper.writeValueAsString(staat);
//
//        stubFor(get(urlEqualTo("/staat/OIDM2_STAATS007")).willReturn(
//                aResponse().withHeader("Content-Type", "application/json")
//                .withBody(json)
//        ));
//
//        Staatsangehoerigkeit staat2 = new Staatsangehoerigkeit();
//        staat2.setCode("fr");
//        staat2.setLand("Frankreich");
//        staat2.setSprache("französisch");
//        staat2.setReference("OIDM2_STAATS008");
//
//        String json2 = mapper.writeValueAsString(staat2);
//
//        stubFor(get(urlEqualTo("/staat/OIDM2_STAATS008")).willReturn(
//                aResponse().withHeader("Content-Type", "application/json")
//                .withBody(json2)
//        ));
//
//        Staatsangehoerigkeit staat3 = new Staatsangehoerigkeit();
//        staat3.setCode("tn");
//        staat3.setLand("Tunesien");
//        staat3.setSprache("Arabisch");
//        staat3.setReference("OIDM2_STAATS015");
//
//        String json3 = mapper.writeValueAsString(staat3);
//
//        stubFor(get(urlEqualTo("/staat/OIDM2_STAATS015")).willReturn(
//                aResponse().withHeader("Content-Type", "application/json")
//                .withBody(json3)
//        ));
//
//        Staatsangehoerigkeit staat4 = new Staatsangehoerigkeit();
//        staat4.setCode("de");
//        staat4.setLand("Deutschland");
//        staat4.setSprache("Deutsch");
//        staat4.setReference("OIDM2_STAATS017");
//
//        String json4 = mapper.writeValueAsString(staat4);
//
//        stubFor(get(urlEqualTo("/staat/OIDM2_STAATS017")).willReturn(
//                aResponse().withHeader("Content-Type", "application/json")
//                .withBody(json4)
//        ));
//
//        urlSave = "http://localhost:" + port + "/pass/save";
//        urlNew = "http://localhost:" + port + "/pass/new";
//
//    }
//
//    @Test
//    @WithMockUser(username = DomainConstants.M2_U001_NAME)
//
//    public void newPassTest() throws JsonProcessingException {
//        System.out.println("========== create Pass Test ==========");
//        //TODO
////        response = restTemplate.getForEntity(urlNew, PassResource.class).getBody();
////        assertNotNull(response.getLink("new"));
////        assertNotNull(response.getLink("save"));
////        assertNotNull(response.getOid());
////        assertNull(repo.findFirstByOidAndMandantOid(response.getOid(), DomainConstants.M2));
////        System.out.println(String.format("Pass wurde mit neuer OID '%s' erstellt (und nicht in der DB gespeichert)", response.getOid()));
//
//    }
//
//    private Pass createPass(String oid) {
//        Pass pass = new Pass();
//        pass.setOid(oid);
//        pass.setBehoerde("M001");
//        pass.setPassNummer("040521");
//        return pass;
//    }
//
//    @Test
//    public void savePassTest() throws JsonProcessingException {
//        System.out.println("========== save Pass Test ==========");
//        String oid = "OIDTEST";
//        //TODO
////        response = restTemplate.postForEntity(urlSave, this.createPass(oid), PassResource.class).getBody();
////        assertEquals("M001", response.getBehoerde());
////        assertEquals(DomainConstants.M2, response.getMandant().getOid());
////        assertNotNull(response.getLink(HateoasUtil.REL_NEW));
////        assertNotNull(response.getLink(HateoasUtil.REL_UPDATE));
////        assertNotNull(response.getLink(HateoasUtil.REL_COPY));
////        assertNotNull(response.getLink(HateoasUtil.REL_SELF));
////        assertNotNull(response.getLink(HateoasUtil.REL_DELETE));
////
////        System.out.println(String.format("Pass wurde mit Mandant '%s' in der DB gespeichert.", response.getMandant().getOid()));
//    }
//
//    @Test
//    public void updatePassTest() throws JsonProcessingException {
//        System.out.println("========== update Pass Test ==========");
//        String oid = "OIDUPDATE";
//        Pass p1 = this.createPass(oid);
//        //TODO
////        response = restTemplate.postForEntity(urlSave, p1, PassResource.class).getBody();
////        p1.setBehoerde("P001");
////        String urlUpdate = "http://localhost:" + port + "/pass/" + oid;
////        restTemplate.put(urlUpdate, p1);
////        String URL11 = "http://localhost:" + port + "/pass/" + oid;
////        response = restTemplate.getForEntity(URL11, PassResource.class).getBody();
////        assertEquals(response.getBehoerde(), "P001");
////        assertEquals(DomainConstants.M2, response.getMandant().getOid());
////
////        System.out.println("Pass wurde mit neuem Vornamen in der DB gespeichert.");
//    }
//
//    @Test
//    public void readPassTest() throws JsonProcessingException {
//
//        System.out.println("========== read Pass Test ==========");
//        String URL11 = "http://localhost:" + port + "/pass/" + DomainConstants.M2_P003;
//        //TODO
////        response = restTemplate.getForEntity(URL11, PassResource.class).getBody();
////        assertNotNull(response);
////        assertNotNull(response.getLink(HateoasUtil.REL_NEW));
////        assertNotNull(response.getLink(HateoasUtil.REL_UPDATE));
////        assertNotNull(response.getLink(HateoasUtil.REL_COPY));
////        assertNotNull(response.getLink(HateoasUtil.REL_SELF));
////        assertNotNull(response.getLink(HateoasUtil.REL_DELETE));
////
////        System.out.println(String.format("Pass konnte mit OID '%s' aus der DB gelesen werden.", response.getOid()));
//    }
//
//    @Test
//    public void copyPassTest() {
//        System.out.println("========== copy Pass Test ==========");
//
//        String URL2 = "http://localhost:" + port + "/pass/copy/" + DomainConstants.M2_P008;
//        //TODO
////        response = restTemplate.getForEntity(URL2, PassResource.class).getBody();
////
////        assertNotNull(response);
////        assertNotNull(response.getLink(HateoasUtil.REL_NEW));
////        assertNotNull(response.getLink(HateoasUtil.REL_UPDATE));
////        assertNotNull(response.getLink(HateoasUtil.REL_COPY));
////        assertNotNull(response.getLink(HateoasUtil.REL_SELF));
////        assertNotNull(response.getLink(HateoasUtil.REL_DELETE));
////
////        System.out.println(String.format("Objekt mit der OID '%s' konnte erfolgreich in Objekt '%s' kopiert (und in DB gespeichert) werden", DomainConstants.M2_P005, response.getOid()));
//
//    }
//
//    /*@Test
//    public void copyListPassTest() {
//        System.out.println("========== copy Liste Pass Test ==========");
//        int x = this.count(DomainConstants.M2);
//        String URL2 = "http://localhost:" + port + "/pass/copy";
//        ArrayList<String> oids = new ArrayList();
//        oids.add(DomainConstants.M2_P025);
//        oids.add(DomainConstants.M2_P026);
//        //TODO
////        restTemplate.postForEntity(URL2, oids, PassResource.class);
////        String URL11 = "http://localhost:" + port + "/pass/query";
////        SearchResultResource queryResponse = restTemplate.getForEntity(URL11, SearchResultResource.class).getBody();
////        assertEquals(x + 2, queryResponse.getResult().size());
////        System.out.println(String.format("Objekte mit der OID '%s' und der OID '%s' konnte erfolgreich in Objekt  kopiert (und in DB gespeichert) werden", DomainConstants.M2_P025, DomainConstants.M2_P026));
//
//    }*/
//
//    @Test
//    @WithMockUser(username = DomainConstants.M2_U001_NAME)
//    public void passDeleteTest() {
//        System.out.println("========== delete Pass Test ==========");
//        String URL11 = "http://localhost:" + port + "/pass/" + DomainConstants.M2_P013;
////        TODO
////        response = restTemplate.getForEntity(URL11, PassResource.class).getBody();
////        assertNotNull(response);
////        restTemplate.delete(URL11);
////        thrown.expect(org.springframework.web.client.HttpServerErrorException.class);
////        thrown.expectMessage(equalTo("500 Internal Server Error"));
////        response = restTemplate.getForEntity(URL11, PassResource.class).getBody();
////
////        System.out.println(String.format("Pass konnte mit OID '%s' aus der DB (und dem Cache) gelöscht werden.", DomainConstants.M2_P004));
//
//    }
//
//    @Test
//    public void passDeleteListTest() {
//        System.out.println("========== delete Pass Test ==========");
//
//        String URL2 = "http://localhost:" + port + "/pass";
//        ArrayList<String> oids = new ArrayList();
//        oids.add(DomainConstants.M2_P020);
//        oids.add(DomainConstants.M2_P021);
//        //TODO
////        restTemplate.postForEntity(URL2, oids, PassResource.class);
////        String URL11 = "http://localhost:" + port + "/pass/" + DomainConstants.M2_P020;
////        thrown.expect(org.springframework.web.client.HttpServerErrorException.class);
////        thrown.expectMessage(equalTo("500 Internal Server Error"));
////        response = restTemplate.getForEntity(URL11, PassResource.class).getBody();
////        assertNull(response);
////        System.out.println(String.format("Pass  mit OID '%s'und OID '%s' konnte aus der DB (und dem Cache) gelöscht werden.", DomainConstants.M2_P020, DomainConstants.M2_P021));
//
//    }
//
//    /*@Test
//    public void queryPassTest() throws JsonProcessingException {
//
//        System.out.println("========== query Pass Test Mandant 2 ==========");
//        int x = this.count(DomainConstants.M2);
//        String URL11 = "http://localhost:" + port + "/pass/query";
//        SearchResultResource queryResponse = restTemplate.getForEntity(URL11, SearchResultResource.class).getBody();
//        assertEquals(x, queryResponse.getResult().size());
//        assertNotNull(null, queryResponse.getLink("self"));
//        assertNotNull(null, queryResponse.getLink("query"));
//        System.out.println(String.format("Suche wurde erfolgreich durchgeführt. Einträge Mandant %s: %s | Ergebnis der Suche: %s", DomainConstants.M2, x, queryResponse.getResult().size()));
//
//    }*/
//
//    /*private int count(String mid) {
//        ArrayList<Pass> all = Lists.newArrayList(repo.findAll());
//        return all.stream().filter(b -> b.getMandant().getOid().equals(mid)).collect(Collectors.counting()).intValue();
//    }*/
//
//    @Test
//    public void readPassStaatsangehoerigkeitTest() {
//        System.out.println("========== read Pass Staatsangehoerigkeit Test ==========");
//        String URL1 = "http://localhost:" + port + "/pass/staat/" + DomainConstants.M2_P007;
//        StaatsangehoerigkeitResource staat = restTemplate.getForEntity(URL1, StaatsangehoerigkeitResource.class).getBody();
//        assertNotNull(staat);
//        System.out.println(String.format("die Staatsangehoerigkeit von dem Pass mit OID '%s' konnten aus der DB gelesen werden.", DomainConstants.M2_P007));
//
//    }
//
//    private boolean checkStaatsangehoerigkeit(String passOid, String StaatsangehoerigkeitOid) {
//        //TODO return service.read(passOid).getStaatsangehoerigkeitReference().getReferencedOid().equals(StaatsangehoerigkeitOid);
//        return false;
//    }
//
//    @Test
//    @WithMockUser(username = DomainConstants.M2_U001_NAME)
//    public void AddPassStaatsangehoerigkeitTest() {
//        System.out.println("========== Add Pass Staatsanghörigkeit Test ==========");
//        assertFalse(this.checkStaatsangehoerigkeit(DomainConstants.M2_P007, DomainConstants.M2_S008));
//        String URL2 = "http://localhost:" + port + "/pass/add/" + DomainConstants.M2_P007 + "/staats/";
//        //TODO
////        restTemplate.postForEntity(URL2, DomainConstants.M2_S008, PassResource.class).getBody();
////        assertTrue(this.checkStaatsangehoerigkeit(DomainConstants.M2_P007, DomainConstants.M2_S008));
////        System.out.println(String.format("die Staatsangehoerigkeit mit OID '%s' könnte zu dem Pass mit OID '%s' hinzufügt werden.", DomainConstants.M2_S008, DomainConstants.M2_P007));
//
//    }
//
//    @Test
//    public void readBuergerPassTest() {
//        System.out.println("========== read Bürger Pass Test ==========");
//        String urlBuerger = "http://localhost:" + port + "/pass/buerger/" + DomainConstants.M2_P019;
//        BuergerResource r1 = restTemplate.getForEntity(urlBuerger, BuergerResource.class).getBody();
//        assertNotNull(r1);
//        System.out.println(String.format("der Bürger von dem Pass mit OID '%s' konnten aus der DB gelesen werden.", DomainConstants.M2_P004));
//
//    }
//
//    @Test
//    public void releasePassBuergerTest() {
//        System.out.println("========== release Pass Buerger Test ==========");
//        String urlBuerger = "http://localhost:" + port + "/pass/buerger/" + DomainConstants.M2_P018;
//        BuergerResource r1 = restTemplate.getForEntity(urlBuerger, BuergerResource.class).getBody();
//        assertNotNull(r1);
//        String urlReleaseBuerger = "http://localhost:" + port + "/pass/release/buerger/" + DomainConstants.M2_P018;
//        restTemplate.getForEntity(urlReleaseBuerger, BuergerResource.class);
//        thrown.expect(org.springframework.web.client.HttpServerErrorException.class);
//        thrown.expectMessage(equalTo("500 Internal Server Error"));
//        BuergerResource r2 = restTemplate.getForEntity(urlBuerger, BuergerResource.class).getBody();
//        assertNull(r2);
//        System.out.println(String.format("release operation für Pass Buerger mit OID '%s' konnte aus der DB erfolgreich durchgeführt.", DomainConstants.M2_P005));
//    }
//
//}
