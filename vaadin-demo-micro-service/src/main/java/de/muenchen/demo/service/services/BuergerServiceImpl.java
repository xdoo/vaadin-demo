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
import java.util.stream.StreamSupport;

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
                buergerList.stream().filter((element) -> (element == this.read(buergerOid))).forEach(removeBuerger::add);
                buergerList.removeAll(removeBuerger);

                //this.sachbearbeiterService.update(sachbearbeiter);
                eventbus.post(new SachbearbeiterEvent(EventType.UPDATE, sachbearbeiter));
                
            }
            
            list.removeAll(removeSachbearbeiter);
            this.update(buerger);
        }
    }
    
    @Override
    public Iterable<Buerger> readEltern(String oid) {
        return repo.findByKinderOidAndMandantOid(oid, readUser().getMandant().getOid());
    }
    
    @Override
    public void releaseBuergerEltern(String kindOid) {
        
        StreamSupport.stream(this.readEltern(kindOid).spliterator(), false)
                .forEach(buerger -> {
            Set<Buerger> kinder = buerger.getKinder();
            kinder.remove(this.read(kindOid));         
            this.update(buerger);
            
        });
        
        
    }
    
    @Override
    public void releaseBuergerElternteil(String kindOid, String buergerOid) {
        
        Buerger buerger = this.read(buergerOid);
        Set<Buerger> kinder = buerger.getKinder();
        Collection<Buerger> removeKinder = new LinkedList<>();
        kinder.stream().filter((element) -> (element == this.read(kindOid))).forEach(removeKinder::add);
        kinder.removeAll(removeKinder);
        this.update(buerger);
        
    }
    
    @Override
    public void releaseBuergerKinder(String oid) {
        
        Buerger buerger = this.read(oid);
        
        Set<Buerger> kinder = buerger.getKinder();
        Collection<Buerger> removeKinder = new LinkedList<>();
        kinder.stream().forEach(removeKinder::add);
        
        kinder.removeAll(removeKinder);
        this.update(buerger);
        
    }
    
    @Override
    public Buerger readPassBuerger(String oid) {
        List<Buerger> result = this.repo.findByPassOidAndMandantOid(oid, readUser().getMandant().getOid());
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
            buerger.getPass().remove(this.passService.read(passOid));
            this.update(buerger);
        }
    }
    
    @Override
    public void releaseBuergerPaesse(String oid) {
        
        Buerger buerger = this.read(oid);
        
        Set<Pass> paesse = buerger.getPass();
        Collection<Pass> removePaesse = new LinkedList<>();
        paesse.stream().forEach(removePaesse::add);
        
        paesse.removeAll(removePaesse);
        this.update(buerger);
        
    }
    
    @Override
    public Iterable<Buerger> readWohnungBuerger(String oid) {
        return repo.findByWohnungenOidAndMandantOid(oid, readUser().getMandant().getOid());
    }
    
    @Override
    public void releaseWohnungAllBuerger(String wohnungOid) {

        this.readWohnungBuerger(wohnungOid).forEach(buerger -> {
            Set<Wohnung> wohnung = buerger.getWohnungen();
            wohnung.remove(this.wohnungService.read(wohnungOid));
            this.update(buerger);
        });
        
        
    }
    
    @Override
    public void releaseBuergerWohnungen(String oid) {
        
        Buerger buerger = this.read(oid);
        
        Set<Wohnung> wohnungen = buerger.getWohnungen();
        Collection<Wohnung> removeWohnungnen = new LinkedList<>();
        wohnungen.stream().forEach(removeWohnungnen::add);
        
        wohnungen.removeAll(removeWohnungnen);
        this.update(buerger);
        
    }
    
    @Override
    public void releaseWohnungBuerger(String wohnungOid, String buergerOid) {
        
        Buerger buerger = this.read(buergerOid);
        Set<Wohnung> wohnung = buerger.getWohnungen();
        wohnung.remove(this.wohnungService.read(wohnungOid));
        this.update(buerger);
        
    }
    
    @Override
    public Iterable<Buerger> readStaatsangehoerigkeitBuerger(String oid) {
        return repo.findByStaatsangehoerigkeitReferencesReferencedOidAndMandantOid(oid, readUser().getMandant().getOid());
    }
    
    @Override
    public void releaseStaatsangehoerigkeitAllBuerger(String staatOid) {

        this.readStaatsangehoerigkeitBuerger(staatOid).forEach(buerger -> {
            Iterator<StaatsangehoerigkeitReference> staatRefIter = buerger.getStaatsangehoerigkeitReferences().iterator();

            while (staatRefIter.hasNext()) {
                StaatsangehoerigkeitReference staatRef = staatRefIter.next();

                if (staatRef == this.staatService.readReference(staatOid)) {
                    staatRefIter.remove();
                    this.update(buerger);
                }
            }
        });

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

    @Subscribe
    public void evnetHandler(BuergerEvent event){
        switch (event.getEventType()) {
            case UPDATE: update(event.getEntity());
                break;
        }
    }
}
