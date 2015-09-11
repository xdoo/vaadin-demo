////package de.muenchen.demo.test.integration;
////
////import com.fasterxml.jackson.core.JsonProcessingException;
////import com.fasterxml.jackson.databind.ObjectMapper;
////import de.muenchen.demo.service.Application;
////import de.muenchen.demo.service.domain.AdresseExterneRepository;
////import de.muenchen.demo.service.domain.AdresseInterneRepository;
////import de.muenchen.demo.service.domain.AdresseReferenceRepository;
////import de.muenchen.demo.service.domain.AuthorityPermissionRepository;
////import de.muenchen.demo.service.domain.AuthorityRepository;
////import de.muenchen.demo.service.domain.Buerger;
////import de.muenchen.demo.service.domain.BuergerRepository;
////import de.muenchen.demo.service.domain.MandantRepository;
////import de.muenchen.demo.service.domain.PassRepository;
////import de.muenchen.demo.service.domain.PermissionRepository;
////import de.muenchen.demo.service.domain.StaatsangehoerigkeitReferenceRepository;
////import de.muenchen.demo.service.domain.UserAuthorityRepository;
////import de.muenchen.demo.service.domain.UserRepository;
////import de.muenchen.demo.service.domain.Sachbearbeiter;
////import de.muenchen.demo.service.domain.SachbearbeiterRepository;
////import de.muenchen.demo.service.domain.User;
////import de.muenchen.vaadin.demo.api.rest.BuergerResource;
////import de.muenchen.vaadin.demo.api.rest.SearchResultResource;
////import de.muenchen.demo.service.rest.api.SachbearbeiterResource;
////import de.muenchen.demo.service.rest.api.UserResource;
////import java.security.KeyManagementException;
////import java.security.KeyStoreException;
////import java.security.NoSuchAlgorithmException;
////import java.util.Collections;
////import java.util.List;
////import javax.net.ssl.SSLContext;
////import org.apache.http.auth.AuthScope;
////import org.apache.http.auth.UsernamePasswordCredentials;
////import org.apache.http.client.HttpClient;
////import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
////import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
////import org.apache.http.conn.ssl.SSLContexts;
////import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
////import org.apache.http.impl.client.BasicCredentialsProvider;
////import org.apache.http.impl.client.HttpClientBuilder;
////import org.junit.After;
////import static org.junit.Assert.*;
////import org.junit.Before;
////import org.junit.Test;
////import org.junit.runner.RunWith;
////import org.springframework.beans.factory.annotation.Autowired;
////import org.springframework.beans.factory.annotation.Value;
////import org.springframework.boot.test.SpringApplicationConfiguration;
////import org.springframework.boot.test.TestRestTemplate;
////import org.springframework.boot.test.WebIntegrationTest;
////import org.springframework.hateoas.hal.Jackson2HalModule;
////import org.springframework.http.client.ClientHttpRequestFactory;
////import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
////import org.springframework.http.converter.HttpMessageConverter;
////import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
////import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
////import org.springframework.web.client.RestTemplate;
////
/////*
//// * To change this license header, choose License Headers in Project Properties.
//// * To change this template file, choose Tools | Templates
//// * and open the template in the editor.
//// */
/////**
//// *
//// * @author praktikant.tmar
//// */
////@RunWith(SpringJUnit4ClassRunner.class)
////@SpringApplicationConfiguration(classes = Application.class)
////@WebIntegrationTest({"server.port=0", "management.port=0"})
////public class SachbearbeiterTest {
////
////    Sachbearbeiter sachbearbeiter = new Sachbearbeiter();
////    Sachbearbeiter sachbearbeiter2 = new Sachbearbeiter();
////    Buerger buerger = new Buerger();
////
////    private RestTemplate restTemplate = new TestRestTemplate();
////    @Value("${local.server.port}")
////    private int port;
////
////    @Autowired
////    UserRepository usersRepo;
////    @Autowired
////    AuthorityRepository authRepo;
////    @Autowired
////    PermissionRepository permRepo;
////    @Autowired
////    UserAuthorityRepository userAuthRepo;
////    @Autowired
////    AuthorityPermissionRepository authPermRepo;
////    @Autowired
////    StaatsangehoerigkeitReferenceRepository staatRepo;
////    @Autowired
////    BuergerRepository buergerRepo;
////    @Autowired
////    SachbearbeiterRepository wohnRepo;
////    @Autowired
////    PassRepository sachbearbeiterRepo;
////    @Autowired
////    AdresseInterneRepository interneRepo;
////    @Autowired
////    AdresseExterneRepository externeRepo;
////    @Autowired
////    AdresseReferenceRepository referenceRepo;
////    @Autowired
////    MandantRepository mandantRepo;
////    @Autowired
////    SachbearbeiterRepository sachbearbeiterRepo;
////
////    private String urlSave;
////    private String urlBuergerSave;
////    private String urlNew;
////    private SachbearbeiterResource response;
////    private SearchResultResource responseQuery;
////    private List responseList;
////    private String urlUserSave;
////
////    @Before
////    public void setUp() throws JsonProcessingException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
////
////        authPermRepo.deleteAll();
////        userAuthRepo.deleteAll();
////        usersRepo.deleteAll();
////        authRepo.deleteAll();
////        permRepo.deleteAll();
////        buergerRepo.deleteAll();
////        staatRepo.deleteAll();
////        wohnRepo.deleteAll();
////        passRepo.deleteAll();
////        referenceRepo.deleteAll();
////        interneRepo.deleteAll();
////        externeRepo.deleteAll();
////        sachbearbeiterRepo.deleteAll();
////
////        mandantRepo.deleteAll();
////
////        InitTest initTest = new InitTest(usersRepo, authRepo, permRepo, userAuthRepo, authPermRepo, mandantRepo);
////        initTest.init();
////        SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, new TrustSelfSignedStrategy()).useTLS().build();
////        SSLConnectionSocketFactory connectionFactory = new SSLConnectionSocketFactory(sslContext, new AllowAllHostnameVerifier());
////        BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
////        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials("hans", "test"));
////
////        HttpClient httpClient = HttpClientBuilder.create()
////                .setSSLSocketFactory(connectionFactory)
////                .setDefaultCredentialsProvider(credentialsProvider)
////                .build();
////
////        ClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
////        restTemplate = new RestTemplate(requestFactory);
////
////        ObjectMapper objectMapper = new ObjectMapper();
////        objectMapper.registerModule(new Jackson2HalModule());
////
////        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
////        converter.setObjectMapper(objectMapper);
////        restTemplate.setMessageConverters(Collections.<HttpMessageConverter<?>>singletonList(converter));
////        urlSave = "http://localhost:" + port + "/sachbearbeiter/save";
////        urlBuergerSave = "http://localhost:" + port + "/buerger/save";
////        urlUserSave = "http://localhost:" + port + "/user/save";
////        urlNew = "http://localhost:" + port + "/sachbearbeiter/new";
////
////        sachbearbeiter.setFax("089214589");
////        sachbearbeiter.setFunktion("employee");
////        sachbearbeiter.setOid("10");
////        sachbearbeiter.setFax("089214589");
////        sachbearbeiter.setFunktion("employe");
////        sachbearbeiter2.setOid("11");
////        buerger.setOid("b");
////        buerger.setNachname("hans");
////        buerger.setVorname("max");
////    }
////
////    @Test
////    public void createSachbearbeiterTest() throws JsonProcessingException {
////
////        restTemplate.postForEntity(urlSave, sachbearbeiter, SachbearbeiterResource.class);
////        String URL2 = "http://localhost:" + port + "/sachbearbeiter/10";
////        response = restTemplate.getForEntity(URL2, SachbearbeiterResource.class).getBody();
////        assertEquals("employe", response.getFunktion());
////        assertNotNull(response.getLink("new"));
////        assertNotNull(response.getLink("update"));
////        assertNotNull(response.getLink("copy"));
////        assertNotNull(response.getLink("self"));
////        assertNotNull(response.getLink("delete"));
////
////    }
////
////    @Test
////    public void updateSachbearbeiterTest() {
////
////        Sachbearbeiter sachbearbeiterUpdate = new Sachbearbeiter();
////        sachbearbeiterUpdate.setFunktion("employe");
////        sachbearbeiterUpdate.setOid("10");
////
////        restTemplate.postForEntity(urlSave, sachbearbeiter, SachbearbeiterResource.class).getBody();
////        String URL2 = "http://localhost:" + port + "/sachbearbeiter/10";
////        response = restTemplate.postForEntity(URL2, sachbearbeiterUpdate, SachbearbeiterResource.class).getBody();
////
////        assertEquals("employe", response.getFunktion());
////        assertNotNull(response.getLink("new"));
////        assertNotNull(response.getLink("update"));
////        assertNotNull(response.getLink("copy"));
////        assertNotNull(response.getLink("self"));
////        assertNotNull(response.getLink("delete"));
////
////    }
////
////    @Test
////    public void querySachbearbeiterTest() {
////
////        restTemplate.postForEntity(urlSave, sachbearbeiter, SachbearbeiterResource.class).getBody();
////        String URL3 = "http://localhost:" + port + "/sachbearbeiter/query";
////        responseQuery = restTemplate.getForEntity(URL3, SearchResultResource.class).getBody();
////
////        assertEquals(1, responseQuery.getResult().size());
////        assertNotNull(responseQuery.getLink("query"));
////        assertNotNull(responseQuery.getLink("self"));
////    }
////
////    @Test
////    public void copySachbearbeiterTest() {
////
////        restTemplate.postForEntity(urlSave, sachbearbeiter, SachbearbeiterResource.class).getBody();
////        String URL2 = "http://localhost:" + port + "/sachbearbeiter/copy/10";
////        response = restTemplate.getForEntity(URL2, SachbearbeiterResource.class).getBody();
////
////        assertEquals("employe", response.getFunktion());
////        assertNotNull(response.getLink("new"));
////        assertNotNull(response.getLink("update"));
////        assertNotNull(response.getLink("copy"));
////        assertNotNull(response.getLink("self"));
////        assertNotNull(response.getLink("delete"));
////    }
////
////    @Test
////    public void deleteSachbearbeiterTest() {
////
////        restTemplate.postForEntity(urlSave, sachbearbeiter, SachbearbeiterResource.class).getBody();
////        String URL2 = "http://localhost:" + port + "/sachbearbeiter/10";
////        restTemplate.delete(URL2, sachbearbeiter);
////        String URL3 = "http://localhost:" + port + "/sachbearbeiter/query";
////        responseQuery = restTemplate.getForEntity(URL3, SearchResultResource.class).getBody();
////
////        assertEquals(true, responseQuery.getResult().isEmpty());
////
////    }
////
////    @Test
////    public void newSachbearbeiterTest() throws JsonProcessingException {
////
////        response = restTemplate.getForEntity(urlNew, SachbearbeiterResource.class).getBody();
////        assertNotNull(response.getLink("new"));
////        assertNotNull(response.getLink("save"));
////        assertNotNull(response.getOid());
////
////    }
////
////    @Test
////    public void sachbearbeiterBuergerTest() {
////        Buerger b = new Buerger();
////        b.setOid("b2");
////        b.setNachname("ali");
////        restTemplate.postForEntity(urlSave, sachbearbeiter, SachbearbeiterResource.class).getBody();
////
////        restTemplate.postForEntity(urlBuergerSave, b, BuergerResource.class);
////
////        String URL3 = "http://localhost:" + port + "/sachbearbeiter/create/buerger/10";
////        restTemplate.postForEntity(URL3, buerger, SachbearbeiterResource.class).getBody();
////
////        String URL5 = "http://localhost:" + port + "/sachbearbeiter/add/sachbearbeiter/10/buerger/b2";
////        restTemplate.getForEntity(URL5, SachbearbeiterResource.class).getBody();
////        
////        /*Test methode readSachbearbeiterBuerger*/
//////        String urlReleaseSachbearbeiterBuerger = "http://localhost:" + port + "/sachbearbeiter/release/buerger/10";
//////        restTemplate.getForEntity(urlReleaseSachbearbeiterBuerger, SachbearbeiterResource.class);
//////        String urlReadSachbearbeiterBuerger = "http://localhost:" + port + "/sachbearbeiter/buerger/10";
//////        responseList = restTemplate.getForEntity(urlReadSachbearbeiterBuerger, List.class).getBody();
//////        assertEquals(0, responseList.size());
////        /*Test Delete Buerger*/
//////        String urlBuergerDelete = "http://localhost:" + port + "/buerger/b2";
//////        restTemplate.delete(urlBuergerDelete);
////        /*Test Delete Buerger*/
////        String urlSachbearbeiterDelete = "http://localhost:" + port + "/sachbearbeiter/10";
////        restTemplate.delete(urlSachbearbeiterDelete);
////        
////        String urlBuergerDelete2 = "http://localhost:" + port + "/buerger/b";
////        restTemplate.delete(urlBuergerDelete2);
////
////    }
////
////    @Test
////    public void sachbearbeiterUserTest() {
////        User user = new User();
////        user.setOid("user1");
////        user.setUsername("hans");
////        User user1 = new User();
////        user1.setOid("user2");
////        user1.setUsername("max");
////        restTemplate.postForEntity(urlSave, sachbearbeiter, SachbearbeiterResource.class).getBody();
////
////        restTemplate.postForEntity(urlUserSave, user, UserResource.class);
////        //restTemplate.postForEntity(urlBuergerSave, buerger, BuergerResource.class);
////
////        String URL3 = "http://localhost:" + port + "/sachbearbeiter/create/user/10";
////        restTemplate.postForEntity(URL3, user1, SachbearbeiterResource.class).getBody();
////
////        String URL5 = "http://localhost:" + port + "/sachbearbeiter/add/sachbearbeiter/10/user/user1";
////        restTemplate.getForEntity(URL5, SachbearbeiterResource.class).getBody();
////
////
////        /*Test methode readSachbearbeiterBuerger*/
////        String urlReadSachbearbeiterBuerger = "http://localhost:" + port + "/sachbearbeiter/user/10";
////        UserResource responseUser = restTemplate.getForEntity(urlReadSachbearbeiterBuerger, UserResource.class).getBody();
////        assertEquals("hans", responseUser.getUsername());
////        String urlUserDelete = "http://localhost:" + port + "/user/user1";
////        restTemplate.delete(urlUserDelete);
////
////    }
////
////    @After
////    public void TearDown() {
////        sachbearbeiterRepo.deleteAll();
////        authPermRepo.deleteAll();
////        userAuthRepo.deleteAll();
////        usersRepo.deleteAll();
////        authRepo.deleteAll();
////        permRepo.deleteAll();
////        buergerRepo.deleteAll();
////        staatRepo.deleteAll();
////        wohnRepo.deleteAll();
////        passRepo.deleteAll();
////        referenceRepo.deleteAll();
////        interneRepo.deleteAll();
////        externeRepo.deleteAll();
////        mandantRepo.deleteAll();
////
////    }
////
////}
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
//import de.muenchen.demo.service.domain.Sachbearbeiter;
//import de.muenchen.demo.service.domain.SachbearbeiterRepository;
//import de.muenchen.demo.service.domain.Staatsangehoerigkeit;
//import de.muenchen.demo.test.service.DomainConstants;
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
//import static org.junit.Assert.assertFalse;
//import static org.junit.Assert.assertTrue;
//
///**
// *
// * @author praktikant.tmar
// */
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringApplicationConfiguration(classes = Application.class)
//@WebIntegrationTest({"server.port=0", "management.port=0"})
//public class SachbearbeiterTest {
//
//    @Rule
//    public ExpectedException thrown = ExpectedException.none();
//    @Rule
//    public WireMockRule wireMockRule = new WireMockRule(8089);
//    @Autowired
//    SachbearbeiterRepository repo;
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
//        urlSave = "http://localhost:" + port + "/sachbearbeiter/save";
//        urlNew = "http://localhost:" + port + "/sachbearbeiter/new";
//
//    }
//
//    @Test
//    @WithMockUser(username = DomainConstants.M2_U001_NAME)
//
//    public void newSachbearbeiterTest() throws JsonProcessingException {
//        System.out.println("========== create Sachbearbeiter Test ==========");
////TODO
////        response = restTemplate.getForEntity(urlNew, SachbearbeiterResource.class).getBody();
////        assertNotNull(response.getLink("new"));
////        assertNotNull(response.getLink("save"));
////        assertNotNull(response.getOid());
////        assertNull(repo.findFirstByOidAndMandantOid(response.getOid(), DomainConstants.M2));
////        System.out.println(String.format("Sachbearbeiter wurde mit neuer OID '%s' erstellt (und nicht in der DB gespeichert)", response.getOid()));
//
//    }
//
//    private Sachbearbeiter createSachbearbeiter(String oid) {
//        Sachbearbeiter sachbearbeiter = new Sachbearbeiter();
//        sachbearbeiter.setOid(oid);
//        sachbearbeiter.setFax("0895236987");
//        sachbearbeiter.setFunktion("director");
//        return sachbearbeiter;
//    }
//
//    @Test
//    public void saveSachbearbeiterTest() throws JsonProcessingException {
//        System.out.println("========== save Sachbearbeiter Test ==========");
//        String oid = "OIDTEST";
//        //TODO
////        response = restTemplate.postForEntity(urlSave, this.createSachbearbeiter(oid), SachbearbeiterResource.class).getBody();
////        assertNotNull(response);
////        assertEquals(DomainConstants.M2, response.getMandant().getOid());
////        assertNotNull(response.getLink(HateoasUtil.REL_NEW));
////        assertNotNull(response.getLink(HateoasUtil.REL_UPDATE));
////        assertNotNull(response.getLink(HateoasUtil.REL_COPY));
////        assertNotNull(response.getLink(HateoasUtil.REL_SELF));
////        assertNotNull(response.getLink(HateoasUtil.REL_DELETE));
////
////        System.out.println(String.format("Sachbearbeiter wurde mit Mandant '%s' in der DB gespeichert.", response.getMandant().getOid()));
//    }
//
//    @Test
//    public void updateSachbearbeiterTest() throws JsonProcessingException {
//        System.out.println("========== update Sachbearbeiter Test ==========");
//        String oid = "OIDUPDATE";
//        Sachbearbeiter p1 = this.createSachbearbeiter(oid);
//        //TODO
////        response = restTemplate.postForEntity(urlSave, p1, SachbearbeiterResource.class).getBody();
////        p1.setOrganisationseinheit("SA001");
////        String urlUpdate = "http://localhost:" + port + "/sachbearbeiter/" + oid;
////        restTemplate.put(urlUpdate, p1);
////        String URL11 = "http://localhost:" + port + "/sachbearbeiter/" + oid;
////        response = restTemplate.getForEntity(URL11, SachbearbeiterResource.class).getBody();
////        assertEquals(response.getOrganisationseinheit(), "SA001");
////        assertEquals(DomainConstants.M2, response.getMandant().getOid());
//
//        System.out.println("Sachbearbeiter wurde mit neuem Vornamen in der DB gespeichert.");
//    }
//
//    @Test
//    public void readSachbearbeiterTest() throws JsonProcessingException {
//
//        System.out.println("========== read Sachbearbeiter Test ==========");
//        String URL11 = "http://localhost:" + port + "/sachbearbeiter/" + DomainConstants.M2_SA003;
//
//        //TODO
////        response = restTemplate.getForEntity(URL11, SachbearbeiterResource.class).getBody();
////        assertNotNull(response);
////        assertNotNull(response.getLink(HateoasUtil.REL_NEW));
////        assertNotNull(response.getLink(HateoasUtil.REL_UPDATE));
////        assertNotNull(response.getLink(HateoasUtil.REL_COPY));
////        assertNotNull(response.getLink(HateoasUtil.REL_SELF));
////        assertNotNull(response.getLink(HateoasUtil.REL_DELETE));
////
////        System.out.println(String.format("Sachbearbeiter konnte mit OID '%s' aus der DB gelesen werden.", response.getOid()));
//    }
//
//    @Test
//    public void copySachbearbeiterTest() {
//        System.out.println("========== copy Sachbearbeiter Test ==========");
//
//        String URL2 = "http://localhost:" + port + "/sachbearbeiter/copy/" + DomainConstants.M2_SA008;
//        //TODO
////        response = restTemplate.getForEntity(URL2, SachbearbeiterResource.class).getBody();
////
////        assertNotNull(response);
////        assertNotNull(response.getLink(HateoasUtil.REL_NEW));
////        assertNotNull(response.getLink(HateoasUtil.REL_UPDATE));
////        assertNotNull(response.getLink(HateoasUtil.REL_COPY));
////        assertNotNull(response.getLink(HateoasUtil.REL_SELF));
////        assertNotNull(response.getLink(HateoasUtil.REL_DELETE));
//
//        //       System.out.println(String.format("Objekt mit der OID '%s' konnte erfolgreich in Objekt '%s' kopiert (und in DB gespeichert) werden", DomainConstants.M2_SA005, response.getOid()));
//
//    }
//
//    /*@Test
//    public void copyListSachbearbeiterTest() {
//        System.out.println("========== copy Liste Sachbearbeiter Test ==========");
//        int x = this.count(DomainConstants.M2);
//        String URL2 = "http://localhost:" + port + "/sachbearbeiter/copy";
//        ArrayList<String> oids = new ArrayList();
//        oids.add(DomainConstants.M2_SA014);
//        oids.add(DomainConstants.M2_SA015);
//        //restTemplate.postForEntity(URL2, oids, SachbearbeiterResource.class);
//        String URL11 = "http://localhost:" + port + "/sachbearbeiter/query";
//        SearchResultResource queryResponse = restTemplate.getForEntity(URL11, SearchResultResource.class).getBody();
//        assertEquals(x + 2, queryResponse.getResult().size());
//        System.out.println(String.format("Objekte mit der OID '%s' und der OID '%s' konnte erfolgreich in Objekt  kopiert (und in DB gespeichert) werden", DomainConstants.M2_SA014, DomainConstants.M2_SA015));
//
//    }*/
//
//    @Test
//    @WithMockUser(username = DomainConstants.M2_U001_NAME)
//    public void sachbearbeiterDeleteTest() {
//        System.out.println("========== delete Sachbearbeiter Test ==========");
//        String URL11 = "http://localhost:" + port + "/sachbearbeiter/" + DomainConstants.M2_SA035;
////        response = restTemplate.getForEntity(URL11, SachbearbeiterResource.class).getBody();
////        assertNotNull(response);
////        restTemplate.delete(URL11);
////        thrown.expect(org.springframework.web.client.HttpServerErrorException.class);
////        thrown.expectMessage(equalTo("500 Internal Server Error"));
////        response = restTemplate.getForEntity(URL11, SachbearbeiterResource.class).getBody();
//
//        System.out.println(String.format("Sachbearbeiter konnte mit OID '%s' aus der DB (und dem Cache) gelöscht werden.", DomainConstants.M2_SA004));
//
//    }
//
//    @Test
//    public void sachbearbeiterDeleteListTest() {
//        System.out.println("========== delete Sachbearbeiter Test ==========");
//
//        String URL2 = "http://localhost:" + port + "/sachbearbeiter";
//        ArrayList<String> oids = new ArrayList();
//        oids.add(DomainConstants.M2_SA020);
//        oids.add(DomainConstants.M2_SA019);
//        //TODO
////        restTemplate.postForEntity(URL2, oids, SachbearbeiterResource.class);
////        String URL11 = "http://localhost:" + port + "/sachbearbeiter/" + DomainConstants.M2_SA020;
////        thrown.expect(org.springframework.web.client.HttpServerErrorException.class);
////        thrown.expectMessage(equalTo("500 Internal Server Error"));
////        response = restTemplate.getForEntity(URL11, SachbearbeiterResource.class).getBody();
////        assertNull(response);
//        System.out.println(String.format("Sachbearbeiter  mit OID '%s'und OID '%s' konnte aus der DB (und dem Cache) gelöscht werden.", DomainConstants.M2_SA020, DomainConstants.M2_SA019));
//
//    }
//
//   /* @Test
//    public void querySachbearbeiterTest() throws JsonProcessingException {
//
//        System.out.println("========== query Sachbearbeiter Test Mandant 2 ==========");
//        int x = this.count(DomainConstants.M2);
//        String URL11 = "http://localhost:" + port + "/sachbearbeiter/query";
//        SearchResultResource queryResponse = restTemplate.getForEntity(URL11, SearchResultResource.class).getBody();
//        assertEquals(x, queryResponse.getResult().size());
//        assertNotNull(null, queryResponse.getLink("self"));
//        assertNotNull(null, queryResponse.getLink("query"));
//        System.out.println(String.format("Suche wurde erfolgreich durchgeführt. Einträge Mandant %s: %s | Ergebnis der Suche: %s", DomainConstants.M2, x, queryResponse.getResult().size()));
//
//    }*/
//
//    /*private int count(String mid) {
//        ArrayList<Sachbearbeiter> all = Lists.newArrayList(repo.findAll());
//        return all.stream().filter(b -> b.getMandant().getOid().equals(mid)).collect(Collectors.counting()).intValue();
//    }*/
//    
//     @Test
//    public void readSachbearbeiterUserTest() {
//        System.out.println("========== read Sachbearbeiter User Test ==========");
//        String URL1 = "http://localhost:" + port + "/sachbearbeiter/user/" + DomainConstants.M2_SA036;
//// TODO       UserResource staat = restTemplate.getForEntity(URL1, UserResource.class).getBody();
////        assertNotNull(staat);
//        System.out.println(String.format("die User von dem Sachbearbeiter mit OID '%s' konnten aus der DB gelesen werden.", DomainConstants.M2_SA036));
//
//    }
//
//    private boolean checkUser(String sachbearbeiterOid, String UserOid) {
//        //TODO return service.read(sachbearbeiterOid).getUser().getOid().equals(UserOid);
//        return false;
//    }
//
//    @Test
//    @WithMockUser(username = DomainConstants.M2_U001_NAME)
//    public void AddSachbearbeiterUserTest() {
//        System.out.println("========== Add Sachbearbeiter Staatsanghörigkeit Test ==========");
//        assertFalse(this.checkUser(DomainConstants.M2_SA037, "oid11"));
//        String URL2 = "http://localhost:" + port + "/sachbearbeiter/add/" + DomainConstants.M2_SA037 + "/user";
//        //TODO restTemplate.postForEntity(URL2, "oid11", SachbearbeiterResource.class).getBody();
//        assertTrue(this.checkUser(DomainConstants.M2_SA037, "oid11"));
//        System.out.println(String.format("die User mit OID '%s' könnte zu dem Sachbearbeiter mit OID '%s' hinzufügt werden.", "oid11", DomainConstants.M2_SA037));
//
//    }
//
//}