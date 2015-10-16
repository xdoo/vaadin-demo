package de.muenchen.vaadin.guilib.components.actions;

import com.vaadin.server.Page;
import com.vaadin.ui.Button;
import de.muenchen.eventbus.selector.entity.RequestEntityKey;
import de.muenchen.eventbus.selector.entity.RequestEvent;
import de.muenchen.vaadin.demo.i18nservice.I18nPaths;
import de.muenchen.vaadin.demo.i18nservice.I18nResolver;
import de.muenchen.vaadin.demo.i18nservice.buttons.SimpleAction;
import de.muenchen.vaadin.guilib.components.GenericWarningNotification;
import reactor.bus.Event;
import reactor.bus.EventBus;

import java.util.function.Supplier;

import static de.muenchen.vaadin.demo.i18nservice.I18nPaths.getNotificationPath;


/**
 * Provides simple general Actions for a single Entity. They are designed to be used as ClickListeners with java8 method
 * reference.
 *
 * @author p.mueller
 * @version 1.0
 */
public class EntitySingleActions<T> {
    /** The supplier for the single entity. */
    private final Supplier<T> entitySupplier;
    /** The EventBus. */
    private final EventBus eventBus;
    /** The class of the entity. */
    private final Class<T> entityClass;
    /** The I18NResolver of this context **/
    private final I18nResolver resolver;

    /**
     * Create single actions for the entity.
     *
     * @param entitySupplier The supplier for the entity.
     * @param eventBus       The EventBus.
     * @param entityClass    The class of the Entity.
     */
    public EntitySingleActions(I18nResolver resolver, Supplier<T> entitySupplier, EventBus eventBus, Class<T> entityClass) {
        this.entityClass = entityClass;
        if (eventBus == null)
            throw new NullPointerException();
        if (entitySupplier == null)
            throw new NullPointerException();

        this.resolver = resolver;
        this.eventBus = eventBus;
        this.entitySupplier = entitySupplier;
    }

    /**
     * Delete the single Entity.
     *
     * @param clickEvent can be null
     */
    public boolean delete(Button.ClickEvent clickEvent) {
        notifyRequest(RequestEvent.DELETE);
        return true;
    }

    /**
     * Create the single Entity.
     *
     * @param clickEvent can be null
     */
    public boolean create(Button.ClickEvent clickEvent) {
        try {
            notifyRequest(RequestEvent.CREATE);
            return true;
        } catch (Exception e) { //TODO Find a good Exception Type
            GenericWarningNotification warn = new GenericWarningNotification(
                    resolver.resolveRelative(getNotificationPath(I18nPaths.NotificationType.warning, SimpleAction.create, I18nPaths.Type.label)),
                    resolver.resolveRelative(getNotificationPath(I18nPaths.NotificationType.warning, SimpleAction.create, I18nPaths.Type.text)));
            warn.show(Page.getCurrent());
            return false;
        }
    }

    /**
     * Update the single Entity.
     *
     * @param clickEvent can be null
     */
    public boolean update(Button.ClickEvent clickEvent) {
        try {
            notifyRequest(RequestEvent.UPDATE);
            return true;
        } catch (Exception e) { //TODO Find a good Exception Type
            GenericWarningNotification warn = new GenericWarningNotification(
                    resolver.resolveRelative(getNotificationPath(I18nPaths.NotificationType.warning, SimpleAction.update, I18nPaths.Type.label)),
                    resolver.resolveRelative(getNotificationPath(I18nPaths.NotificationType.warning, SimpleAction.update, I18nPaths.Type.text)));
            warn.show(Page.getCurrent());
            return false;
        }
    }

    /**
     * Read the Entity and set it as current selected Entity.
     *
     * @param clickEvent can be null
     */
    public boolean read(Button.ClickEvent clickEvent) {
        notifyRequest(RequestEvent.READ_SELECTED);
        return true;
    }

    /**
     * Notify the Request.
     *
     * @param event The type of the Request.
     */
    private void notifyRequest(RequestEvent event) {
        if (getEntity() == null)
            throw new NullPointerException();
        final RequestEntityKey key = new RequestEntityKey(event, getEntityClass());
        getEventBus().notify(key, Event.wrap(getEntity()));
    }

    /**
     * Get the Entity.
     *
     * @return the entity.
     */
    public T getEntity() {
        return entitySupplier.get();
    }

    /**
     * Get the EventBus.
     * @return The EventBus.
     */
    public EventBus getEventBus() {
        return eventBus;
    }

    /**
     * Get the class of the Entity.
     * @return The class of the entity.
     */
    public Class<T> getEntityClass() {
        return entityClass;
    }
}
