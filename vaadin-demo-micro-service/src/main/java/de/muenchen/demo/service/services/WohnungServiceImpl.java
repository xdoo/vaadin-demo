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
import de.muenchen.demo.service.domain.Wohnung;
import de.muenchen.demo.service.domain.WohnungRepository;
import de.muenchen.demo.service.util.IdService;
import de.muenchen.demo.service.util.QueryService;
import java.util.List;
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
public class WohnungServiceImpl implements WohnungService {

    @Autowired
    private BuergerService buergerService;
    private static final Logger LOG = LoggerFactory.getLogger(WohnungService.class);
    WohnungRepository repo;
    QueryService<Wohnung> search;
    @Autowired
    private UserService userService;
    @Autowired
    MandantService mandantService;

    public WohnungServiceImpl() {
    }

    @Autowired
    public WohnungServiceImpl(WohnungRepository repo, UserService userService, EntityManager em) {
        this.repo = repo;
        this.search = new QueryService<>(userService, em, Wohnung.class, "adresseOid", "ausrichtung", "stock");
    }

    @Override
    public void delete(String oid) {
        Wohnung item = this.read(oid);
        this.buergerService.releaseWohnungAllBuerger(oid);

        this.repo.delete(item);

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
        Mandant mandant = mandantService.read(readUser().getMandant().getOid());
        wohnung.setMandant(mandant);
        return this.repo.save(wohnung);
    }

    @Override
    public Wohnung read(String oid) {
        List<Wohnung> result = this.repo.findByOidAndMandantOid(oid, readUser().getMandant().getOid());
        if (result.isEmpty()) {
// TODO
            LOG.warn(String.format("found no wohnung with oid '%s'", oid));
            return null;
        } else {
            return result.get(0);
        }
    }

    public User readUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();
        return userService.readByUsername(name);
    }

    @Override
    public Wohnung update(Wohnung wohnung) {
        return this.repo.save(wohnung);
    }

    @Override
    public List<Wohnung> query() {
        Iterable<Wohnung> all = this.repo.findByMandantOid(readUser().getMandant().getOid());
        return Lists.newArrayList(all);
    }

    @Override
    public List<Wohnung> query(String query) {
        return this.search.query(query);
    }

    @Override
    public Wohnung copy(String oid) {
        Wohnung in = this.read(oid);
        // map
        Wohnung out = new Wohnung(in);
        out.setOid(IdService.next());

        // in DB speichern
        this.save(out);
        return out;
    }

    @Override
    public Wohnung readAdresse(String oid) {
        return repo.findByAdresseOidAndMandantOid(oid, readUser().getMandant().getOid());
    }

    @Override
    public void deleteWohnungAdresse(String adresseOid) {
        Wohnung wohnung = this.readAdresse(adresseOid);
        if (wohnung != null) {
            wohnung.setAdresse(null);
            this.update(wohnung);
        }
    }
}
