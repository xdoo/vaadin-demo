/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.demo.service.services;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import de.muenchen.demo.service.domain.Authority;
import de.muenchen.demo.service.domain.AuthorityPermission;
import de.muenchen.demo.service.domain.AuthorityPermissionRepository;
import de.muenchen.demo.service.domain.AuthorityRepository;
import de.muenchen.demo.service.domain.UserAuthority;
import de.muenchen.demo.service.domain.UserAuthorityRepository;
import de.muenchen.demo.service.util.IdService;
import de.muenchen.demo.service.util.QueryService;
import java.util.List;
import java.util.Objects;
import javax.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author praktikant.tmar
 */
@Service
public class AuthorityServiceImpl implements AuthorityService {

    private static final Logger LOG = LoggerFactory.getLogger(AuthorityService.class);

    AuthorityRepository repo;
    QueryService<Authority> search;
    @Autowired
    UserAuthorityRepository userAuthorityRepository;
    @Autowired
    AuthorityPermissionRepository authorityPermissionRepository;

    public AuthorityServiceImpl() {
    }

    @Autowired
    public AuthorityServiceImpl(AuthorityRepository repo, UserService userService, EntityManager em) {
        this.repo = repo;
        this.search = new QueryService<>(userService, em, Authority.class, "userName", "email");
    }

    @Override
    public Authority create() {
        Authority authority = new Authority();
        authority.setOid(IdService.next());
        return authority;
    }

    @Override
    public Authority save(Authority authority) {
        LOG.info(authority.toString());
        Preconditions.checkArgument(authority.getId() == null, "On save, the ID must be empty");
        return this.repo.save(authority);
    }

    @Override
    public Authority read(String oid) {
        Authority result = this.repo.findFirstByOid(oid);
        if (Objects.isNull(result)) {
            // TODO
            LOG.warn(String.format("found no authority with oid '%s'", oid));
            return null;
        } else {
            return result;
        }
    }

    @Override
    public Authority update(Authority authority) {
        return this.repo.save(authority);
    }

    @Override
    public void delete(String oid) {
        Authority item = this.read(oid);
        this.deleteUserAuthority(item.getAuthority());
        this.deletePermissionAuthority(item.getAuthority());
        this.repo.delete(item);
    }

    public void deleteUserAuthority(String authority) {
        List<UserAuthority> result = this.userAuthorityRepository.findByIdAuthorityAuthority(authority);
        if (result != null) {
            result.stream().forEach(u -> {
                this.userAuthorityRepository.delete(u);
            });
        }
    }

    public void deletePermissionAuthority(String authority) {
        List<AuthorityPermission> result = this.authorityPermissionRepository.findByIdAuthorityAuthority(authority);
        if (result != null) {
            result.stream().forEach(u -> {
                this.authorityPermissionRepository.delete(u);
            });
        }
    }

    @Override
    public List<Authority> query() {
        Iterable<Authority> all = this.repo.findAll();
        return Lists.newArrayList(all);
    }

    @Override
    public List<Authority> query(String query) {
        return this.search.query(query);
    }

    @Override
    public Authority copy(String oid) {

        Authority in = this.read(oid);

        // map
        Authority out = new Authority(in);
        out.setOid(IdService.next());

        // in DB speichern
        this.save(out);

        return out;
    }

}
