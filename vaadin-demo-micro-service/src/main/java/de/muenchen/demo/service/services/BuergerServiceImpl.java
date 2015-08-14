package de.muenchen.demo.service.services;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import de.muenchen.demo.service.domain.Buerger;
import de.muenchen.demo.service.domain.BuergerRepository;
import de.muenchen.demo.service.domain.Mandant;
import de.muenchen.demo.service.domain.Pass;
import de.muenchen.demo.service.domain.StaatsangehoerigkeitReference;
import de.muenchen.demo.service.domain.User;
import de.muenchen.demo.service.domain.Wohnung;
import de.muenchen.demo.service.util.IdService;
import de.muenchen.demo.service.util.QueryService;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
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
        this.releaseBuergerEltern(oid);
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
    public void releaseBuergerEltern(String kindOid) {
        Iterator<Buerger> iter = this.readEltern(kindOid).iterator();
        for (Iterator iterator = iter; iterator.hasNext();) {

            Buerger buerger = iter.next();

            Set<Buerger> kinder = (Set<Buerger>) buerger.getKinder();
            Collection<Buerger> removeKinder = new LinkedList<>();
            kinder.stream().filter((element) -> (element == this.read(kindOid))).forEach((element) -> {
                removeKinder.add(element);
            });
            kinder.removeAll(removeKinder);
            this.update(buerger);

        }
    }

    @Override
    public void releaseBuergerElternteil(String kindOid, String buergerOid) {

        Buerger buerger = this.read(buergerOid);
        Set<Buerger> kinder = (Set<Buerger>) buerger.getKinder();
        Collection<Buerger> removeKinder = new LinkedList<>();
        kinder.stream().filter((element) -> (element == this.read(kindOid))).forEach((element) -> {
            removeKinder.add(element);
        });
        kinder.removeAll(removeKinder);
        this.update(buerger);

    }

    @Override
    public Iterable<Buerger> readPass(String oid) {
        return repo.findByPassOid(oid);
    }

    @Override
    public void releasePassAllBuerger(String passOid) {

        Iterator<Buerger> iter = this.readPass(passOid).iterator();
        while (iter.hasNext()) {
            Buerger buerger = iter.next();
            Set<Pass> pass = (Set<Pass>) buerger.getPass();
            Collection<Pass> removePass = new LinkedList<>();
            pass.stream().filter((element) -> (element == this.passService.read(passOid))).forEach((element) -> {
                removePass.add(element);
            });
            pass.removeAll(removePass);
            this.update(buerger);

        }
    }

    @Override
    public void releasePassBuerger(String passOid, String buergerOid) {

        Buerger buerger = this.read(buergerOid);
        Set<Pass> pass = (Set<Pass>) buerger.getPass();
        Collection<Pass> removePass = new LinkedList<>();
        pass.stream().filter((element) -> (element == this.passService.read(passOid))).forEach((element) -> {
            removePass.add(element);
        });
        pass.removeAll(removePass);
        this.update(buerger);

    }

    @Override
    public Iterable<Buerger> readWohnung(String oid) {
        return repo.findByWohnungenOid(oid);
    }

    @Override
    public void releaseWohnungAllBuerger(String wohnungOid) {
        Iterator<Buerger> iter = this.readWohnung(wohnungOid).iterator();
        while (iter.hasNext()) {
            Buerger buerger = iter.next();
            Set<Wohnung> wohnung = (Set<Wohnung>) buerger.getWohnungen();
            Collection<Wohnung> removeWohnung = new LinkedList<>();
            wohnung.stream().filter((element) -> (element == this.wohnungService.read(wohnungOid))).forEach((element) -> {
                removeWohnung.add(element);
            });
            wohnung.removeAll(removeWohnung);
            this.update(buerger);

        }
    }

    @Override
    public void releaseWohnungBuerger(String wohnungOid, String buergerOid) {

        Buerger buerger = this.read(buergerOid);
        Set<Wohnung> wohnung = (Set<Wohnung>) buerger.getWohnungen();
        Collection<Wohnung> remove = new LinkedList<>();

        wohnung.stream().filter((element) -> (element == this.wohnungService.read(wohnungOid))).forEach((element) -> {
            remove.add(element);
        });
        wohnung.removeAll(remove);
        this.update(buerger);

    }

    @Override
    public Iterable<Buerger> readStaatsangehoerigkeit(String oid) {
        return repo.findByStaatsangehoerigkeitReferencesReferencedOid(oid);
    }

    @Override
    public void releaseStaatsangehoerigkeitAllBuerger(String staatOid) {
        Iterator<Buerger> iter = this.readStaatsangehoerigkeit(staatOid).iterator();
        while (iter.hasNext()) {
            Buerger buerger = iter.next();
            Iterator<StaatsangehoerigkeitReference> staatRefIter = buerger.getStaatsangehoerigkeitReferences().iterator();

            while (staatRefIter.hasNext()) {
                StaatsangehoerigkeitReference staatRef = staatRefIter.next();

                if (staatRef == this.staatService.readReference(staatOid)) {
                    staatRefIter.remove();
                    this.update(buerger);
                }
            }
        }
    }

    @Override
    public void releaseStaatsangehoerigkeitBuerger(String staatOid, String buergerOid) {

        Buerger buerger = this.read(buergerOid);
        Iterator<StaatsangehoerigkeitReference> staatRefIter = buerger.getStaatsangehoerigkeitReferences().iterator();
        while (staatRefIter.hasNext()) {
            StaatsangehoerigkeitReference staatRef = staatRefIter.next();
            if (staatRef == this.staatService.readReference(staatOid)) {
                staatRefIter.remove();
                this.update(buerger);
            }
        }
    }
}
