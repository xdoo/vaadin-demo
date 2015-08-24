package de.muenchen.vaadin.ui.components.buttons;

import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import de.muenchen.vaadin.ui.controller.ControllerContext;

import static de.muenchen.vaadin.ui.util.I18nPaths.*;

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
     * Create a new ActionButton with the specified label, context, action and //TODO navigateTo String.
     *
     * It won't have any ClickListener.
     *
     * The navigateTo String is optional.
     * @param context //TODO used to generate the id
     * @param action Action the button should represent (is styled for).
     * @param navigateTo //TODO used to generate the id
     */
    public ActionButton(final String label, final ControllerContext context, final Action action,final String navigateTo) {
        Button button = new Button(label);

        configureButton(context, action, navigateTo, button);

        this.button = button;
    }

    /**
     * Style the button by the action rules.
     *
     * @param context //TODO used to generate the id
     * @param action Action the button should represent (is styled for).
     * @param navigateTo //TODO used to generate the id
     * @param button The button to configure.
     */
    private void configureButton(ControllerContext context, Action action, String navigateTo, Button button) {
        action.getIcon().ifPresent(button::setIcon);
        action.getClickShortCut().ifPresent(button::setClickShortcut);
        action.getStyleNames().forEach(style -> button.setStyleName(style, true));
        button.setId(action.getID(navigateTo, context));

        setCompositionRoot(button);
    }

    /**
     * Create a new Actionbutton representing the action.
     *
     * The label of the button is fetched via i18n matching the action.
     *
     * @param context resolves the i18n key.
     * @param action action the button should represent (is styled for).
     * @param navigateTo //TODO used to generate id
     */
    public ActionButton(final ControllerContext context, final Action action, final String navigateTo) {
        this(resolveLabel(action,context),context,action,navigateTo);
    }

    /**
     * Resolve the action label with help of the context [i18n].
     *
     * @param action The action that will be resolved to a i18n string.
     * @param context Used to resolve the i18n key.
     * @return
     */
    private static String resolveLabel(Action action, ControllerContext context) {
        final String labelPath = getFormPath(action, Component.button, Type.label);
        return context.resolveRelative(labelPath);
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
