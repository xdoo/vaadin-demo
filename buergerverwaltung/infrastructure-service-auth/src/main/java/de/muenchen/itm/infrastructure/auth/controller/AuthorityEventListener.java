package de.muenchen.itm.infrastructure.auth.controller;

import de.muenchen.itm.infrastructure.auth.entities.Authority;
import de.muenchen.itm.infrastructure.auth.services.AuthorityEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.event.AbstractRepositoryEventListener;
import org.springframework.stereotype.Component;

/*
 * This file will be overwritten on every change of the model!
 * This file was automatically generated by GAIA.
 */

/**
 * This event-listener allows a AuthorityEventService to implement logic before and after Events.
 */
@Component
public class AuthorityEventListener extends AbstractRepositoryEventListener<Authority> {
    @Autowired
    AuthorityEventService service;

    @Override
    protected void onAfterCreate(Authority entity) {
        service.onAfterCreate(entity);
    }

    @Override
    protected void onBeforeCreate(Authority entity) {
        service.onBeforeCreate(entity);
    }

    @Override
    protected void onBeforeSave(Authority entity) {
        service.onBeforeSave(entity);
    }

    @Override
    protected void onAfterSave(Authority entity) {
        service.onAfterSave(entity);
    }

    @Override
    protected void onBeforeLinkSave(Authority parent, Object linked) {
        service.onBeforeLinkSave(parent, linked);
    }

    @Override
    protected void onAfterLinkSave(Authority parent, Object linked) {
        service.onAfterLinkSave(parent, linked);
    }

    @Override
    protected void onBeforeLinkDelete(Authority parent, Object linked) {
        service.onBeforeLinkDelete(parent, linked);
    }

    @Override
    protected void onBeforeDelete(Authority entity) {
        service.onBeforeDelete(entity);
    }

    @Override
    protected void onAfterDelete(Authority entity) {
        service.onAfterDelete(entity);
    }

    @Override
    protected void onAfterLinkDelete(Authority parent, Object linked) {
        service.onAfterLinkDelete(parent, linked);
    }
}
