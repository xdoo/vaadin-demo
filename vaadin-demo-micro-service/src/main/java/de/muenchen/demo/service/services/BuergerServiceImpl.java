package de.muenchen.demo.service.services;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import de.muenchen.demo.service.domain.Buerger;
import de.muenchen.demo.service.domain.BuergerRepository;
import de.muenchen.demo.service.util.IdService;
import de.muenchen.demo.service.util.QueryService;
import java.util.List;
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
public class BuergerServiceImpl implements BuergerService {
    
    private static final Logger LOG = LoggerFactory.getLogger(BuergerService.class);
    
    BuergerRepository repo;
    QueryService<Buerger> search;

    public BuergerServiceImpl() {
    }

    @Autowired
    public BuergerServiceImpl(BuergerRepository repo, EntityManager em) {
        this.repo = repo;
        this.search = new QueryService<>(em, Buerger.class, "vorname", "nachname");
    }
    
    @Override
    public Buerger create() {
        Buerger buerger = new Buerger();
        buerger.setOid(IdService.next());
        return buerger;
    }

    @Override
    public Buerger save(Buerger buerger) {
        LOG.info(buerger.toString());
        Preconditions.checkArgument(buerger.getId() == null, "On save, the ID must be empty");
        return this.repo.save(buerger);
    }
    
    @Override
    public Buerger read(String oid) {
        List<Buerger> result = this.repo.findByOid(oid);
        if(result.isEmpty()) {
            // TODO
            LOG.warn(String.format("found no buerger with oid '%s'", oid));
            return null;
        } else {
            return result.get(0);
        }
    }
    
    @Override
    public Buerger update(Buerger buerger) {
        return this.repo.save(buerger);
    }
    
    @Override
    public void delete(String oid) {
        Buerger item = this.read(oid);
        this.repo.delete(item);
    }

    @Override
    public List<Buerger> query() {
        Iterable<Buerger> all = this.repo.findAll();
        return Lists.newArrayList(all);
    }

    @Override
    public List<Buerger> query(String query) { 
        return this.search.query(query);
    }

    @Override
    public Buerger copy(String oid) {
        Buerger source = this.read(oid);
        Buerger result = null;
        Buerger clone = new Buerger();
        clone.setOid(IdService.next());
        // start mapping
        clone.setVorname(source.getVorname());
        clone.setNachname(source.getNachname());
        clone.setGeburtsdatum(source.getGeburtsdatum());
        // end mapping
        LOG.info("clone --> " + clone.toString());
        result = this.repo.save(clone);
        return result;
    }
    
}
