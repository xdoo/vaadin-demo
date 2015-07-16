/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.demo.service.services;

import de.muenchen.demo.service.domain.Staatsangehoerigkeit;
import de.muenchen.demo.service.domain.StaatsangehoerigkeitReference;
import de.muenchen.demo.service.domain.StaatsangehoerigkeitReferenceRepository;
import de.muenchen.demo.service.util.QueryService;
import java.util.List;
import javax.persistence.EntityManager;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author praktikant.tmar
 */
@Service
public class StaatsangehoerigkeitServiceImpl implements StaatsangehoerigkeitService {
    @Value("${URL}")
    private String URL;
    RestTemplate restTemplate = new TestRestTemplate();
    QueryService<Staatsangehoerigkeit> search;


    StaatsangehoerigkeitReferenceRepository repo;
    
        @Autowired
    public StaatsangehoerigkeitServiceImpl(StaatsangehoerigkeitReferenceRepository repo, EntityManager em) {
        this.repo = repo;
        this.search = new QueryService<>(em, Staatsangehoerigkeit.class, "reference","land","sprache");
    }

    @Override
    public Staatsangehoerigkeit read(String referencedOid) {
        String URL2=URL+"staat/"+referencedOid;
        ResponseEntity<Staatsangehoerigkeit> result = this.restTemplate.getForEntity(URL2,Staatsangehoerigkeit.class);
        return result.getBody();
    }

    @Override
    public List<Staatsangehoerigkeit> query() {

        /*read All from the webservice */
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Staatsangehoerigkeit create(String referencedOid) {

        Staatsangehoerigkeit resource = this.read(referencedOid);
        if (resource == null) {
            return null;
        } else {
            StaatsangehoerigkeitReference staatsangehoerigkeitReference = new StaatsangehoerigkeitReference();
            staatsangehoerigkeitReference.setReferencedOid(referencedOid);
            this.repo.save(staatsangehoerigkeitReference);
            return resource;
        }
    }

    @Override
    public void delete(String referencedOid) {
        List<StaatsangehoerigkeitReference> item = this.repo.findByReferencedOid(referencedOid);
        if (!item.isEmpty()) 
            this.repo.delete(item);
        }      
    }    
