package de.muenchen.demo.test.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.Lists;
import de.muenchen.demo.service.Application;
import de.muenchen.demo.service.domain.Buerger;
import de.muenchen.demo.service.domain.BuergerRepository;
import de.muenchen.demo.service.domain.Pass;
import de.muenchen.demo.service.domain.Sachbearbeiter;
import de.muenchen.demo.service.domain.Wohnung;
import de.muenchen.demo.service.services.BuergerService;
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
public class BuergerServiceTest {

    @Autowired
    BuergerService service;
    @Autowired
    BuergerRepository repo;
    @Autowired
    CacheManager cacheManager;

    @Test
    @WithMockUser(username = DomainConstants.M2_U001_NAME)
    public void testCacheOnSave() {
        System.out.println("========== save cache Test ==========");
        Cache cache = cacheManager.getCache(BuergerRepository.BUERGER_CACHE);
        Buerger b1 = this.createBuerger("OIDC00");
        assertNull(cache.get(b1.getOid() + "2", Buerger.class));
        service.save(b1);
        assertNotNull(cache.get(b1.getOid() + b1.getMandant().getOid(), Buerger.class));
        System.out.println(String.format("Objekt wurde beim Speichern mit der ID '%s' in den Cache gelegt.", b1.getOid() + b1.getMandant().getOid()));
    }

    @Test
    @WithMockUser(username = DomainConstants.M2_U001_NAME)
    public void testCacheOnRead() {
        System.out.println("========== read cache Test ==========");
        Cache cache = cacheManager.getCache(BuergerRepository.BUERGER_CACHE);
        String cacheId = DomainConstants.M2_B029 + DomainConstants.M2;
        assertNull(cache.get(cacheId));
        Buerger b1 = service.read(DomainConstants.M2_B029);
        assertNotNull(cache.get(b1.getOid() + b1.getMandant().getOid(), Buerger.class));
        assertEquals(cacheId, b1.getOid() + b1.getMandant().getOid());
        System.out.println(String.format("Objekt wurde beim Lesen mit der ID '%s' in den Cache gelegt.", b1.getOid() + b1.getMandant().getOid()));
    }

    @Test
    @WithMockUser(username = DomainConstants.M2_U001_NAME)
    public void createTest() throws JsonProcessingException {
        System.out.println("========== create Bürger Test ==========");
        Buerger b1 = service.create();
        assertNotNull(b1);
        assertNotNull(b1.getOid());
        assertNull(repo.findFirstByOidAndMandantOid(b1.getOid(), DomainConstants.M2));
        System.out.println(String.format("Bürger wurde mit neuer OID '%s' erstellt (und nicht in der DB gespeichert)", b1.getOid()));
    }

    @Test
    @WithMockUser(username = DomainConstants.M2_U001_NAME)
    public void saveTest() {
        System.out.println("========== save Bürger Test ==========");
        String oid = "OID0";
        Buerger b1 = service.save(this.createBuerger(oid));
        assertEquals(oid, b1.getOid());
        assertEquals(DomainConstants.M2, b1.getMandant().getOid());
        assertNotNull(repo.findFirstByOidAndMandantOid(oid, DomainConstants.M2));
        System.out.println(String.format("Bürger wurde mit Mandant '%s' in der DB gespeichert.", b1.getMandant().getOid()));
    }

    private Buerger createBuerger(String oid) {
        Buerger buerger = new Buerger();
        buerger.setOid(oid);
        buerger.setNachname("hans");
        buerger.setVorname("peter");
        return buerger;
    }

    @Test
    @WithMockUser(username = DomainConstants.M2_U001_NAME)
    public void updateTest() {
        System.out.println("========== update Bürger Test ==========");
        Buerger b1 = service.read(DomainConstants.M2_B002);
        b1.setVorname("dagobert");
        service.update(b1);
        Buerger b2 = service.read(DomainConstants.M2_B002);
        assertEquals("dagobert", b2.getVorname());
        System.out.println("Bürger wurde mit neuem Vornamen in der DB gespeichert.");
    }

