package de.muenchen.vaadin.ui.components.buttons;

import com.sun.istack.internal.Nullable;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.ui.Button;
import com.vaadin.ui.themes.ValoTheme;
import de.muenchen.vaadin.demo.api.domain.BaseEntity;
import de.muenchen.vaadin.ui.app.views.events.AppEvent;
import de.muenchen.vaadin.ui.controller.BuergerViewController;
import de.muenchen.vaadin.ui.controller.ControllerContext;
import de.muenchen.vaadin.ui.util.EventType;
import de.muenchen.vaadin.ui.util.I18nPaths;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * A simple enum declaring all actions and providing Values/Settings that depend on a action.
 */
public interface Action extends I18nPaths.I18nPath {


    default List<String> getStyleNames() {
        return new LinkedList<>();
    }

    default Optional<Integer> getClickShortCut() {
        return Optional.empty();
    }

    default Optional<FontAwesome> getIcon() {
        return Optional.empty();
    }

    <E extends BaseEntity> String getID(String navigateTo, ControllerContext<E> context);

}
