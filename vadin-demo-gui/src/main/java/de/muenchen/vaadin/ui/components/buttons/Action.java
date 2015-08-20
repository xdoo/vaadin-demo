package de.muenchen.vaadin.ui.components.buttons;

import com.sun.istack.internal.Nullable;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.ui.themes.ValoTheme;
import de.muenchen.vaadin.demo.api.domain.BaseEntity;
import de.muenchen.vaadin.ui.app.views.events.AppEvent;
import de.muenchen.vaadin.ui.controller.BuergerViewController;
import de.muenchen.vaadin.ui.controller.ControllerContext;
import de.muenchen.vaadin.ui.util.EventType;
import de.muenchen.vaadin.ui.util.I18nPaths;

import java.util.Optional;

/**
 * A simple enum declaring all actions and providing Values/Settings that depend on a action.
 */
public enum Action implements I18nPaths.I18nPath {

    //TODO check for duplicate/unused actions
    create, read, update, back, save, delete, cancel, copy, logout;


    /**
     * Get the Button ValoTheme StyleName for this action.
     * @return the theme name for an button.
     */
    public Optional<String> getStyleName() {
        String styleName = null;

        if (this == create)
            styleName = ValoTheme.BUTTON_FRIENDLY;

        return Optional.ofNullable(styleName);
    }

    /**
     * Get the configured {@link AppEvent} for the action.
     *
     * The right AppEvent class will be obtained from the ControllerContext (to ensure the EventBus can handle it).
     *
     * @param context used to create the correct 'Entity'AppEvent for the EventBus.
     * @param entity the entity to include into the AppEvent {@see AppEvent}.
     * @param navigateTo optional String to navigate on-click.
     * @param from optional String from.
     * @param <E> the type of the entity for the AppEvent.
     * @return
     */
    public <E extends BaseEntity> Optional<AppEvent<E>> getAppEvent(final ControllerContext<E> context,@Nullable final E entity,@Nullable final String navigateTo,@Nullable final String from) {

        AppEvent<E> appEvent = null;

        switch (this) {
            case back:      appEvent = context.buildEvent(EventType.CANCEL);    break;
            case update:    appEvent = context.buildEvent(EventType.SELECT2UPDATE).setEntity(entity);   break;
            case create:    appEvent = context.buildEvent(EventType.CREATE); break;
        }

        if (appEvent != null) {
            appEvent.navigateTo(navigateTo);
            appEvent.from(from);
        }
        return Optional.ofNullable(appEvent);
    }

    /**
     * Get the {@link com.vaadin.event.ShortcutAction.KeyCode} for this action.
     *
     * @return the keycode as int.
     */
    public Optional<Integer> getClickShortCut() {
        Integer shortcutAction = null;

        switch (this) {
            case back:  shortcutAction = ShortcutAction.KeyCode.ARROW_LEFT; break;
        }

        return Optional.ofNullable(shortcutAction);

    }

    /**
     * Get the {@link FontAwesome} icon for this action.
     * @return the icon.
     */
    public Optional<Resource> getIcon() {
        Resource resource = null;

        switch (this) {
            case back:      resource = FontAwesome.ANGLE_LEFT; break;
            case update:    resource = FontAwesome.PENCIL; break;
            case create:    resource = FontAwesome.MAGIC; break;
        }

        return Optional.ofNullable(resource);
    }

    public <E extends BaseEntity> String getID(String navigateTo, ControllerContext<E> context) {
        return String.format("%s_%s_%s_BUTTON", navigateTo,this.name().toUpperCase(), context.getBasePath());
    }
}
