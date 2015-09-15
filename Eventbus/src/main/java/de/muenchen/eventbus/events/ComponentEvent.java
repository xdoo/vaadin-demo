package de.muenchen.eventbus.events;

import com.vaadin.data.util.BeanItem;
import de.muenchen.eventbus.types.EventType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author claus
 * @param <T>
 */
public class ComponentEvent<T> extends Event {
    
    protected static final Logger LOG = LoggerFactory.getLogger(ComponentEvent.class);
    
    private final List<T> entities = new ArrayList<>();
    private final List<BeanItem<T>> items = new ArrayList<>();
    protected EventType eventType;
    protected Object itemID;
    protected String from;

    private final Class entityClass;

    public ComponentEvent(Class<T> entityClass,EventType eventType) {
        this.eventType = eventType;
        this.entityClass = entityClass;
    }

    
    public ComponentEvent<T> addEntity(T entity) {
        this.entities.add(entity);
        this.createAndAddBeanItem(entity);
        return this;
    }
    
    public ComponentEvent<T> addEntities(List<T> entities) {
        entities.stream().forEach(e -> { 
            this.addEntity(e);
        });
        return this;
    }

    public List<T> getEntities() {
        return entities;
    }
    
    public Optional<T> getEntity() {
        if(this.entities.size() == 1) {
            return Optional.ofNullable(this.entities.get(0));
        }
        LOG.warn(String.format("You're asking for one entity. In the list are %s entities.", this.entities.size()));
        return Optional.empty();
    }
    
    public ComponentEvent<T> createAndAddBeanItem(T entity) {
        this.items.add(new BeanItem(entity));
        return this;
    }
    
    public ComponentEvent<T> addItem(BeanItem<T> item) {
        this.items.add(item);
        this.entities.add(item.getBean());
        return this;
    }
    
    public ComponentEvent<T> addItems(List<BeanItem<T>> items) {
        items.stream().forEach(i -> {
            this.items.add(i);
            this.entities.add(i.getBean());
        });
        return this;
    }

    public List<BeanItem<T>> getItems() {
        return items;
    }
    
    public Optional<BeanItem<T>> getItem() {
        if(this.items.size() == 1) {
            return Optional.ofNullable(this.items.get(0));
        }
        LOG.warn(String.format("You're asking for one item. In the list are %s items.", this.items.size()));
        return Optional.empty();
    }

    public EventType getEventType() {
        return eventType;
    }

    public ComponentEvent<T> setEventType(EventType eventType) {
        this.eventType = eventType;
        return this;
    }

    public Object getItemID() {
        return itemID;
    }

    public ComponentEvent<T> setItemID(Object itemID) {
        this.itemID = itemID;
        return this;
    }

    public String getFrom() {
        return from;
    }

    public ComponentEvent<T> setFrom(String from) {
        this.from = from;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ComponentEvent<?> event = (ComponentEvent<?>) o;

        if (eventType != event.eventType) return false;
        return !(entityClass != null ? !entityClass.equals(event.entityClass) : event.entityClass != null);

    }

    @Override
    public int hashCode() {
        return 0;
    }

}