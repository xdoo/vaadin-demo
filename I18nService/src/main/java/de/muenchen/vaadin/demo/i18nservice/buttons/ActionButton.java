package de.muenchen.vaadin.demo.i18nservice.buttons;

import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import de.muenchen.vaadin.demo.i18nservice.I18nResolver;

import static de.muenchen.vaadin.demo.i18nservice.I18nPaths.*;

/**
 * Provides a styled Button that represents a specific action.
 *
 * @author p.mueller
 * @version 1.0
 */
public class ActionButton extends CustomComponent {

    /** The Button wrapped by this ActionButton. */
    private final Button button;

    /**
     * Create a new ActionButton with the specified label, context, action.
     *
     * It won't have any ClickListener.
     *
     * The navigateTo String is optional.
     * @param action Action the button should represent (is styled for).
     */
    public ActionButton(final String label, final Action action) {
        Button button = new Button(label);

        configureButton(action, button);

        this.button = button;
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
     * @param button The button to configure.
     */
    private void configureButton(Action action, Button button) {
        action.getIcon().ifPresent(button::setIcon);
        action.getClickShortCut().ifPresent(button::setClickShortcut);
        action.getStyleNames().forEach(style -> button.setStyleName(style, true));

        setCompositionRoot(button);
    }

    /**
     * Get the Button.
     * @return the button.
     */
    public Button getButton() {
        return this.button;
    }

    /**
     * Add a {@link com.vaadin.ui.Button.ClickListener} to this button.
     * @param clickListener to be added.
     */
    public void addClickListener(Button.ClickListener clickListener) {
        button.addClickListener(clickListener);
    }
}
