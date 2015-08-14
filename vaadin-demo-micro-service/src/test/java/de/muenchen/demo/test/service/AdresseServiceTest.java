/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.demo.test.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import de.muenchen.demo.service.Application;
import de.muenchen.demo.service.domain.AdresseExterneRepository;
import de.muenchen.demo.service.domain.AdresseInterneRepository;
import de.muenchen.demo.service.domain.AdresseReferenceRepository;
import de.muenchen.demo.service.domain.AuthorityPermissionRepository;
import de.muenchen.demo.service.domain.AuthorityRepository;
import de.muenchen.demo.service.domain.Adresse;
import de.muenchen.demo.service.domain.MandantRepository;
import de.muenchen.demo.service.domain.PermissionRepository;
import de.muenchen.demo.service.domain.UserAuthorityRepository;
import de.muenchen.demo.service.domain.UserRepository;
import de.muenchen.demo.service.rest.api.AdresseResource;
import de.muenchen.vaadin.demo.api.rest.SearchResultResource;
import de.muenchen.demo.service.services.AdresseService;
import de.muenchen.demo.test.integration.InitTest;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author praktikant.tmar
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest({"server.port=0", "management.port=0"})
public class AdresseServiceTest {

    @Autowired
    AdresseService service;

    private final Adresse adresse2 = new Adresse();
    private final Adresse adresse = new Adresse();
    private final Adresse adresse3 = new Adresse();
    private final Adresse adresse4 = new Adresse();
    private final Adresse adresse5 = new Adresse();

    @JsonProperty("result")
    private SearchResultResource<AdresseResource> response;
    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8089);

    @Autowired
    UserRepository usersRepo;
    @Autowired
    AuthorityRepository authRepo;
    @Autowired
    PermissionRepository permRepo;
    @Autowired
    UserAuthorityRepository userAuthRepo;
    @Autowired
    AuthorityPermissionRepository authPermRepo;
    @Autowired
    MandantRepository mandantRepo;
    @Autowired
    AdresseInterneRepository interneRepo;
    @Autowired
    AdresseExterneRepository externeRepo;
    @Autowired
    AdresseReferenceRepository referenceRepo;
    @Autowired
    //@Qualifier("authenticationManager")
    AuthenticationManager authenticationManager;

    @Before
    public void setUp() throws JsonProcessingException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {

        authPermRepo.deleteAll();
        userAuthRepo.deleteAll();
        usersRepo.deleteAll();
        authRepo.deleteAll();
        permRepo.deleteAll();
        referenceRepo.deleteAll();
        interneRepo.deleteAll();
        externeRepo.deleteAll();
        mandantRepo.deleteAll();

        InitTest initTest = new InitTest(usersRepo, authRepo, permRepo, userAuthRepo, authPermRepo, mandantRepo);
        initTest.init();
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken("hans", "test");
        Authentication auth = authenticationManager.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(auth);

        ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally

        List adresses = new ArrayList();

        adresse.setOid("123");
        adresse.setStrasse("Donau-Schwaben-Str");
        adresse.setHausnummer("18");
        adresse.setStadt("Passau");
        adresse.setPlz(96034);

        adresse2.setOid("12");
        adresse2.setStrasse("ampfing strasse");
        adresse2.setHausnummer("15");
        adresse2.setStadt("Passau");
        adresse2.setPlz(80331);
        adresses.add(adresse2);

        adresse3.setOid("1234");
        adresse3.setStrasse("ampfing strasse");
        adresse3.setHausnummer("16");
        adresse3.setStadt("Passau");
        adresse3.setPlz(80331);
        adresses.add(adresse3);

        adresse5.setOid("aa");
        adresse5.setStrasse("ampfing strasse");
        adresse5.setHausnummer("16");
        adresse5.setStadt("Passau");
        adresse5.setPlz(96452);

        adresse4.setStrasse("goethe strasse");
        adresse4.setStrasseReference("goeth2500");
        adresse4.setStadt("München");
        adresse4.setPlz(80331);
        adresses.add(adresse4);

        String json80331 = mapper.writeValueAsString(adresses);
        String jsonAdresse = mapper.writeValueAsString(adresse4);
        adresse4.setHausnummer("16");
        adresse4.setOid("12345");

        stubFor(get(urlEqualTo("/adresse/80331")).willReturn(
                aResponse().withHeader("Content-Type", "application/json")
                .withBody(json80331)
        ));
        stubFor(get(urlEqualTo("/adresse/goeth2500")).willReturn(
                aResponse().withHeader("Content-Type", "application/json")
                .withBody(jsonAdresse)
        ));
    }

    @Test
    public void createTest() throws JsonProcessingException {

        Adresse a = service.create();
        assertNotEquals(null, a.getOid());
    }

    @Test
    public void saveTest() throws JsonProcessingException {

        Adresse externe = service.save(adresse);
        assertNotEquals(null, externe.getOid());
        Adresse interne = service.save(adresse4);
        assertNotEquals(null, interne.getOid());

    }

    @Test
    public void updateTest() throws JsonProcessingException {
        Adresse ad = service.save(adresse);
        ad.setOid("12");
        Adresse a = service.update(ad);
        assertEquals(ad.getHausnummer(), a.getHausnummer());

    }

    @Test
    public void readTest() throws JsonProcessingException {
        Adresse externe = service.save(adresse);
        assertNotEquals(null, externe.getOid());
        Adresse interne = service.save(adresse4);
        assertNotEquals(null, interne.getOid());
        Adresse i = service.read("123");
        assertEquals("123", i.getOid());
        Adresse e = service.read("12345");
        assertEquals("12345", e.getOid());

    }

    @Test
    public void deleteTest() throws JsonProcessingException {
        service.save(adresse);
        service.delete("123");
        Adresse a = service.read("123");
        assertEquals(null, a);
    }

    @Test
    public void queryTest() throws JsonProcessingException {
        service.save(adresse);
        service.save(adresse4);
        List<Adresse> a = service.query();
        assertEquals(2, a.size());
    }

    @Test
    public void copyTest() throws JsonProcessingException {
        Adresse ad = service.save(adresse);
        Adresse a = service.copy("123");
        assertNotEquals(a.getOid(), ad.getOid());
    }

    @After
    public void TearDown() {
        authPermRepo.deleteAll();
        userAuthRepo.deleteAll();
        usersRepo.deleteAll();
        authRepo.deleteAll();
        permRepo.deleteAll();
        referenceRepo.deleteAll();
        interneRepo.deleteAll();
        externeRepo.deleteAll();
        mandantRepo.deleteAll();

    }
}
