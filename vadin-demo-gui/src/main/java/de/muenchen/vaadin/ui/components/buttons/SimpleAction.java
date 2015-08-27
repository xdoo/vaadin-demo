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
 *
 * @author p.mueller
 * @version 1.0
 * @
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
    add,
    logout(FontAwesome.SIGN_OUT);
    

    /** The potential empy set of {@link com.vaadin.ui.themes.ValoTheme} style Strings. */
    private final Set<String> styleNames;
    /** The icon for this action. */
    private final FontAwesome icon;
    /** The {@link com.vaadin.event.ShortcutAction.KeyCode} for the action. */
    private final Integer shortcutAction;

    /**
     * Create a new SimpleAction with icon, shortcut action and multiple strings for styles.
     *
     * No styleName means the default style.
     *
     * @param icon The icon to use.
     * @param shortcutAction The shortcut for this action.
     * @param styleNames The styles for this action.
     */
    SimpleAction(FontAwesome icon, Integer shortcutAction, String... styleNames) {
        this.shortcutAction = shortcutAction;
        this.styleNames = Stream.of(styleNames).collect(Collectors.toSet());
        this.icon = icon;
    }

    /**
     * Create a new SimpleAction with the icon and possible style strings.
     *
     * @param icon The icon to use.
     * @param styleNames The styles for this action.
     */
    SimpleAction(FontAwesome icon, String... styleNames) {
        this(icon, null, styleNames);
    }

    /**
     * Create a new SimpleAction with now styling at all.
     */
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
    public String getID(String navigateTo, ControllerContext context) {
        return String.format("%s_%s_%s_BUTTON", navigateTo, this.name().toUpperCase(), context.getBasePath());
    }
}
