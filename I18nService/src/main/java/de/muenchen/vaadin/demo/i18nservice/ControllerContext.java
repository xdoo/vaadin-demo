package de.muenchen.vaadin.demo.i18nservice;

import de.muenchen.vaadin.demo.api.local.LocalBuerger;
import de.muenchen.vaadin.demo.api.util.EventType;
import de.muenchen.vaadin.ui.app.views.events.AppEvent;
import de.muenchen.vaadin.ui.app.views.events.ComponentEvent;

/**
 * Interface to open up most important functionality of a Controller.
 *
 * @author p.mueller
 */
public interface ControllerContext<E> {
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

    FontAwesome resolveIcon(String relativePath);

    /**
     * Tell the controller to post this Event on the EventBus.
     * @param event the event to publish.
     */
    void postEvent(Object event);


    /**
     * Build an AppEvent like using the constructor, but using the right Entity/Controller specific Class.
     *
     * This is needed for the EventBus (it cannot distinguish between generic classes because of type erasure).
     * @param eventType the type the event should be
     * @return an Controller-specific AppEvent.
     */
    AppEvent<LocalBuerger> buildAppEvent(EventType eventType);

    /**
     * Build an ComponentEvent like using the constructor, but using the right Entity/Controller specific Class.
     *
     * This is needed for the EventBus (it cannot distinguish between generic classes because of type erasure).
     * @param eventType the type the event should be
     * @return an Controller-specific AppEvent.
     */
    ComponentEvent<LocalBuerger> buildComponentEvent(EventType eventType);

    String getBasePath();
}
