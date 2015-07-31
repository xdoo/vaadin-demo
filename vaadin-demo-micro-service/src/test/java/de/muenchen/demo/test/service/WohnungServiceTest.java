/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.demo.test.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.muenchen.demo.service.Application;
import de.muenchen.demo.service.domain.AdresseExterneRepository;
import de.muenchen.demo.service.domain.AdresseInterneRepository;
import de.muenchen.demo.service.domain.AdresseReferenceRepository;
import de.muenchen.demo.service.domain.AuthorityPermissionRepository;
import de.muenchen.demo.service.domain.AuthorityRepository;
import de.muenchen.demo.service.domain.Wohnung;
import de.muenchen.demo.service.domain.WohnungRepository;
import de.muenchen.demo.service.domain.MandantRepository;
import de.muenchen.demo.service.domain.PermissionRepository;
import de.muenchen.demo.service.domain.UserAuthorityRepository;
import de.muenchen.demo.service.domain.UserRepository;
import de.muenchen.demo.service.services.WohnungService;
import de.muenchen.demo.test.InitTest;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import org.apache.http.auth.AuthenticationException;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import org.junit.Before;
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
public class WohnungServiceTest {

    @Autowired
    WohnungService service;

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
    WohnungRepository wohnungRepo;

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
        wohnungRepo.deleteAll();
        mandantRepo.deleteAll();
        InitTest initTest = new InitTest(usersRepo, authRepo, permRepo, userAuthRepo, authPermRepo, mandantRepo);
        initTest.init();
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken("hans", "test");
        Authentication auth = authenticationManager.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Test
    public void createTest() throws JsonProcessingException {

        Wohnung a = service.create();
        assertNotEquals(null, a.getOid());
    }

    @Test
    public void saveTest() throws JsonProcessingException, AuthenticationException {
        Wohnung wohnung = new Wohnung();
        wohnung.setOid("123");
        wohnung.setAusrichtung("Nord");
        wohnung.setStock("2");
        Wohnung a = service.save(wohnung);
        assertNotEquals(null, a.getOid());

    }

    @Test
    public void updateTest() throws JsonProcessingException, AuthenticationException {
        Wohnung wohnung = new Wohnung();
        wohnung.setOid("123");
        wohnung.setAusrichtung("Nord");
        wohnung.setStock("2");
        Wohnung response = service.save(wohnung);
        response.setOid("12");
        Wohnung a = service.update(response);
        assertEquals(response.getId(), a.getId());

    }

    @Test
    public void readTest() throws JsonProcessingException, AuthenticationException {
        Wohnung wohnung = new Wohnung();
        wohnung.setOid("123");
        wohnung.setAusrichtung("Nord");
        wohnung.setStock("2");
        service.save(wohnung);
        Wohnung a = service.read("123");
        assertEquals("123", a.getOid());

    }

    @Test
    public void deleteTest() throws JsonProcessingException, AuthenticationException {
        Wohnung wohnung = new Wohnung();
        wohnung.setOid("123");
        wohnung.setAusrichtung("Nord");
        wohnung.setStock("2");
        service.save(wohnung);
        service.delete("123");
        Wohnung a = service.read("123");
        assertEquals(null, a);
    }

    @Test
    public void queryTest() throws JsonProcessingException, AuthenticationException {
        Wohnung wohnung = new Wohnung();
        wohnung.setOid("123");
        wohnung.setAusrichtung("Nord");
        wohnung.setStock("2");
        service.save(wohnung);
        Wohnung wohnung2 = new Wohnung();
        wohnung2.setOid("124");
        wohnung2.setAusrichtung("Süd");
        wohnung2.setStock("3");
        service.save(wohnung2);
        List<Wohnung> a = service.query();
        assertEquals(2, a.size());
    }

    @Test
    public void copyTest() throws JsonProcessingException, AuthenticationException {
        Wohnung wohnung = new Wohnung();
        wohnung.setOid("123");
        wohnung.setAusrichtung("Nord");
        wohnung.setStock("2");
        service.save(wohnung);
        Wohnung a = service.copy("123");
        assertNotEquals(a.getId(), wohnung.getId());
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
        wohnungRepo.deleteAll();
        mandantRepo.deleteAll();

    }
}
