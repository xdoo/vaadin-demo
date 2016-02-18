package de.muenchen.itm.infrastructure.auth.services;

import de.muenchen.itm.infrastructure.auth.entities.User;
import org.springframework.stereotype.Service;


/*
 * This file will NOT be overwritten by GAIA.
 * This file was automatically generated by GAIA.
 */

/**
 * Provides methods to implement logic before and after Events.
 * If used as generated by GAIA this service will be autowired and called by UserEventListener.
 */
@Service
public class UserEventServiceImpl implements UserEventService {
    // If you need access to the database you can autowire a Repository.
    // Repositories are generated into the package: .gen.rest
    //
    // @Autowired
    // <EntityName>Repository repo;

    @Override
    public void onAfterCreate(User entity) {
        // Add your logic here.
    }

    @Override
    public void onBeforeCreate(User entity) {
        // Add your logic here.
    }

    @Override
    public void onBeforeSave(User entity) {
        // Add your logic here.
    }

    @Override
    public void onAfterSave(User entity) {
        // Add your logic here.
    }

    @Override
    public void onBeforeLinkSave(User parent, Object linked) {
        // Add your logic here.
    }

    @Override
    public void onAfterLinkSave(User parent, Object linked) {
        // Add your logic here.
    }

    @Override
    public void onBeforeLinkDelete(User parent, Object linked) {
        // Add your logic here.
    }

    @Override
    public void onBeforeDelete(User entity) {
        // Add your logic here.
    }

    @Override
    public void onAfterDelete(User entity) {
        // Add your logic here.
    }

    @Override
    public void onAfterLinkDelete(User parent, Object linked) {
        // Add your logic here.
    }
}
