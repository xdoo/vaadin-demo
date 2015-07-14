/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.demo.service.services;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import de.muenchen.demo.service.domain.Wohnung;
import de.muenchen.demo.service.domain.WohnungRepository;
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
 * @author praktikant.tmar
 */
@Service
public class WohnungServiceImpl implements WohnungService {
    
    private static final Logger LOG = LoggerFactory.getLogger(WohnungService.class);
    
    WohnungRepository repo;
    QueryService<Wohnung> search;

    public WohnungServiceImpl() {
    }

    @Autowired
    public WohnungServiceImpl(WohnungRepository repo, EntityManager em) {
        this.repo = repo;
        this.search = new QueryService<>(em, Wohnung.class, "adresseOid","ausrichtung","stock");
    }
    
    @Override
    public Wohnung create() {
        Wohnung wohnung = new Wohnung();
        wohnung.setOid(IdService.next());
        return wohnung;
    }

    @Override
    public Wohnung save(Wohnung wohnung) {
        LOG.info(wohnung.toString());
        Preconditions.checkArgument(wohnung.getId() == null, "On save, the ID must be empty");
        return this.repo.save(wohnung);
    }
    
    @Override
    public Wohnung read(String oid) {
        List<Wohnung> result = this.repo.findByOid(oid);
        if(result.isEmpty()) {
            // TODO
            LOG.warn(String.format("found no wohnung with oid '%s'", oid));
            return null;
        } else {
            return result.get(0);
        }
    }
    
    @Override
    public Wohnung update(Wohnung wohnung) {
        return this.repo.save(wohnung);
    }
    
    @Override
    public void delete(String oid) {
        Wohnung item = this.read(oid);
        this.repo.delete(item);
    }

    @Override
    public List<Wohnung> query() {
        Iterable<Wohnung> all = this.repo.findAll();
        return Lists.newArrayList(all);
    }

    @Override
    public List<Wohnung> query(String query) { 
        return this.search.query(query);
    }

    @Override
    public Wohnung copy(String oid) {
        Wohnung source = this.read(oid);
        Wohnung result = null;
        Wohnung clone = new Wohnung();
        clone.setOid(IdService.next());
        // start mapping
        clone.setAdresse(source.getAdresse());
        clone.setStock(source.getStock());
        clone.setAusrichtung(source.getAusrichtung());
        
        // end mapping
        LOG.info("clone --> " + clone.toString());
        result = this.repo.save(clone);
        return result;
    }
    
}

