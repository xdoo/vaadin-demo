package de.muenchen.vaadin.ui.controller;

import de.muenchen.vaadin.demo.api.domain.BaseEntity;
import de.muenchen.vaadin.services.MessageService;
import de.muenchen.vaadin.ui.app.views.events.AppEvent;
import de.muenchen.vaadin.ui.app.views.events.BuergerAppEvent;
import de.muenchen.vaadin.ui.util.EventBus;
import de.muenchen.vaadin.ui.util.EventType;

/**
 * Created by p.mueller on 19.08.15.
 */
public interface ControllerContext<E extends BaseEntity> {
    /**
     * Build the complete Path with the basePath and resolve the String from the properties.
     * @param path
     * @return the resolved String.
     */
    String resolve(String path);

    void postToEventBus(AppEvent<?> appEvent);

    AppEvent<E> buildEvent(EventType eventType);
}
