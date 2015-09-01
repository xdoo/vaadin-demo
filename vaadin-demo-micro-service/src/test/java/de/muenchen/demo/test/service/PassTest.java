package de.muenchen.demo.test.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.Lists;
import de.muenchen.demo.service.Application;
import de.muenchen.demo.service.domain.PassRepository;
import de.muenchen.demo.service.domain.Pass;
import de.muenchen.demo.service.domain.StaatsangehoerigkeitReference;
import de.muenchen.demo.service.services.PassService;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
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
public class PassTest {

    @Autowired
    PassService service;
    @Autowired
    PassRepository repo;
    @Autowired
    CacheManager cacheManager;

    @Test
    @WithMockUser(username = DomainConstants.M2_U001_NAME)
    public void testCacheOnSave() {
        System.out.println("========== save cache Test ==========");
        Cache cache = cacheManager.getCache(PassRepository.Pass_CACHE);
        Pass b1 = this.createPass("OIDC00");
        assertNull(cache.get(b1.getOid() + "2", Pass.class));
        service.save(b1);
        assertNotNull(cache.get(b1.getOid() + b1.getMandant().getOid(), Pass.class));
        System.out.println(String.format("Objekt wurde beim Speichern mit der ID '%s' in den Cache gelegt.", b1.getOid() + b1.getMandant().getOid()));
    }

    @Test
    @WithMockUser(username = DomainConstants.M2_U001_NAME)
    public void testCacheOnRead() {
        System.out.println("========== read cache Test ==========");
        Cache cache = cacheManager.getCache(PassRepository.Pass_CACHE);
        String cacheId = DomainConstants.M2_P020 + DomainConstants.M2;
        assertNull(cache.get(cacheId));
        Pass b1 = service.read(DomainConstants.M2_P020);
        assertNotNull(cache.get(b1.getOid() + b1.getMandant().getOid(), Pass.class));
        assertEquals(cacheId, b1.getOid() + b1.getMandant().getOid());
        System.out.println(String.format("Objekt wurde beim Lesen mit der ID '%s' in den Cache gelegt.", b1.getOid() + b1.getMandant().getOid()));
    }

    @Test
    @WithMockUser(username = DomainConstants.M2_U001_NAME)
    public void createTest() throws JsonProcessingException {
        System.out.println("========== create Pass Test ==========");
        Pass b1 = service.create();
        assertNotNull(b1);
        assertNotNull(b1.getOid());
        assertNull(repo.findFirstByOidAndMandantOid(b1.getOid(), DomainConstants.M2));
        System.out.println(String.format("Pass wurde mit neuer OID '%s' erstellt (und nicht in der DB gespeichert)", b1.getOid()));
    }

    @Test
    @WithMockUser(username = DomainConstants.M2_U001_NAME)
    public void saveTest() {
        System.out.println("========== save Pass Test ==========");
        String oid = "OID0";
        Pass b1 = service.save(this.createPass(oid));
        assertEquals(oid, b1.getOid());
        assertEquals(DomainConstants.M2, b1.getMandant().getOid());
        assertNotNull(repo.findFirstByOidAndMandantOid(oid, DomainConstants.M2));
        System.out.println(String.format("Pass wurde mit Mandant '%s' in der DB gespeichert.", b1.getMandant().getOid()));
    }

    private Pass createPass(String oid) {
        Pass pass = new Pass();
        pass.setOid(oid);
        pass.setBehoerde("M001");
        pass.setPassNummer("040208");
        return pass;
    }

    @Test
    @WithMockUser(username = DomainConstants.M2_U001_NAME)
    public void updateTest() {
        System.out.println("========== update Pass Test ==========");
        Pass b1 = service.read(DomainConstants.M2_P002);
        b1.setBehoerde("P001");
        service.update(b1);
        Pass b2 = service.read(DomainConstants.M2_P002);
        assertEquals("P001", b2.getBehoerde());
        System.out.println("Pass wurde mit neuem Vornamen in der DB gespeichert.");
    }

    @Test
    @WithMockUser(username = DomainConstants.M2_U001_NAME)
    public void readTest() {
        System.out.println("========== read Pass Test ==========");
        Pass b1 = service.read(DomainConstants.M2_P003);
        assertNotNull(b1);
        System.out.println(String.format("Pass konnte mit OID '%s' aus der DB gelesen werden.", b1.getOid()));
    }

    @Test
    @WithMockUser(username = DomainConstants.M2_U001_NAME)
    public void deleteTest() {
        System.out.println("========== delete Pass Test ==========");
        Pass b1 = service.read(DomainConstants.M2_P004);
        assertNotNull(b1);
        service.delete(DomainConstants.M2_P004);
        Pass b2 = service.read(DomainConstants.M2_P004);
        assertNull(b2);
        System.out.println(String.format("Pass konnte mit OID '%s' aus der DB (und dem Cache) gelöscht werden.", DomainConstants.M2_P004));
    }

    @Test
    @WithMockUser(username = DomainConstants.M2_U001_NAME)
    public void queryTestM2() {
        System.out.println("========== query Pass Test Mandant 2 ==========");
        int x = this.count(DomainConstants.M2);
        List<Pass> bs = service.query();
        assertEquals(x, bs.size());
        System.out.println(String.format("Suche wurde erfolgreich durchgeführt. Einträge Mandant %s: %s | Ergebnis der Suche: %s", DomainConstants.M2, x, bs.size()));
    }

