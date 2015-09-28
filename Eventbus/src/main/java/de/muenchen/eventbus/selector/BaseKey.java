package de.muenchen.eventbus.selector;

import com.vaadin.data.util.BeanItem;
import de.muenchen.eventbus.types.EventType;

/**
 * Created by claus.straube on 28.09.15.
 * fabian.holtkoetter ist unschuldig.
 */
public abstract class BaseKey {

    final Class entityClass;

    public BaseKey(Class entityClass) {
        if(entityClass==null)
            throw new IllegalArgumentException("EntityClass can't be null.");
        this.entityClass = entityClass;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BaseKey baseKey = (BaseKey) o;

        return entityClass.equals(baseKey.entityClass);

    }

    @Override
    public int hashCode() {
        return entityClass.hashCode();
    }
}
