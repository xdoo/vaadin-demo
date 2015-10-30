package de.muenchen.vaadin.guilib.components.actions;

import com.vaadin.server.Page;
import com.vaadin.ui.Button;
import de.muenchen.eventbus.events.Association;
import de.muenchen.eventbus.selector.entity.RequestEntityKey;
import de.muenchen.eventbus.selector.entity.RequestEvent;
import de.muenchen.vaadin.demo.i18nservice.I18nPaths;
import de.muenchen.vaadin.demo.i18nservice.I18nResolver;
import de.muenchen.vaadin.demo.i18nservice.buttons.SimpleAction;
import de.muenchen.vaadin.guilib.BaseUI;
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

    /** The class of the Entity */
    private final Class<T> entityClass;
    /** The supplier for the Association. */
    private final Supplier<List<Association<?>>> association;


    /**
     * Create new AssociationActions for the Entity with the single association.
     *
     * @param association The association.
     * @param entityClass The class of the Entity.
     */
    public EntityAssociationListAction(Supplier<List<Association<?>>> association, Class<T> entityClass) {

        if (entityClass == null) {
            throw new NullPointerException();
        }
        if (association == null) {
            throw new NullPointerException();
        }

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
                        BaseUI.getCurrentI18nResolver().resolveRelative(entityClass, getNotificationPath(I18nPaths.NotificationType.warning, SimpleAction.save, I18nPaths.Type.label)),
                        BaseUI.getCurrentI18nResolver().resolveRelative(entityClass, getNotificationPath(I18nPaths.NotificationType.warning, SimpleAction.save, I18nPaths.Type.text)));
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
    private EventBus getEventBus() {
        return BaseUI.getCurrentEventBus();
    }

    /**
     * Get the Association.
     * @return The Association.
     */
    public List<Association<?>> getAssociations() {
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
