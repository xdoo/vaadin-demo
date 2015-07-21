package de.muenchen.demo.service.services;

import de.muenchen.demo.service.domain.Buerger;
import de.muenchen.demo.service.domain.BuergerRepository;
import de.muenchen.demo.service.util.QueryService;
import javax.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author claus
 */
@Service
public class BuergerServiceImpl extends BaseService<Buerger> implements BuergerService {
    
    private static final Logger LOG = LoggerFactory.getLogger(BuergerService.class);
    

    public BuergerServiceImpl() {
    }

    @Autowired
    public BuergerServiceImpl(BuergerRepository repo, EntityManager em) {
        this.repo = repo;
        this.search = new QueryService<>(em, Buerger.class, "vorname", "nachname");
    }
    
    
}
