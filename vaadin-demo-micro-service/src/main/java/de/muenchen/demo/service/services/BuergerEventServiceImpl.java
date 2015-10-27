package de.muenchen.demo.service.services;

import de.muenchen.demo.service.gen.domain.Buerger;
import de.muenchen.demo.service.gen.services.BuergerEventService;
import org.springframework.stereotype.Service;

/*
 * This file will NOT be overwritten by GAIA.
 * This file was automatically generated by GAIA.
 */

/**
 * Provides methods to implement logic before and after Events.
 * If used as generated by GAIA this service will be autowired and called by BuergerEventListener.
 */
@Service
public class BuergerEventServiceImpl implements BuergerEventService {
    // If you need access to the database you can autowire a Repository.
    // Repositories are generated into the package: .gen.rest
    //
    // @Autowired
    // <EntityName>Repository repo;

    @Override
    public void onAfterCreate(Buerger entity) {
        // Add your logic here.
    }

    @Override
    public void onBeforeCreate(Buerger entity) {
        // Add your logic here.
    }

    @Override
    public void onBeforeSave(Buerger entity) {
        // Add your logic here.
    }

    @Override
    public void onAfterSave(Buerger entity) {
        // Add your logic here.
    }

    @Override
    public void onBeforeLinkSave(Buerger parent, Object linked) {
        // Add your logic here.
    }

    @Override
    public void onAfterLinkSave(Buerger parent, Object linked) {
        // Add your logic here.
    }

    @Override
    public void onBeforeLinkDelete(Buerger parent, Object linked) {
        // Add your logic here.
    }

    @Override
    public void onBeforeDelete(Buerger entity) {
        // Add your logic here.
    }

    @Override
    public void onAfterDelete(Buerger entity) {
        // Add your logic here.
    }

    @Override
    public void onAfterLinkDelete(Buerger parent, Object linked) {
        // Add your logic here.
    }
}