    @Test
    @WithMockUser(username = DomainConstants.M2_U001_NAME)
    public void readTest() {
        System.out.println("========== read Bürger Test ==========");
        Buerger b1 = service.read(DomainConstants.M2_B003);
        assertNotNull(b1);
        System.out.println(String.format("Bürger konnte mit OID '%s' und Mandant 2 aus der DB gelesen werden.", b1.getOid()));
        Buerger b2 = service.read(DomainConstants.M3_B003);
        assertNull(b2);
        System.out.println(String.format("Bürger konnte mit OID '%s' und Mandant 3 aus der DB nicht gelesen werden.", DomainConstants.M3_B003));
    }

    @Test
    @WithMockUser(username = DomainConstants.M2_U001_NAME)
    public void deleteTest() {
        System.out.println("========== delete Bürger Test ==========");
        Buerger b1 = service.read(DomainConstants.M2_B004);
        assertNotNull(b1);
        service.delete(DomainConstants.M2_B004);
        Buerger b2 = service.read(DomainConstants.M2_B004);
        assertNull(b2);
        System.out.println(String.format("Bürger konnte mit OID '%s' aus der DB (und dem Cache) gelöscht werden.", DomainConstants.M2_B004));
    }
    
    @Test
    @WithMockUser(username = DomainConstants.M3_U002_NAME)
    public void createM3Test() throws JsonProcessingException {
        System.out.println("========== create Bürger M3 Test ==========");
        Buerger b1 = service.create();
        assertNotNull(b1);
        assertNotNull(b1.getOid());
        assertNull(repo.findFirstByOidAndMandantOid(b1.getOid(), DomainConstants.M3));
        System.out.println(String.format("Bürger wurde mit neuer OID '%s' erstellt (und nicht in der DB gespeichert)", b1.getOid()));
    }

    @Test
    @WithMockUser(username = DomainConstants.M3_U002_NAME)
    public void saveM3Test() {
        System.out.println("========== save Bürger Test ==========");
        String oid = "OID0M3";
        Buerger b1 = service.save(this.createBuerger(oid));
        assertEquals(oid, b1.getOid());
        assertEquals(DomainConstants.M3, b1.getMandant().getOid());
        assertNotNull(repo.findFirstByOidAndMandantOid(oid, DomainConstants.M3));
        System.out.println(String.format("Bürger wurde mit Mandant '%s' in der DB gespeichert.", b1.getMandant().getOid()));
    }

    @Test
    @WithMockUser(username = DomainConstants.M3_U002_NAME)
    public void updateM3Test() {
        System.out.println("========== update Bürger Test ==========");
        Buerger b1 = service.read(DomainConstants.M3_B002);
        b1.setVorname("armin");
        service.update(b1);
        Buerger b2 = service.read(DomainConstants.M3_B002);
        assertEquals("armin", b2.getVorname());
        System.out.println("Bürger wurde mit neuem Vornamen in der DB gespeichert.");
    }

    @Test
    @WithMockUser(username = DomainConstants.M3_U002_NAME)
    public void readM3Test() {
        System.out.println("========== read Bürger M3 Test ==========");
        Buerger b1 = service.read(DomainConstants.M3_B003);
        assertNotNull(b1);
        System.out.println(String.format("Bürger konnte mit OID '%s' und Mandant 3 aus der DB nicht gelesen werden.", DomainConstants.M2_B003));
    }

    @Test
    @WithMockUser(username = DomainConstants.M3_U002_NAME)
    public void deleteM3Test() {
        System.out.println("========== delete Bürger Test ==========");
        Buerger b1 = service.read(DomainConstants.M3_B004);
        assertNotNull(b1);
        service.delete(DomainConstants.M3_B004);
        Buerger b2 = service.read(DomainConstants.M3_B004);
        assertNull(b2);
        System.out.println(String.format("Bürger konnte mit OID '%s' aus der DB (und dem Cache) gelöscht werden.", DomainConstants.M3_B004));
    }

