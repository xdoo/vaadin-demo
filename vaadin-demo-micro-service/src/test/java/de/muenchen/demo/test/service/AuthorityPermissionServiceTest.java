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
import de.muenchen.demo.service.domain.AuthPermId;
import de.muenchen.demo.service.domain.Authority;
import de.muenchen.demo.service.domain.AuthorityPermissionRepository;
import de.muenchen.demo.service.domain.AuthorityRepository;
import de.muenchen.demo.service.domain.AuthorityPermission;
import de.muenchen.demo.service.domain.AuthorityPermissionRepository;
import de.muenchen.demo.service.domain.Mandant;
import de.muenchen.demo.service.domain.MandantRepository;
import de.muenchen.demo.service.domain.Permission;
import de.muenchen.demo.service.domain.PermissionRepository;
import de.muenchen.demo.service.domain.UserAuthorityRepository;
import de.muenchen.demo.service.domain.UserRepository;
import de.muenchen.demo.service.services.AuthorityPermissionService;
import de.muenchen.demo.service.util.IdService;
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
public class AuthorityPermissionServiceTest {

    @Autowired
    AuthorityPermissionService service;

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
        mandantRepo.deleteAll();
        InitTest initTest = new InitTest(usersRepo, authRepo, permRepo, userAuthRepo, authPermRepo, mandantRepo);
        initTest.init();
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken("hans", "test");
        Authentication auth = authenticationManager.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Test
    public void saveTest() throws JsonProcessingException, AuthenticationException {
        Mandant mandant = new Mandant();
        mandant.setOid("10");
        mandantRepo.save(mandant);
        Authority auth = new Authority();
        auth.setAuthority("ADMIN");
        auth.setOid(IdService.next());
        auth.setMandant(mandant);
        authRepo.save(auth);
        Permission permission = new Permission();
        permission.setPermision("New_Buerger");
        permission.setOid(IdService.next());
        permission.setMandant(mandant);
        permRepo.save(permission);
        AuthorityPermission authPerm = new AuthorityPermission();
        AuthPermId idA = new AuthPermId(permission, auth);
        authPerm.setId(idA);
        AuthorityPermission a = service.save(authPerm);
        assertNotEquals(null, a);

        
        
    }

    @Test
    public void deleteTest() throws JsonProcessingException, AuthenticationException {
        Mandant mandant = new Mandant();
        mandant.setOid("10");
        mandantRepo.save(mandant);
        Authority auth = new Authority();
        auth.setAuthority("SACHBEARBEITER");
        auth.setOid(IdService.next());
        auth.setMandant(mandant);
        authRepo.save(auth);
        Permission permission = new Permission();
        permission.setPermision("New_Buerger");
        permission.setOid(IdService.next());
        permission.setMandant(mandant);
        permRepo.save(permission);
        AuthorityPermission authPerm = new AuthorityPermission();
        AuthPermId idA = new AuthPermId(permission, auth);
        authPerm.setId(idA);
        service.save(authPerm);
        service.delete(idA);
        List<AuthorityPermission> a = service.readByAuthority("SACHBEARBEITER");
        assertEquals(null, a);
    }

    @Test
    public void queryTest() throws JsonProcessingException, AuthenticationException {
        Mandant mandant = new Mandant();
        mandant.setOid("10");
        mandantRepo.save(mandant);
        Authority auth = new Authority();
        auth.setAuthority("ADMIN");
        auth.setOid(IdService.next());
        auth.setMandant(mandant);
        authRepo.save(auth);
        Permission permission = new Permission();
        permission.setPermision("New_Buerger");
        permission.setOid(IdService.next());
        permission.setMandant(mandant);
        permRepo.save(permission);
        AuthorityPermission authPerm = new AuthorityPermission();
        AuthPermId idA = new AuthPermId(permission, auth);
        authPerm.setId(idA);
        service.save(authPerm);
        List<AuthorityPermission> a = service.query();
        assertEquals(false, a.isEmpty());
    }

    @After
    public void TearDown() {
        authPermRepo.deleteAll();
        userAuthRepo.deleteAll();
        usersRepo.deleteAll();
        authRepo.deleteAll();
        permRepo.deleteAll();
        mandantRepo.deleteAll();

    }
}
