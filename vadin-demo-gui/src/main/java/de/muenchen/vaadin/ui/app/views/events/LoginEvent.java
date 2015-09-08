package de.muenchen.vaadin.ui.app.views.events;


/**
 *
 * @author claus
 */
public class LoginEvent extends Event {

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
