package de.muenchen.vaadin.guilib.components;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Grid;
import de.muenchen.eventbus.events.ComponentEvent;
import de.muenchen.eventbus.types.EventType;
import de.muenchen.vaadin.demo.i18nservice.ControllerContext;
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
    public GenericGrid(final ControllerContext controller, Class<T> entityClass) {
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
        LOG.error(id.toString());
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

        if (EventType.SAVE.equals(event.getEventType())) {
            this.add(event.getEntity());
        }

        if (EventType.COPY.equals(event.getEventType())) {
            this.add(event.getEntity());
        }

        if (EventType.DELETE.equals(event.getEventType())) {
            this.delete(event.getItemID());
        }

        if (EventType.UPDATE.equals(event.getEventType())) {
            this.add(event.getEntity());
        }

        if (EventType.QUERY.equals(event.getEventType())) {
            this.addAll(event.getEntities());
        }
    }
}