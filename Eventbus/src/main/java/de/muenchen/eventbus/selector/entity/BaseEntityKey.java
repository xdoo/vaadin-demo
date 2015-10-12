package de.muenchen.eventbus.selector.entity;

import de.muenchen.eventbus.selector.Keyable;

/**
 * Provides a simple ABC for an Key that is specific to the class of an entity.
 *
 * @author fabian.holtoetter p.mueller
 * @version 1.0
 */
public abstract class BaseEntityKey implements Keyable {
    /**
     * The class of the entity the key is for.
     */
    final private Class entityClass;

    /**
     * Create a new Key for the entity by class.
     * <p/>
     * If the class is null it matches any class.
     *
     * @param entityClass The class of an Entity.
     */
    public BaseEntityKey(Class entityClass) {
        this.entityClass = entityClass;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BaseEntityKey baseEntityKey = (BaseEntityKey) o;

        return entityClass == null || baseEntityKey.entityClass == null || entityClass.equals(baseEntityKey.entityClass);

    }

    @Override
    public int hashCode() {
        return entityClass.hashCode();
    }
}
