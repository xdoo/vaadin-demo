/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.demo.service.services;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import de.muenchen.demo.service.domain.Mandant;
import de.muenchen.demo.service.domain.User;
import de.muenchen.demo.service.domain.Pass;
import de.muenchen.demo.service.domain.PassRepository;
import de.muenchen.demo.service.util.IdService;
import de.muenchen.demo.service.util.QueryService;
import java.util.List;
import java.util.Objects;
import javax.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 *
 * @author praktikant.tmar
 */
@Service
public class PassServiceImpl implements PassService {

    @Autowired
    private BuergerService buergerService;
    private static final Logger LOG = LoggerFactory.getLogger(PassService.class);
    PassRepository repo;
    QueryService<Pass> search;
    @Autowired
    private UserService userService;
    @Autowired
    MandantService mandantService;

    public PassServiceImpl() {
    }

    @Autowired
    public PassServiceImpl(PassRepository repo, UserService userService, EntityManager em) {
        this.repo = repo;
        this.search = new QueryService<>(userService, em, Pass.class, "adresseOid", "ausrichtung", "stock");
    }

    @Override
    public Pass create() {
        Pass pass = new Pass();
        pass.setOid(IdService.next());
        return pass;
    }

    @Override
    public Pass save(Pass pass) {
        LOG.info(pass.toString());
        Preconditions.checkArgument(pass.getId() == null, "On save, the ID must be empty");
        Mandant mandant = mandantService.read(readUser().getMandant().getOid());
        pass.setMandant(mandant);
        return this.repo.save(pass);
    }

    @Override
    public Pass read(String oid) {
        Pass result = this.repo.findFirstByOidAndMandantOid(oid, readUser().getMandant().getOid());

        if (Objects.isNull(result)) {
            LOG.warn(String.format("found no pass with oid '%s'", oid));
            return null;
        } else {
            return result;
        }
    }

    public User readUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();
        return userService.readByUsername(name);
    }

    @Override
    public Pass update(Pass pass) {
        return this.repo.save(pass);
    }

    @Override
    public List<Pass> query() {
        Iterable<Pass> all = this.repo.findByMandantOid(readUser().getMandant().getOid());
        return Lists.newArrayList(all);
    }

    @Override
    public List<Pass> query(String query) {
        return this.search.query(query);
    }

    @Override
    public Pass copy(String oid) {
        Pass in = this.read(oid);
        // map
        Pass out = new Pass(in);
        out.setOid(IdService.next());

        // in DB speichern
        this.save(out);
        return out;
    }

    @Override
    public Pass readStaat(String oid) {
        return repo.findFirstByStaatsangehoerigkeitReferenceReferencedOidAndMandantOid(oid, readUser().getMandant().getOid());
    }

    @Override
    public void deletePassStaat(String adresseOid) {
        Pass pass = this.readStaat(adresseOid);
        if (pass != null) {
            pass.setStaatsangehoerigkeitReference(null);
            this.update(pass);
        }
    }

    @Override
    public void delete(String oid) {
        Pass item = this.read(oid);
        this.buergerService.releasePassBuerger(oid);
        this.repo.delete(item);
    }
}
