package de.muenchen.demo.test.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.Lists;
import de.muenchen.demo.service.Application;
import de.muenchen.demo.service.domain.Adresse;
import de.muenchen.demo.service.domain.AdresseReference;
import de.muenchen.demo.service.domain.AdresseReferenceRepository;
import de.muenchen.demo.service.services.AdresseService;
import de.muenchen.demo.service.services.WohnungService;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
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
public class AdresseServiceTest {

    @Autowired
    WohnungService wohnungService;
    @Autowired
    AdresseService service;
    @Autowired
    AdresseReferenceRepository repo;
    @Autowired
    CacheManager cacheManager;


    @Test
    @WithMockUser(username = DomainConstants.M2_U001_NAME)
    public void createTest() throws JsonProcessingException {
        System.out.println("========== create Adresse Test ==========");
        Adresse b1 = service.create();
        assertNotNull(b1);
        assertNotNull(b1.getOid());
        assertNull(repo.findFirstByOidAndMandantOid(b1.getOid(), DomainConstants.M2));
        System.out.println(String.format("Adresse wurde mit neuer OID '%s' erstellt (und nicht in der DB gespeichert)", b1.getOid()));
    }

    @Test
    @WithMockUser(username = DomainConstants.M2_U001_NAME)
    public void readTest() {
        System.out.println("========== read Adresse Test ==========");
        Adresse b1 = service.read(DomainConstants.M2_AR016);
        assertNotNull(b1);
        System.out.println(String.format("Adresse konnte mit OID '%s' aus der DB gelesen werden.", b1.getOid()));
    }


    @Test
    @WithMockUser(username = DomainConstants.M2_U001_NAME)
    public void queryTestM2() {
        System.out.println("========== query Adresse Test Mandant 2 ==========");
        int x = this.count(DomainConstants.M2);
        List<Adresse> bs = service.query();
        assertEquals(x, bs.size());
        System.out.println(String.format("Suche wurde erfolgreich durchgeführt. Einträge Mandant %s: %s | Ergebnis der Suche: %s", DomainConstants.M2, x, bs.size()));
    }

    @Test
    @WithMockUser(username = DomainConstants.M3_U002_NAME)
    public void queryTestM3() {
        System.out.println("========== query Adresse Test Mandant 3 ==========");
        int x = this.count(DomainConstants.M3);
        List<Adresse> bs = service.query();
        assertEquals(x, bs.size());
        System.out.println(String.format("Suche wurde erfolgreich durchgeführt. Einträge Mandant %s: %s | Ergebnis der Suche: %s", DomainConstants.M3, x, bs.size()));
    }

    private int count(String mid) {
        ArrayList<AdresseReference> all = Lists.newArrayList(repo.findAll());
        return all.stream().filter(b -> b.getMandant().getOid().equals(mid)).collect(Collectors.counting()).intValue();
    }
    

    @Test
    @WithMockUser(username = DomainConstants.M2_U001_NAME)
    public void searchTest() {
        System.out.println("========== suche Adresse Test ==========");
        Adresse adr = new Adresse();
        adr.setStrasseReference(209);
        List<Adresse> b1 = service.suche(adr);
        assertNotNull(b1);
        System.out.println("Suche wurde erfolgreich durchgeführt");
    }

}