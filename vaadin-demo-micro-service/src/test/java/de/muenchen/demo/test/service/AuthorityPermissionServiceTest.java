
package de.muenchen.demo.test.service;

import com.google.common.collect.Lists;
import de.muenchen.demo.service.Application;
import de.muenchen.demo.service.domain.AuthPermId;
import de.muenchen.demo.service.domain.Authority;
import de.muenchen.demo.service.domain.AuthorityPermission;
import de.muenchen.demo.service.domain.AuthorityPermissionRepository;
import de.muenchen.demo.service.domain.Permission;
import de.muenchen.demo.service.services.AuthorityPermissionService;
import de.muenchen.demo.service.services.AuthorityService;
import de.muenchen.demo.service.services.PermissionService;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.security.test.context.support.WithMockUser;
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
    AuthorityService authorityService;
    @Autowired
    PermissionService permissionService;
    @Autowired
    AuthorityPermissionRepository repo;
    @Autowired
    CacheManager cacheManager;

    @Test
    @WithMockUser(username = DomainConstants.M2_U001_NAME)
    public void testCacheOnSave() {
        System.out.println("========== save cache Test ==========");
        Cache cache = cacheManager.getCache(AuthorityPermissionRepository.AuthorityPermission_CACHE);
        Authority a1 = authorityService.read(DomainConstants.M2_AU001);
        Permission p1 = permissionService.read(DomainConstants.M_P008);
        AuthPermId idA = new AuthPermId(p1, a1);
        AuthorityPermission authPerm = new AuthorityPermission();
        authPerm.setId(idA);
        assertNull(cache.get(authPerm.getId(), AuthorityPermission.class));
        AuthorityPermission ap1 = service.save(authPerm);
        assertNotNull(cache.get(authPerm.getId(), AuthorityPermission.class));
        System.out.println(String.format("Objekt wurde beim Speichern mit der ID '%s' in den Cache gelegt.", authPerm.getId()));
    }

//    @Test
//    @WithMockUser(username = DomainConstants.M2_U001_NAME)
//    public void testCacheOnRead() {
//        System.out.println("========== read cache Test ==========");
//        Cache cache = cacheManager.getCache(AuthorityPermissionRepository.AuthorityPermission_CACHE);
//
//        Authority a1 = authorityService.read(DomainConstants.M2_AU002);
//        Permission p1 = permissionService.read(DomainConstants.M_P009);
//        AuthPermId idA = new AuthPermId(p1, a1);
//        AuthPermId cacheId = idA;
//        assertNull(cache.get(cacheId));
//        AuthorityPermission b1 = service.read(idA);
//        assertNotNull(cache.get(b1.getId(), AuthorityPermission.class));
//        assertEquals(cacheId, b1.getId());
//        System.out.println(String.format("Objekt wurde beim Lesen mit der ID '%s' in den Cache gelegt.", b1.getId()));
//    }

    @Test
    @WithMockUser(username = DomainConstants.M2_U001_NAME)
    public void saveTest() {
        System.out.println("========== save AuthorityPermission Test ==========");
        Authority a1 = authorityService.read(DomainConstants.M2_AU001);
        Permission p1 = permissionService.read(DomainConstants.M_P007);
        AuthPermId idA = new AuthPermId(p1, a1);
        AuthorityPermission authPerm = new AuthorityPermission();
        authPerm.setId(idA);
        AuthorityPermission ap1 = service.save(authPerm);
        assertNotNull(ap1);
        assertNotNull(repo.findFirstById(idA));
        System.out.println(String.format("AuthorityPermission wurde in der DB gespeichert."));
    }

    @Test
    @WithMockUser(username = DomainConstants.M2_U001_NAME)
    public void readTest() {
        System.out.println("========== read AuthorityPermission Test ==========");
        Authority a1 = authorityService.read(DomainConstants.M2_AU002);
        Permission p1 = permissionService.read(DomainConstants.M_P006);
        AuthPermId idA = new AuthPermId(p1, a1);
        AuthorityPermission b1 = service.read(idA);
        assertNotNull(b1);
        System.out.println(String.format("AuthorityPermission konnte mit OID '%s' aus der DB gelesen werden.", b1.getId()));
    }

    @Test
    @WithMockUser(username = DomainConstants.M2_U001_NAME)
    public void deleteTest() {
        System.out.println("========== delete AuthorityPermission Test ==========");
        Authority a1 = authorityService.read(DomainConstants.M2_AU002);
        Permission p1 = permissionService.read(DomainConstants.M_P008);
        AuthPermId idA = new AuthPermId(p1, a1);
        int x = this.count();
        service.delete(idA);
        assertEquals(x - 1, this.count());
        System.out.println(String.format("AuthorityPermission konnte  aus der DB (und dem Cache) gelöscht werden."));
    }

    @Test
    @WithMockUser(username = DomainConstants.M2_U001_NAME)
    public void queryTest() {
        System.out.println("========== query AuthorityPermission==========");
        int x = this.count();
        List<AuthorityPermission> bs = service.query();
        assertEquals(x, bs.size());
        System.out.println(String.format("Suche wurde erfolgreich durchgeführt.  Ergebnis der Suche: %s", bs.size()));
    }

    private int count() {
        ArrayList<AuthorityPermission> all = Lists.newArrayList(repo.findAll());
        return all.size();
    }

}
