
package de.muenchen.demo.test.service;

import com.google.common.collect.Lists;
import de.muenchen.demo.service.Application;
import de.muenchen.demo.service.domain.UserAuthId;
import de.muenchen.demo.service.domain.Authority;
import de.muenchen.demo.service.domain.User;
import de.muenchen.demo.service.domain.UserAuthority;
import de.muenchen.demo.service.domain.UserAuthorityRepository;
import de.muenchen.demo.service.services.UserAuthorityService;
import de.muenchen.demo.service.services.AuthorityService;
import de.muenchen.demo.service.services.UserService;
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
public class UserAuthorityServiceTest {

    @Autowired
    UserAuthorityService service;
    @Autowired
    AuthorityService authorityService;
    @Autowired
    UserService userService;
    @Autowired
    UserAuthorityRepository repo;
    @Autowired
    CacheManager cacheManager;

    @Test
    @WithMockUser(username = DomainConstants.M2_U001_NAME)
    public void testCacheOnSave() {
        System.out.println("========== save cache Test ==========");
        Cache cache = cacheManager.getCache(UserAuthorityRepository.UserAuthority_CACHE);
        Authority a1 = authorityService.read(DomainConstants.M2_AU003);
        User u1 = userService.read("oid14");
        UserAuthId idA = new UserAuthId(u1, a1);
        UserAuthority authPerm = new UserAuthority();
        authPerm.setId(idA);
        assertNull(cache.get(authPerm.getId(), UserAuthority.class));
        UserAuthority au1 = service.save(authPerm);
        assertNotNull(cache.get(authPerm.getId(), UserAuthority.class));
        System.out.println(String.format("Objekt wurde beim Speichern mit der ID '%s' in den Cache gelegt.", authPerm.getId()));
    }

    @Test
    @WithMockUser(username = DomainConstants.M2_U001_NAME)
    public void saveTest() {
        System.out.println("========== save UserAuthority Test ==========");
        Authority a1 = authorityService.read(DomainConstants.M2_AU003);
        User u1 = userService.read("oid15");
        UserAuthId idA = new UserAuthId(u1, a1);
        UserAuthority userAuth = new UserAuthority();
        userAuth.setId(idA);
        UserAuthority au1 = service.save(userAuth);
        assertNotNull(au1);
        assertNotNull(repo.findFirstById(idA));
        System.out.println(String.format("UserAuthority wurde in der DB gespeichert."));
    }

    @Test
    @WithMockUser(username = DomainConstants.M2_U001_NAME)
    public void readTest() {
        System.out.println("========== read UserAuthority Test ==========");
        Authority a1 = authorityService.read(DomainConstants.M2_AU003);
        User u1 = userService.read("oid16");
        UserAuthId idA = new UserAuthId(u1, a1);
        UserAuthority b1 = service.read(idA);
        assertNotNull(b1);
        System.out.println(String.format("UserAuthority konnte mit OID '%s' aus der DB gelesen werden.", b1.getId()));
    }

    @Test
    @WithMockUser(username = DomainConstants.M2_U001_NAME)
    public void deleteTest() {
        System.out.println("========== delete UserAuthority Test ==========");
        Authority a1 = authorityService.read(DomainConstants.M2_AU011);
        User u1 = userService.read("oid24");
        UserAuthId idA = new UserAuthId(u1, a1);
        int x = this.count();
        service.delete(idA);
        assertEquals(x - 1, this.count());
        System.out.println(String.format("UserAuthority konnte  aus der DB (und dem Cache) gelöscht werden."));
    }

    @Test
    @WithMockUser(username = DomainConstants.M2_U001_NAME)
    public void queryTest() {
        System.out.println("========== query UserAuthority==========");
        int x = this.count();
        List<UserAuthority> bs = service.query();
        assertEquals(x, bs.size());
        System.out.println(String.format("Suche wurde erfolgreich durchgeführt.  Ergebnis der Suche: %s", bs.size()));
    }

    private int count() {
        ArrayList<UserAuthority> all = Lists.newArrayList(repo.findAll());
        return all.size();
    }

}
