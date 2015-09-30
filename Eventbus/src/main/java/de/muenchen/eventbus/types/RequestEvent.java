package de.muenchen.eventbus.types;

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
    READ_LIST,
    READ_SELECTED,
    DELETE
}