    @Test
    @WithMockUser(username = DomainConstants.M3_U002_NAME)
    public void copyM3Test() {
        System.out.println("========== copy Bürger Test ==========");
        Buerger b1 = service.copy(DomainConstants.M3_B005);
        assertNotNull(b1);
        Buerger b2 = service.read(b1.getOid());
        assertNotNull(b2);
        assertEquals(b1.getVorname(), b2.getVorname());
        assertEquals(b1.getNachname(), b2.getNachname());
        System.out.println(String.format("Objekt mit der OID '%s' konnte erfolgreich in Objekt '%s' kopiert (und in DB gespeichert) werden", DomainConstants.M3_B005, b2.getOid()));
    }

    @Test
    @WithMockUser(username = DomainConstants.M3_U002_NAME)
    public void copyListBuergerM3Test() {
        System.out.println("========== copy Liste Bürger Test ==========");
        int x = this.count(DomainConstants.M3);
        ArrayList<String> oids = new ArrayList();
        oids.add(DomainConstants.M3_B007);
        oids.add(DomainConstants.M3_B008);
        service.copy(oids);
        List<Buerger> bs = service.query();
        assertEquals(x + 2, bs.size());
        System.out.println(String.format("Objekte mit der OID '%s' und der OID '%s' konnte erfolgreich in Objekt  kopiert (und in DB gespeichert) werden", DomainConstants.M3_B007, DomainConstants.M3_B008));

    }

    @Test
    @WithMockUser(username = DomainConstants.M3_U002_NAME)
    public void buergerDeleteM3ListTest() {
        System.out.println("========== delete Bürger Test ==========");
        ArrayList<String> oids = new ArrayList();
        oids.add(DomainConstants.M3_B009);
        oids.add(DomainConstants.M3_B010);
        service.delete(oids);
        Buerger b1 = service.read(DomainConstants.M3_B010);
        assertNull(b1);
        Buerger b2 = service.read(DomainConstants.M3_B009);
        assertNull(b2);
        System.out.println(String.format("Bürger  mit OID '%s'und OID '%s' konnte aus der DB (und dem Cache) gelöscht werden.", DomainConstants.M3_B010, DomainConstants.M3_B009));

    }

    @Test
    @WithMockUser(username = DomainConstants.M2_U001_NAME)
    public void queryTestM2() {
        System.out.println("========== query Bürger Test Mandant 2 ==========");
        int x = this.count(DomainConstants.M2);
        List<Buerger> bs = service.query();
        assertEquals(x, bs.size());
        System.out.println(String.format("Suche wurde erfolgreich durchgeführt. Einträge Mandant %s: %s | Ergebnis der Suche: %s", DomainConstants.M2, x, bs.size()));
    }

    @Test
    @WithMockUser(username = DomainConstants.M3_U002_NAME)
    public void queryTestM3() {
        System.out.println("========== query Bürger Test Mandant 3 ==========");
        int x = this.count(DomainConstants.M3);
        List<Buerger> bs = service.query();
        assertEquals(x, bs.size());
        System.out.println(String.format("Suche wurde erfolgreich durchgeführt. Einträge Mandant %s: %s | Ergebnis der Suche: %s", DomainConstants.M3, x, bs.size()));
    }

    private int count(String mid) {
        ArrayList<Buerger> all = Lists.newArrayList(repo.findAll());
        return all.stream().filter(b -> b.getMandant().getOid().equals(mid)).collect(Collectors.counting()).intValue();
    }

