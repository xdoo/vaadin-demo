package de.muenchen.vaadin.guilib;

import com.vaadin.ui.UI;
import de.muenchen.eventbus.EventBus;

/**
 * Provides a simple BaseUI with out EventBus.
 *
 * @author p.mueller
 * @version 1.0
 */
public abstract class BaseUI extends UI {
    /** The one and only EventBus. */
    private final EventBus eventBus = new EventBus();

    /**
     * Get the EventBus from the current BaseUI.
     *
     * @return The EventBus.
     */
    public static EventBus getEventBus() {
        return ((BaseUI) getCurrent()).eventBus;
    }
}
