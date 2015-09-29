package de.muenchen.vaadin.demo.i18nservice.buttons;

import com.vaadin.server.FontAwesome;
import de.muenchen.vaadin.demo.i18nservice.I18nResolver;
import de.muenchen.vaadin.demo.i18nservice.I18nPaths;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * A simple enum declaring all actions and providing all required values/settings that depend on a action.
 *
 * @author p.mueller
 * @version 1.0
 */
public interface Action extends I18nPaths.I18nPath {

    /**
     * Get all the the {@link com.vaadin.ui.themes.ValoTheme} style strings for this action.
     * @return zero, one or multiple Strings for the styles.
     */
    default Set<String> getStyleNames() {
        return new HashSet<>();
    }

    /**
     * Get the integer representation of the {@link com.vaadin.event.ShortcutAction.KeyCode} for this action.
     * @return an optional int value.
     */
    default Optional<Integer> getClickShortCut() {
        return Optional.empty();
    }

    /**
     * Get the {@link FontAwesome} icon for this action.
     * @return an optional FontAwesome icon.
     */
    default Optional<FontAwesome> getIcon() {
        return Optional.empty();
    }

    /**
     * Get the ID representation for this action.
     *
     * //TODO its questionable that the id is provided this way. This means that only one id exists per action.
     *
     * @param navigateTo
     * @param context
     * @return the String identifier.
     */
    String getID(String navigateTo, I18nResolver context);

}