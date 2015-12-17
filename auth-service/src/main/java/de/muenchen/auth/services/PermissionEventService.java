package de.muenchen.auth.services;

/*
 * This file will be overwritten on every change of the model!
 * This file was automatically generated by GAIA.
 */

import de.muenchen.auth.entities.Permission;

/**
 * Provides methods to implement logic before and after Events.
 */
public interface PermissionEventService {
    void onAfterCreate(Permission entity);

    void onBeforeCreate(Permission entity);

    void onBeforeSave(Permission entity);

    void onAfterSave(Permission entity);

    void onBeforeLinkSave(Permission parent, Object linked);

    void onAfterLinkSave(Permission parent, Object linked);

    void onBeforeLinkDelete(Permission parent, Object linked);

    void onBeforeDelete(Permission entity);

    void onAfterDelete(Permission entity);

    void onAfterLinkDelete(Permission parent, Object linked);
}
