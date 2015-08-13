package de.muenchen.demo.service.services;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import de.muenchen.demo.service.domain.Buerger;
import de.muenchen.demo.service.domain.BuergerRepository;
import de.muenchen.demo.service.domain.Mandant;
import de.muenchen.demo.service.domain.Pass;
import de.muenchen.demo.service.domain.PassRepository;
import de.muenchen.demo.service.domain.Staatsangehoerigkeit;
import de.muenchen.demo.service.domain.StaatsangehoerigkeitReference;
import de.muenchen.demo.service.domain.User;
import de.muenchen.demo.service.domain.Wohnung;
import de.muenchen.demo.service.util.IdService;
import de.muenchen.demo.service.util.QueryService;
import java.util.Iterator;
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
 * @author claus
 */
@Service
public class BuergerServiceImpl implements BuergerService {

    private static final Logger LOG = LoggerFactory.getLogger(BuergerService.class);
    BuergerRepository repo;
    @Autowired
    private PassService passService;
    QueryService<Buerger> search;
    @Autowired
    private UserService userService;
    @Autowired
    MandantService mandantService;
    @Autowired
    private WohnungService wohnungService;
    @Autowired
    private StaatsangehoerigkeitService staatService;

    public BuergerServiceImpl() {
    }

    @Autowired
    public BuergerServiceImpl(BuergerRepository repo, EntityManager em, UserService userService) {
        this.repo = repo;
        this.search = new QueryService<>(userService, em, Buerger.class, "vorname", "nachname");
    }

    @Override
    public Buerger create() {
        Buerger buerger = new Buerger();
        buerger.setOid(IdService.next());
        return buerger;
    }

    public User readUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();
        return userService.readByUsername(name);
    }

    @Override
    public Buerger save(Buerger buerger) {
        LOG.info(buerger.toString());
        Preconditions.checkArgument(buerger.getId() == null, "On save, the ID must be empty");
        Mandant mandant = mandantService.read(readUser().getMandant().getOid());
        buerger.setMandant(mandant);
        return this.repo.save(buerger);
    }

    @Override
    public Buerger read(String oid) {
        List<Buerger> result = this.repo.findByOidAndMandantOid(oid, readUser().getMandant().getOid());
        if (result.isEmpty()) {
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
        this.deleteBuergerEltern(oid);

        this.repo.delete(item);
    }

    @Override
    public List<Buerger> query() {
        Iterable<Buerger> all = this.repo.findByMandantOid(readUser().getMandant().getOid());
        return Lists.newArrayList(all);
    }

    @Override
    public List<Buerger> query(String query) {
        return this.search.query(query);
    }

    @Override
    public Buerger copy(String oid) {
        Buerger in = this.read(oid);
        // map
        Buerger out = new Buerger(in);
        out.setOid(IdService.next());

        // in DB speichern
        this.save(out);
        return out;
    }

    @Override
    public Iterable<Buerger> readEltern(String oid) {
        return repo.findByKinderOid(oid);
    }

    @Override
    public Buerger deleteBuergerEltern(String kindOid) {
        Iterator<Buerger> iter = this.readEltern(kindOid).iterator();
        while (iter.hasNext()) {
            Buerger buerger = iter.next();
            Iterator<Buerger> kinderIter = buerger.getKinder().iterator();
            while (kinderIter.hasNext()) {
                Buerger kind = kinderIter.next();
                if (kind == this.read(kindOid)) {
                    kinderIter.remove();
                    return this.update(buerger);
                }
            }
        }
        return null;
    }

    @Override
    public Iterable<Buerger> readPass(String oid) {
        return repo.findByPassOid(oid);
    }

    @Override
    public Buerger deleteBuergerPass(String passOid) {
        Iterator<Buerger> iter = this.readPass(passOid).iterator();
        while (iter.hasNext()) {
            Buerger buerger = iter.next();
            Iterator<Pass> passItr = buerger.getPass().iterator();
            while (passItr.hasNext()) {
                Pass pass = passItr.next();
                if (pass == this.passService.read(passOid)) {
                    passItr.remove();
                    return this.update(buerger);
                }
            }
        }
        return null;
    }

    @Override
    public Iterable<Buerger> readWohnung(String oid) {
        return repo.findByWohnungenOid(oid);
    }

    @Override
    public Buerger deleteBuergerWohnung(String wohnungOid) {
        Iterator<Buerger> iter = this.readWohnung(wohnungOid).iterator();
        while (iter.hasNext()) {
            Buerger buerger = iter.next();
            Iterator<Wohnung> wohnungIter = buerger.getWohnungen().iterator();
            while (wohnungIter.hasNext()) {
                Wohnung wohnung = wohnungIter.next();
                if (wohnung == this.wohnungService.read(wohnungOid)) {
                    wohnungIter.remove();
                    return this.update(buerger);
                }
            }
        }
        return null;
    }

    @Override
    public Iterable<Buerger> readStaatsangehoerigkeit(String oid) {
        return repo.findByStaatsangehoerigkeitReferencesReferencedOid(oid);
    }

    @Override
    public Buerger deleteBuergerStaatsangehoerigkeit(String staatOid) {
        Iterator<Buerger> iter = this.readStaatsangehoerigkeit(staatOid).iterator();
        while (iter.hasNext()) {
            Buerger buerger = iter.next();
            Iterator<StaatsangehoerigkeitReference> staatRefIter = buerger.getStaatsangehoerigkeitReferences().iterator();

            while (staatRefIter.hasNext()) {
                StaatsangehoerigkeitReference staatRef = staatRefIter.next();

                if (staatRef == this.staatService.readReference(staatOid)) {
                    staatRefIter.remove();
                    return this.update(buerger);
                }
            }
        }
        return null;
    }

}
