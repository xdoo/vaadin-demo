package de.muenchen.demo.service.services;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import de.muenchen.demo.service.domain.Buerger;
import de.muenchen.demo.service.domain.BuergerRepository;
import de.muenchen.demo.service.domain.Mandant;
import de.muenchen.demo.service.domain.Pass;
import de.muenchen.demo.service.domain.Sachbearbeiter;
import de.muenchen.demo.service.domain.StaatsangehoerigkeitReference;
import de.muenchen.demo.service.domain.User;
import de.muenchen.demo.service.domain.Wohnung;
import de.muenchen.demo.service.util.IdService;
import de.muenchen.demo.service.util.QueryService;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
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
    private SachbearbeiterService sachbearbeiterService;
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
        Buerger result = this.repo.findFirstByOidAndMandantOid(oid, readUser().getMandant().getOid());
        if (Objects.isNull(result)) {
            LOG.warn(String.format("found no buerger with oid '%s'", oid));
            return null;
        } else {
            return result;
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
        this.releaseBuergerAllSachbearbeiter(oid);
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
    public void releaseBuergerAllSachbearbeiter(String buergerOid) {
        Buerger buerger = this.read(buergerOid);
        Set<Sachbearbeiter> list = buerger.getSachbearbeiter();

        if (list != null) {
            Iterator<Sachbearbeiter> iter = list.iterator();
            Collection<Sachbearbeiter> removeSachbearbeiter = new LinkedList<>();
            removeSachbearbeiter.add(null);
            while (iter.hasNext()) {
                Sachbearbeiter sachbearbeiter = iter.next();
                removeSachbearbeiter.add(sachbearbeiter);

                Set<Buerger> buergerList = sachbearbeiter.getBuerger();
                Collection<Buerger> removeBuerger = new LinkedList<>();
                buergerList.stream().filter((element) -> (element == this.read(buergerOid))).forEach((element) -> {
                    removeBuerger.add(element);
                });
                buergerList.removeAll(removeBuerger);
                this.sachbearbeiterService.update(sachbearbeiter);

            }

            list.removeAll(removeSachbearbeiter);
            this.update(buerger);
        }
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

            Set<Buerger> kinder = buerger.getKinder();
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
        Set<Buerger> kinder = buerger.getKinder();
        Collection<Buerger> removeKinder = new LinkedList<>();
        kinder.stream().filter((element) -> (element == this.read(kindOid))).forEach((element) -> {
            removeKinder.add(element);
        });
        kinder.removeAll(removeKinder);
        this.update(buerger);

    }

    @Override
    public void releaseBuergerKinder(String oid) {

        Buerger buerger = this.read(oid);

        Set<Buerger> kinder = buerger.getKinder();
        Collection<Buerger> removeKinder = new LinkedList<>();
        kinder.stream().forEach((kind) -> {
            removeKinder.add(kind);
        });

        kinder.removeAll(removeKinder);
        this.update(buerger);

    }

    @Override
    public Buerger readPassBuerger(String oid) {
        List<Buerger> result = this.repo.findByPassOid(oid);
        if (result.isEmpty()) {
// TODO
            LOG.warn(String.format("found no buerger with PassOid '%s'", oid));
            return null;
        } else {
            return result.get(0);
        }
    }

    @Override
    public void releasePassBuerger(String passOid) {

        Buerger buerger = this.readPassBuerger(passOid);
        if (buerger != null) {
            Set<Pass> pass = buerger.getPass();
            Collection<Pass> removePass = new LinkedList<>();
            pass.stream().filter((element) -> (element == this.passService.read(passOid))).forEach((element) -> {
                removePass.add(element);
            });
            pass.removeAll(removePass);
            this.update(buerger);
        }
    }

    @Override
    public void releaseBuergerPaesse(String oid) {

        Buerger buerger = this.read(oid);

        Set<Pass> paesse = buerger.getPass();
        Collection<Pass> removePaesse = new LinkedList<>();
        paesse.stream().forEach((pass) -> {
            removePaesse.add(pass);
        });

        paesse.removeAll(removePaesse);
        this.update(buerger);

    }

    @Override
    public Iterable<Buerger> readWohnungBuerger(String oid) {
        return repo.findByWohnungenOid(oid);
    }

    @Override
    public void releaseWohnungAllBuerger(String wohnungOid) {
        Iterator<Buerger> iter = this.readWohnungBuerger(wohnungOid).iterator();
        while (iter.hasNext()) {
            Buerger buerger = iter.next();
            Set<Wohnung> wohnung = buerger.getWohnungen();
            Collection<Wohnung> removeWohnung = new LinkedList<>();
            wohnung.stream().filter((element) -> (element == this.wohnungService.read(wohnungOid))).forEach((element) -> {
                removeWohnung.add(element);
            });
            wohnung.removeAll(removeWohnung);
            this.update(buerger);

        }
    }

    @Override
    public void releaseBuergerWohnungen(String oid) {

        Buerger buerger = this.read(oid);

        Set<Wohnung> wohnungen = buerger.getWohnungen();
        Collection<Wohnung> removeWohnungnen = new LinkedList<>();
        wohnungen.stream().forEach((wohnung) -> {
            removeWohnungnen.add(wohnung);
        });

        wohnungen.removeAll(removeWohnungnen);
        this.update(buerger);

    }

    @Override
    public void releaseWohnungBuerger(String wohnungOid, String buergerOid) {

        Buerger buerger = this.read(buergerOid);
        Set<Wohnung> wohnung = buerger.getWohnungen();
        Collection<Wohnung> remove = new LinkedList<>();

        wohnung.stream().filter((element) -> (element == this.wohnungService.read(wohnungOid))).forEach((element) -> {
            remove.add(element);
        });
        wohnung.removeAll(remove);
        this.update(buerger);

    }

    @Override
    public Iterable<Buerger> readStaatsangehoerigkeitBuerger(String oid) {
        return repo.findByStaatsangehoerigkeitReferencesReferencedOid(oid);
    }

    @Override
    public void releaseStaatsangehoerigkeitAllBuerger(String staatOid) {
        Iterator<Buerger> iter = this.readStaatsangehoerigkeitBuerger(staatOid).iterator();
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
