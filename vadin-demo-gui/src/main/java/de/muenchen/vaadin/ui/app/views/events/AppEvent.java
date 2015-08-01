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

    public void setType(EventType type) {
        this.type = type;
    }

    public void setItemId(Object itemId) {
        this.itemId = itemId;
    }

    public Object getItemId() {
        return itemId;
    }

    public void setNavigateTo(String navigateTo) {
        this.navigateTo = navigateTo;
    }

    public String getNavigateTo() {
        return navigateTo;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public T getEntity() {
        return this.entity;
    }

    public void setEntity(T entity) {
        this.entity = entity;
    }
    
    public void setItem(BeanItem<T> item) {
        this.entity = item.getBean();
        this.item = item;
    }

    public BeanItem<T> getItem() {
        return item;
    }
}
