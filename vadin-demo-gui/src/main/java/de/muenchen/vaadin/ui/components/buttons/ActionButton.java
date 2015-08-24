package de.muenchen.vaadin.ui.components.buttons;

import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import de.muenchen.vaadin.ui.controller.ControllerContext;

import static de.muenchen.vaadin.ui.util.I18nPaths.*;

/**
 * Provides a Generic Button for an complete Entity that represents a specific action.
 *
 * @author p.mueller
 */
public class ActionButton extends CustomComponent {

    private final Button button;
    private String from;
    private String navigateTo;

    /**
     * Create a new EntityButton with the specified context and the Action.
     *
     * It will contain no clicklistener.
     *
     * The navigateTo and from Strings are optional.
     * @param context
     * @param action
     * @param navigateTo
     */
    public ActionButton(final String label, final ControllerContext context, final Action action,final String navigateTo) {
        Button button = new Button(label);
        action.getIcon().ifPresent(button::setIcon);
        action.getClickShortCut().ifPresent(button::setClickShortcut);
        action.getStyleNames().forEach(style -> button.setStyleName(style, true));
        button.setId(action.getID(navigateTo, context));
        setCompositionRoot(button);
        this.button = button;
    }

    public ActionButton(final ControllerContext context, final Action action, final String navigateTo) {
        this(resolveLabel(action,context),context,action,navigateTo);
    }

    private static String resolveLabel(Action action, ControllerContext context) {
        final String labelPath = getFormPath(action, Component.button, Type.label);
        return context.resolveRelative(labelPath);
    }

    public Button getButton() {
        return this.button;
    }


    public void addClickListener(Button.ClickListener clickListener) {
        button.addClickListener(clickListener);
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getNavigateTo() {
        return navigateTo;
    }

    public void setNavigateTo(String navigateTo) {
        this.navigateTo = navigateTo;
    }
}
