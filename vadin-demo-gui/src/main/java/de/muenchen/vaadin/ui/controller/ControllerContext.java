package de.muenchen.vaadin.ui.controller;

import de.muenchen.vaadin.demo.api.domain.BaseEntity;
import de.muenchen.vaadin.ui.app.views.events.AppEvent;
import de.muenchen.vaadin.demo.api.util.EventType;

/**
 * Interface to open up most important functionality of a Controller.
 *
 * @author p.mueller
 */
public interface ControllerContext<E extends BaseEntity> {
    /**
     * Build the complete Path with the basePath and resolve the String from the properties.
     * @param path
     * @return the resolved String.
     */
    String resolve(String path);

    /**
     * //TODO
     * @param relativePath
     * @return
     */
    String resolveRelative(String relativePath);

    /**
     * Tell the controller to post this Event on the EventBus.
     * @param appEvent the event to publish.
     */
    void postToEventBus(AppEvent<?> appEvent);

    /**
     * Build an AppEvent like using the constructor, but using the right Entity/Controller specific Class.
     *
     * This is needed for the EventBus (it cannot distinguish between generic classes because of type erasure).
     * @param eventType the type the event should be
     * @return an Controller-specific AppEvent.
     */
    AppEvent<E> buildEvent(EventType eventType);

    String getBasePath();
}
