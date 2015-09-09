/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.demo.service.services;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import de.muenchen.demo.service.domain.*;
import de.muenchen.demo.service.util.IdService;
import de.muenchen.demo.service.util.QueryService;
import de.muenchen.demo.service.util.events.BuergerEvent;
import de.muenchen.demo.service.util.events.SachbearbeiterEvent;
import de.muenchen.demo.service.util.events.UserEvent;
import de.muenchen.eventbus.EventBus;
import de.muenchen.eventbus.types.EventType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import reactor.bus.Event;

import javax.persistence.EntityManager;
import java.util.*;
import java.util.stream.Collectors;

import static reactor.bus.selector.Selectors.T;

/**
 *
 * @author praktikant.tmar
 */
@Service
public class SachbearbeiterServiceImpl implements SachbearbeiterService {

    @Autowired
    private BuergerService buergerService;
    private static final Logger LOG = LoggerFactory.getLogger(SachbearbeiterService.class);
    SachbearbeiterRepository repo;
    QueryService<Sachbearbeiter> search;
    @Autowired
    private UserService userService;
    @Autowired
    MandantService mandantService;
    @Autowired
    EventBus eventbus;

    @Autowired
    public SachbearbeiterServiceImpl(SachbearbeiterRepository repo, UserService userService, EntityManager em, EventBus eventbus) {
        this.repo = repo;
        this.search = new QueryService<>(userService, em, Sachbearbeiter.class, "adresseOid", "ausrichtung", "stock");
        this.eventbus = eventbus;
        eventbus.on(T(SachbearbeiterEvent.class), this::sachbearbeiterEventHandler);
        eventbus.on(T(UserEvent.class), this::userEventHandler);

    }

    @Override
    public void delete(String oid) {
        Sachbearbeiter item = this.read(oid);
        this.releaseSachbearbeiterAllBuerger(oid);
        this.repo.delete(item);

    }

    @Override
    public Sachbearbeiter create() {
        Sachbearbeiter sachbearbeiter = new Sachbearbeiter();
        sachbearbeiter.setOid(IdService.next());
        return sachbearbeiter;
    }

    @Override
    public Sachbearbeiter save(Sachbearbeiter sachbearbeiter) {
        LOG.info(sachbearbeiter.toString());
        Preconditions.checkArgument(sachbearbeiter.getId() == null, "On save, the ID must be empty");
        Mandant mandant = mandantService.read(readUser().getMandant().getOid());
        sachbearbeiter.setMandant(mandant);
        return this.repo.save(sachbearbeiter);
    }

    @Override
    public Sachbearbeiter read(String oid) {
        Sachbearbeiter result = this.repo.findFirstByOidAndMandantOid(oid, readUser().getMandant().getOid());
        if (Objects.isNull(result)) {
// TODO
            LOG.warn(String.format("found no sachbearbeiter with oid '%s'", oid));
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
    public Sachbearbeiter update(Sachbearbeiter sachbearbeiter) {
        return this.repo.save(sachbearbeiter);
    }

    @Override
    public List<Sachbearbeiter> query() {
        Iterable<Sachbearbeiter> all = this.repo.findByMandantOid(readUser().getMandant().getOid());
        return Lists.newArrayList(all);
    }

    @Override
    public List<Sachbearbeiter> query(String query) {
        return this.search.query(query);
    }

    @Override
    public Sachbearbeiter copy(String oid) {
        Sachbearbeiter in = this.read(oid);
        // map
        Sachbearbeiter out = new Sachbearbeiter(in);
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
    public void releaseSachbearbeiterBuerger(String buergerOid, String sOid) {

        Sachbearbeiter sachbearbeiter = this.read(sOid);
        Buerger buerger = this.buergerService.read(buergerOid);
        List<Buerger> b = sachbearbeiter.getBuerger().stream().filter(k -> k.getOid().equals(buergerOid)).collect(Collectors.toList());
        if (!b.isEmpty()) {
            sachbearbeiter.getBuerger().remove(b.get(0));
        } else {
            LOG.warn(String.format("found no buerger with oid %s", buergerOid));
        }
        List<Sachbearbeiter> s = buerger.getSachbearbeiter().stream().filter(k -> k.getOid().equals(sOid)).collect(Collectors.toList());
        if (!s.isEmpty()) {
            buerger.getSachbearbeiter().remove(s.get(0));
        } else {
            LOG.warn(String.format("found no sachbearbeiter with oid %s", sOid));
        }
        this.update(sachbearbeiter);

        //this.buergerService.update(buerger);
        eventbus.notify(BuergerEvent.class, Event.wrap(new BuergerEvent(EventType.UPDATE, buerger)));
    }

    @Override
    public void releaseSachbearbeiterAllBuerger(String oid) {

        Sachbearbeiter sachbearbeiter = this.read(oid);
        Set<Buerger> buergerList = sachbearbeiter.getBuerger();
        Collection<Buerger> removeBuerger = new LinkedList<>();
        buergerList.stream().forEach((buerger) -> {
            buerger.getSachbearbeiter().remove(sachbearbeiter);
            //this.buergerService.update(buerger);
            eventbus.notify(BuergerEvent.class, Event.wrap(new BuergerEvent(EventType.UPDATE, buerger)));
            removeBuerger.add(buerger);
        });

        buergerList.removeAll(removeBuerger);
        this.update(sachbearbeiter);
    }

    @Override
    public Sachbearbeiter readUserSachbearbeiter(String oid) {
        Sachbearbeiter result = this.repo.findByUserOidAndMandantOid(oid, readUser().getMandant().getOid());
        if (result == null) {
// TODO
            LOG.warn(String.format("found no Sachbearbeiter with UserOid '%s'", oid));
            return null;
        } else {
            return result;
        }
    }

    @Override
    public void releaseUserSachbearbeiter(String userOid) {

        Sachbearbeiter sachbearbeiter = this.readUserSachbearbeiter(userOid);
        if (sachbearbeiter != null) {
            User user = sachbearbeiter.getUser();
            if (user.equals(this.userService.read(userOid))) {
                sachbearbeiter.setUser(null);
                this.update(sachbearbeiter);

            }
        }
    }

    @Override
    public void releaseSachbearbeiterUser(String sOid) {

        Sachbearbeiter sachbearbeiter = this.read(sOid);
        sachbearbeiter.setUser(null);
        this.update(sachbearbeiter);

    }

    public void sachbearbeiterEventHandler(Event<SachbearbeiterEvent> eventWrapper) {
        SachbearbeiterEvent event = eventWrapper.getData();
        switch (event.getEventType()) {
            case UPDATE:
                update(event.getEntity());
                break;
        }
    }

    public void userEventHandler(Event<UserEvent> eventWrapper) {
        UserEvent event = eventWrapper.getData();
        switch (event.getEventType()) {
            case RELEASE:
                readUserSachbearbeiter(event.getEntity().getOid());
                break;
        }
    }
}
