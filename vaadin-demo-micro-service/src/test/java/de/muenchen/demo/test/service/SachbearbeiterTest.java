package de.muenchen.demo.test.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.Lists;
import de.muenchen.demo.service.Application;
import de.muenchen.demo.service.domain.Buerger;
import de.muenchen.demo.service.domain.SachbearbeiterRepository;
import de.muenchen.demo.service.domain.Sachbearbeiter;
import de.muenchen.demo.service.services.SachbearbeiterService;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
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
public class SachbearbeiterTest {

    @Autowired
    SachbearbeiterService service;
    @Autowired
    SachbearbeiterRepository repo;
    @Autowired
    CacheManager cacheManager;

    @Test
    @WithMockUser(username = DomainConstants.M2_U001_NAME)
    public void testCacheOnSave() {
        System.out.println("========== save cache Test ==========");
        Cache cache = cacheManager.getCache(SachbearbeiterRepository.Sachbearbeiter_CACHE);
        Sachbearbeiter b1 = this.createSachbearbeiter("OIDC00");
        assertNull(cache.get(b1.getOid() + "2", Sachbearbeiter.class));
        service.save(b1);
        assertNotNull(cache.get(b1.getOid() + b1.getMandant().getOid(), Sachbearbeiter.class));
        System.out.println(String.format("Objekt wurde beim Speichern mit der ID '%s' in den Cache gelegt.", b1.getOid() + b1.getMandant().getOid()));
    }

    @Test
    @WithMockUser(username = DomainConstants.M2_U001_NAME)
    public void testCacheOnRead() {
        System.out.println("========== read cache Test ==========");
        Cache cache = cacheManager.getCache(SachbearbeiterRepository.Sachbearbeiter_CACHE);
        String cacheId = DomainConstants.M2_SA020 + DomainConstants.M2;
        assertNull(cache.get(cacheId));
        Sachbearbeiter b1 = service.read(DomainConstants.M2_SA020);
        assertNotNull(cache.get(b1.getOid() + b1.getMandant().getOid(), Sachbearbeiter.class));
        assertEquals(cacheId, b1.getOid() + b1.getMandant().getOid());
        System.out.println(String.format("Objekt wurde beim Lesen mit der ID '%s' in den Cache gelegt.", b1.getOid() + b1.getMandant().getOid()));
    }

    @Test
    @WithMockUser(username = DomainConstants.M2_U001_NAME)
    public void createTest() throws JsonProcessingException {
        System.out.println("========== create Sachbearbeiter Test ==========");
        Sachbearbeiter b1 = service.create();
        assertNotNull(b1);
        assertNotNull(b1.getOid());
        assertNull(repo.findFirstByOidAndMandantOid(b1.getOid(), DomainConstants.M2));
        System.out.println(String.format("Sachbearbeiter wurde mit neuer OID '%s' erstellt (und nicht in der DB gespeichert)", b1.getOid()));
    }

    @Test
    @WithMockUser(username = DomainConstants.M2_U001_NAME)
    public void saveTest() {
        System.out.println("========== save Sachbearbeiter Test ==========");
        String oid = "OID0";
        Sachbearbeiter b1 = service.save(this.createSachbearbeiter(oid));
        assertEquals(oid, b1.getOid());
        assertEquals(DomainConstants.M2, b1.getMandant().getOid());
        assertNotNull(repo.findFirstByOidAndMandantOid(oid, DomainConstants.M2));
        System.out.println(String.format("Sachbearbeiter wurde mit Mandant '%s' in der DB gespeichert.", b1.getMandant().getOid()));
    }

    private Sachbearbeiter createSachbearbeiter(String oid) {
        Sachbearbeiter sachbearbeiter = new Sachbearbeiter();
        sachbearbeiter.setOid(oid);
        sachbearbeiter.setFax("089658");
        sachbearbeiter.setFunktion("beamter");
        return sachbearbeiter;
    }

    @Test
    @WithMockUser(username = DomainConstants.M2_U001_NAME)
    public void updateTest() {
        System.out.println("========== update Sachbearbeiter Test ==========");
        Sachbearbeiter b1 = service.read(DomainConstants.M2_SA002);
        b1.setFunktion("officier");
        service.update(b1);
        Sachbearbeiter b2 = service.read(DomainConstants.M2_SA002);
        assertEquals("officier", b2.getFunktion());
        System.out.println("Sachbearbeiter wurde mit neuem Vornamen in der DB gespeichert.");
    }

    @Test
    @WithMockUser(username = DomainConstants.M2_U001_NAME)
    public void readTest() {
        System.out.println("========== read Sachbearbeiter Test ==========");
        Sachbearbeiter b1 = service.read(DomainConstants.M2_SA003);
        assertNotNull(b1);
        System.out.println(String.format("Sachbearbeiter konnte mit OID '%s' aus der DB gelesen werden.", b1.getOid()));
    }

    @Test
    @WithMockUser(username = DomainConstants.M2_U001_NAME)
    public void deleteTest() {
        System.out.println("========== delete Sachbearbeiter Test ==========");
        Sachbearbeiter b1 = service.read(DomainConstants.M2_SA004);
        assertNotNull(b1);
        service.delete(DomainConstants.M2_SA004);
        Sachbearbeiter b2 = service.read(DomainConstants.M2_SA004);
        assertNull(b2);
        System.out.println(String.format("Sachbearbeiter konnte mit OID '%s' aus der DB (und dem Cache) gelöscht werden.", DomainConstants.M2_SA004));
    }

