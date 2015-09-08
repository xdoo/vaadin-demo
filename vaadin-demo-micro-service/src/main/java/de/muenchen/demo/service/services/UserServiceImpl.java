/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.demo.service.services;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import de.muenchen.demo.service.domain.User;
import de.muenchen.demo.service.domain.UserAuthority;
import de.muenchen.demo.service.domain.UserAuthorityRepository;
import de.muenchen.demo.service.domain.UserRepository;
import de.muenchen.demo.service.util.Eventbus;
import de.muenchen.demo.service.util.IdService;
import de.muenchen.demo.service.util.QueryService;
import de.muenchen.demo.service.util.events.UserEvent;
import de.muenchen.eventbus.types.EventType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.bus.Event;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author praktikant.tmar
 */
@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    UserRepository repo;
    QueryService<User> search;
    @Autowired
    UserAuthorityRepository userAuthorityRepository;
    Eventbus eventbus;

    @Autowired
    public UserServiceImpl(UserRepository repo, EntityManager em, Eventbus eventbus) {
        this.repo = repo;
        this.search = new QueryService<>(this, em, User.class, "userName", "email");
        this.eventbus = eventbus;
    }

    @Override
    public User create() {
        User users = new User();
        users.setOid(IdService.next());
        return users;
    }

    @Override
    public User save(User users) {
        LOG.info(users.toString());
        Preconditions.checkArgument(users.getId() == null, "On save, the ID must be empty");
        return this.repo.save(users);
    }

    @Override
    public User read(String oid) {
        User result = this.repo.findFirstByOid(oid);
        if (Objects.isNull(result)) {
            // TODO
            LOG.warn(String.format("found no users with oid '%s'", oid));
            return null;
        } else {
            return result;
        }
    }

    @Override
    public User update(User users) {
        return this.repo.save(users);
    }

    @Override
    public void delete(String oid) {
        User item = this.read(oid);

        //this.sachbearbeiterService.releaseUserSachbearbeiter(oid);
        this.deleteUser(item.getUsername());
        eventbus.notify(UserEvent.class, Event.wrap(new UserEvent(EventType.RELEASE, item)));

        this.repo.delete(item);
    }

    public void deleteUser(String username) {
        List<UserAuthority> result = this.userAuthorityRepository.findByIdUserUsername(username);
        if (result != null) {
            result.stream().forEach(u -> {
                this.userAuthorityRepository.delete(u);
            });
        }
    }
    @Override
    public List<User> query() {
        Iterable<User> all = this.repo.findAll();
        return Lists.newArrayList(all);
    }

    @Override
    public List<User> query(String query) {
        return this.search.query(query);
    }

    @Override
    public User readByUsername(String username) {
        User result = this.repo.findFirstByUsername(username);
        if (Objects.isNull(result)) {
            // TODO
            LOG.warn(String.format("found no users with username '%s'", username));
            return null;
        } else {
            return result;
        }
    }

    @Override
    public User copy(String oid) {

        User in = this.read(oid);

        // map
        User out = new User(in);
        out.setOid(IdService.next());

        // in DB speichern
        this.save(out);

        return out;
    }

}
