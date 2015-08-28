package de.muenchen.demo.test.integration;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import de.muenchen.demo.service.Application;
import de.muenchen.demo.service.domain.Staatsangehoerigkeit;
import de.muenchen.demo.test.service.DomainConstants;
import de.muenchen.vaadin.demo.api.rest.SearchResultResource;
import de.muenchen.vaadin.demo.api.rest.StaatsangehoerigkeitResource;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import javax.net.ssl.SSLContext;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
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
public class StaatsangehoerigkeitTest {

    private RestTemplate restTemplate = new TestRestTemplate();
    @Value("${local.server.port}")
    private int port;
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    public StaatsangehoerigkeitTest() {
    }
    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8089);


    @Before
    public void setUp() throws JsonProcessingException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {

        ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally
        Staatsangehoerigkeit staatCreate = new Staatsangehoerigkeit();
        staatCreate.setCode("it");
        staatCreate.setLand("Italien");
        staatCreate.setSprache("Italienisch");
        staatCreate.setReference("OID_CCREATE");

        String jsonCreate = mapper.writeValueAsString(staatCreate);

        stubFor(get(urlEqualTo("/staat/OID_CCREATE")).willReturn(
                aResponse().withHeader("Content-Type", "application/json")
                .withBody(jsonCreate)
        ));
        Staatsangehoerigkeit staat10 = new Staatsangehoerigkeit();
        staat10.setCode("it");
        staat10.setLand("Italien");
        staat10.setSprache("Italienisch");
        staat10.setReference(DomainConstants.M2_S011);

        String json10 = mapper.writeValueAsString(staat10);

        stubFor(get(urlEqualTo("/staat/" + DomainConstants.M2_S011)).willReturn(
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
        staat4.setReference(DomainConstants.M2_S016);

        String json4 = mapper.writeValueAsString(staat4);

        stubFor(get(urlEqualTo("/staat/" + DomainConstants.M2_S016)).willReturn(
                aResponse().withHeader("Content-Type", "application/json")
                .withBody(json4)
        ));

        SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, new TrustSelfSignedStrategy()).useTLS().build();
        SSLConnectionSocketFactory connectionFactory = new SSLConnectionSocketFactory(sslContext, new AllowAllHostnameVerifier());
        BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials("hans", "test"));

        HttpClient httpClient = HttpClientBuilder.create()
                .setSSLSocketFactory(connectionFactory)
                .setDefaultCredentialsProvider(credentialsProvider)
                .build();

        ClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        restTemplate = new RestTemplate(requestFactory);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new Jackson2HalModule());

        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(objectMapper);
        restTemplate.setMessageConverters(Collections.<HttpMessageConverter<?>>singletonList(converter));

    }

    @Test
    @WithMockUser(username = DomainConstants.M2_U001_NAME)
    public void readTest() throws JsonProcessingException, AuthenticationException {
        System.out.println("========== create StaatsangehoerigkeitRefence Test ==========");
        String URL = "http://localhost:" + port + "/staat/" + DomainConstants.M2_S007;
        StaatsangehoerigkeitResource staat2 = restTemplate.getForEntity(URL, StaatsangehoerigkeitResource.class).getBody();
        assertNotNull(staat2);
        assertNotNull(null, staat2.getLink("self"));
        assertNotNull(null, staat2.getLink("delete"));
        System.out.println(String.format("Staatsangehoerigkeit konnte mit ReferenceOid '%s' aus der DB gelesen werden.", staat2.getReference()));

    }

    @Test
    @WithMockUser(username = DomainConstants.M2_U001_NAME)
    public void deleteTest() throws JsonProcessingException, AuthenticationException {
        System.out.println("========== delete StaatsangehoerigkeitRefence Test ==========");
        String urlRead = "http://localhost:" + port + "/staat/" + DomainConstants.M2_S015;
        StaatsangehoerigkeitResource staat = restTemplate.getForEntity(urlRead, StaatsangehoerigkeitResource.class).getBody();
        assertNotNull(staat);
        String URL = "http://localhost:" + port + "/staat/" + DomainConstants.M2_S015;
        restTemplate.delete(URL);
        thrown.expect(org.springframework.web.client.HttpServerErrorException.class);
        thrown.expectMessage(equalTo("500 Internal Server Error"));
        StaatsangehoerigkeitResource staat2 = restTemplate.getForEntity(urlRead, StaatsangehoerigkeitResource.class).getBody();
        assertNull(staat2);
        System.out.println(String.format("Staatsangehoerigkeit konnte mit ReferenceOid '%s' aus der DB (und dem Cache) gelöscht werden.", DomainConstants.M2_S010));

    }

    @Test
    public void createReadStaatsangehoerigkeitTest() throws JsonProcessingException {

        System.out.println("========== create StaatsangehoerigkeitRefence Test ==========");
        String URL2 = "http://localhost:" + port + "/staat/create/OID_CCREATE";
        StaatsangehoerigkeitResource staat = restTemplate.getForEntity(URL2, StaatsangehoerigkeitResource.class).getBody();
        assertNotNull(staat);
        System.out.println(String.format("Staatsangehoerigkeit wurde mit neuer ReferenceOid '%s' erstellt (und in der DB gespeichert)", staat.getReference()));

    }
 @Test
    public void readBuergerStaatsangehoerigkeitTest() {
        System.out.println("========== read Bürger Staatsangehoerigkeit Test ==========");
        String URL1 = "http://localhost:" + port + "/staat/buerger/" + DomainConstants.M2_S008;
        SearchResultResource responseList = restTemplate.getForEntity(URL1, SearchResultResource.class).getBody();
        assertFalse(responseList.getResult().isEmpty());
        System.out.println(String.format("die Staatsangehoerigkeit von dem Bürger mit OID '%s' konnten aus der DB gelesen werden.", DomainConstants.M2_B007));

    }
@Test
    public void releaseStaatsangehoerigkeitBuergerTest() {
        System.out.println("========== release Staatsangehoerigkeit Buerger Test ==========");
        String urlBuerger = "http://localhost:" + port + "/staat/buerger/" + DomainConstants.M2_S016;
        SearchResultResource responseListBuerger = restTemplate.getForEntity(urlBuerger, SearchResultResource.class).getBody();
        assertFalse(responseListBuerger.getResult().isEmpty());
        String urlReleaseBuerger = "http://localhost:" + port + "/staat/release/buerger/" + DomainConstants.M2_S016;
        restTemplate.getForEntity(urlReleaseBuerger, StaatsangehoerigkeitResource.class);
        SearchResultResource responseListBuerger2 = restTemplate.getForEntity(urlBuerger, SearchResultResource.class).getBody();
        assertTrue(responseListBuerger2.getResult().isEmpty());
        System.out.println(String.format("release operation für den Buerger einer Staatsangehoerigkeit mit OID '%s' konnte aus der DB erfolgreich durchgeführt.", DomainConstants.M2_S016));
    }

}
