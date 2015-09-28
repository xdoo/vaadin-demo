package de.muenchen.eventbus.selector;
/**
 * Stellt einen Key bereit, der einen Produzenten darüber Benachrichtigt, dass Daten benötigt werden.
 *
 * <p>
 *     Komponenten können nur Requests versenden. Controller fressen nur requests. Controller versenden nur Responses. Komponenten empfangen nur responses.
 * </p>
 *
 * @author fabian.holtkoetter
 * @version 0.00000001
 */
public class RequestKey extends BaseKey {

    public enum RequestEvent{
        CREATE, UPDATE, DELETE, READ_CURRENT, READ_LIST
    }

    private final RequestEvent requestEvent;

    public RequestKey(RequestEvent requestEvent, Class entityClass) {
        super(entityClass);
        if (requestEvent == null)
            throw new IllegalArgumentException("requestEvent can't be null.");
        this.requestEvent = requestEvent;

    }

    public RequestEvent getRequestEvent() {
        return requestEvent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        RequestKey that = (RequestKey) o;

        return getRequestEvent().equals(that.getRequestEvent());

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + getRequestEvent().hashCode();
        return result;
    }
}
