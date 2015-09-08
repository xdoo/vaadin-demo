/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.demo.test.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import de.muenchen.demo.service.Application;
import de.muenchen.demo.service.domain.Adresse;
import de.muenchen.demo.service.domain.AdresseReference;
import de.muenchen.demo.service.domain.AdresseReferenceRepository;
import de.muenchen.demo.test.service.DomainConstants;
import de.muenchen.vaadin.demo.api.hateoas.HateoasUtil;
import de.muenchen.vaadin.demo.api.rest.AdresseResource;
import de.muenchen.vaadin.demo.api.rest.SearchResultResource;
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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.hateoas.hal.Jackson2HalModule;
import org.springframework.http.ResponseEntity;
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
public class AdresseTest {

    private RestTemplate restTemplate;
    @Value("${local.server.port}")
    private int port;
    @Autowired
    AdresseReferenceRepository repo;
    private String urlNew;
    ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally

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

        urlNew = "http://localhost:" + port + "/adresse/new";
    }

    @Test
    public void sucheAdresseTest() throws JsonProcessingException {
        System.out.println("========== suche Adresse Test ==========");

        String URL = "http://localhost:" + port + "/adresse/suche";

//Suche adresse Interne mit Plz 80331 
        Adresse adr = new Adresse();
        adr.setStrasseReference(209);
        ResponseEntity<List> responseAdresse;
        responseAdresse = restTemplate.postForEntity(URL, adr, List.class);
        assertNotNull(responseAdresse);
        System.out.println(String.format("Suche wurde erfolgreich durchgeführt. Ergebnis der Suche: %s", responseAdresse.getBody().size()));

    }

    @Test
    @WithMockUser(username = DomainConstants.M2_U001_NAME)

    public void newAdresseTest() throws JsonProcessingException {
        System.out.println("========== create Adresse Test ==========");

        AdresseResource response = restTemplate.getForEntity(urlNew, AdresseResource.class).getBody();
        assertNotNull(response.getLink("new"));
        assertNotNull(response.getOid());
        assertNull(repo.findFirstByOidAndMandantOid(response.getOid(), DomainConstants.M2));
        System.out.println(String.format("Adresse wurde mit neuer OID '%s' erstellt (und nicht in der DB gespeichert)", response.getOid()));

    }

    @Test
    public void queryAdresseTest() throws JsonProcessingException {
        System.out.println("========== query Pass Test Mandant 2 ==========");
        int x = this.count(DomainConstants.M2);
        String URL11 = "http://localhost:" + port + "/adresse/query";
        SearchResultResource queryResponse = restTemplate.getForEntity(URL11, SearchResultResource.class).getBody();
        assertEquals(x, queryResponse.getResult().size());
        assertNotNull(null, queryResponse.getLink("self"));
        assertNotNull(null, queryResponse.getLink("query"));
        System.out.println(String.format("Suche wurde erfolgreich durchgeführt. Einträge Mandant %s: %s | Ergebnis der Suche: %s", DomainConstants.M2, x, queryResponse.getResult().size()));

    }

    private int count(String mid) {
        ArrayList<AdresseReference> all = Lists.newArrayList(repo.findAll());
        return all.stream().filter(b -> b.getMandant().getOid().equals(mid)).collect(Collectors.counting()).intValue();
    }
@Test
    public void readAdresseTest() throws JsonProcessingException {

        System.out.println("========== read Adresse Test ==========");
        String URL11 = "http://localhost:" + port + "/adresse/" + DomainConstants.M2_AR006;
        AdresseResource response = restTemplate.getForEntity(URL11, AdresseResource.class).getBody();
        assertNotNull(response);
        assertNotNull(response.getLink(HateoasUtil.REL_NEW));
        assertNotNull(response.getLink(HateoasUtil.REL_SELF));        

        System.out.println(String.format("Adresse konnte mit OID '%s' aus der DB gelesen werden.", response.getOid()));
    }

}
