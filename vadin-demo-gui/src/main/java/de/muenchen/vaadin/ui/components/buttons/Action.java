package de.muenchen.vaadin.ui.components.buttons;

import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.ui.themes.ValoTheme;
import de.muenchen.vaadin.demo.api.domain.BaseEntity;
import de.muenchen.vaadin.ui.app.views.events.AppEvent;
import de.muenchen.vaadin.ui.controller.ControllerContext;
import de.muenchen.vaadin.ui.util.EventType;
import de.muenchen.vaadin.ui.util.I18nPaths;

import java.util.Optional;

/**
 * Created by p.mueller on 19.08.15.
 */
public enum Action implements I18nPaths.I18nPath {

    create, read, update, back, save, delete, cancel, copy;


    public Optional<String> getStyleName() {
        String styleName = null;
        if (this == create)
            styleName = ValoTheme.BUTTON_FRIENDLY;

        return Optional.ofNullable(styleName);
    }

    public <E extends BaseEntity> Optional<AppEvent<E>> getAppEvent(final ControllerContext<E> context, final E entity, final String navigateTo, final String from) {

        AppEvent<E> appEvent = null;
        if (this == back)
            appEvent = context.buildEvent(EventType.CANCEL);
        if (this == update)
            appEvent = context.buildEvent(EventType.SELECT2UPDATE).setEntity(entity);
        if (this == create)
            appEvent = context.buildEvent(EventType.CREATE);

        if (appEvent != null) {
            appEvent.navigateTo(navigateTo);
            appEvent.from(from);
        }
        return Optional.ofNullable(appEvent);
    }


    public Optional<Integer> getClickShortCut() {
        Integer shortcutAction = null;
        if (this == back)
            shortcutAction = ShortcutAction.KeyCode.ARROW_LEFT;

        return Optional.ofNullable(shortcutAction);

    }

    public Optional<Resource> getIcon() {
        Resource resource = null;
        if (this == back)
            resource = FontAwesome.ANGLE_LEFT;
        if (this == update)
            resource = FontAwesome.PENCIL;
        if (this == create)
            resource = FontAwesome.MAGIC;

        return Optional.ofNullable(resource);
    }
}
