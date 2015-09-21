package de.muenchen.demo.test.integration;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import de.muenchen.demo.service.Application;
import de.muenchen.demo.service.domain.Buerger;
import de.muenchen.demo.service.domain.BuergerRepository;
import de.muenchen.vaadin.demo.api.domain.Augenfarbe;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.hal.Jackson2HalModule;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.MalformedURLException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

/**
 *
 * @author praktikant.tmar
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest({"server.port=0", "management.port=0"})
public class BuergerDTOTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();
    @Autowired
    BuergerRepository repo;
    @Autowired
    AuthenticationManager authenticationManager;
    Boolean lock = false;
    private RestTemplate restTemplate;
    private RestTemplate restTemplate2;
    @Value("${local.server.port}")
    private int port;
    private String url;
//    @AfterClass
//    public 

    @Before
    public void setUp() throws JsonProcessingException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials("hans", "test"));

        HttpClient httpClient = HttpClientBuilder.create()
                .setDefaultCredentialsProvider(credentialsProvider)
                .build();

        ClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        restTemplate = new RestTemplate(requestFactory);

        MappingJackson2HttpMessageConverter halConverter = new MappingJackson2HttpMessageConverter();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new Jackson2HalModule());

        halConverter.setObjectMapper(objectMapper);
        halConverter.setSupportedMediaTypes(Arrays.asList(MediaTypes.HAL_JSON));

        restTemplate.setMessageConverters(Arrays.asList(
                halConverter,
                new StringHttpMessageConverter()
        ));
        BasicCredentialsProvider credentialsProvider2 = new BasicCredentialsProvider();
        credentialsProvider2.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials("hans2", "test2"));

        HttpClient httpClient2 = HttpClientBuilder.create()
                .setDefaultCredentialsProvider(credentialsProvider2)
                .build();

        ClientHttpRequestFactory requestFactory2 = new HttpComponentsClientHttpRequestFactory(httpClient2);
        restTemplate2 = new RestTemplate(requestFactory2);

        restTemplate2.setMessageConverters(Arrays.asList(
                halConverter,
                new StringHttpMessageConverter()
        ));

        if (lock == false) {
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken("hans", "test");
            Authentication auth = authenticationManager.authenticate(token);
            SecurityContextHolder.getContext().setAuthentication(auth);
            Buerger b1M1 = new Buerger();
            b1M1.setNachname("name1");
            b1M1.setVorname("vorname1");
            b1M1.setMandant("test");
            b1M1.setAugenfarbe(Augenfarbe.Blau);
            b1M1.setOid(1L);
            repo.save(b1M1);
            Buerger b2M1 = new Buerger();
            b2M1.setNachname("name2");
            b2M1.setVorname("vorname2");
            b2M1.setMandant("test");
            b2M1.setAugenfarbe(Augenfarbe.Grün);
            b2M1.setOid(2L);
            repo.save(b2M1);
            Buerger b3M1 = new Buerger();
            b3M1.setNachname("name3");
            b3M1.setVorname("vorname3");
            b3M1.setMandant("test");
            b3M1.setAugenfarbe(Augenfarbe.Braun);
            b3M1.setOid(3L);
            repo.save(b3M1);
            Buerger b4M1 = new Buerger();
            b4M1.setNachname("name4");
            b4M1.setVorname("vorname4");
            b4M1.setMandant("test");
            b4M1.setAugenfarbe(Augenfarbe.Rot);
            b4M1.setOid(4L);
            b4M1.getKinder().add(b3M1);
            repo.save(b4M1);
            Buerger b5M1 = new Buerger();
            b5M1.setNachname("name5");
            b5M1.setVorname("vorname5");
            b5M1.setMandant("test");
            b5M1.setAugenfarbe(Augenfarbe.Braun);
            b5M1.setOid(5L);
            repo.save(b5M1);
            UsernamePasswordAuthenticationToken token2 = new UsernamePasswordAuthenticationToken("hans2", "test2");
            Authentication auth2 = authenticationManager.authenticate(token2);
            SecurityContextHolder.getContext().setAuthentication(auth2);
            Buerger b1M2 = new Buerger();
            b1M2.setNachname("name1");
            b1M2.setVorname("vorname1");
            b1M2.setMandant("default");
            b1M2.setAugenfarbe(Augenfarbe.Blau);
            b1M2.setOid(6L);
            repo.save(b1M2);
            Buerger b2M2 = new Buerger();
            b2M2.setNachname("name2");
            b2M2.setVorname("vorname2");
            b2M2.setMandant("default");
            b2M2.setAugenfarbe(Augenfarbe.Grün);
            b2M2.setOid(7L);
            repo.save(b2M2);
            Buerger b3M2 = new Buerger();
            b3M2.setNachname("name3");
            b3M2.setVorname("vorname3");
            b3M2.setMandant("default");
            b3M2.setAugenfarbe(Augenfarbe.Rot);
            b3M2.setOid(8L);
            repo.save(b3M2);
            Buerger b4M2 = new Buerger();
            b4M2.setNachname("name4");
            b4M2.setVorname("vorname4");
            b4M2.setMandant("default");
            b4M2.setAugenfarbe(Augenfarbe.Blau);
            b4M2.setOid(9L);
            repo.save(b4M2);
            Buerger b5M2 = new Buerger();
            b5M2.setNachname("name5");
            b5M2.setVorname("vorname5");
            b5M2.setMandant("default");
            b5M2.setAugenfarbe(Augenfarbe.Braun);
            b5M2.setOid(10L);
            repo.save(b5M2);
            lock = true;
        }
    }

    @Test
    public void getBuergersM1Test() throws JsonProcessingException {
        System.out.println("========== get Alle Bürger Mandant 'test' Test ==========");
        int x = this.count("test", "hans", "test");
        url = "http://localhost:" + port + "/buergers";
        ResponseEntity<Resources<BuergerResource>> result = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                BuergerResource.LIST
        );
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(x, result.getBody().getContent().size());
        assertFalse(result.getBody().getContent().isEmpty());
        System.out.println(String.format("Suche wurde erfolgreich durchgeführt. Einträge Mandant 'test': %s | Ergebnis der Suche: %s", x, result.getBody().getContent().size()));
    }

    @Test
    public void getBuergersM2Test() throws JsonProcessingException {
        System.out.println("========== get Alle Bürger Mandant 'default' Test ==========");
        int x = this.count("default", "hans2", "test2");
        url = "http://localhost:" + port + "/buergers";
        ResponseEntity<Resources<BuergerResource>> result = restTemplate2.exchange(
                url,
                HttpMethod.GET,
                null,
                BuergerResource.LIST
        );
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(x, result.getBody().getContent().size());
        System.out.println(String.format("Suche wurde erfolgreich durchgeführt. Einträge Mandant 'default': %s | Ergebnis der Suche: %s", x, result.getBody().getContent().size()));
    }

    private int count(String mid, String name, String password) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(name, password);
        Authentication auth = authenticationManager.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(auth);
        ArrayList<Buerger> all = Lists.newArrayList(repo.findAll());
        return all.stream().filter(b -> b.getMandant().equals(mid)).collect(Collectors.counting()).intValue();
    }

    @Test
    public void postBuergerM1Test() throws JsonProcessingException {
        System.out.println("========== save Bürger M1 Test ==========");
        url = "http://localhost:" + port + "/buergers";
        ResponseEntity<BuergerResource> response2 = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(this.createBuerger()), BuergerResource.class);
        assertEquals(HttpStatus.CREATED, response2.getStatusCode());
        assertNotNull(response2.getBody().getContent());
        System.out.println(String.format("Bürger wurde in der DB gespeichert."));
    }

    private Buerger createBuerger() {
        Buerger buerger = new Buerger();
        buerger.setNachname("hans");
        buerger.setVorname("peter");
        buerger.setAugenfarbe(Augenfarbe.Blau);
        return buerger;
    }

    @Test
    public void getBuergerM1Test() throws JsonProcessingException {
        System.out.println("========== get Bürger Test ==========");
        url = "http://localhost:" + port + "/buergers/1";
        ResponseEntity<BuergerResource> result = restTemplate.getForEntity(url, BuergerResource.class);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody().getLink("kinder"));
        assertNotNull(result.getBody().getLink("sachbearbeiter"));
        assertNotNull(result.getBody().getLink("partner"));
        assertNotNull(result.getBody().getLink("staatsangehoerigkeitReferences"));
        assertNotNull(result.getBody().getLink("wohnungen"));
        assertNotNull(result.getBody().getLink("pass"));
        assertNotNull(result.getBody().getLink("self"));
        System.out.println(String.format("Bürger mit Mandant 'test' wurde von der DB gelesen."));

    }

    @Test(expected = HttpClientErrorException.class)
    public void getBuergerM2Test() throws JsonProcessingException {
        System.out.println("========== get Bürger Test ==========");
        url = "http://localhost:" + port + "/buergers/1";
            restTemplate2.getForEntity(url, BuergerResource.class);
    }

    @Test
    public void deleteBuergerM1Test() throws JsonProcessingException {
        System.out.println("========== delete Bürger Test ==========");
        url = "http://localhost:" + port + "/buergers/2";
        restTemplate.delete(url);
        try {
            restTemplate.getForEntity(url, Buerger.class);
            assertNull(1);

        } catch (HttpClientErrorException e) {
            assertNotNull(e);
            System.out.println(String.format("Bürger mit OID : 5 und Mandant 'test' wurde nicht von der DB gelöscht."));
        }
    }

    @Test(expected = HttpClientErrorException.class)
    public void deleteBuergerM2Test() throws JsonProcessingException {
        System.out.println("========== delete Bürger Test ==========");
            url = "http://localhost:" + port + "/buergers/5";
            restTemplate2.delete(url);
    }

    @Test
    public void putBuergerM1Test() throws JsonProcessingException {
        System.out.println("========== put Bürger Test ==========");
        url = "http://localhost:" + port + "/buergers/3";
        BuergerResource response2 = restTemplate.getForEntity(url, BuergerResource.class).getBody();
        Buerger buerger = response2.getContent();
        buerger.setVorname("peter");
        restTemplate.put(url, buerger);
        BuergerResource response3 = restTemplate.getForEntity(url, BuergerResource.class).getBody();
        assertEquals("peter", response3.getContent().getVorname());
        System.out.println("Bürger wurde mit neuem Vornamen in der DB gespeichert.");

    }

    @Test(expected = HttpClientErrorException.class)
    public void putBuergerM2Test() throws JsonProcessingException {
        System.out.println("========== put Bürger Test ==========");
        url = "http://localhost:" + port + "/buergers/4";
        BuergerResource response4 = restTemplate.getForEntity(url, BuergerResource.class).getBody();
        Buerger buerger4 = response4.getContent();
        buerger4.setVorname("hans");
            restTemplate2.put(url, buerger4);
    }

    @Test
    public void patchBuergerM1Test() throws JsonProcessingException, MalformedURLException {
        System.out.println("========== patch Bürger M1 Test ==========");
        url = "http://localhost:" + port + "/buergers/1";
        Map m = new HashMap();
        m.put("vorname", "peter");
        ResponseEntity<BuergerResource> response2 = restTemplate.exchange(url, HttpMethod.PATCH, new HttpEntity(m), BuergerResource.class);
        assertEquals("peter", response2.getBody().getContent().getVorname());
        System.out.println("Bürger wurde mit neuem Vornamen in der DB gespeichert.");
    }

    @Test(expected = HttpClientErrorException.class)
    public void patchBuergerM2Test() throws JsonProcessingException, MalformedURLException {
        System.out.println("========== patch Bürger Test ==========");
        Map m = new HashMap();
        m.put("vorname", "peter");
            url = "http://localhost:" + port + "/buergers/4";
            restTemplate2.exchange(url, HttpMethod.PATCH, new HttpEntity(m), BuergerResource.class);
    }

    @Test
    public void getBuergerKinderM1Test() throws JsonProcessingException {
        System.out.println("========== get Bürger Kinder Test ==========");
        url = "http://localhost:" + port + "/buergers/4/kinder";
        ResponseEntity<Resources<BuergerResource>> result = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                BuergerResource.LIST
        );
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertFalse(result.getBody().getContent().isEmpty());
        System.out.println("Kinder von den Bürger mit OID : 2001 wurden von in der DB gelesen.");

    }

    @Test(expected = HttpClientErrorException.class)
    public void getBuergerKinderM2Test() throws JsonProcessingException {
        System.out.println("========== get Bürger Kinder Test ==========");
        url = "http://localhost:" + port + "/buergers/2/kinder";
            restTemplate2.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    BuergerResource.LIST
            );
    }

    @Test
    public void putBuergerKinderM1Test() {
        System.out.println("========== put Bürger Kinder Test ==========");
        url = "http://localhost:" + port + "/buergers/3/kinder";
        String uri = "http://localhost:" + port + "/buergers/1";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("text", "uri-list"));
        restTemplate.put(url, new HttpEntity<>(uri, headers));
        ResponseEntity<Resources<BuergerResource>> result2 = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                BuergerResource.LIST
        );

        assertEquals(HttpStatus.OK, result2.getStatusCode());
        assertEquals(1, result2.getBody().getContent().size());
        System.out.println("Kind mit oid 2 wurde zu dem BuergerDTO mit Oid 1 hinzufügt.");
    }

    @Test(expected = HttpClientErrorException.class)
    public void putBuergerKinderM2Test() {
        System.out.println("========== put Bürger Kinder M2 Test ==========");
        url = "http://localhost:" + port + "/buergers/3/kinder";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("text", "uri-list"));
            String uri2 = "http://localhost:" + port + "/buergers/6";
            restTemplate2.put(url, new HttpEntity<>(uri2, headers));
    }

}

class BuergerResource extends Resource<Buerger> {

    public static final ParameterizedTypeReference<Resources<BuergerResource>> LIST = new ParameterizedTypeReference<Resources<BuergerResource>>() {
    };

    public BuergerResource() {
        super(new Buerger());
    }

    public BuergerResource(Buerger entity) {
        super(entity);
    }

    public BuergerResource(Buerger entity, Link... links) {
        super(entity, links);
    }
}
