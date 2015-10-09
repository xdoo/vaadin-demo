package de.muenchen.vaadin.demo.i18nservice.buttons;

import com.vaadin.ui.Button;
import de.muenchen.vaadin.demo.i18nservice.I18nResolver;

import static de.muenchen.vaadin.demo.i18nservice.I18nPaths.*;

/**
 * Provides a styled Button that represents a specific action.
 *
 * @author p.mueller
 * @version 1.0
 */
public class ActionButton extends Button {


    /**
     * Create a new ActionButton with the specified label, context, action.
     *
     * It won't have any ClickListener.
     *
     * The navigateTo String is optional.
     * @param action Action the button should represent (is styled for).
     */
    public ActionButton(final String label, final Action action) {
        super(label);

        configureButton(action);
    }

    /**
     * Create a new Actionbutton representing the action.
     *
     * The label of the button is fetched via i18n matching the action.
     *
     * @param context resolves the i18n key.
     * @param action action the button should represent (is styled for).
     */
    public ActionButton(final I18nResolver context, final Action action) {
        this(resolveLabel(action, context), action);
    }

    /**
     * Resolve the action label with help of the context [i18n].
     *
     * @param action The action that will be resolved to a i18n string.
     * @param context Used to resolve the i18n key.
     * @return
     */
    private static String resolveLabel(Action action, I18nResolver context) {
        final String labelPath = getFormPath(action, Component.button, Type.label);
        return context.resolveRelative(labelPath);
    }

    /**
     * Style the button by the action rules.
     *
     * @param action Action the button should represent (is styled for).
     */
    private void configureButton(Action action) {
        action.getIcon().ifPresent(this::setIcon);
        action.getClickShortCut().ifPresent(this::setClickShortcut);
        action.getStyleNames().forEach(style -> this.setStyleName(style, true));
    }
}
