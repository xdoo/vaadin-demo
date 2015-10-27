package de.muenchen.demo.service.gen.services;


import de.muenchen.demo.service.gen.domain.Wohnung;

/*
 * This file will be overwritten on every change of the model!
 * This file was automatically generated by GAIA.
 */

/**
 * Provides methods to implement logic before and after Events.
 */
public interface WohnungEventService {
    void onAfterCreate(Wohnung entity);

    void onBeforeCreate(Wohnung entity);

    void onBeforeSave(Wohnung entity);

    void onAfterSave(Wohnung entity);

    void onBeforeLinkSave(Wohnung parent, Object linked);

    void onAfterLinkSave(Wohnung parent, Object linked);

    void onBeforeLinkDelete(Wohnung parent, Object linked);

    void onBeforeDelete(Wohnung entity);

    void onAfterDelete(Wohnung entity);

    void onAfterLinkDelete(Wohnung parent, Object linked);
}
