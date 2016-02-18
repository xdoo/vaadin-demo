package de.muenchen.itm.infrastructure.auth.services;

import de.muenchen.itm.infrastructure.auth.entities.Authority;
import org.springframework.stereotype.Service;


/*
 * This file will NOT be overwritten by GAIA.
 * This file was automatically generated by GAIA.
 */

/**
 * Provides methods to implement logic before and after Events.
 * If used as generated by GAIA this service will be autowired and called by AuthorityEventListener.
 */
@Service
public class AuthorityEventServiceImpl implements AuthorityEventService {
    // If you need access to the database you can autowire a Repository.
    // Repositories are generated into the package: .gen.rest
    //
    // @Autowired
    // <EntityName>Repository repo;

    @Override
    public void onAfterCreate(Authority entity) {
        // Add your logic here.
    }

    @Override
    public void onBeforeCreate(Authority entity) {
        // Add your logic here.
    }

    @Override
    public void onBeforeSave(Authority entity) {
        // Add your logic here.
    }

    @Override
    public void onAfterSave(Authority entity) {
        // Add your logic here.
    }

    @Override
    public void onBeforeLinkSave(Authority parent, Object linked) {
        // Add your logic here.
    }

    @Override
    public void onAfterLinkSave(Authority parent, Object linked) {
        // Add your logic here.
    }

    @Override
    public void onBeforeLinkDelete(Authority parent, Object linked) {
        // Add your logic here.
    }

    @Override
    public void onBeforeDelete(Authority entity) {
        // Add your logic here.
    }

    @Override
    public void onAfterDelete(Authority entity) {
        // Add your logic here.
    }

    @Override
    public void onAfterLinkDelete(Authority parent, Object linked) {
        // Add your logic here.
    }
}
