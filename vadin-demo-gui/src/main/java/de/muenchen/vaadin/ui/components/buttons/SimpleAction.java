package de.muenchen.vaadin.ui.components.buttons;

import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.themes.ValoTheme;
import de.muenchen.vaadin.demo.api.domain.BaseEntity;
import de.muenchen.vaadin.ui.controller.ControllerContext;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A simple enum declaring all actions and providing Values/Settings that depend on a action.
 */
public enum SimpleAction implements Action {

    //TODO check for duplicate/unused actions
    create(FontAwesome.MAGIC, ValoTheme.BUTTON_FRIENDLY),
    read,
    update(FontAwesome.PENCIL),
    back(FontAwesome.ANGLE_LEFT, ShortcutAction.KeyCode.ARROW_LEFT),
    save,
    delete(FontAwesome.TRASH_O),
    cancel,
    copy,
    logout(FontAwesome.SIGN_OUT);

    private final Set<String> styleNames;
    private final FontAwesome icon;
    private final Integer shortcutAction;

    SimpleAction(FontAwesome icon, Integer shortcutAction, String... styleNames) {
        this.shortcutAction = shortcutAction;
        this.styleNames = Stream.of(styleNames).collect(Collectors.toSet());
        this.icon = icon;
    }

    SimpleAction(FontAwesome icon, String... styleNames) {
        this(icon, null, styleNames);
    }

    SimpleAction() {
        this(null);
    }

    @Override
    public Optional<FontAwesome> getIcon() {
        return Optional.ofNullable(icon);
    }

    @Override
    public Set<String> getStyleNames() {
        return styleNames;
    }


    @Override
    public Optional<Integer> getClickShortCut() {
        return Optional.ofNullable(shortcutAction);
    }


    @Override
    public <E extends BaseEntity> String getID(String navigateTo, ControllerContext<E> context) {
        return String.format("%s_%s_%s_BUTTON", navigateTo, this.name().toUpperCase(), context.getBasePath());
    }
}
