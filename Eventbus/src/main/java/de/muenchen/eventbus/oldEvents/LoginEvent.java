package de.muenchen.eventbus.oldEvents;


/**
 *
 * @author claus
 */
public class LoginEvent{

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