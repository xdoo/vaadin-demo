package de.muenchen.itm.infrastructure.auth.controller;

import de.muenchen.itm.infrastructure.auth.entities.Permission;
import de.muenchen.itm.infrastructure.auth.services.PermissionEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.event.AbstractRepositoryEventListener;
import org.springframework.stereotype.Component;

/*
 * This file will be overwritten on every change of the model!
 * This file was automatically generated by GAIA.
 */

/**
 * This event-listener allows a PermissionEventService to implement logic before and after Events.
 */
@Component
public class PermissionEventListener extends AbstractRepositoryEventListener<Permission> {
    @Autowired
    PermissionEventService service;

    @Override
    protected void onAfterCreate(Permission entity) {
        service.onAfterCreate(entity);
    }

    @Override
    protected void onBeforeCreate(Permission entity) {
        service.onBeforeCreate(entity);
    }

    @Override
    protected void onBeforeSave(Permission entity) {
        service.onBeforeSave(entity);
    }

    @Override
    protected void onAfterSave(Permission entity) {
        service.onAfterSave(entity);
    }

    @Override
    protected void onBeforeLinkSave(Permission parent, Object linked) {
        service.onBeforeLinkSave(parent, linked);
    }

    @Override
    protected void onAfterLinkSave(Permission parent, Object linked) {
        service.onAfterLinkSave(parent, linked);
    }

    @Override
    protected void onBeforeLinkDelete(Permission parent, Object linked) {
        service.onBeforeLinkDelete(parent, linked);
    }

    @Override
    protected void onBeforeDelete(Permission entity) {
        service.onBeforeDelete(entity);
    }

    @Override
    protected void onAfterDelete(Permission entity) {
        service.onAfterDelete(entity);
    }

    @Override
    protected void onAfterLinkDelete(Permission parent, Object linked) {
        service.onAfterLinkDelete(parent, linked);
    }
}
