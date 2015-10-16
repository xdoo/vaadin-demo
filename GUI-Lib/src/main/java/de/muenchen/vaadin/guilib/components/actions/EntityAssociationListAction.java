package de.muenchen.vaadin.guilib.components.actions;

import com.vaadin.server.Page;
import com.vaadin.ui.Button;
import de.muenchen.eventbus.events.Association;
import de.muenchen.eventbus.selector.entity.RequestEntityKey;
import de.muenchen.eventbus.selector.entity.RequestEvent;
import de.muenchen.vaadin.demo.i18nservice.I18nPaths;
import de.muenchen.vaadin.demo.i18nservice.I18nResolver;
import de.muenchen.vaadin.demo.i18nservice.buttons.SimpleAction;
import de.muenchen.vaadin.guilib.components.GenericWarningNotification;
import reactor.bus.Event;
import reactor.bus.EventBus;

import java.util.List;
import java.util.function.Supplier;

import static de.muenchen.vaadin.demo.i18nservice.I18nPaths.getNotificationPath;

/**
 * Provides simple Actions for changing an Association on a List of Associations.
 * They are designed to be used as ClickListeners with java8 method reference.
 *
 * @author rene.zarwel
 * @version 1.0
 */
public class EntityAssociationListAction<T> {

    /** The EventBus for notifying the Action. */
    private final EventBus eventBus;
    /** The class of the Entity */
    private final Class<T> entityClass;
    /** The supplier for the Association. */
    private final Supplier<List<Association<T>>> association;
    /** The I18NResolver of this context **/
    private final I18nResolver resolver;

    /**
     * Create new AssociationActions for the Entity with the single association.
     *
     * @param association The association.
     * @param eventBus    The EventBus.
     * @param entityClass The class of the Entity.
     */
    public EntityAssociationListAction(I18nResolver resolver, Supplier<List<Association<T>>> association, EventBus eventBus, Class<T> entityClass) {
        if (eventBus == null) {
            throw new NullPointerException();
        }
        if (entityClass == null) {
            throw new NullPointerException();
        }
        if (association == null) {
            throw new NullPointerException();
        }

        this.resolver = resolver;
        this.eventBus = eventBus;
        this.entityClass = entityClass;
        this.association = association;
    }

    /**
     * Add the associations to the selected Buerger.
     * @param clickEvent can be null
     */
    public boolean addAssociations(Button.ClickEvent clickEvent) {
        return getAssociations().stream()
                .allMatch(association -> {
            try {
                getEventBus().notify(new RequestEntityKey(RequestEvent.ADD_ASSOCIATION, getEntityClass()), Event.wrap(association));
                return true;
            } catch (Exception e) { //TODO Find a good Exception Type
                GenericWarningNotification warn = new GenericWarningNotification(
                        resolver.resolveRelative(getNotificationPath(I18nPaths.NotificationType.warning, SimpleAction.save, I18nPaths.Type.label)),
                        resolver.resolveRelative(getNotificationPath(I18nPaths.NotificationType.warning, SimpleAction.save, I18nPaths.Type.text)));
                warn.show(Page.getCurrent());
                return false;
            }
        });
    }

    /**
     * Remove the association from the selected Buerger.
     * @param clickEvent can be null
     */
    public boolean removeAssociations(Button.ClickEvent clickEvent) {
        return getAssociations().stream()
                .allMatch(association -> {
                    getEventBus().notify(new RequestEntityKey(RequestEvent.REMOVE_ASSOCIATION, getEntityClass()), Event.wrap(association));
                    return true;
                });

    }

    /**
     * Get the EventBus.
     * @return The EventBus.
     */
    public EventBus getEventBus() {
        return eventBus;
    }

    /**
     * Get the Association.
     * @return The Association.
     */
    public List<Association<T>> getAssociations() {
        return association.get();
    }

    /**
     * Get the entity class.
     * @return The class of the entity.
     */
    public Class<T> getEntityClass() {
        return entityClass;
    }
}