    @Test
    @WithMockUser(username = DomainConstants.M2_U001_NAME)
    public void copyTest() {
        System.out.println("========== copy Bürger Test ==========");
        Buerger b1 = service.copy(DomainConstants.M2_B005);
        assertNotNull(b1);
        Buerger b2 = service.read(b1.getOid());
        assertNotNull(b2);
        assertEquals(b1.getVorname(), b2.getVorname());
        assertEquals(b1.getNachname(), b2.getNachname());
        System.out.println(String.format("Objekt mit der OID '%s' konnte erfolgreich in Objekt '%s' kopiert (und in DB gespeichert) werden", DomainConstants.M2_B005, b2.getOid()));
        Buerger b3 = service.copy(DomainConstants.M3_B005);
        assertNull(b3);
        System.out.println(String.format("Objekt mit der OID '%s' und der Mandant 3 konnte nicht in einem    Objekt  kopiert  werden", DomainConstants.M3_B005));

    }

    @Test
    @WithMockUser(username = DomainConstants.M2_U001_NAME)
    public void copyListBuergerTest() {
        System.out.println("========== copy Liste Bürger Test ==========");
        int x = this.count(DomainConstants.M2);
        ArrayList<String> oids = new ArrayList();
        oids.add(DomainConstants.M2_B027);
        oids.add(DomainConstants.M2_B028);
        service.copy(oids);
        List<Buerger> bs = service.query();
        assertEquals(x + 2, bs.size());
        System.out.println(String.format("Objekte mit der OID '%s' und der OID '%s' konnte erfolgreich in Objekt  kopiert (und in DB gespeichert) werden", DomainConstants.M2_B027, DomainConstants.M2_B028));

    }

    @Test
    @WithMockUser(username = DomainConstants.M2_U001_NAME)
    public void buergerDeleteListTest() {
        System.out.println("========== delete Bürger Test ==========");
        ArrayList<String> oids = new ArrayList();
        oids.add(DomainConstants.M2_B022);
        oids.add(DomainConstants.M2_B023);
        service.delete(oids);
        Buerger b1 = service.read(DomainConstants.M2_B022);
        assertNull(b1);
        Buerger b2 = service.read(DomainConstants.M2_B023);
        assertNull(b2);
        System.out.println(String.format("Bürger  mit OID '%s'und OID '%s' konnte aus der DB (und dem Cache) gelöscht werden.", DomainConstants.M2_B022, DomainConstants.M2_B023));

    }

    @Test
    @WithMockUser(username = DomainConstants.M2_U001_NAME)
    public void readPassBürgerTest() {
        System.out.println("========== read Pass Bürger Test ==========");
        Buerger b1 = service.readPassBuerger(DomainConstants.M2_P001);
        assertNotNull(b1);
        System.out.println(String.format("Bürger, der hat ein Pass mit OID '%s',  konnte aus der DB gelesen werden.", b1.getOid()));
    }

    @Test
    @WithMockUser(username = DomainConstants.M2_U001_NAME)
    public void readElternTest() {
        System.out.println("========== read Eltern Test ==========");
        Iterable<Buerger> b1 = service.readEltern(DomainConstants.M2_B001);
        assertFalse(Lists.newArrayList(b1).isEmpty());
        System.out.println(String.format("die Eltern von dem Bürger mit OID '%s' konnten aus der DB gelesen werden.", DomainConstants.M2_B001));
    }

    @Test
    @WithMockUser(username = DomainConstants.M2_U001_NAME)
    public void releaseBuergerElternteilTest() {
        System.out.println("========== release Bürger Elternteil Test ==========");
        Buerger b1 = service.read(DomainConstants.M2_B001);
        assertEquals(1, this.checkChild(DomainConstants.M2_B011, b1));
        service.releaseBuergerElternteil(DomainConstants.M2_B011, DomainConstants.M2_B001);
        Buerger b2 = service.read(DomainConstants.M2_B001);
        assertEquals(0, this.checkChild(DomainConstants.M2_B011, b2));
        System.out.println(String.format("release operation für den Bürger mit OID '%s' und den elternteil mit OID '%s' konnte aus der DB erfolgreich durchgeführt.", DomainConstants.M2_B011, DomainConstants.M2_B001));
    }

