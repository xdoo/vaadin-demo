package de.muenchen.eventbus.selector;

import reactor.bus.selector.Selector;
import reactor.bus.selector.Selectors;

/**
 * Provides a simple Interface to mark a Class as an EventBus compatible Selector.
 *
 * @author p.mueller
 * @version 1.0
 */
public interface Keyable {
    /**
     * Get this Object as a {@link Selector}.
     *
     * @return The Selector
     */
    default Selector<?> getSelector() {
        return Selectors.$(this);
    }
}
