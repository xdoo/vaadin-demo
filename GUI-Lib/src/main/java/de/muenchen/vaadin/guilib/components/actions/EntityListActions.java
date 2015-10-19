package de.muenchen.vaadin.guilib.components.actions;

import com.vaadin.ui.Button;
import de.muenchen.eventbus.selector.entity.RequestEntityKey;
import de.muenchen.eventbus.selector.entity.RequestEvent;
import de.muenchen.vaadin.guilib.BaseUI;
import reactor.bus.Event;
import reactor.bus.EventBus;

import java.util.List;
import java.util.function.Supplier;

/**
 * Provides the List Actions for an entity.
 * e.g. multiple deletes with one action.
 *
 * @author p.mueller
 * @version 1.0
 */
public class EntityListActions<T> {

    /** The supplier for the List of entities. */
    private final Supplier<List<T>> entityListSupplier;
    /** The class of the entity. */
    private final Class<T> entityClass;

    /**
     * Create new List Actions for the Entity. All entitys for the actions are fetched via the supplier.
     *
     * @param entityListSupplier The supplier for the List of Entities.
     * @param entityClass        The class of the entity.
     */
    public EntityListActions(Supplier<List<T>> entityListSupplier, Class<T> entityClass) {

        if (entityListSupplier == null)
            throw new NullPointerException();
        if (entityClass == null)
            throw new NullPointerException();


        this.entityClass = entityClass;
        this.entityListSupplier = entityListSupplier;
    }

    /**
     * Delete all the Entities from the Supplier.
     * @param clickEvent can be null
     */
    public boolean delete(Button.ClickEvent clickEvent) {
        notifyRequest(RequestEvent.DELETE);
        return true;
    }

    /**
     * Create all the Entities from the Supplier.
     * @param clickEvent can be null
     */
    public boolean create(Button.ClickEvent clickEvent) {
        notifyRequest(RequestEvent.CREATE);
        return true;
    }

    /**
     * Update all the Entities from the Supplier.
     * @param clickEvent can be null
     */
    public boolean update(Button.ClickEvent clickEvent) {
        notifyRequest(RequestEvent.UPDATE);
        return true;
    }

    /**
     * Notify the request for each elem.
     *
     * @param event The type of the Event.
     */
    private void notifyRequest(RequestEvent event) {

        List<T> entityList = getEntityList();

        if (entityList == null)
            throw new NullPointerException();
        entityList.stream().forEach(buerger -> {
            final RequestEntityKey key = new RequestEntityKey(event, getEntityClass());
            getEventBus().notify(key, Event.wrap(buerger));
        });
    }

    /**
     * Get the List of entities.
     * @return The list.
     */
    public List<T> getEntityList() {
        return entityListSupplier.get();
    }

    /**
     * Get the EventBus.
     * @return The EventBus.
     */
    private EventBus getEventBus() {
        return BaseUI.getCurrentEventBus();
    }

    /**
     * Get the class of the entity.
     * @return The class of the entity.
     */
    public Class<T> getEntityClass() {
        return entityClass;
    }
}


