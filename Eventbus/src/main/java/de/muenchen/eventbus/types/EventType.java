package de.muenchen.eventbus.types;

/**
 *
 * @author claus.straube
 */
public enum EventType {
    // GUI Specific Events
    LOGIN,
    LOGOUT,
    ////

    // Buerger Events
    UPDATE_BUERGER,
    SAVE_BUERGER,
    DELETE_BUERGER,
    COPY_BUERGER,
    QUERY_BUERGER,

    SAVE_AND_ADD_CHILD,
    SAVE_CHILD,
    RELEASE_CHILD,
    ADD_CHILD,
    QUERY_CHILD,

    SAVE_AS_PARTNER,
    SAVE_PARTNER,
    RELEASE_PARTNER,
    ADD_PARTNER,
    QUERY_PARTNER,

    SELECT_TO_EDIT,
    SELECT_TO_READ,
    ////

    // Auditing Events
    READ_AUDIT,
    CREATE_AUDIT,
    UPDATE_AUDIT,
    DELETE_AUDIT
    ////
}
