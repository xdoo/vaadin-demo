package de.muenchen.vaadin.guilib.components;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Grid;
import de.muenchen.eventbus.oldEvents.ComponentEvent;
import de.muenchen.eventbus.types.EventType;
import de.muenchen.vaadin.demo.i18nservice.I18nResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.bus.Event;
import reactor.fn.Consumer;

import java.util.List;
import java.util.Optional;

/**
 * Created by arne.schoentag on 18.09.15.
 */
public class GenericGrid<T> extends Grid implements Consumer<Event<ComponentEvent<T>>> {

    /**
     * The constant LOG.
     */
    protected static final Logger LOG = LoggerFactory.getLogger(GenericGrid.class);

    /**
     * Instantiates a new Generic table.
     *
     * @param controller  the controller
     * @param entityClass the entity class
     */
    public GenericGrid(final I18nResolver controller, Class<T> entityClass) {
        this.setContainerDataSource(new BeanItemContainer<>(entityClass));
    }

    /**
     * Adds a entity.
     *
     * @param entity the entity
     */
    public void add(Optional<T> entity) {
        entity.ifPresent(getContainerDataSource()::addItem);
    }

    /**
     * Adds a list of entities.
     *
     * @param entity the entity
     */
    public void addAll(List<T> entity) {
        getContainerDataSource().removeAllItems();
        entity.forEach(getContainerDataSource()::addItem);
    }

    /**
     * Delete an entity.
     *
     * @param id the id
     */
    public void delete(Object id) {
        getContainerDataSource().removeItem(id);
    }

    /**
     * Accept method to register to receive AppEvents.
     *
     * @param eventWrapper
     */
    @Override
    public void accept(reactor.bus.Event<ComponentEvent<T>> eventWrapper) {
        ComponentEvent event = eventWrapper.getData();

        if (EventType.SAVE_BUERGER.equals(event.getEventType())) {
            this.add(event.getEntity());
        }

        if (EventType.COPY_BUERGER.equals(event.getEventType())) {
            this.add(event.getEntity());
        }

        if (EventType.DELETE_BUERGER.equals(event.getEventType())) {
            this.delete(event.getItemID());
        }

        if (EventType.UPDATE_BUERGER.equals(event.getEventType())) {
            this.add(event.getEntity());
        }

        if (EventType.QUERY_BUERGER.equals(event.getEventType())) {
            this.addAll(event.getEntities());
        }
    }
}
