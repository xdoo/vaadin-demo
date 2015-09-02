//package de.muenchen.demo.test.service;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.google.common.collect.Lists;
//import de.muenchen.demo.service.Application;
//import de.muenchen.demo.service.domain.WohnungRepository;
//import de.muenchen.demo.service.domain.Wohnung;
//import de.muenchen.demo.service.services.WohnungService;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.stream.Collectors;
//import static org.junit.Assert.*;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.SpringApplicationConfiguration;
//import org.springframework.boot.test.WebIntegrationTest;
//import org.springframework.cache.Cache;
//import org.springframework.cache.CacheManager;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//
///**
// *
// * @author praktikant.tmar
// */
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringApplicationConfiguration(classes = Application.class)
//@WebIntegrationTest({"server.port=0", "management.port=0"})
//public class WohnungTest {
//
//    @Autowired
//    WohnungService service;
//    @Autowired
//    WohnungRepository repo;
//    @Autowired
//    CacheManager cacheManager;
//
//    @Test
//    @WithMockUser(username = DomainConstants.M2_U001_NAME)
//    public void testCacheOnSave() {
//        System.out.println("========== save cache Test ==========");
//        Cache cache = cacheManager.getCache(WohnungRepository.Wohnung_CACHE);
//        Wohnung b1 = this.createWohnung("OIDC00");
//        assertNull(cache.get(b1.getOid() + "2", Wohnung.class));
//        service.save(b1);
//        assertNotNull(cache.get(b1.getOid() + b1.getMandant().getOid(), Wohnung.class));
//        System.out.println(String.format("Objekt wurde beim Speichern mit der ID '%s' in den Cache gelegt.", b1.getOid() + b1.getMandant().getOid()));
//    }
//
//    @Test
//    @WithMockUser(username = DomainConstants.M2_U001_NAME)
//    public void testCacheOnRead() {
//        System.out.println("========== read cache Test ==========");
//        Cache cache = cacheManager.getCache(WohnungRepository.Wohnung_CACHE);
//        String cacheId = DomainConstants.M2_W020 + DomainConstants.M2;
//        assertNull(cache.get(cacheId));
//        Wohnung b1 = service.read(DomainConstants.M2_W020);
//        assertNotNull(cache.get(b1.getOid() + b1.getMandant().getOid(), Wohnung.class));
//        assertEquals(cacheId, b1.getOid() + b1.getMandant().getOid());
//        System.out.println(String.format("Objekt wurde beim Lesen mit der ID '%s' in den Cache gelegt.", b1.getOid() + b1.getMandant().getOid()));
//    }
//
//    @Test
//    @WithMockUser(username = DomainConstants.M2_U001_NAME)
//    public void createTest() throws JsonProcessingException {
//        System.out.println("========== create Wohnung Test ==========");
//        Wohnung b1 = service.create();
//        assertNotNull(b1);
//        assertNotNull(b1.getOid());
//        assertNull(repo.findFirstByOidAndMandantOid(b1.getOid(), DomainConstants.M2));
//        System.out.println(String.format("Wohnung wurde mit neuer OID '%s' erstellt (und nicht in der DB gespeichert)", b1.getOid()));
//    }
//
//    @Test
//    @WithMockUser(username = DomainConstants.M2_U001_NAME)
//    public void saveTest() {
//        System.out.println("========== save Wohnung Test ==========");
//        String oid = "OID0";
//        Wohnung b1 = service.save(this.createWohnung(oid));
//        assertEquals(oid, b1.getOid());
//        assertEquals(DomainConstants.M2, b1.getMandant().getOid());
//        assertNotNull(repo.findFirstByOidAndMandantOid(oid, DomainConstants.M2));
//        System.out.println(String.format("Wohnung wurde mit Mandant '%s' in der DB gespeichert.", b1.getMandant().getOid()));
//    }
//
//    private Wohnung createWohnung(String oid) {
//        Wohnung wohnung = new Wohnung();
//        wohnung.setOid(oid);
//        wohnung.setAusrichtung("Sued");
//        wohnung.setStock("dritte");
//        return wohnung;
//    }
//
//    @Test
//    @WithMockUser(username = DomainConstants.M2_U001_NAME)
//    public void updateTest() {
//        System.out.println("========== update Wohnung Test ==========");
//        Wohnung b1 = service.read(DomainConstants.M2_W002);
//        b1.setAusrichtung("nord");
//        service.update(b1);
//        Wohnung b2 = service.read(DomainConstants.M2_W002);
//        assertEquals("nord", b2.getAusrichtung());
//        System.out.println("Wohnung wurde mit neuem Vornamen in der DB gespeichert.");
//    }
//
//    @Test
//    @WithMockUser(username = DomainConstants.M2_U001_NAME)
//    public void readTest() {
//        System.out.println("========== read Wohnung Test ==========");
//        Wohnung b1 = service.read(DomainConstants.M2_W003);
//        assertNotNull(b1);
//        System.out.println(String.format("Wohnung konnte mit OID '%s' aus der DB gelesen werden.", b1.getOid()));
//    }
//
//    @Test
//    @WithMockUser(username = DomainConstants.M2_U001_NAME)
//    public void deleteTest() {
//        System.out.println("========== delete Wohnung Test ==========");
//        Wohnung b1 = service.read(DomainConstants.M2_W004);
//        assertNotNull(b1);
//        service.delete(DomainConstants.M2_W004);
//        Wohnung b2 = service.read(DomainConstants.M2_W004);
//        assertNull(b2);
//        System.out.println(String.format("Wohnung konnte mit OID '%s' aus der DB (und dem Cache) gelöscht werden.", DomainConstants.M2_W004));
//    }
//
//    @Test
//    @WithMockUser(username = DomainConstants.M2_U001_NAME)
//    public void queryTestM2() {
//        System.out.println("========== query Wohnung Test Mandant 2 ==========");
//        int x = this.count(DomainConstants.M2);
//        List<Wohnung> bs = service.query();
//        assertEquals(x, bs.size());
//        System.out.println(String.format("Suche wurde erfolgreich durchgeführt. Einträge Mandant %s: %s | Ergebnis der Suche: %s", DomainConstants.M2, x, bs.size()));
//    }
//
//    @Test
//    @WithMockUser(username = DomainConstants.M3_U002_NAME)
//    public void queryTestM3() {
//        System.out.println("========== query Wohnung Test Mandant 3 ==========");
//        int x = this.count(DomainConstants.M3);
//        List<Wohnung> bs = service.query();
//        assertEquals(x, bs.size());
//        System.out.println(String.format("Suche wurde erfolgreich durchgeführt. Einträge Mandant %s: %s | Ergebnis der Suche: %s", DomainConstants.M3, x, bs.size()));
//    }
//
//    private int count(String mid) {
//        ArrayList<Wohnung> all = Lists.newArrayList(repo.findAll());
//        return all.stream().filter(b -> b.getMandant().getOid().equals(mid)).collect(Collectors.counting()).intValue();
//    }
//
//    @Test
//    @WithMockUser(username = DomainConstants.M2_U001_NAME)
//    public void copyTest() {
//        System.out.println("========== copy Wohnung Test ==========");
//        Wohnung b1 = service.copy(DomainConstants.M2_W005);
//        assertNotNull(b1);
//        Wohnung b2 = service.read(b1.getOid());
//        assertNotNull(b2);
//        assertEquals(b1.getAusrichtung(), b2.getAusrichtung());
//        assertEquals(b1.getStock(), b2.getStock());
//        System.out.println(String.format("Objekt mit der OID '%s' konnte erfolgreich in Objekt '%s' kopiert (und in DB gespeichert) werden", DomainConstants.M2_W005, b2.getOid()));
//    }
//}