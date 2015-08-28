package de.muenchen.demo.service.services;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import de.muenchen.demo.service.domain.*;
import de.muenchen.demo.service.util.IdService;
import de.muenchen.demo.service.util.QueryService;
import de.muenchen.demo.service.util.events.BuergerEvent;
import de.muenchen.demo.service.util.events.SachbearbeiterEvent;
import de.muenchen.vaadin.demo.api.util.EventType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 *
 * @author claus
 */
@Service
public class BuergerServiceImpl implements BuergerService {

    private static final Logger LOG = LoggerFactory.getLogger(BuergerService.class);
    BuergerRepository repo;
    QueryService<Buerger> search;
    @Autowired
    private UserService userService;
    @Autowired
    MandantService mandantService;

    EventBus eventbus;

    @Autowired
    public BuergerServiceImpl(BuergerRepository repo, EntityManager em, UserService userService, EventBus eventBus) {
        this.repo = repo;
        this.search = new QueryService<>(userService, em, Buerger.class, "vorname", "nachname");
        this.eventbus = eventBus;
        eventBus.register(this);
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
    public void copy(List<String> oids) {
        oids.stream().forEach(this::copy);
    }

    @Override
    public void delete(List<String> oids) {
        oids.stream().forEach(this::delete);
    }

    /**
     * release Operation für einen Bürger und alle seine Sachbearbeiter .
     *
     * @param buergerOid
     */
    @Override
    public void releaseBuergerAllSachbearbeiter(String buergerOid) {
        Buerger buerger = this.read(buergerOid);
        Set<Sachbearbeiter> list = buerger.getSachbearbeiter();

        if (list != null) {
            Iterator<Sachbearbeiter> iter = list.iterator();
            Collection<Sachbearbeiter> removeSachbearbeiter = new LinkedList<>();
            //removeSachbearbeiter.add(null);
            while (iter.hasNext()) {
                Sachbearbeiter sachbearbeiter = iter.next();
                removeSachbearbeiter.add(sachbearbeiter);
                
                Set<Buerger> buergerList = sachbearbeiter.getBuerger();
                Collection<Buerger> removeBuerger = new LinkedList<>();
                buergerList.stream().filter((element) -> (element.getOid().equals(buergerOid) )).forEach(removeBuerger::add);
                buergerList.removeAll(removeBuerger);

                //this.sachbearbeiterService.update(sachbearbeiter);
                eventbus.post(new SachbearbeiterEvent(EventType.UPDATE, sachbearbeiter));
                
            }

            list.removeAll(removeSachbearbeiter);
            this.update(buerger);
        }
    }

    /**
     * read die Eltern Ein Bürger
     *
     * @param buergerOid
     * @return
     */
    @Override
    public Iterable<Buerger> readEltern(String buergerOid) {
        return repo.findByKinderOidAndMandantOid(buergerOid, readUser().getMandant().getOid());
    }

    /**
     * release Operation für ein Bürger und seine Eltern .
     *
     * @param buergerOid
     */
    @Override
    public void releaseBuergerEltern(String buergerOid) {

        Iterator<Buerger> iter = this.readEltern(buergerOid).iterator();
        while (iter.hasNext()) {
            Buerger elternteil = iter.next();
            List<Buerger> kinder = elternteil.getKinder().stream().filter(k -> k.getOid().equals(buergerOid)).collect(Collectors.toList());
            if (!kinder.isEmpty()) {
                elternteil.getKinder().remove(kinder.get(0));
            } else {
                LOG.warn(String.format("found no child"));
            }

            this.update(elternteil);
        }

    }

    /**
     * release Operation für ein Elternteil eines Bürgers.
     *
     * @param kindOid
     * @param elternOid
     */
    @Override
    public void releaseBuergerElternteil(String kindOid, String elternOid) {

        Buerger elternteil = this.read(elternOid);
        List<Buerger> kinder = elternteil.getKinder().stream().filter(k -> k.getOid().equals(kindOid)).collect(Collectors.toList());
        if (!kinder.isEmpty()) {
            elternteil.getKinder().remove(kinder.get(0));
        } else {
            LOG.warn(String.format("found no child with oid %s", kindOid));
        }

        this.update(elternteil);

    }

    /**
     * release Operation für die Kinder eines Bürgers.
     *
     * @param buergerOid
     */
    @Override
    public void releaseBuergerKinder(String buergerOid) {

        Buerger buerger = this.read(buergerOid);
        buerger.getKinder().clear();
        this.update(buerger);
    }

    /**
     * release Operation für ein Kind eines Bürgers.
     *
     * @param buergerOid
     */
    @Override
    public void releaseBuergerKind(String buergerOid,String kindOid) {
        Buerger buerger = this.read(buergerOid);
        Buerger kind = this.read(kindOid);
        for(Buerger next : buerger.getKinder())
            if(next.getOid().equals(kindOid))
                buerger.getKinder().remove(next);
        this.update(buerger);
    }

    /**
     * read der bürger eines Pass.
     *
     * @param passOid
     * @return
     */
    @Override
    public Buerger readPassBuerger(String passOid) {
        List<Buerger> result = this.repo.findByPassOidAndMandantOid(passOid, readUser().getMandant().getOid());
        if (result.isEmpty()) {
// TODO
            LOG.warn(String.format("found no buerger with PassOid '%s'", passOid));
            return null;
        } else {
            return result.get(0);
        }
    }

    /**
     * release Operation für ein Pass und sein Bürger.
     *
     * @param passOid
     */
    @Override
    public void releasePassBuerger(String passOid) {

        Buerger buerger = this.readPassBuerger(passOid);
        if (buerger != null) {
            List<Pass> paesss = buerger.getPass().stream().filter(k -> k.getOid().equals(passOid)).collect(Collectors.toList());
            if (!paesss.isEmpty()) {
                buerger.getPass().remove(paesss.get(0));
            } else {
                LOG.warn(String.format("found no pass with oid %s", passOid));
            }
            this.update(buerger);
        }
    }

    /**
     * release Operation für ein Büreger und alle seine Pässe.
     *
     * @param buergerOid
     */
    @Override
    public void releaseBuergerPaesse(String buergerOid) {

        Buerger buerger = this.read(buergerOid);

        Set<Pass> paesse = buerger.getPass();
        Collection<Pass> removePaesse = new LinkedList<>();
        paesse.stream().forEach(removePaesse::add);
        
        paesse.removeAll(removePaesse);
        this.update(buerger);

    }

    /**
     * read die Bürger einer Wohnung.
     *
     * @param wohnungOid
     * @return
     */
    @Override
    public Iterable<Buerger> readWohnungBuerger(String wohnungOid) {
        return repo.findByWohnungenOidAndMandantOid(wohnungOid, readUser().getMandant().getOid());
    }

    /**
     * release Operation für eine Wohnung und alle seine Bürger.
     *
     * @param wohnungOid
     */
    @Override
    public void releaseWohnungAllBuerger(String wohnungOid) {
        Iterator<Buerger> iter = this.readWohnungBuerger(wohnungOid).iterator();
        while (iter.hasNext()) {
            Buerger buerger = iter.next();
            List<Wohnung> wohnung = buerger.getWohnungen().stream().filter(k -> k.getOid().equals(wohnungOid)).collect(Collectors.toList());
            if (!wohnung.isEmpty()) {
                buerger.getWohnungen().remove(wohnung.get(0));
            } else {
                LOG.warn(String.format("found no wohnung with oid %s", wohnungOid));
            }
            this.update(buerger);
        }

    }

    /**
     * release Operation für ein Büreger und alle seine Wohnungen.
     *
     * @param buergerOid
     */
    @Override
    public void releaseBuergerWohnungen(String buergerOid) {

        Buerger buerger = this.read(buergerOid);

        Set<Wohnung> wohnungen = buerger.getWohnungen();
        Collection<Wohnung> removeWohnungnen = new LinkedList<>();
        wohnungen.stream().forEach(removeWohnungnen::add);
        
        wohnungen.removeAll(removeWohnungnen);
        this.update(buerger);

    }

    /**
     * release Operation für eine Wohnung und ein Bürger.
     *
     * @param wohnungOid
     * @param buergerOid
     */
    @Override
    public void releaseWohnungBuerger(String wohnungOid, String buergerOid) {

        Buerger buerger = this.read(buergerOid);
        List<Wohnung> wohnung = buerger.getWohnungen().stream().filter(k -> k.getOid().equals(wohnungOid)).collect(Collectors.toList());
        if (!wohnung.isEmpty()) {
            buerger.getWohnungen().remove(wohnung.get(0));
        } else {
            LOG.warn(String.format("found no wohnung with oid %s", wohnungOid));
        }
        this.update(buerger);

    }

    /**
     * read die Bürger einer Staatsangehoerigkeit.
     *
     * @param StaatOid
     * @return
     */
    @Override
    public Iterable<Buerger> readStaatsangehoerigkeitBuerger(String StaatOid) {
        return repo.findByStaatsangehoerigkeitReferencesReferencedOidAndMandantOid(StaatOid, readUser().getMandant().getOid());
    }

    /**
     * release Operation für eine Staatsangehoerigkeit alle seine Bürger.
     *
     * @param staatOid
     */
    @Override
    public void releaseStaatsangehoerigkeitAllBuerger(String staatOid) {
        Iterator<Buerger> iter = this.readStaatsangehoerigkeitBuerger(staatOid).iterator();
        while (iter.hasNext()) {
            Buerger buerger = iter.next();
            List<StaatsangehoerigkeitReference> staat = buerger.getStaatsangehoerigkeitReferences().stream().filter(k -> k.getReferencedOid().equals(staatOid)).collect(Collectors.toList());
            if (!staat.isEmpty()) {
                buerger.getStaatsangehoerigkeitReferences().remove(staat.get(0));
            } else {
                LOG.warn(String.format("found no staatsangehoerigkeit with oid %s", staatOid));
            }
            this.update(buerger);
        }
    }

    /**
     * release Operation für eine Staatsangehoerigkeit und einen Bürger.
     *
     * @param staatOid
     * @param buergerOid
     */
    @Override
    public void releaseStaatsangehoerigkeitBuerger(String staatOid, String buergerOid) {

        Buerger buerger = this.read(buergerOid);
        List<StaatsangehoerigkeitReference> staats = buerger.getStaatsangehoerigkeitReferences().stream().filter(k -> k.getReferencedOid().equals(staatOid)).collect(Collectors.toList());
        if (!staats.isEmpty()) {
            buerger.getStaatsangehoerigkeitReferences().remove(staats.get(0));
        } else {
            LOG.warn(String.format("found no staatsaangehoerigkeit with oid %s", staatOid));
        }
        this.update(buerger);
    }

    @Subscribe
    public void evnetHandler(BuergerEvent event){
        switch (event.getEventType()) {
            case UPDATE: update(event.getEntity());
                break;
        }
    }
}
