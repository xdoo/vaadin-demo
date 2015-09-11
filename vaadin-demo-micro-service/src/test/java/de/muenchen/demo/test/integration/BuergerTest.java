package de.muenchen.demo.test.integration;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import de.muenchen.demo.service.Application;
import de.muenchen.demo.service.domain.Buerger;
import de.muenchen.demo.service.domain.BuergerRepository;
import de.muenchen.demo.service.domain.Staatsangehoerigkeit;
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
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import com.sun.jndi.toolkit.url.Uri;
import de.muenchen.vaadin.demo.api.rest.SearchResultResource;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import static org.hamcrest.CoreMatchers.equalTo;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.UriTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.request.WebRequest;

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
    @Value("${local.server.port}")
    private int port;
    private String url;

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
        converter.setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_JSON));
        converter.setObjectMapper(objectMapper);

        restTemplate.setMessageConverters(Arrays.asList(converter));

    }

    @Test
    public void getBuergersTest() throws JsonProcessingException {
        System.out.println("========== get Alle Bürger Test ==========");
        url = "http://localhost:" + port + "/buergers";
        ResponseEntity<Resources<BuergerResource>> result = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                BuergerResource.LIST
        );
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody().getLink("search"));
        assertFalse(result.getBody().getContent().isEmpty());
        System.out.println("Alle Bürger wurde von der DB gelesen.");
    }

    private Buerger createBuerger(String oid) {
        Buerger buerger = new Buerger();
        buerger.setOid(oid);
        buerger.setNachname("hans");
        buerger.setVorname("peter");
        return buerger;
    }

    @Test
    public void postBuergerTest() throws JsonProcessingException {
        System.out.println("========== save Bürger Test ==========");
        String oid = "OIDTEST";
        url = "http://localhost:" + port + "/buergers";
        //ResponseEntity<Buerger> response2 = restTemplate.postForEntity(url, this.createBuerger(oid), Buerger.class);
        ResponseEntity<BuergerResource> response2 = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(this.createBuerger(oid)), BuergerResource.class);

        assertEquals(HttpStatus.CREATED, response2.getStatusCode());
        System.out.println(String.format("Bürger wurde in der DB gespeichert."));
    }

    @Test
    public void getBuergerTest() throws JsonProcessingException {
        System.out.println("========== get Bürger Test ==========");
        url = "http://localhost:" + port + "/buergers/1";
        ResponseEntity<BuergerResource> result = restTemplate.getForEntity(url, BuergerResource.class);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody().getLink("kinder"));
        assertNotNull(result.getBody().getLink("sachbearbeiter"));
        assertNotNull(result.getBody().getLink("mandant"));
        assertNotNull(result.getBody().getLink("partner"));
        assertNotNull(result.getBody().getLink("staatsangehoerigkeitReferences"));
        assertNotNull(result.getBody().getLink("wohnungen"));
        assertNotNull(result.getBody().getLink("pass"));
        assertNotNull(result.getBody().getLink("self"));

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
    public void putBuergerKinderTest() throws URISyntaxException, MalformedURLException {
        System.out.println("========== put Bürger Kinder Test ==========");
        url = "http://localhost:" + port + "/buergers/1/kinder";

        Uri uri = new Uri("http://localhost:" + port + "/buergers/2");
        //String uri = "http://localhost:" + port + "/buergers/1";
        //UriTemplate expanded = new UriTemplate("http://localhost:" + port + "/buergers/1");

        restTemplate.put(url, uri);
        //restTemplate.exchange(url,HttpMethod.PUT, new HttpEntity(uri), );
        ResponseEntity<Resources<BuergerResource>> result2 = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                BuergerResource.LIST
        );

        System.out.println(result2.getBody().getContent().size());
        assertEquals(HttpStatus.OK, result2.getStatusCode());
        //assertEquals(1, result2.getBody().getContent().size());
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