    @Test
    @WithMockUser(username = DomainConstants.M2_U001_NAME)
    public void releaseBuergerElternTest() {
        System.out.println("========== release Bürger Eltern Test ==========");
        ArrayList<Buerger> b1 = Lists.newArrayList(service.readEltern(DomainConstants.M2_B012));
        assertFalse(b1.isEmpty());
        service.releaseBuergerEltern(DomainConstants.M2_B012);
        ArrayList<Buerger> b2 = Lists.newArrayList(service.readEltern(DomainConstants.M2_B012));
        assertTrue(b2.isEmpty());
        System.out.println(String.format("release operation für den Eltern eines Bürgers mit OID '%s' konnte aus der DB erfolgreich durchgeführt.", DomainConstants.M2_B012));
    }

    @Test
    @WithMockUser(username = DomainConstants.M2_U001_NAME)
    public void releaseBuergerKinderTest() {
        System.out.println("========== release Bürger Kinder Test ==========");
        ArrayList<Buerger> b1 = Lists.newArrayList(service.read(DomainConstants.M2_B002).getKinder());
        assertFalse(b1.isEmpty());
        service.releaseBuergerKinder(DomainConstants.M2_B002);
        ArrayList<Buerger> b2 = Lists.newArrayList(service.read(DomainConstants.M2_B002).getKinder());
        assertTrue(b2.isEmpty());
        System.out.println(String.format("release operation für die kinder eines Bürgers mit OID '%s' konnte aus der DB erfolgreich durchgeführt.", DomainConstants.M2_B002));
    }

    private long checkChild(String oid, Buerger b1) {
        return b1.getKinder().stream().filter(k -> k.getOid().equals(oid)).count();
    }

    @Test
    @WithMockUser(username = DomainConstants.M2_U001_NAME)
    public void readPassBuergerTest() {
        System.out.println("========== read Pass Test ==========");
        Buerger b1 = service.readPassBuerger(DomainConstants.M2_P001);
        assertFalse(Lists.newArrayList(b1).isEmpty());
        System.out.println(String.format("der Bürger von dem Pass mit OID '%s' konnte aus der DB gelesen werden.", DomainConstants.M2_P001));
    }

    @Test
    @WithMockUser(username = DomainConstants.M2_U001_NAME)
    public void releaseBuergerPassTest() {
        System.out.println("========== release Bürger Pass Test ==========");
        Buerger b1 = service.readPassBuerger(DomainConstants.M2_P003);
        assertEquals(1, this.checkPass(DomainConstants.M2_P003, b1));
        service.releasePassBuerger(DomainConstants.M2_P003);
        Buerger b2 = service.read(b1.getOid());
        assertEquals(0, this.checkPass(DomainConstants.M2_P003, b2));
        System.out.println(String.format("release operation für den pass mit OID '%s' und seinen Bürger konnte aus der DB erfolgreich durchgeführt.", DomainConstants.M2_P001));
    }

    @Test
    @WithMockUser(username = DomainConstants.M2_U001_NAME)
    public void releaseBuergerPaesseTest() {
        System.out.println("========== release Bürger Paesse Test ==========");
        Set<Pass> b1 = service.read(DomainConstants.M2_B002).getPass();
        assertFalse(b1.isEmpty());
        service.releaseBuergerPaesse(DomainConstants.M2_B002);
        Set<Pass> b2 = service.read(DomainConstants.M2_B002).getPass();
        assertTrue(b2.isEmpty());
        System.out.println(String.format("release operation für die Paesse eines Bürgers mit OID '%s' konnte aus der DB erfolgreich durchgeführt.", DomainConstants.M2_B002));
    }

    private long checkPass(String oid, Buerger b1) {
        return b1.getPass().stream().filter(k -> k.getOid().equals(oid)).count();
    }

    @Test
    @WithMockUser(username = DomainConstants.M2_U001_NAME)
    public void readWohnungBuergerTest() {
        System.out.println("========== read Wohnung Bürger Test ==========");
        Iterable<Buerger> b1 = service.readWohnungBuerger(DomainConstants.M2_W011);
        assertFalse(Lists.newArrayList(b1).isEmpty());
        System.out.println(String.format("die Eltern von dem Bürger mit OID '%s' konnten aus der DB gelesen werden.", DomainConstants.M2_B001));
    }