    @Test
    @WithMockUser(username = DomainConstants.M3_U002_NAME)
    public void queryTestM3() {
        System.out.println("========== query Pass Test Mandant 3 ==========");
        int x = this.count(DomainConstants.M3);
        List<Pass> bs = service.query();
        assertEquals(x, bs.size());
        System.out.println(String.format("Suche wurde erfolgreich durchgeführt. Einträge Mandant %s: %s | Ergebnis der Suche: %s", DomainConstants.M3, x, bs.size()));
    }

    private int count(String mid) {
        ArrayList<Pass> all = Lists.newArrayList(repo.findAll());
        return all.stream().filter(b -> b.getMandant().getOid().equals(mid)).collect(Collectors.counting()).intValue();
    }

    @Test
    @WithMockUser(username = DomainConstants.M2_U001_NAME)
    public void copyTest() {
        System.out.println("========== copy Pass Test ==========");
        Pass b1 = service.copy(DomainConstants.M2_P006);
        assertNotNull(b1);
        Pass b2 = service.read(b1.getOid());
        assertNotNull(b2);
        assertEquals(b1.getBehoerde(), b2.getBehoerde());
        assertEquals(b1.getPassNummer(), b2.getPassNummer());
        System.out.println(String.format("Objekt mit der OID '%s' konnte erfolgreich in Objekt '%s' kopiert (und in DB gespeichert) werden", DomainConstants.M2_P006, b2.getOid()));
    }
@Test
    @WithMockUser(username = DomainConstants.M2_U001_NAME)
    public void passCopyLisTest() {
        System.out.println("========== copy Liste Pass Test ==========");
        int x = this.count(DomainConstants.M2);
        ArrayList<String> oids = new ArrayList();
        oids.add(DomainConstants.M2_P027);
        oids.add(DomainConstants.M2_P028);
        service.copy(oids);
        List<Pass> bs = service.query();
        assertEquals(x + 2, bs.size());
        System.out.println(String.format("Objekte mit der OID '%s' und der OID '%s' konnte erfolgreich in Objekt  kopiert (und in DB gespeichert) werden", DomainConstants.M2_P027, DomainConstants.M2_P028));

    }

    @Test
    @WithMockUser(username = DomainConstants.M2_U001_NAME)
    public void passDeleteListTest() {
        System.out.println("========== delete pass Test ==========");
        ArrayList<String> oids = new ArrayList();
        oids.add(DomainConstants.M2_P022);
        oids.add(DomainConstants.M2_P023);
        service.delete(oids);
        Pass b1 = service.read(DomainConstants.M2_P022);
        assertNull(b1);
        Pass b2 = service.read(DomainConstants.M2_P023);
        assertNull(b2);
        System.out.println(String.format("Pässe  mit OID '%s'und OID '%s' konnte aus der DB (und dem Cache) gelöscht werden.", DomainConstants.M2_P022, DomainConstants.M2_P023));

    }

    @Test
    @WithMockUser(username = DomainConstants.M2_U001_NAME)
    public void releaseStaatsangehoerigkeitPassTest() {
        System.out.println("========== release Staatsangehoerigkeit Pass Test ==========");
        Pass p1 = service.readStaat(DomainConstants.M2_S012);
        assertNotNull(p1);
        service.releaseStaatsangehoerigkeitPass(DomainConstants.M2_S012);
        Pass p2 = service.readStaat(DomainConstants.M2_S012);
        assertNull(p2);
        System.out.println(String.format("release operation für den Staatsangehoerigkeit eines Pass mit OID '%s' konnte aus der DB erfolgreich durchgeführt.", DomainConstants.M2_S012));
    }

    @Test
    @WithMockUser(username = DomainConstants.M2_U001_NAME)
    public void releasePassStaatsangehoerigkeitTest() {
        System.out.println("========== release Pass Staatsangehoerigkeiten Test ==========");
        StaatsangehoerigkeitReference b1 = service.read(DomainConstants.M2_P014).getStaatsangehoerigkeitReference();
        assertNotNull(b1);
        service.releasePassStaatsangehoerigkeit(DomainConstants.M2_P014);
        StaatsangehoerigkeitReference b2 = service.read(DomainConstants.M2_P014).getStaatsangehoerigkeitReference();
        assertTrue(b2 == null);
        System.out.println(String.format("release operation für die Staatsangehoerigkeit eines Pass mit OID '%s' konnte aus der DB erfolgreich durchgeführt.", DomainConstants.M2_P014));
    }

    @Test
    @WithMockUser(username = DomainConstants.M2_U001_NAME)
    public void StaatsangehoerigkeitPassTest() {
        System.out.println("========== read Staatsangehoerigkeit Pass Test ==========");
        Pass p1 = service.readStaat(DomainConstants.M2_S011);
        assertNotNull(p1);
        System.out.println(String.format("der Pass der Staatsangehoerigkeit mit OID '%s' konnten aus der DB gelesen werden.", DomainConstants.M2_S011));
    }
}
