package de.muenchen.eventbus.selector;

/**
 * Provides a simple ABC for an Key that is specific to the class of an entity.
 *
 * @author fabian.holtoetter p.mueller
 * @version 1.0
 */
public abstract class BaseKey {
    /**
     * The class of the entity the key is for.
     */
    final Class entityClass;

    /**
     * Create a new Key for the entity by class.
     * The class cannot be null.
     * @param entityClass The non-null class of an Entity.
     */
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
