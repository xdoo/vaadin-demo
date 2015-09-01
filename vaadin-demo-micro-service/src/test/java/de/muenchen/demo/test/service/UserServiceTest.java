
package de.muenchen.demo.test.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.Lists;
import de.muenchen.demo.service.Application;
import de.muenchen.demo.service.domain.UserRepository;
import de.muenchen.demo.service.domain.User;
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
public class UserServiceTest {

    @Autowired
    UserService service;
    @Autowired
    UserRepository repo;
    @Autowired
    CacheManager cacheManager;

    @Test
    @WithMockUser(username = DomainConstants.M2_U001_NAME)
    public void testCacheOnSave() {
        System.out.println("========== save cache Test ==========");
        Cache cache = cacheManager.getCache(UserRepository.User_CACHE);
        User b1 = this.createUser("OIDC00");
        assertNull(cache.get(b1.getOid(), User.class));
        service.save(b1);
        assertNotNull(cache.get(b1.getOid(), User.class));
        System.out.println(String.format("Objekt wurde beim Speichern mit der ID '%s' in den Cache gelegt.", b1.getOid() ));
    }

    @Test
    @WithMockUser(username = DomainConstants.M2_U001_NAME)
    public void testCacheOnRead() {
        System.out.println("========== read cache Test ==========");
        Cache cache = cacheManager.getCache(UserRepository.User_CACHE);
        String cacheId = "oid6";
        assertNull(cache.get(cacheId));
        User b1 = service.read("oid6");
        assertNotNull(cache.get(b1.getOid(), User.class));
        assertEquals(cacheId, b1.getOid());
        System.out.println(String.format("Objekt wurde beim Lesen mit der ID '%s' in den Cache gelegt.", b1.getOid() ));
    }

    @Test
    @WithMockUser(username = DomainConstants.M2_U001_NAME)
    public void createTest() throws JsonProcessingException {
        System.out.println("========== create User Test ==========");
        User b1 = service.create();
        assertNotNull(b1);
        assertNotNull(b1.getOid());
        assertNull(repo.findFirstByOid(b1.getOid()));
        System.out.println(String.format("User wurde mit neuer OID '%s' erstellt (und nicht in der DB gespeichert)", b1.getOid()));
    }

    @Test
    @WithMockUser(username = DomainConstants.M2_U001_NAME)
    public void saveTest() {
        System.out.println("========== save User Test ==========");
        String oid = "OID0";
        User b1 = service.save(this.createUser(oid));
        assertEquals(oid, b1.getOid());
        assertNotNull(repo.findFirstByOid(oid));
        System.out.println(String.format("User wurde in der DB gespeichert."));
    }

    private User createUser(String oid) {
        User user = new User();
        user.setOid(oid);
        user.setUsername("sandi");
        user.setPassword("1234");
        return user;
    }

    @Test
    @WithMockUser(username = DomainConstants.M2_U001_NAME)
    public void updateTest() {
        System.out.println("========== update User Test ==========");
        User b1 = service.read("oid25");
        b1.setUsername("lukas");
        service.update(b1);
        User b2 = service.read("oid25");
        assertEquals("lukas", b2.getUsername());
        System.out.println("User wurde mit neuem Vornamen in der DB gespeichert.");
    }

    @Test
    @WithMockUser(username = DomainConstants.M2_U001_NAME)
    public void readTest() {
        System.out.println("========== read User Test ==========");
        User b1 = service.read("oid25");
        assertNotNull(b1);
        System.out.println(String.format("User konnte mit OID '%s' aus der DB gelesen werden.", b1.getOid()));
    }

    @Test
    @WithMockUser(username = DomainConstants.M2_U001_NAME)
    public void deleteTest() {
        System.out.println("========== delete User Test ==========");
        User b1 = service.read("oid22");
        assertNotNull(b1);
        service.delete("oid22");
        User b2 = service.read("oid22");
        assertNull(b2);
        System.out.println(String.format("User konnte mit OID '%s' aus der DB (und dem Cache) gelöscht werden.", "oid22"));
    }

    @Test
    @WithMockUser(username = DomainConstants.M2_U001_NAME)
    public void queryTest() {
        System.out.println("========== query User  ==========");
        int x = this.count();
        List<User> bs = service.query();
        assertEquals(x, bs.size());
        System.out.println(String.format("Suche wurde erfolgreich durchgeführt.  Ergebnis der Suche: %s",  bs.size()));
    }



    private int count() {
        ArrayList<User> all = Lists.newArrayList(repo.findAll());
        return all.size();
    }

    @Test
    @WithMockUser(username = DomainConstants.M2_U001_NAME)
    public void copyTest() {
        System.out.println("========== copy User Test ==========");
        User b1 = service.copy("oid26");
        assertNotNull(b1);
        User b2 = service.read(b1.getOid());
        assertNotNull(b2);
        assertEquals(b1.getUsername(), b2.getUsername());
        assertEquals(b1.getPassword(), b2.getPassword());
        System.out.println(String.format("Objekt mit der OID '%s' konnte erfolgreich in Objekt '%s' kopiert (und in DB gespeichert) werden", "oid26", b2.getOid()));
    }
}