    @Test
    @WithMockUser(username = DomainConstants.M2_U001_NAME)
    public void releaseWohnungBuergerTest() {
        System.out.println("========== release Wohnung Bürger  Test ==========");
        Buerger b1 = service.read(DomainConstants.M2_B001);
        assertEquals(1, this.checkWohnung(DomainConstants.M2_W001, b1));
        service.releaseWohnungBuerger(DomainConstants.M2_W001, DomainConstants.M2_B001);
        Buerger b2 = service.read(DomainConstants.M2_B001);
        assertEquals(0, this.checkWohnung(DomainConstants.M2_W001, b2));
        System.out.println(String.format("release operation für den pass mit OID '%s' und seinen Bürger konnte aus der DB erfolgreich durchgeführt.", DomainConstants.M2_P001));
    }

    @Test
    @WithMockUser(username = DomainConstants.M2_U001_NAME)
    public void releaseWohnungAllBuergerTest() {
        System.out.println("========== release Wohnung Bürger Test ==========");
        ArrayList<Buerger> b1 = Lists.newArrayList(service.readWohnungBuerger(DomainConstants.M2_W017));
        assertFalse(b1.isEmpty());
        service.releaseWohnungAllBuerger(DomainConstants.M2_W017);
        ArrayList<Buerger> b2 = Lists.newArrayList(service.readWohnungBuerger(DomainConstants.M2_W017));
        assertTrue(b2.isEmpty());
        System.out.println(String.format("release operation für den Eltern eines Bürgers mit OID '%s' konnte aus der DB erfolgreich durchgeführt.", DomainConstants.M2_B012));
    }

    @Test
    @WithMockUser(username = DomainConstants.M2_U001_NAME)
    public void releaseBuergerWohnungTest() {
        System.out.println("========== release Bürger Wohnungen Test ==========");
        Set<Wohnung> b1 = service.read(DomainConstants.M2_B014).getWohnungen();
        assertFalse(b1.isEmpty());
        service.releaseBuergerWohnungen(DomainConstants.M2_B014);
        Set<Wohnung> b2 = service.read(DomainConstants.M2_B014).getWohnungen();
        assertTrue(b2.isEmpty());
        System.out.println(String.format("release operation für die kinder eines Bürgers mit OID '%s' konnte aus der DB erfolgreich durchgeführt.", DomainConstants.M2_B002));
    }

    private long checkWohnung(String oid, Buerger b1) {
        return b1.getWohnungen().stream().filter(k -> k.getOid().equals(oid)).count();
    }

    @Test
    @WithMockUser(username = DomainConstants.M2_U001_NAME)
    public void readStaatsangehoerigkeitBuergerTest() {
        System.out.println("========== read Staatsangehoerigkeit Bürger Test ==========");
        Iterable<Buerger> b1 = service.readStaatsangehoerigkeitBuerger(DomainConstants.M2_S011);
        assertNotNull(b1);
        System.out.println(String.format("die Eltern von dem Bürger mit OID '%s' konnten aus der DB gelesen werden.", DomainConstants.M2_B001));
    }

    @Test
    @WithMockUser(username = DomainConstants.M2_U001_NAME)
    public void releaseStaatsangehoerigkeitBuergerTest() {
        System.out.println("========== release Staatsangehoerigkeit Bürger  Test ==========");
        Buerger b1 = service.read(DomainConstants.M2_B001);
        assertEquals(1, this.checkStaatsangehoerigkeit(DomainConstants.M2_S001, b1));
        service.releaseStaatsangehoerigkeitBuerger(DomainConstants.M2_S001, DomainConstants.M2_B001);
        Buerger b2 = service.read(DomainConstants.M2_B001);
        assertEquals(0, this.checkStaatsangehoerigkeit(DomainConstants.M2_S001, b2));
        System.out.println(String.format("release operation für den pass mit OID '%s' und seinen Bürger konnte aus der DB erfolgreich durchgeführt.", DomainConstants.M2_P001));
    }

