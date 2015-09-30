package de.muenchen.eventbus.selector.entity;

import de.muenchen.eventbus.selector.EventBusKey;

/**
 * Provides a simple ABC for an Key that is specific to the class of an entity.
 *
 * @author fabian.holtoetter p.mueller
 * @version 1.0
 */
public abstract class BaseEntityKey implements EventBusKey {
    /**
     * The class of the entity the key is for.
     */
    final Class entityClass;

    /**
     * Create a new Key for the entity by class.
     * The class cannot be null.
     * @param entityClass The non-null class of an Entity.
     */
    public BaseEntityKey(Class entityClass) {
        if(entityClass==null)
            throw new IllegalArgumentException("EntityClass can't be null.");
        this.entityClass = entityClass;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BaseEntityKey baseEntityKey = (BaseEntityKey) o;

        return entityClass.equals(baseEntityKey.entityClass);

    }

    @Override
    public int hashCode() {
        return entityClass.hashCode();
    }
}
