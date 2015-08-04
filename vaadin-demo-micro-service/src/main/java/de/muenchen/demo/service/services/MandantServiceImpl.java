/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.demo.service.services;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import de.muenchen.demo.service.domain.Mandant;
import de.muenchen.demo.service.domain.MandantRepository;
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
public class MandantServiceImpl implements MandantService {

    private static final Logger LOG = LoggerFactory.getLogger(MandantService.class);

    @Autowired
    UserService userService;
    MandantRepository repo;
    QueryService<Mandant> search;

    public MandantServiceImpl() {
    }

    @Autowired
    public MandantServiceImpl(MandantRepository repo, EntityManager em) {
        this.repo = repo;
        this.search = new QueryService<>(userService, em,  Mandant.class, "mid");
    }

    @Override
    public Mandant create() {
        Mandant mandant = new Mandant();
        mandant.setOid(IdService.next());
        return mandant;
    }

    @Override
    public Mandant save(Mandant mandant) {
        LOG.info(mandant.toString());
        Preconditions.checkArgument(mandant.getId() == null, "On save, the ID must be empty");
        return this.repo.save(mandant);
    }

    @Override
    public Mandant read(String oid) {
        List<Mandant> result = this.repo.findByOid(oid);
        if (result.isEmpty()) {
            // TODO
            LOG.warn(String.format("found no mandant with oid '%s'", oid));
            return null;
        } else {
            return result.get(0);
        }
    }

    @Override
    public Mandant update(Mandant mandant) {
        return this.repo.save(mandant);
    }

    @Override
    public void delete(String oid) {
        Mandant item = this.read(oid);
        this.repo.delete(item);
    }

    @Override
    public List<Mandant> query() {
        Iterable<Mandant> all = this.repo.findAll();
        return Lists.newArrayList(all);
    }

    @Override
    public List<Mandant> query(String query) {
        return this.search.query(query);
    }

    @Override
    public Mandant copy(String oid) {
        Mandant source = this.read(oid);
        Mandant result = null;
        Mandant clone = new Mandant();
        clone.setOid(IdService.next());
        // start mapping

        clone.setMid(source.getMid());


        // end mapping
        LOG.info("clone --> " + clone.toString());
        result = this.repo.save(clone);
        return result;
    }

}