    @Test
    @WithMockUser(username = DomainConstants.M2_U001_NAME)
    public void releaseStaatsangehoerigkeitAllBuergerTest() {
        System.out.println("========== release Staatsangehoerigkeit Bürger Test ==========");
        ArrayList<Buerger> b1 = Lists.newArrayList(service.readStaatsangehoerigkeitBuerger(DomainConstants.M2_S017));
        assertFalse(b1.isEmpty());
        service.releaseStaatsangehoerigkeitAllBuerger(DomainConstants.M2_S017);
        ArrayList<Buerger> b2 = Lists.newArrayList(service.readStaatsangehoerigkeitBuerger(DomainConstants.M2_S017));
        assertTrue(b2.isEmpty());
        System.out.println(String.format("release operation für den Eltern eines Bürgers mit OID '%s' konnte aus der DB erfolgreich durchgeführt.", DomainConstants.M2_B012));
    }

    private long checkStaatsangehoerigkeit(String oid, Buerger b1) {
        return b1.getStaatsangehoerigkeitReferences().stream().filter(k -> k.getReferencedOid().equals(oid)).count();
    }

    @Test
    @WithMockUser(username = DomainConstants.M2_U001_NAME)
    public void releaseBuergerAllSAchbearbeiterTest() {
        System.out.println("========== release Bürger Wohnungen Test ==========");
        Set<Sachbearbeiter> b1 = service.read(DomainConstants.M2_B014).getSachbearbeiter();
        assertFalse(b1.isEmpty());
        service.releaseBuergerAllSachbearbeiter(DomainConstants.M2_B014);
        Set<Sachbearbeiter> b2 = service.read(DomainConstants.M2_B014).getSachbearbeiter();
        assertTrue(b2.isEmpty());
        System.out.println(String.format("release operation für die kinder eines Bürgers mit OID '%s' konnte aus der DB erfolgreich durchgeführt.", DomainConstants.M2_B002));
    }


    @Test
    @WithMockUser(username = DomainConstants.M3_U002_NAME)
    public void readElternM3Test() {
        System.out.println("========== read Eltern Test ==========");
        Iterable<Buerger> b1 = service.readEltern(DomainConstants.M3_B006);
        assertFalse(Lists.newArrayList(b1).isEmpty());
        System.out.println(String.format("die Eltern von dem Bürger mit OID '%s' konnten aus der DB gelesen werden.", DomainConstants.M3_B006));
        Iterable<Buerger> b2 = service.readEltern(DomainConstants.M2_B010);
        assertTrue(Lists.newArrayList(b2).isEmpty());
        System.out.println(String.format("die Eltern von dem Bürger mit OID '%s'und mandant 2 konnten aus der DB nicht gelesen werden.", DomainConstants.M3_B010));
    }

    @Test
    @WithMockUser(username = DomainConstants.M3_U002_NAME)
    public void releaseBuergerElternteilM3Test() {
        System.out.println("========== release Bürger Elternteil Test ==========");
        Buerger b1 = service.read(DomainConstants.M3_B001);
        assertEquals(1, this.checkChild(DomainConstants.M3_B006, b1));
        service.releaseBuergerElternteil(DomainConstants.M3_B006, DomainConstants.M3_B001);
        Buerger b2 = service.read(DomainConstants.M3_B001);
        assertEquals(0, this.checkChild(DomainConstants.M3_B006, b2));
        System.out.println(String.format("release operation für den Bürger mit OID '%s' und den elternteil mit OID '%s' konnte aus der DB erfolgreich durchgeführt.", DomainConstants.M3_B004, DomainConstants.M3_B001));
    }

