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
import de.muenchen.demo.service.domain.User;
import de.muenchen.demo.service.domain.UserRepository;
import de.muenchen.demo.service.domain.MandantRepository;
import de.muenchen.demo.service.domain.PermissionRepository;
import de.muenchen.demo.service.domain.UserAuthorityRepository;
import de.muenchen.demo.service.domain.UserRepository;
import de.muenchen.demo.service.services.UserService;
import de.muenchen.demo.test.integration.InitTest;
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
public class UserServiceTest {

    @Autowired
    UserService service;

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
    UserRepository userRepo;

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
        userRepo.deleteAll();
        mandantRepo.deleteAll();
        InitTest initTest = new InitTest(usersRepo, authRepo, permRepo, userAuthRepo, authPermRepo, mandantRepo);
        initTest.init();
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken("hans", "test");
        Authentication auth = authenticationManager.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Test
    public void createTest() throws JsonProcessingException {

        User a = service.create();
        assertNotEquals(null, a.getOid());
    }

    @Test
    public void saveTest() throws JsonProcessingException, AuthenticationException {
        User user = new User();
        user.setOid("123");
        user.setUsername("user");
        user.setPassword("user");
        User a = service.save(user);
        assertNotEquals(null, a.getOid());

    }

    @Test
    public void updateTest() throws JsonProcessingException, AuthenticationException {
        User user = new User();
        user.setOid("123");
        user.setUsername("user");
        user.setPassword("user");
        User response = service.save(user);
        response.setOid("12");
        User a = service.update(response);
        assertEquals(response.getId(), a.getId());

    }

    @Test
    public void readTest() throws JsonProcessingException, AuthenticationException {
        User user = new User();
        user.setOid("123");
        user.setUsername("user");
        user.setPassword("user");
        service.save(user);
        User a = service.read("123");
        assertEquals("123", a.getOid());

    }

    @Test
    public void deleteTest() throws JsonProcessingException, AuthenticationException {
        User user = new User();
        user.setOid("123");
        user.setUsername("user");
        user.setPassword("user");
        service.save(user);
        service.delete("123");
        User a = service.read("123");
        assertEquals(null, a);
    }

    @Test
    public void queryTest() throws JsonProcessingException, AuthenticationException {
        User user = new User();
        user.setOid("123");
        user.setUsername("user");
        user.setPassword("user");
        service.save(user);
        User user2 = new User();
        user2.setOid("124");
        user2.setUsername("user2");
        user2.setPassword("user2");
        service.save(user2);
        List<User> a = service.query();
        assertEquals(4, a.size());
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
        userRepo.deleteAll();
        mandantRepo.deleteAll();

    }
}
