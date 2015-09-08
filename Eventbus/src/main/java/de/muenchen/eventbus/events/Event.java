package de.muenchen.eventbus.events;

/**
 * Created by rene.zarwel on 07.09.15.
 */
public abstract class Event {
    @Override
    public abstract boolean equals(Object o);
    @Override
    public abstract int hashCode();
}
