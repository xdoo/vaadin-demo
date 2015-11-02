package de.muenchen.demo.service.gen.controller;

import de.muenchen.demo.service.gen.domain.AdresseIntern;
import de.muenchen.demo.service.gen.services.AdresseInternEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.event.AbstractRepositoryEventListener;
import org.springframework.stereotype.Component;

/*
 * This file will be overwritten on every change of the model!
 * This file was automatically generated by GAIA.
 */

/**
 * This event-listener allows a AdresseInternEventService to implement logic before and after Events.
 */
@Component
public class AdresseInternEventListener extends AbstractRepositoryEventListener<AdresseIntern> {
    @Autowired
    AdresseInternEventService service;

    @Override
    protected void onAfterCreate(AdresseIntern entity) {
        service.onAfterCreate(entity);
    }

    @Override
    protected void onBeforeCreate(AdresseIntern entity) {
        service.onBeforeCreate(entity);
    }

    @Override
    protected void onBeforeSave(AdresseIntern entity) {
        service.onBeforeSave(entity);
    }

    @Override
    protected void onAfterSave(AdresseIntern entity) {
        service.onAfterSave(entity);
    }

    @Override
    protected void onBeforeLinkSave(AdresseIntern parent, Object linked) {
        service.onBeforeLinkSave(parent, linked);
    }

    @Override
    protected void onAfterLinkSave(AdresseIntern parent, Object linked) {
        service.onAfterLinkSave(parent, linked);
    }

    @Override
    protected void onBeforeLinkDelete(AdresseIntern parent, Object linked) {
        service.onBeforeLinkDelete(parent, linked);
    }

    @Override
    protected void onBeforeDelete(AdresseIntern entity) {
        service.onBeforeDelete(entity);
    }

    @Override
    protected void onAfterDelete(AdresseIntern entity) {
        service.onAfterDelete(entity);
    }

    @Override
    protected void onAfterLinkDelete(AdresseIntern parent, Object linked) {
        service.onAfterLinkDelete(parent, linked);
    }
}