    @Test
    @WithMockUser(username = DomainConstants.M2_U001_NAME)
    public void queryTestM2() {
        System.out.println("========== query Sachbearbeiter Test Mandant 2 ==========");
        int x = this.count(DomainConstants.M2);
        List<Sachbearbeiter> bs = service.query();
        assertEquals(x, bs.size());
        System.out.println(String.format("Suche wurde erfolgreich durchgeführt. Einträge Mandant %s: %s | Ergebnis der Suche: %s", DomainConstants.M2, x, bs.size()));
    }

    @Test
    @WithMockUser(username = DomainConstants.M3_U002_NAME)
    public void queryTestM3() {
        System.out.println("========== query Sachbearbeiter Test Mandant 3 ==========");
        int x = this.count(DomainConstants.M3);
        List<Sachbearbeiter> bs = service.query();
        assertEquals(x, bs.size());
        System.out.println(String.format("Suche wurde erfolgreich durchgeführt. Einträge Mandant %s: %s | Ergebnis der Suche: %s", DomainConstants.M3, x, bs.size()));
    }

    private int count(String mid) {
        ArrayList<Sachbearbeiter> all = Lists.newArrayList(repo.findAll());
        return all.stream().filter(b -> b.getMandant().getOid().equals(mid)).collect(Collectors.counting()).intValue();
    }

    @Test
    @WithMockUser(username = DomainConstants.M2_U001_NAME)
    public void copyTest() {
        System.out.println("========== copy Sachbearbeiter Test ==========");
        Sachbearbeiter b1 = service.copy(DomainConstants.M2_SA005);
        assertNotNull(b1);
        Sachbearbeiter b2 = service.read(b1.getOid());
        assertNotNull(b2);
        assertEquals(b1.getFax(), b2.getFax());
        assertEquals(b1.getTelephone(), b2.getTelephone());
        System.out.println(String.format("Objekt mit der OID '%s' konnte erfolgreich in Objekt '%s' kopiert (und in DB gespeichert) werden", DomainConstants.M2_SA005, b2.getOid()));
    }
    @Test
    @WithMockUser(username = DomainConstants.M2_U001_NAME)
    public void releaseSachbearbeiterAllBuergerTest() {
        System.out.println("========== release Sachbearbeiter All Bürger Test ==========");
        Set<Buerger> b1 = service.read(DomainConstants.M2_SA002).getBuerger();
        assertFalse(b1.isEmpty());
        service.releaseSachbearbeiterAllBuerger(DomainConstants.M2_SA002);
        Set<Buerger> b2 = service.read(DomainConstants.M2_SA002).getBuerger();
        assertTrue(b2.isEmpty());
        System.out.println(String.format("release operation für die Sachbearbeiter eines Bürgers mit OID '%s' konnte aus der DB erfolgreich durchgeführt.", DomainConstants.M2_SA015));
    }
    @Test
    @WithMockUser(username = DomainConstants.M2_U001_NAME)
    public void releaseSachbearbeiterBuergerTest() {
        System.out.println("========== release Sachbearbeiter Bürger  Test ==========");
        Sachbearbeiter s1 = service.read(DomainConstants.M2_SA001);
        assertEquals(1, this.checkBuerger(DomainConstants.M2_B001, s1));
        service.releaseSachbearbeiterBuerger(DomainConstants.M2_B001, DomainConstants.M2_SA001);
        Sachbearbeiter s2 = service.read(DomainConstants.M2_SA001);
        assertEquals(0, this.checkBuerger(DomainConstants.M2_B001, s2));
        System.out.println(String.format("release operation für den sachbearbeiter mit OID '%s' und seinen Bürger konnte aus der DB erfolgreich durchgeführt.", DomainConstants.M2_SA001));
    }
    private long checkBuerger(String oid, Sachbearbeiter b1) {
        return b1.getBuerger().stream().filter(k -> k.getOid().equals(oid)).count();
    }
    @Test
    @WithMockUser(username = DomainConstants.M2_U001_NAME)
    public void sachbearbeiterCopyListTest() {
        System.out.println("========== copy Liste Sachbearbeiter Test ==========");
        int x = this.count(DomainConstants.M2);
        ArrayList<String> oids = new ArrayList();
        oids.add(DomainConstants.M2_SA010);
        oids.add(DomainConstants.M2_SA011);
        service.copy(oids);
        List<Sachbearbeiter> bs = service.query();
        assertEquals(x + 2, bs.size());
        System.out.println(String.format("Objekte mit der OID '%s' und der OID '%s' konnte erfolgreich in Objekt  kopiert (und in DB gespeichert) werden", DomainConstants.M2_SA010, DomainConstants.M2_SA011));

    }

    @Test
    @WithMockUser(username = DomainConstants.M2_U001_NAME)
    public void SachbearbeiterDeleteListTest() {
        System.out.println("========== delete Sachbearbeiter Test ==========");
        ArrayList<String> oids = new ArrayList();
        oids.add(DomainConstants.M2_SA012);
        oids.add(DomainConstants.M2_SA013);
        service.delete(oids);
        Sachbearbeiter b1 = service.read(DomainConstants.M2_SA012);
        assertNull(b1);
        Sachbearbeiter b2 = service.read(DomainConstants.M2_SA013);
        assertNull(b2);
        System.out.println(String.format("Sachbearbeiter  mit OID '%s'und OID '%s' konnte aus der DB (und dem Cache) gelöscht werden.", DomainConstants.M2_SA012, DomainConstants.M2_SA013));

    }

}
