/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.demo.service.services;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import de.muenchen.demo.service.domain.BaseEntity;
import de.muenchen.demo.service.domain.BaseRepository;
import de.muenchen.demo.service.domain.Mandant;
import de.muenchen.demo.service.domain.User;
import de.muenchen.demo.service.util.IdService;
import de.muenchen.demo.service.util.QueryService;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 *
 * @author praktikant.tmar
 * @param <T>
 */
public abstract class BaseService<  T extends BaseEntity> {

    @Autowired
    private UserService userService;
    @Autowired
    MandantService mandantService;
    BaseRepository<T> repo;
    QueryService<T> search;


    public T read(String oid) {
        List<T> result = this.repo.findByOidAndMandantMid(oid, readUser().getMandant().getMid());
        if (result.isEmpty()) {
            // TODO
            return null;
        } else {

            return result.get(0);

        }

    }

    public T create() {
        try {
            T t = (T) ((Class<T>) ((ParameterizedType) this.getClass().
                    getGenericSuperclass()).getActualTypeArguments()[0]).newInstance();

            t.setOid(IdService.next());
            return t;
        } catch (InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(BaseService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public User readUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();
        return userService.readByUsername(name);
    }

    public T save(T t) {
        Preconditions.checkArgument(t.getId() == null, "On save, the ID must be empty");
        Mandant mandant = mandantService.read(readUser().getMandant().getMid());
        t.setMandant(mandant);

        return this.repo.save(t);
    }

    public T update(T t) {
        return this.repo.save(t);
    }

    public void delete(String oid) {
        T item = this.read(oid);
        this.repo.delete(item);
    }

    public List<T> query() {
        Iterable<T> all = this.repo.findByMandantMid(readUser().getMandant().getMid());
        return Lists.newArrayList(all);
    }

    public T copy(String oid) {
        try {
            T in = this.read(oid);

            
            try {
                try {

                    Class<?> clazz = in.getClass();
                    Constructor<?> copyConstructor = clazz.getConstructor(clazz);

                    @SuppressWarnings("unchecked")
                    T out = (T) copyConstructor.newInstance(in);

                    // in DB speichern
                    out.setOid(IdService.next());
                    this.save(out);
                    return out;
                } catch (NoSuchMethodException | SecurityException ex) {
                    Logger.getLogger(BaseService.class.getName()).log(Level.SEVERE, null, ex);
                }

            } catch (IllegalArgumentException | InvocationTargetException ex) {
                Logger.getLogger(BaseService.class.getName()).log(Level.SEVERE, null, ex);
            }

        } catch (InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(BaseService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public List<T> query(String query) {
        return this.search.query(query);
    }
}
