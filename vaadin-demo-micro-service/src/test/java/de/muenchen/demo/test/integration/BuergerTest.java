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
import org.springframework.hateoas.hal.Jackson2HalModule;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 *
 * @author praktikant.tmar
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest({"server.port=0", "management.port=0"})
public class BuergerTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();
    @Autowired
    BuergerRepository repo;
    private RestTemplate restTemplate;
    private RestTemplate restTemplate2;
    @Value("${local.server.port}")
    private int port;
    private String url;
    @Autowired
    AuthenticationManager authenticationManager;

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

    }

    @Test
    public void getBuergersM1Test() throws JsonProcessingException {
        System.out.println("========== get Alle Bürger Mandant 'test' Test ==========");
        int x = this.count("test","hans","test");
        url = "http://localhost:" + port + "/buergers";
        ResponseEntity<Resources<BuergerResource>> result = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                BuergerResource.LIST
        );
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(x,result.getBody().getContent().size());
        assertFalse(result.getBody().getContent().isEmpty());
        System.out.println(String.format("Suche wurde erfolgreich durchgeführt. Einträge Mandant 'test': %s | Ergebnis der Suche: %s", x, result.getBody().getContent().size()));
    }

    @Test
    public void getBuergersM2Test() throws JsonProcessingException {
        System.out.println("========== get Alle Bürger Mandant 'default' Test ==========");
                int x = this.count("default","hans2","test2");
        url = "http://localhost:" + port + "/buergers";
        ResponseEntity<Resources<BuergerResource>> result = restTemplate2.exchange(
                url,
                HttpMethod.GET,
                null,
                BuergerResource.LIST
        );
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(x,result.getBody().getContent().size());
        System.out.println(String.format("Suche wurde erfolgreich durchgeführt. Einträge Mandant 'default': %s | Ergebnis der Suche: %s", x, result.getBody().getContent().size()));
    }

    private int count(String mid,String name, String password) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(name, password);
        Authentication auth = authenticationManager.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(auth);
        ArrayList<Buerger> all = Lists.newArrayList(repo.findAll());
        return all.stream().filter(b -> b.getMandant().equals(mid)).collect(Collectors.counting()).intValue();
    }
    


    @Test
    public void postBuergerTest() throws JsonProcessingException {
        System.out.println("========== save Bürger Test ==========");
        String oid = "OIDTEST";
        url = "http://localhost:" + port + "/buergers";
        ResponseEntity<BuergerResource> response2 = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(this.createBuerger(oid)), BuergerResource.class);

        assertEquals(HttpStatus.CREATED, response2.getStatusCode());
        System.out.println(String.format("Bürger wurde in der DB gespeichert."));
    }
    
    private Buerger createBuerger(String oid) {
        Buerger buerger = new Buerger();
        buerger.setOid(oid);
        buerger.setNachname("hans");
        buerger.setVorname("peter");
        return buerger;
    }
    @Test
    public void getBuergerTest() throws JsonProcessingException {
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
        thrown.expect(org.springframework.web.client.HttpClientErrorException.class);
        thrown.expectMessage(equalTo("403 Forbidden"));
        ResponseEntity<BuergerResource> result2 = restTemplate2.getForEntity(url, BuergerResource.class);
        assertEquals(HttpStatus.OK, result2.getStatusCode());
        assertNotNull(result2.getBody().getLink("kinder"));

        System.out.println(String.format("Bürger wurde von der DB gelesen."));
    }

    @Test
    public void deleteBuergerTest() throws JsonProcessingException {
        System.out.println("========== delete Bürger Test ==========");
        url = "http://localhost:" + port + "/buergers/2";
        restTemplate.delete(url);
        thrown.expect(org.springframework.web.client.HttpClientErrorException.class);
        thrown.expectMessage(equalTo("404 Not Found"));
        ResponseEntity<Buerger> response2 = restTemplate.getForEntity(url, Buerger.class);
        assertEquals(HttpStatus.NOT_FOUND, response2.getStatusCode());
        System.out.println(String.format("Bürger wurde in der DB gelöscht."));
    }

    @Test
    public void putBuergerTest() throws JsonProcessingException {
        System.out.println("========== put Bürger Test ==========");
        url = "http://localhost:" + port + "/buergers/3";
        BuergerResource response2 = restTemplate.getForEntity(url, BuergerResource.class).getBody();
        Buerger buerger = response2.getContent();
        buerger.setVorname("peter");
        restTemplate.put(url, buerger);
        BuergerResource response3 = restTemplate.getForEntity(url, BuergerResource.class).getBody();
        assertEquals("peter", response3.getContent().getVorname());
        
        BuergerResource response4 = restTemplate.getForEntity(url, BuergerResource.class).getBody();
        Buerger buerger4 = response4.getContent();
        buerger4.setVorname("hans");
        thrown.expect(org.springframework.web.client.HttpClientErrorException.class);
        thrown.expectMessage(equalTo("403 Forbidden"));
        restTemplate2.put(url, buerger4);
        
        BuergerResource response5 = restTemplate2.getForEntity(url, BuergerResource.class).getBody();
        assertEquals("peter", response5.getContent().getVorname());
        System.out.println("Bürger wurde mit neuem Vornamen in der DB gespeichert.");
    }

    @Test
    public void getBuergerKinderTest() throws JsonProcessingException {
        System.out.println("========== get Bürger Kinder Test ==========");
        url = "http://localhost:" + port + "/buergers/2002/kinder";
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

    @Test
    public void putBuergerKinderTest() {
        System.out.println("========== put Bürger Kinder Test ==========");
        url = "http://localhost:" + port + "/buergers/3/kinder";
        String uri = "http://localhost:" + port + "/buergers/1";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("text", "uri-list"));
        restTemplate.put(url, new HttpEntity<>(uri, headers));
        //String uri = "http://localhost:" + port + "/buergers/1";
        //UriTemplate expanded = new UriTemplate("http://localhost:" + port + "/buergers/1");

//        restTemplate.put(url, uri);
        //restTemplate.exchange(url,HttpMethod.PUT, new HttpEntity(uri), );
        ResponseEntity<Resources<BuergerResource>> result2 = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                BuergerResource.LIST
        );

        System.out.println(result2.getBody().getContent().size());
        assertEquals(HttpStatus.OK, result2.getStatusCode());
        assertEquals(1, result2.getBody().getContent().size());
        System.out.println("Bürger wurde mit neuem Vornamen in der DB gespeichert.");
    }

    @Test
    public void patchBuergerKinderTest() throws JsonProcessingException, MalformedURLException {
        System.out.println("========== put Bürger Kinder Test ==========");
        url = "http://localhost:" + port + "/buergers/1";
        Map m = new HashMap();
        m.put("vorname", "peter");
        restTemplate.exchange(url, HttpMethod.PATCH, new HttpEntity(m), Buerger.class);
        ResponseEntity<BuergerResource> response2 = restTemplate.getForEntity(url, BuergerResource.class);
        assertEquals("peter", response2.getBody().getContent().getVorname());

        System.out.println("Bürger wurde mit neuem Vornamen in der DB gespeichert.");
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
