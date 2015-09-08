package de.muenchen.eventbus.events;

/**
 * Created by fabian.holtkoetter on 01.09.15.
 */
public class RefreshEvent extends Event {
    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        return true;

    }

    @Override
    public int hashCode() {
        return 0;
    }
}
