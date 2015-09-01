package de.muenchen.demo.test.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.Lists;
import de.muenchen.demo.service.Application;
import de.muenchen.demo.service.domain.Permission;
import de.muenchen.demo.service.domain.PermissionRepository;
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
public class PermissionServiceTest {

    @Autowired
    PermissionService service;
    @Autowired
    PermissionRepository repo;
    @Autowired
    CacheManager cacheManager;

    @Test
    @WithMockUser(username = DomainConstants.M2_U001_NAME)
    public void testCacheOnSave() {
        System.out.println("========== save cache Test ==========");
        Cache cache = cacheManager.getCache(PermissionRepository.Permission_CACHE);
        Permission b1 = this.createPermission("OIDC00");
        assertNull(cache.get(b1.getOid(), Permission.class));
        service.save(b1);
        assertNotNull(cache.get(b1.getOid(), Permission.class));
        System.out.println(String.format("Objekt wurde beim Speichern mit der ID '%s' in den Cache gelegt.", b1.getOid() ));
    }

    @Test
    @WithMockUser(username = DomainConstants.M2_U001_NAME)
    public void testCacheOnRead() {
        System.out.println("========== read cache Test ==========");
        Cache cache = cacheManager.getCache(PermissionRepository.Permission_CACHE);
        String cacheId = DomainConstants.M_P020;
        assertNull(cache.get(cacheId));
        Permission b1 = service.read(DomainConstants.M_P020);
        assertNotNull(cache.get(b1.getOid(), Permission.class));
        assertEquals(cacheId, b1.getOid());
        System.out.println(String.format("Objekt wurde beim Lesen mit der ID '%s' in den Cache gelegt.", b1.getOid() ));
    }

    @Test
    @WithMockUser(username = DomainConstants.M2_U001_NAME)
    public void createTest() throws JsonProcessingException {
        System.out.println("========== create Permission Test ==========");
        Permission b1 = service.create();
        assertNotNull(b1);
        assertNotNull(b1.getOid());
        assertNull(repo.findFirstByOid(b1.getOid()));
        System.out.println(String.format("Permission wurde mit neuer OID '%s' erstellt (und nicht in der DB gespeichert)", b1.getOid()));
    }

    @Test
    @WithMockUser(username = DomainConstants.M2_U001_NAME)
    public void saveTest() {
        System.out.println("========== save Permission Test ==========");
        String oid = "OID0";
        Permission b1 = service.save(this.createPermission(oid));
        assertEquals(oid, b1.getOid());
        assertNotNull(repo.findFirstByOid(oid));
        System.out.println(String.format("Permission wurde in der DB gespeichert."));
    }

    private Permission createPermission(String oid) {
        Permission user = new Permission();
        user.setOid(oid);
        user.setPermission("mitarbeiter");
        return user;
    }

   
    @Test
    @WithMockUser(username = DomainConstants.M2_U001_NAME)
    public void readTest() {
        System.out.println("========== read Permission Test ==========");
        Permission b1 = service.read(DomainConstants.M_P002);
        assertNotNull(b1);
        System.out.println(String.format("Permission konnte mit OID '%s' aus der DB gelesen werden.", b1.getOid()));
    }

    @Test
    @WithMockUser(username = DomainConstants.M2_U001_NAME)
    public void deleteTest() {
        System.out.println("========== delete Permission Test ==========");
        Permission b1 = service.read(DomainConstants.M_P003);
        assertNotNull(b1);
        service.delete(DomainConstants.M_P003);
        Permission b2 = service.read(DomainConstants.M_P003);
        assertNull(b2);
        System.out.println(String.format("Permission konnte mit OID '%s' aus der DB (und dem Cache) gelöscht werden.", DomainConstants.M_P003));
    }

    @Test
    @WithMockUser(username = DomainConstants.M2_U001_NAME)
    public void queryTest() {
        System.out.println("========== query Permission Test  ==========");
        int x = this.count();
        List<Permission> bs = service.query();
        assertEquals(x, bs.size());
        System.out.println(String.format("Suche wurde erfolgreich durchgeführt. Einträge Mandant %s: %s | Ergebnis der Suche: %s", DomainConstants.M2, x, bs.size()));
    }



    private int count() {
        ArrayList<Permission> all = Lists.newArrayList(repo.findAll());
        return all.size();
    }

   
}

