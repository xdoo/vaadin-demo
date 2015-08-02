package de.muenchen.vaadin.ui.app.views.events;

import com.vaadin.data.util.BeanItem;
import de.muenchen.vaadin.ui.util.EventType;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author claus
 * @param <T>
 */
public class ComponentEvent<T> {
    
    protected static final Logger LOG = LoggerFactory.getLogger(ComponentEvent.class);
    
    private final List<T> entities = new ArrayList<>();
    private final List<BeanItem<T>> items = new ArrayList<>();
    protected EventType eventType;
    protected Object itemID;

    public ComponentEvent(EventType eventType) {
        this.eventType = eventType;
    }
    
    public ComponentEvent(T entity, EventType eventType) {
        this.entities.add(entity);
        this.eventType = eventType;
        this.items.add(new BeanItem<>(entity));
    }
    
    public ComponentEvent(BeanItem<T> item, EventType eventType) {
        this.items.add(item);
        this.eventType = eventType;
    }
    
    public void addEntity(T entity) {
        this.entities.add(entity);
        this.createAndAddBeanItem(entity);
    }
    
    public void addEntities(List<T> entities) {
        entities.stream().forEach(e -> { 
            this.addEntity(e);
        });
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
    
    public void createAndAddBeanItem(T entity) {
        this.items.add(new BeanItem(entity));
    }
    
    public void addItem(BeanItem<T> item) {
        this.items.add(item);
        this.entities.add(item.getBean());
    }
    
    public void addItems(List<BeanItem<T>> items) {
        items.stream().forEach(i -> {
            this.items.add(i);
            this.entities.add(i.getBean());
        });
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

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public Object getItemID() {
        return itemID;
    }

    public void setItemID(Object itemID) {
        this.itemID = itemID;
    }
}
