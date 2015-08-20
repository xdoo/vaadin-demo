package de.muenchen.demo.test.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.muenchen.demo.service.Application;
import de.muenchen.demo.service.domain.Buerger;
import de.muenchen.demo.service.domain.BuergerRepository;
import de.muenchen.demo.service.services.BuergerService;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
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

    @Autowired BuergerService service;
    @Autowired BuergerRepository repo;

    @Test @WithMockUser(username = "hans")
    public void createTest() throws JsonProcessingException {
        System.out.println("========== create Bürger Test ==========");
        Buerger a = service.create();
        assertNotEquals(null, a.getOid());
    }

    @Test @WithMockUser(username = "hans")
    public void saveTest() {
        System.out.println("========== save Bürger Test ==========");
        Buerger buerger = new Buerger();
        buerger.setOid("OID0");
        buerger.setNachname("hans");
        buerger.setVorname("peter");
        Buerger a = service.save(buerger);
        assertEquals("OID0", a.getOid());
        assertEquals("2", a.getMandant().getOid());
    }

    @Test @WithMockUser(username = "hans")
    public void updateTest() {
        System.out.println("========== update Bürger Test ==========");
        Buerger b1 = service.read("OID1");
        b1.setVorname("dagobert");
        service.update(b1);
        
        // check
        Buerger b2 = service.read("OID1");
        assertEquals("dagobert", b2.getVorname());

    }

    @Test @WithMockUser(username = "hans")
    public void readTest() {
        System.out.println("========== read Bürger Test ==========");
        Buerger b1 = service.read("OID2");
        assertNotNull(b1);
    }

    @Test @WithMockUser(username = "hans")
    public void deleteTest() {
        System.out.println("========== delete Bürger Test ==========");
        Buerger b1 = service.read("OID3");
        assertNotNull(b1);
        service.delete(b1.getOid());
        Buerger b2 = service.read("OID3");
        assertNull(b2);
    }

    @Test @WithMockUser(username = "hans")
    public void queryTest() {
        long num = repo.count();
        List<Buerger> bs = service.query();
        assertEquals(num, bs.size());
    }

    @Test @WithMockUser(username = "hans")
    public void copyTest() {
        System.out.println("========== copy Bürger Test ==========");
        Buerger b1 = service.copy("OID4");
        assertNotNull(b1);
        Buerger b2 = service.read(b1.getOid());
        assertNotNull(b2);
    }
}
