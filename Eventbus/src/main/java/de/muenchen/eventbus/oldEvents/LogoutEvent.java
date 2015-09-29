package de.muenchen.eventbus.oldEvents;

/**
 * Created by maximilian.zollbrecht on 19.08.15.
 */
public class LogoutEvent{
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
