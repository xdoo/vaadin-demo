package de.muenchen.vaadin.ui.components.buttons;

import com.vaadin.server.FontAwesome;
import de.muenchen.vaadin.demo.api.domain.BaseEntity;
import de.muenchen.vaadin.ui.controller.ControllerContext;
import de.muenchen.vaadin.ui.util.I18nPaths;

import java.util.*;

/**
 * A simple enum declaring all actions and providing Values/Settings that depend on a action.
 */
public interface Action extends I18nPaths.I18nPath {

    default Set<String> getStyleNames() {
        return new HashSet<>();
    }

    default Optional<Integer> getClickShortCut() {
        return Optional.empty();
    }

    default Optional<FontAwesome> getIcon() {
        return Optional.empty();
    }

    <E extends BaseEntity> String getID(String navigateTo, ControllerContext<E> context);

}
