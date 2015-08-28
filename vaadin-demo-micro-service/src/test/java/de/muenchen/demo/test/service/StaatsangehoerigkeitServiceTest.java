/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.demo.test.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import de.muenchen.demo.service.Application;
import de.muenchen.demo.service.domain.Staatsangehoerigkeit;
import de.muenchen.demo.service.domain.StaatsangehoerigkeitReference;
import de.muenchen.demo.service.domain.StaatsangehoerigkeitReferenceRepository;
import de.muenchen.demo.service.services.StaatsangehoerigkeitService;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.List;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.cache.CacheManager;
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
public class StaatsangehoerigkeitServiceTest {

    @Autowired
    StaatsangehoerigkeitService service;
    @Autowired
    StaatsangehoerigkeitReferenceRepository repo;
    @Autowired
    CacheManager cacheManager;
    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8089);
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() throws JsonProcessingException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {

        ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally
        Staatsangehoerigkeit staatCreate = new Staatsangehoerigkeit();
        staatCreate.setCode("it");
        staatCreate.setLand("Italien");
        staatCreate.setSprache("Italienisch");
        staatCreate.setReference("OID_CREATE");

        String jsonCreate = mapper.writeValueAsString(staatCreate);

        stubFor(get(urlEqualTo("/staat/OID_CREATE")).willReturn(
                aResponse().withHeader("Content-Type", "application/json")
                .withBody(jsonCreate)
        ));
        Staatsangehoerigkeit staat10 = new Staatsangehoerigkeit();
        staat10.setCode("it");
        staat10.setLand("Italien");
        staat10.setSprache("Italienisch");
        staat10.setReference(DomainConstants.M2_S010);

        String json10 = mapper.writeValueAsString(staat10);

        stubFor(get(urlEqualTo("/staat/" + DomainConstants.M2_S010)).willReturn(
                aResponse().withHeader("Content-Type", "application/json")
                .withBody(json10)
        ));
        Staatsangehoerigkeit staat = new Staatsangehoerigkeit();
        staat.setCode("it");
        staat.setLand("Italien");
        staat.setSprache("Italienisch");
        staat.setReference(DomainConstants.M2_S007);

        String json = mapper.writeValueAsString(staat);

        stubFor(get(urlEqualTo("/staat/" + DomainConstants.M2_S007)).willReturn(
                aResponse().withHeader("Content-Type", "application/json")
                .withBody(json)
        ));

        Staatsangehoerigkeit staat2 = new Staatsangehoerigkeit();
        staat2.setCode("fr");
        staat2.setLand("Frankreich");
        staat2.setSprache("französisch");
        staat2.setReference(DomainConstants.M2_S008);

        String json2 = mapper.writeValueAsString(staat2);

        stubFor(get(urlEqualTo("/staat/" + DomainConstants.M2_S008)).willReturn(
                aResponse().withHeader("Content-Type", "application/json")
                .withBody(json2)
        ));

        Staatsangehoerigkeit staat3 = new Staatsangehoerigkeit();
        staat3.setCode("tn");
        staat3.setLand("Tunesien");
        staat3.setSprache("Arabisch");
        staat3.setReference(DomainConstants.M2_S015);

        String json3 = mapper.writeValueAsString(staat3);

        stubFor(get(urlEqualTo("/staat/" + DomainConstants.M2_S015)).willReturn(
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

    }

    @Test
    @WithMockUser(username = DomainConstants.M2_U001_NAME)
    public void createTest() throws JsonProcessingException {
        System.out.println("========== create StaatsangehoerigkeitRefence Test ==========");
        Staatsangehoerigkeit a = service.create("OID_CREATE");
        assertNotNull(a.getReference());
        System.out.println(String.format("Staatsangehoerigkeit wurde mit neuer ReferenceOid '%s' erstellt (und nicht in der DB gespeichert)", a.getReference()));

    }

    @Test
    @WithMockUser(username = DomainConstants.M2_U001_NAME)
    public void readTest() throws JsonProcessingException, AuthenticationException {
        System.out.println("========== create StaatsangehoerigkeitRefence Test ==========");
        Staatsangehoerigkeit a = service.read(DomainConstants.M2_S007);
        assertNotNull(a);
        System.out.println(String.format("Staatsangehoerigkeit konnte mit ReferenceOid '%s' aus der DB gelesen werden.", a.getReference()));

    }

    @Test
    @WithMockUser(username = DomainConstants.M2_U001_NAME)
    public void deleteTest() throws JsonProcessingException, AuthenticationException {
        System.out.println("========== create StaatsangehoerigkeitRefence Test ==========");
        service.delete("OIDM2_STAATS010");
        Staatsangehoerigkeit a = service.read(DomainConstants.M2_S010);
        assertEquals(null, a);
        System.out.println(String.format("Staatsangehoerigkeit konnte mit ReferenceOid '%s' aus der DB (und dem Cache) gelöscht werden.", DomainConstants.M2_S010));

    }

    @Test
    @WithMockUser(username = DomainConstants.M2_U001_NAME)
    public void readReferenceTest() throws JsonProcessingException, AuthenticationException {
        System.out.println("========== read StaatsangehoerigkeitRefence Test ==========");
        StaatsangehoerigkeitReference a = service.readReference(DomainConstants.M2_S007);
        assertNotNull(a);
        System.out.println(String.format("StaatsangehoerigkeitRefence konnte mit OID '%s' aus der DB gelesen werden.", a.getReferencedOid()));

    }

}
