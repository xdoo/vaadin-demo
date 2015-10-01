package de.muenchen.eventbus.selector.entity;

/**
 * Stellt einen Key bereit, der einen Produzenten darüber Benachrichtigt, dass Daten benötigt werden.
 *
 * <p>
 *     Komponenten können nur Requests versenden. Controller fressen nur requests. Controller versenden nur Responses. Komponenten empfangen nur responses.
 * </p>
 *
 * @author fabian.holtkoetter
 * @version 1.0
 */
public class RequestEntityKey extends BaseEntityKey {

    /**
     * The specific Event that is requested.
     */
    private final RequestEvent requestEvent;

    /**
     * Create a new RequestEntityKey with the desired RequestEvent and for the entity class.
     *
     * @param requestEvent The requested Event.
     * @param entityClass The class of the entity the request is for.
     */
    public RequestEntityKey(RequestEvent requestEvent, Class entityClass) {
        super(entityClass);
        if (requestEvent == null)
            throw new IllegalArgumentException("requestEvent can't be null.");
        this.requestEvent = requestEvent;

    }

    /**
     * Get the requested Event by this key.
     * @return
     */
    public RequestEvent getRequestEvent() {
        return requestEvent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        RequestEntityKey that = (RequestEntityKey) o;

        return getRequestEvent().equals(that.getRequestEvent());

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + getRequestEvent().hashCode();
        return result;
    }
}
