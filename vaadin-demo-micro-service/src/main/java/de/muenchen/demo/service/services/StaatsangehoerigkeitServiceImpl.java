/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.demo.service.services;

import de.muenchen.demo.service.domain.Mandant;
import de.muenchen.demo.service.domain.Staatsangehoerigkeit;
import de.muenchen.demo.service.domain.StaatsangehoerigkeitReference;
import de.muenchen.demo.service.domain.StaatsangehoerigkeitReferenceRepository;
import de.muenchen.demo.service.domain.User;
import de.muenchen.demo.service.util.QueryService;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author praktikant.tmar
 */
@Service
public class StaatsangehoerigkeitServiceImpl implements StaatsangehoerigkeitService {

    @Autowired
    MandantService madantService;
    @Autowired
    BuergerService buergerService;
    @Autowired
    PassService passService;
    UserService userService;
    @Value("${URL}")
    private String URL;
    RestTemplate restTemplate = new TestRestTemplate();
    QueryService<Staatsangehoerigkeit> search;

    StaatsangehoerigkeitReferenceRepository repo;

    @Autowired
    public StaatsangehoerigkeitServiceImpl(StaatsangehoerigkeitReferenceRepository repo, UserService userService, EntityManager em) {
        this.repo = repo;
        this.userService = userService;
        this.search = new QueryService<>(userService, em, Staatsangehoerigkeit.class, "reference", "land", "sprache");
    }

    @Override
    public Staatsangehoerigkeit read(String referencedOid) {

        List<StaatsangehoerigkeitReference> result = this.repo.findByReferencedOidAndMandantOid(referencedOid, readUser().getMandant().getOid());
        if (result.isEmpty()) {

            return null;
        } else {

            String URL2 = URL + "staat/" + result.get(0).getReferencedOid();
            ResponseEntity<Staatsangehoerigkeit> result2 = this.restTemplate.getForEntity(URL2, Staatsangehoerigkeit.class);
            return result2.getBody();
        }
    }

    @Override
    public StaatsangehoerigkeitReference readReference(String referencedOid) {

        List<StaatsangehoerigkeitReference> result = this.repo.findByReferencedOidAndMandantOid(referencedOid, readUser().getMandant().getOid());
        if (result.isEmpty()) {

            return null;
        } else {

            return result.get(0);
        }
    }

    @Override
    public List<Staatsangehoerigkeit> query() {

        ArrayList<Staatsangehoerigkeit> list = new ArrayList();
        Iterable<StaatsangehoerigkeitReference> all = this.repo.findByMandantOid(readUser().getMandant().getOid());

        for (StaatsangehoerigkeitReference staat : all) {
            list.add(read(staat.getReferencedOid()));
        }
        return list;
    }

    @Override
    public Staatsangehoerigkeit create(String referencedOid) {

        String URL2 = URL + "staat/" + referencedOid;
        Staatsangehoerigkeit resource = this.restTemplate.getForEntity(URL2, Staatsangehoerigkeit.class).getBody();
        if (resource == null) {
            return null;
        } else {
            StaatsangehoerigkeitReference staatsangehoerigkeitReference = new StaatsangehoerigkeitReference();
            staatsangehoerigkeitReference.setReferencedOid(referencedOid);
            Mandant mandant = madantService.read(readUser().getMandant().getOid());
            staatsangehoerigkeitReference.setMandant(mandant);
            this.repo.save(staatsangehoerigkeitReference);
            return resource;
        }
    }

    @Override
    public void delete(String referencedOid) {
        StaatsangehoerigkeitReference item = this.readReference(referencedOid);
        this.buergerService.releaseStaatsangehoerigkeitAllBuerger(referencedOid);
        this.passService.deletePassStaat(referencedOid);

        this.repo.delete(item);
    }

    public User readUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();
        return userService.readByUsername(name);
    }
}
