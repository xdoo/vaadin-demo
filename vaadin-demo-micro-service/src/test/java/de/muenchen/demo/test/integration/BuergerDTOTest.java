package de.muenchen.demo.test.integration;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import de.muenchen.demo.service.MicroServiceApplication;
import de.muenchen.demo.service.gen.domain.Augenfarben;
import de.muenchen.demo.service.gen.domain.Buerger;
import de.muenchen.demo.service.gen.rest.BuergerRepository;
import de.muenchen.service.QueryService;
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
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.hal.Jackson2HalModule;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.security.oauth2.common.exceptions.UserDeniedAuthorizationException;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.HttpClientErrorException;

import java.net.MalformedURLException;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * @author praktikant.tmar
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MicroServiceApplication.class)
@WebIntegrationTest({"server.port=8080", "management.port=8080"})
public class BuergerDTOTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Autowired
    BuergerRepository repo;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    QueryService queryService;

    /**
     * First init lock
     **/
    Boolean lock = false;
    /**
     * Helper for holding current TEST URL
     **/
    private String url;

    /**
     * Templates for REST access
     **/
    private OAuth2RestTemplate restTemplate;
    private OAuth2RestTemplate restTemplate2;

    /**
     * Necessary properties
     **/
    @Value("${local.server.port:8080}")
    private int port;
    @Value("${service.token.url}")
    private String TOKEN_URL;
    @Value("${security.oauth2.client.id}")
    private String clientID;
    @Value("${security.oauth2.client.scope:defaultScope}")
    private Set<String> scopes;


    @Before
    public void setUp() throws JsonProcessingException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {

        if (lock == false) {

            // Init RestTemplate Hans1 -----------------------------
            ResourceOwnerPasswordResourceDetails resource = new ResourceOwnerPasswordResourceDetails();
            resource.setUsername("admin1");
            resource.setPassword("admin1");
            resource.setGrantType("password");
            resource.setClientId(clientID);
            resource.setAccessTokenUri(TOKEN_URL);
//            resource.setScope(Arrays.asList("scope"));

            restTemplate = new OAuth2RestTemplate(resource, new DefaultOAuth2ClientContext());

            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());


            MappingJackson2HttpMessageConverter halConverter = new MappingJackson2HttpMessageConverter();

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new Jackson2HalModule());

            halConverter.setObjectMapper(objectMapper);
            halConverter.setSupportedMediaTypes(MediaType.parseMediaTypes(MediaTypes.HAL_JSON_VALUE));

            restTemplate.setMessageConverters(Arrays.asList(
                    new StringHttpMessageConverter(Charset.forName("UTF-8")),
                    halConverter
            ));
            //-------------------------------------------------------

            // Init RestTemplate Hans2 ------------------------------
            ResourceOwnerPasswordResourceDetails resource2 = new ResourceOwnerPasswordResourceDetails();
            resource2.setUsername("admin2");
            resource2.setPassword("admin2");
            resource2.setGrantType("password");
            resource2.setClientId(clientID);
            resource2.setAccessTokenUri(TOKEN_URL);

            restTemplate2 = new OAuth2RestTemplate(resource2);

            restTemplate2.setRequestFactory(new HttpComponentsClientHttpRequestFactory());

            restTemplate2.setMessageConverters(Arrays.asList(
                    halConverter,
                    new StringHttpMessageConverter()
            ));
            //-------------------------------------------------------

            //INIT Repo ---------------------------------------------
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken("admin1", "admin1");
            Authentication auth = authenticationManager.authenticate(token);
            SecurityContextHolder.getContext().setAuthentication(auth);

            Buerger b1M1 = new Buerger();
            Set<String> eig1 = new HashSet<>();
            eig1.add("eig");
            b1M1.setEigenschaften(eig1);
            b1M1.setNachname("name1");
            b1M1.setVorname("vorname1");
            b1M1.setAlive(true);
            b1M1.setMandant("m1");
            b1M1.setGeburtsdatum(new Date(System.currentTimeMillis()));
            b1M1.setAugenfarbe(Augenfarben.blau);
            b1M1.setOid(1L);
            b1M1.setGeburtsdatum(new Date());
            repo.save(b1M1);
            Buerger b2M1 = new Buerger();
            Set<String> eig2 = new HashSet<>();
            eig2.add("eig");
            b2M1.setEigenschaften(eig2);
            b2M1.setNachname("name2");
            b2M1.setVorname("vorname2");
            b2M1.setAlive(true);
            b2M1.setMandant("m1");
            b2M1.setAugenfarbe(Augenfarben.grün);
            b2M1.setOid(2L);
            b2M1.setGeburtsdatum(new Date());
            repo.save(b2M1);
            Buerger b3M1 = new Buerger();
            Set<String> eig3 = new HashSet<>();
            eig3.add("eig");
            b3M1.setEigenschaften(eig3);
            b3M1.setNachname("name3");
            b3M1.setVorname("vorname3");
            b3M1.setAlive(true);
            b3M1.setMandant("m1");
            b3M1.setAugenfarbe(Augenfarben.braun);
            b3M1.setOid(3L);
            b3M1.setGeburtsdatum(new Date());
            repo.save(b3M1);
            Buerger b4M1 = new Buerger();
            Set<String> eig4 = new HashSet<>();
            eig4.add("eig");
            b4M1.setEigenschaften(eig4);
            b4M1.setNachname("name4");
            b4M1.setVorname("vorname4");
            b4M1.setAlive(true);
            b4M1.setMandant("m1");
            b4M1.setAugenfarbe(Augenfarben.rot);
            b4M1.setOid(4L);
            b4M1.getKinder().add(b3M1);
            b4M1.setGeburtsdatum(new Date());
            repo.save(b4M1);
            Buerger b5M1 = new Buerger();
            Set<String> eig5 = new HashSet<>();
            eig5.add("eig");
            b5M1.setEigenschaften(eig5);
            b5M1.setNachname("name5");
            b5M1.setVorname("vorname5");
            b5M1.setAlive(true);
            b5M1.setMandant("m1");
            b5M1.setAugenfarbe(Augenfarben.braun);
            b5M1.setOid(5L);
            b5M1.setGeburtsdatum(new Date());
            repo.save(b5M1);
            UsernamePasswordAuthenticationToken token2 = new UsernamePasswordAuthenticationToken("admin2", "admin2");
            Authentication auth2 = authenticationManager.authenticate(token2);
            SecurityContextHolder.getContext().setAuthentication(auth2);
            Buerger b1M2 = new Buerger();
            Set<String> eig6 = new HashSet<>();
            eig6.add("eig");
            b1M2.setEigenschaften(eig6);
            b1M2.setNachname("name1");
            b1M2.setVorname("vorname1");
            b1M2.setAlive(true);
            b1M2.setMandant("m2");
            b1M2.setAugenfarbe(Augenfarben.blau);
            b1M2.setOid(6L);
            b1M2.setGeburtsdatum(new Date());
            repo.save(b1M2);
            Buerger b2M2 = new Buerger();
            Set<String> eig7 = new HashSet<>();
            eig7.add("eig");
            b2M2.setEigenschaften(eig7);
            b2M2.setNachname("name2");
            b2M2.setVorname("vorname2");
            b2M2.setAlive(true);
            b2M2.setMandant("m2");
            b2M2.setAugenfarbe(Augenfarben.grün);
            b2M2.setOid(7L);
            b2M2.setGeburtsdatum(new Date());
            repo.save(b2M2);
            Buerger b3M2 = new Buerger();
            Set<String> eig8 = new HashSet<>();
            eig8.add("eig");
            b3M2.setEigenschaften(eig8);
            b3M2.setNachname("name3");
            b3M2.setVorname("vorname3");
            b3M2.setAlive(true);
            b3M2.setMandant("m2");
            b3M2.setAugenfarbe(Augenfarben.rot);
            b3M2.setOid(8L);
            b3M2.setGeburtsdatum(new Date());
            repo.save(b3M2);
            Buerger b4M2 = new Buerger();
            Set<String> eig9 = new HashSet<>();
            eig9.add("eig");
            b4M2.setEigenschaften(eig9);
            b4M2.setNachname("name4");
            b4M2.setVorname("vorname4");
            b4M2.setAlive(true);
            b4M2.setMandant("m2");
            b4M2.setAugenfarbe(Augenfarben.blau);
            b4M2.setOid(9L);
            b4M2.setGeburtsdatum(new Date());
            repo.save(b4M2);
            Buerger b5M2 = new Buerger();
            Set<String> eig10 = new HashSet<>();
            eig10.add("eig");
            b5M2.setEigenschaften(eig10);
            b5M2.setNachname("name5");
            b5M2.setVorname("vorname5");
            b5M2.setAlive(true);
            b5M2.setMandant("m2");
            b5M2.setAugenfarbe(Augenfarben.braun);
            b5M2.setOid(10L);
            b5M2.setGeburtsdatum(new Date());
            repo.save(b5M2);
            //-------------------------------------------------------

            lock = true;

        }
    }

    @Test
    public void queryBuergsByFuzzySearch() {
        List<Buerger> buergers = queryService.query("September", Buerger.class, new String[]{"vorname", "nachname", "augenfarbe", "geburtsdatum"});
        System.out.println(buergers);
    }

    @Test
    public void getBuergersM1Test() throws JsonProcessingException {
        System.out.println("========== get Alle Bürger Mandant 'm1' Test ==========");
        int x = this.count("m1", "admin1", "admin1");
        url = "http://localhost:" + port + "/buergers";
        ResponseEntity<PagedResources<BuergerResource>> result = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                BuergerResource.LIST
        );
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(x, result.getBody().getContent().size());
        assertFalse(result.getBody().getContent().isEmpty());
        System.out.println(String.format("Suche wurde erfolgreich durchgeführt. Einträge Mandant 'm1': %s | Ergebnis der Suche: %s", x, result.getBody().getContent().size()));
    }

    @Test
    public void getBuergersM2Test() throws JsonProcessingException {
        System.out.println("========== get Alle Bürger Mandant 'm2' Test ==========");
        int x = this.count("m2", "admin2", "admin2");
        url = "http://localhost:" + port + "/buergers";
        ResponseEntity<PagedResources<BuergerResource>> result = restTemplate2.exchange(
                url,
                HttpMethod.GET,
                null,
                BuergerResource.LIST
        );
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(x, result.getBody().getContent().size());
        System.out.println(String.format("Suche wurde erfolgreich durchgeführt. Einträge Mandant 'm2': %s | Ergebnis der Suche: %s", x, result.getBody().getContent().size()));
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
        buerger.setAugenfarbe(Augenfarben.blau);
        buerger.setGeburtsdatum(new Date());
        buerger.setAlive(true);
        Set<String> eig = new HashSet<>();
        eig.add("eig");
        buerger.setEigenschaften(eig);
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
        assertNotNull(result.getBody().getLink("staatsangehoerigkeiten"));
        assertNotNull(result.getBody().getLink("wohnungen"));
        assertNotNull(result.getBody().getLink("paesse"));
        assertNotNull(result.getBody().getLink("self"));
        System.out.println(String.format("Bürger mit Mandant 'm1' wurde von der DB gelesen."));

    }

    @Test(expected = UserDeniedAuthorizationException.class)
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
            System.out.println(String.format("Bürger mit OID : 5 und Mandant 'm1' wurde nicht von der DB gelöscht."));
        }
    }

    @Test(expected = UserDeniedAuthorizationException.class)
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

    @Test(expected = UserDeniedAuthorizationException.class)
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

    @Test(expected = UserDeniedAuthorizationException.class)
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
        ResponseEntity<PagedResources<BuergerResource>> result = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                BuergerResource.LIST
        );
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertFalse(result.getBody().getContent().isEmpty());
        System.out.println("Kinder von den Bürger mit OID : 2001 wurden von in der DB gelesen.");

    }

    @Test(expected = UserDeniedAuthorizationException.class)
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
        ResponseEntity<PagedResources<BuergerResource>> result2 = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                BuergerResource.LIST
        );

        assertEquals(HttpStatus.OK, result2.getStatusCode());
        assertEquals(1, result2.getBody().getContent().size());
        System.out.println("Kind mit oid 2 wurde zu dem BuergerDTO mit Oid 1 hinzufügt.");
    }

    @Test(expected = UserDeniedAuthorizationException.class)
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

    public static final ParameterizedTypeReference<PagedResources<BuergerResource>> LIST = new ParameterizedTypeReference<PagedResources<BuergerResource>>() {};

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
