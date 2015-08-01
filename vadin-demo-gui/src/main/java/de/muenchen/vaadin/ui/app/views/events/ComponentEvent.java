package de.muenchen.vaadin.ui.app.views.events;

import com.vaadin.data.util.BeanItem;
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

    public ComponentEvent() {
    }
    
    public ComponentEvent(T entity) {
        this.entities.add(entity);
        this.items.add(new BeanItem<>(entity));
    }
    
    public ComponentEvent(BeanItem<T> item) {
        this.items.add(item);
    }
    
    public void addEntity(T entity) {
        this.entities.add(entity);
        this.addBeanItem(entity);
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
    
    public void addBeanItem(T entity) {
        this.items.add(new BeanItem(entity));
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
}
