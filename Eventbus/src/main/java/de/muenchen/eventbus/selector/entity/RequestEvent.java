package de.muenchen.eventbus.selector.entity;

/**
 * Provides a simple enum for all possible Events that can be requested on the Eventbus.
 *
 * @author fabian.holtkoetter
 * @version 1.0
 */
public enum RequestEvent {
    CREATE,
    UPDATE,
    ADD_ASSOCIATION,
    REMOVE_ASSOCIATION,
    READ,
    READ_LIST,
    READ_SELECTED,
    DELETE
}
