package de.muenchen.demo.service.services;

import de.muenchen.demo.service.gen.domain.Wohnung;
import de.muenchen.demo.service.gen.services.WohnungEventService;
import org.springframework.stereotype.Service;

/*
 * This file will NOT be overwritten by GAIA.
 * This file was automatically generated by GAIA.
 */

/**
 * Provides methods to implement logic before and after Events.
 * If used as generated by GAIA this service will be autowired and called by WohnungEventListener.
 */
@Service
public class WohnungEventServiceImpl implements WohnungEventService {
    // If you need access to the database you can autowire a Repository.
    // Repositories are generated into the package: .gen.rest
    //
    // @Autowired
    // <EntityName>Repository repo;

    @Override
    public void onAfterCreate(Wohnung entity) {
        // Add your logic here.
    }

    @Override
    public void onBeforeCreate(Wohnung entity) {
        // Add your logic here.
    }

    @Override
    public void onBeforeSave(Wohnung entity) {
        // Add your logic here.
    }

    @Override
    public void onAfterSave(Wohnung entity) {
        // Add your logic here.
    }

    @Override
    public void onBeforeLinkSave(Wohnung parent, Object linked) {
        // Add your logic here.
    }

    @Override
    public void onAfterLinkSave(Wohnung parent, Object linked) {
        // Add your logic here.
    }

    @Override
    public void onBeforeLinkDelete(Wohnung parent, Object linked) {
        // Add your logic here.
    }

    @Override
    public void onBeforeDelete(Wohnung entity) {
        // Add your logic here.
    }

    @Override
    public void onAfterDelete(Wohnung entity) {
        // Add your logic here.
    }

    @Override
    public void onAfterLinkDelete(Wohnung parent, Object linked) {
        // Add your logic here.
    }
}