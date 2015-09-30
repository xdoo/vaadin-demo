package de.muenchen.eventbus.selector;

import reactor.bus.selector.Selector;
import reactor.bus.selector.Selectors;

public interface Keyable {
    default Selector<?> getSelector() {
        return Selectors.$(this);
    }
}
