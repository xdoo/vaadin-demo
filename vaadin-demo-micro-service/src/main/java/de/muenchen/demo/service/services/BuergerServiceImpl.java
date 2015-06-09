package de.muenchen.demo.service.services;

import de.muenchen.demo.service.domain.Buerger;
import de.muenchen.demo.service.domain.BuergerRepository;
import de.muenchen.demo.service.util.IdService;
import java.util.List;
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
    
    @Autowired BuergerRepository repo;
    
    @Override
    public Buerger create(Buerger buerger) {
        buerger.setOid(IdService.next());
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
    
}
