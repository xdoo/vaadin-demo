/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.demo.service.services;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import de.muenchen.demo.service.domain.Pass;
import de.muenchen.demo.service.domain.PassRepository;
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
public class PassServiceImpl extends BaseService<Pass> implements PassService {

    private static final Logger LOG = LoggerFactory.getLogger(PassService.class);

//    PassRepository repo;
//    QueryService<Pass> search;

    public PassServiceImpl() {
    }

    @Autowired
    public PassServiceImpl(PassRepository repo, EntityManager em) {
        this.repo = repo;
        this.search = new QueryService<>(em, Pass.class, "passnummer", "kode","typ","austellungsdatum","gueltigBis","behoerde");
    }

//    @Override
//    public Pass create() {
//        Pass pass = new Pass();
//        pass.setOid(IdService.next());
//        return pass;
//    }
//
//    @Override
//    public Pass save(Pass pass) {
//        LOG.info(pass.toString());
//        Preconditions.checkArgument(pass.getId() == null, "On save, the ID must be empty");
//        return this.repo.save(pass);
//    }
//
//    @Override
//    public Pass read(String oid) {
//        List<Pass> result = this.repo.findByOid(oid);
//        if (result.isEmpty()) {
//            // TODO
//            LOG.warn(String.format("found no pass with oid '%s'", oid));
//            return null;
//        } else {
//            return result.get(0);
//        }
//    }
//
//    @Override
//    public Pass update(Pass pass) {
//        return this.repo.save(pass);
//    }
//
//    @Override
//    public void delete(String oid) {
//        Pass item = this.read(oid);
//        this.repo.delete(item);
//    }
//
//    @Override
//    public List<Pass> query() {
//        Iterable<Pass> all = this.repo.findAll();
//        return Lists.newArrayList(all);
//    }
//
//    @Override
//    public List<Pass> query(String query) {
//        return this.search.query(query);
//    }
//
//    @Override
//    public Pass copy(String oid) {
//        Pass source = this.read(oid);
//        Pass result = null;
//        Pass clone = new Pass();
//        clone.setOid(IdService.next());
//        // start mapping
//        clone.setAustellungsdatum(source.getAustellungsdatum());
//        clone.setBehoerde(source.getBehoerde());
//        clone.setGueltigBis(source.getGueltigBis());
//        clone.setKode(source.getKode());
//        clone.setTyp(source.getTyp());
//        clone.setPassNummer(source.getPassNummer());
//        clone.setStaatsangehoerigkeit(source.getStaatsangehoerigkeit());
//
//        // end mapping
//        LOG.info("clone --> " + clone.toString());
//        result = this.repo.save(clone);
//        return result;
//    }

}
