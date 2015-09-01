package de.muenchen.demo.test.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.Lists;
import de.muenchen.demo.service.Application;
import de.muenchen.demo.service.domain.Authority;
import de.muenchen.demo.service.domain.AuthorityRepository;
import de.muenchen.demo.service.services.AuthorityService;
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
public class AuthorityServiceTest {

    @Autowired
    AuthorityService service;
    @Autowired
    AuthorityRepository repo;
    @Autowired
    CacheManager cacheManager;

    @Test
    @WithMockUser(username = DomainConstants.M2_U001_NAME)
    public void testCacheOnSave() {
        System.out.println("========== save cache Test ==========");
        Cache cache = cacheManager.getCache(AuthorityRepository.Authority_CACHE);
        Authority b1 = this.createAuthority("OIDC00");
        assertNull(cache.get(b1.getOid(), Authority.class));
        service.save(b1);
        assertNotNull(cache.get(b1.getOid(), Authority.class));
        System.out.println(String.format("Objekt wurde beim Speichern mit der ID '%s' in den Cache gelegt.", b1.getOid() ));
    }

    @Test
    @WithMockUser(username = DomainConstants.M2_U001_NAME)
    public void testCacheOnRead() {
        System.out.println("========== read cache Test ==========");
        Cache cache = cacheManager.getCache(AuthorityRepository.Authority_CACHE);
        String cacheId = DomainConstants.M2_AU009;
        assertNull(cache.get(cacheId));
        Authority b1 = service.read(DomainConstants.M2_AU009);
        assertNotNull(cache.get(b1.getOid(), Authority.class));
        assertEquals(cacheId, b1.getOid());
        System.out.println(String.format("Objekt wurde beim Lesen mit der ID '%s' in den Cache gelegt.", b1.getOid() ));
    }

    @Test
    @WithMockUser(username = DomainConstants.M2_U001_NAME)
    public void createTest() throws JsonProcessingException {
        System.out.println("========== create Authority Test ==========");
        Authority b1 = service.create();
        assertNotNull(b1);
        assertNotNull(b1.getOid());
        assertNull(repo.findFirstByOid(b1.getOid()));
        System.out.println(String.format("Authority wurde mit neuer OID '%s' erstellt (und nicht in der DB gespeichert)", b1.getOid()));
    }

    @Test
    @WithMockUser(username = DomainConstants.M2_U001_NAME)
    public void saveTest() {
        System.out.println("========== save Authority Test ==========");
        String oid = "OID0";
        Authority b1 = service.save(this.createAuthority(oid));
        assertEquals(oid, b1.getOid());
        assertNotNull(repo.findFirstByOid(oid));
        System.out.println(String.format("Authority wurde in der DB gespeichert."));
    }

    private Authority createAuthority(String oid) {
        Authority user = new Authority();
        user.setOid(oid);
        user.setAuthority("mitarbeiter");
        return user;
    }

    @Test
    @WithMockUser(username = DomainConstants.M2_U001_NAME)
    public void updateTest() {
        System.out.println("========== update Authority Test ==========");
        Authority b1 = service.read(DomainConstants.M2_AU001);
        b1.setAuthority("type1");
        service.update(b1);
        Authority b2 = service.read(DomainConstants.M2_AU001);
        assertEquals("type1", b2.getAuthority());
        System.out.println("Authority wurde mit neuem Vornamen in der DB gespeichert.");
    }

    @Test
    @WithMockUser(username = DomainConstants.M2_U001_NAME)
    public void readTest() {
        System.out.println("========== read Authority Test ==========");
        Authority b1 = service.read(DomainConstants.M2_AU001);
        assertNotNull(b1);
        System.out.println(String.format("Authority konnte mit OID '%s' aus der DB gelesen werden.", b1.getOid()));
    }

    @Test
    @WithMockUser(username = DomainConstants.M2_U001_NAME)
    public void deleteTest() {
        System.out.println("========== delete Authority Test ==========");
        Authority b1 = service.read(DomainConstants.M2_AU002);
        assertNotNull(b1);
        service.delete(DomainConstants.M2_AU002);
        Authority b2 = service.read(DomainConstants.M2_AU002);
        assertNull(b2);
        System.out.println(String.format("Authority konnte mit OID '%s' aus der DB (und dem Cache) gelöscht werden.", DomainConstants.M2_AU002));
    }

    @Test
    @WithMockUser(username = DomainConstants.M2_U001_NAME)
    public void queryTest() {
        System.out.println("========== query Authority Test ==========");
        int x = this.count();
        List<Authority> bs = service.query();
        assertEquals(x, bs.size());
        System.out.println(String.format("Suche wurde erfolgreich durchgeführt.  Ergebnis der Suche: %s", bs.size()));
    }



    private int count() {
        ArrayList<Authority> all = Lists.newArrayList(repo.findAll());
        return all.size();
    }

    @Test
    @WithMockUser(username = DomainConstants.M2_U001_NAME)
    public void copyTest() {
        System.out.println("========== copy Authority Test ==========");
        Authority b1 = service.copy(DomainConstants.M2_AU001);
        assertNotNull(b1);
        Authority b2 = service.read(b1.getOid());
        assertNotNull(b2);
        assertEquals(b1.getAuthority(), b2.getAuthority());
        System.out.println(String.format("Objekt mit der OID '%s' konnte erfolgreich in Objekt '%s' kopiert (und in DB gespeichert) werden", DomainConstants.M2_AU001, b2.getOid()));
    }
}

