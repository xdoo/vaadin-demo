package de.muenchen.eventbus.types;

/**
 *
 * @author claus.straube
 */
public enum EventType {
    CREATE,
    UPDATE,
    SAVE,
    DELETE,
    READ,
    SELECT,
    SELECT2UPDATE,
    SELECT2READ,
    COPY,
    CANCEL,
    QUERY,
    QUERY_CHILD,
    SAVE_CHILD,
    RELEASE,
    RELEASE_PARENT,
    RELEASE_PARTNER,
    SAVE_AS_CHILD,
    ADD_SEARCHED_CHILD,
    SAVE_AS_PARTNER,
    ADD_PARTNER,
    QUERY_PARTNER,
    UPDATE_PARTNER,
    SAVE_PARTNER,
    UPDATE_CHILD,
    REFRESH,
    LOGIN,
    LOGOUT,
    AUDIT_READ,
    AUDIT_CREATE,
    AUDIT_UPDATE,
    AUDIT_DELETE
}
