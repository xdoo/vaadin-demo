package de.muenchen.vaadin.ui.app.views.events;

import com.vaadin.data.util.BeanItem;
import de.muenchen.vaadin.ui.util.EventType;

/**
 *
 * @author claus.straube
 */
public class AppEvent<T> {
   
    protected EventType type;
    protected Object itemId;
    protected String navigateTo;
    protected String query;
    private T entity;
    private BeanItem<T> item;

    public AppEvent() {
    }

    public AppEvent(EventType type) {
        this.type = type;
    }

    public AppEvent(T entity, EventType type) {
        this.entity = entity;
        this.type = type;
    }

    public AppEvent(BeanItem<T> item, Object itemId, EventType type) {
        this.entity = item.getBean();
        this.item = item;
        this.type = type;
        this.itemId = itemId;
    }
    
    public EventType getType() {
        return type;
    }

    public AppEvent<T> setType(EventType type) {
        this.type = type;
        return this;
    }

    public AppEvent<T> setItemId(Object itemId) {
        this.itemId = itemId;
        return this;
    }

    public Object getItemId() {
        return itemId;
    }

    public AppEvent<T> setNavigateTo(String navigateTo) {
        this.navigateTo = navigateTo;
        return this;
    }

    public String getNavigateTo() {
        return navigateTo;
    }

    public String getQuery() {
        return query;
    }

    public AppEvent<T> setQuery(String query) {
        this.query = query;
        return this;
    }

    public T getEntity() {
        return this.entity;
    }

    public AppEvent<T> setEntity(T entity) {
        this.entity = entity;
        return this;
    }
    
    public AppEvent<T> setItem(BeanItem<T> item) {
        this.entity = item.getBean();
        this.item = item;
        return this;
    }

    public BeanItem<T> getItem() {
        return item;
    }
}
