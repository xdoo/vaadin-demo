package de.muenchen.vaadin.guilib.security.services;

import de.muenchen.vaadin.demo.apilib.local.Permission_;
import org.springframework.hateoas.Link;

import java.util.List;
import java.util.Optional;

/*
 * This file will be overwritten on every change of the model!
 * This file was automatically generated by GAIA.
 */

/**
 * Service zum Verwalten von Permission_ aus der GUI heraus.
 */
public interface Permission_Service {
    Permission_ create(Permission_ permission);

    Permission_ update(Permission_ permission);

    boolean delete(Link link);
		
    List<Permission_> findAll();

    List<Permission_> findAll(Link relation);

    Optional<Permission_> findOne(Link link);

    List<Permission_> queryPermission(String query);

    boolean setRelations(Link link, List<Link> relations);

    boolean setRelation(Link link, Link relation);
}