    @Test
    @WithMockUser(username = DomainConstants.M3_U002_NAME)
    public void releaseBuergerElternM3Test() {
        System.out.println("========== release Bürger Eltern Test ==========");
        ArrayList<Buerger> b1 = Lists.newArrayList(service.readEltern(DomainConstants.M3_B008));
        assertFalse(b1.isEmpty());
        service.releaseBuergerEltern(DomainConstants.M3_B008);
        ArrayList<Buerger> b2 = Lists.newArrayList(service.readEltern(DomainConstants.M3_B008));
        assertTrue(b2.isEmpty());
        System.out.println(String.format("release operation für den Eltern eines Bürgers mit OID '%s' konnte aus der DB erfolgreich durchgeführt.", DomainConstants.M3_B008));
    }

    @Test
    @WithMockUser(username = DomainConstants.M3_U002_NAME)
    public void releaseBuergerKinderM3Test() {
        System.out.println("========== release Bürger Kinder Test ==========");
        ArrayList<Buerger> b1 = Lists.newArrayList(service.read(DomainConstants.M3_B001).getKinder());
        assertFalse(b1.isEmpty());
        service.releaseBuergerKinder(DomainConstants.M3_B001);
        ArrayList<Buerger> b2 = Lists.newArrayList(service.read(DomainConstants.M3_B001).getKinder());
        assertTrue(b2.isEmpty());
        System.out.println(String.format("release operation für die kinder eines Bürgers mit OID '%s' konnte aus der DB erfolgreich durchgeführt.", DomainConstants.M3_B001));
    }

    @Test
    @WithMockUser(username = DomainConstants.M3_U002_NAME)
    public void readPassBuergerM3Test() {
        System.out.println("========== read Pass Test ==========");
        Buerger b1 = service.readPassBuerger(DomainConstants.M3_P001);
        assertNotNull(b1);
        System.out.println(String.format("der Bürger von dem Pass mit OID '%s' konnte aus der DB gelesen werden.", DomainConstants.M3_P001));
        Buerger b2 = service.readPassBuerger(DomainConstants.M2_P001);
        assertNull(b2);
        System.out.println(String.format("der Bürger von dem Pass mit OID '%s' und den Mandant 3 konnte aus der DB gelesen werden.", DomainConstants.M2_P001));

    }


    
    @Test
    @WithMockUser(username = DomainConstants.M3_U002_NAME)
    public void readWohnungBuergerM3Test() {
        System.out.println("========== read Wohnung Bürger Test ==========");
        
        Iterable<Buerger> b1 = service.readWohnungBuerger(DomainConstants.M3_W001);
        assertFalse(Lists.newArrayList(b1).isEmpty());
        System.out.println(String.format("die Büreger von dem Wohnungen mit OID '%s' konnten aus der DB gelesen werden.", DomainConstants.M3_W001));
        
        Iterable<Buerger> b2 = service.readWohnungBuerger(DomainConstants.M2_W011);
        assertTrue(Lists.newArrayList(b2).isEmpty());
        System.out.println(String.format("die Büreger von dem Wohnungen mit OID '%s'und Mandant 2 konnten nicht aus der DB gelesen werden.", DomainConstants.M2_W011));
    
    }

    @Test
    @WithMockUser(username = DomainConstants.M3_U002_NAME)
    public void readStaatsangehoerigkeitBuergerM3Test() {
        System.out.println("========== read Staatsangehoerigkeit Bürger Test ==========");
        Iterable<Buerger> b1 = service.readStaatsangehoerigkeitBuerger(DomainConstants.M3_S001);
        assertFalse(Lists.newArrayList(b1).isEmpty());
        System.out.println(String.format("die Bürger von dem Staatsangehoerigkeit mit OID '%s' konnten aus der DB gelesen werden.", DomainConstants.M3_S001));
        Iterable<Buerger> b2 = service.readStaatsangehoerigkeitBuerger(DomainConstants.M2_S001);
        assertTrue(Lists.newArrayList(b2).isEmpty());
        System.out.println(String.format("die Eltern von dem Staatsangehoerigkeit mit OID '%s' und Mandant 2 konnten nicht aus der DB gelesen werden.", DomainConstants.M3_S001));
    }

}
