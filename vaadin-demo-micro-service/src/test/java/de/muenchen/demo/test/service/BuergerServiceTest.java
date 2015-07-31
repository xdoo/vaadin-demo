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
import de.muenchen.demo.service.domain.Buerger;
import de.muenchen.demo.service.domain.BuergerRepository;
import de.muenchen.demo.service.domain.MandantRepository;
import de.muenchen.demo.service.domain.PermissionRepository;
import de.muenchen.demo.service.domain.UserAuthorityRepository;
import de.muenchen.demo.service.domain.UserRepository;
import de.muenchen.demo.service.services.BuergerService;
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
public class BuergerServiceTest {

    @Autowired
    BuergerService service;

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
    BuergerRepository buergerRepo;

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
        buergerRepo.deleteAll();
        mandantRepo.deleteAll();
        InitTest initTest = new InitTest(usersRepo, authRepo, permRepo, userAuthRepo, authPermRepo, mandantRepo);
        initTest.init();
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken("hans", "test");
        Authentication auth = authenticationManager.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Test
    public void createTest() throws JsonProcessingException {

        Buerger a = service.create();
        assertNotEquals(null, a.getOid());
    }

    @Test
    public void saveTest() throws JsonProcessingException, AuthenticationException {
        Buerger buerger = new Buerger();
        buerger.setOid("123");
        buerger.setNachname("hans");
        buerger.setVorname("peter");
        Buerger a = service.save(buerger);
        assertNotEquals(null, a.getOid());

    }

    @Test
    public void updateTest() throws JsonProcessingException, AuthenticationException {
        Buerger buerger = new Buerger();
        buerger.setOid("123");
        buerger.setNachname("hans");
        buerger.setVorname("peter");
        Buerger response = service.save(buerger);
        response.setOid("12");
        Buerger a = service.update(response);
        assertEquals(response.getId(), a.getId());

    }

    @Test
    public void readTest() throws JsonProcessingException, AuthenticationException {
        Buerger buerger = new Buerger();
        buerger.setOid("123");
        buerger.setNachname("hans");
        buerger.setVorname("peter");
        service.save(buerger);
        Buerger a = service.read("123");
        assertEquals("123", a.getOid());

    }

    @Test
    public void deleteTest() throws JsonProcessingException, AuthenticationException {
        Buerger buerger = new Buerger();
        buerger.setOid("123");
        buerger.setNachname("hans");
        buerger.setVorname("peter");
        service.save(buerger);
        service.delete("123");
        Buerger a = service.read("123");
        assertEquals(null, a);
    }

    @Test
    public void queryTest() throws JsonProcessingException, AuthenticationException {
        Buerger buerger = new Buerger();
        buerger.setOid("123");
        buerger.setNachname("hans");
        buerger.setVorname("peter");
        service.save(buerger);
        Buerger buerger2 = new Buerger();
        buerger2.setOid("124");
        buerger2.setNachname("hans2");
        buerger2.setVorname("peter2");
        service.save(buerger2);
        List<Buerger> a = service.query();
        assertEquals(2, a.size());
    }

    @Test
    public void copyTest() throws JsonProcessingException, AuthenticationException {
        Buerger buerger = new Buerger();
        buerger.setOid("123");
        buerger.setNachname("hans");
        buerger.setVorname("peter");
        service.save(buerger);
        Buerger a = service.copy("123");
        assertNotEquals(a.getId(), buerger.getId());
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
        buergerRepo.deleteAll();
        mandantRepo.deleteAll();

    }
}
