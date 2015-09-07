package de.muenchen.vaadin.ui.app.views.events;

import com.vaadin.data.util.BeanItem;
import de.muenchen.vaadin.demo.api.util.EventType;

import java.util.Optional;

/**
 *
 * @author claus.straube
 */
public class AppEvent<T> extends Event{
   
    protected EventType type;
    protected Object itemId;
    protected String query;
    private T entity;
    private BeanItem<T> item;

    private final Class entityClass;

    public AppEvent(Class<T> entityClass, EventType type) {
        this.type = type;
        this.entityClass = entityClass;
    }

    public EventType getType() {
        return type;
    }

    public AppEvent<T> setType(EventType type) {
        this.type = type;
        return this;
    }

    public Object getItemId() {
        return itemId;
    }

    public AppEvent<T> setItemId(Object itemId) {
        this.itemId = itemId;
        return this;
    }

    public Optional<String> getQuery() {
        return Optional.ofNullable(this.query);
    }

    public AppEvent<T> query(String query) {
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

    public BeanItem<T> getItem() {
        return item;
    }

    public AppEvent<T> setItem(BeanItem<T> item) {
        this.entity = item.getBean();
        this.item = item;
        return this;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AppEvent<?> appEvent = (AppEvent<?>) o;

        if (type != appEvent.type) return false;
        return !(entityClass != null ? !entityClass.equals(appEvent.entityClass) : appEvent.entityClass != null);

    }

    @Override
    public int hashCode() {
        return 0;
    }

}
