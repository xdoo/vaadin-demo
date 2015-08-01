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
    
//    public BaseService(UserService userService, MandantService mandantService) {
//    	this.userService = userService;
//    	this.mandantService = mandantService;
//	}
    
    public T read(String oid) {
        List<T> result = this.repo.findByOidAndMandantOid(oid, readUser().getMandant().getOid());
        if (result.isEmpty()) {
            // TODO
            return null;
        } else {

            return result.get(0);

        }

    }

    T createContents(Class<T> clazz) throws InstantiationException, IllegalAccessException {
        return clazz.newInstance();
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
        Mandant mandant = mandantService.read(readUser().getMandant().getOid());
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
        Iterable<T> all = this.repo.findByMandantOid(readUser().getMandant().getOid());
        return Lists.newArrayList(all);
    }

    public T copy(String oid) {
        T source = this.read(oid);
        T result;
        try {
            T clone = (T) ((Class<T>) ((ParameterizedType) this.getClass().
                    getGenericSuperclass()).getActualTypeArguments()[0]).newInstance();
            try {
                clone = (T) source.clone();
            } catch (CloneNotSupportedException ex) {
                java.util.logging.Logger.getLogger(WohnungServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
            clone.setOid(IdService.next());
            clone.setId(null);
            result = save(clone);
            return result;

        } catch (InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(BaseService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public List<T> query(String query) {
        return this.search.query(query);
    }
}
