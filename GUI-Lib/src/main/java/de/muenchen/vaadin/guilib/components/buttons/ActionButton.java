package de.muenchen.vaadin.guilib.components.buttons;

import com.vaadin.ui.Button;
import de.muenchen.vaadin.demo.i18nservice.I18nResolver;
import de.muenchen.vaadin.demo.i18nservice.buttons.Action;
import de.muenchen.vaadin.guilib.BaseUI;

import java.util.ArrayList;
import java.util.List;

import static de.muenchen.vaadin.demo.i18nservice.I18nPaths.Component;
import static de.muenchen.vaadin.demo.i18nservice.I18nPaths.Type;
import static de.muenchen.vaadin.demo.i18nservice.I18nPaths.getFormPath;

/**
 * Provides a styled Button that represents a specific action.
 *
 * @author p.mueller
 * @version 1.0
 */
public class ActionButton extends Button {

    /** Interface for Actions to perform.
     * If an Action crashes it returns true.
     * **/
    public interface CrashableActionPerformer {
        boolean perform(ClickEvent event);
    }

    /** List of Actions to perform on click. **/
    private List<CrashableActionPerformer> actions = new ArrayList<>();


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
        addClickListener(this::perform);
    }

    /**
     * Create a new Actionbutton representing the action.
     *
     * The label of the button is fetched via i18n matching the action.
     *
     * @param clazz resolves the i18n key.
     * @param action action the button should represent (is styled for).
     */
    public ActionButton(final Class clazz, final Action action) {
        this(resolveLabel(action, clazz), action);
    }

    /**
     * Resolve the action label with help of the context [i18n].
     *
     * @param action The action that will be resolved to a i18n string.
     * @param clazz Used to resolve the i18n key.
     * @return
     */
    private static String resolveLabel(Action action, Class clazz) {
        final String labelPath = getFormPath(action, Component.button, Type.label);
        return BaseUI.getCurrentI18nResolver().resolveRelative(clazz, labelPath);
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

    /**
     * Add an crashable action performer as click listener.
     * @param performer performer which can crash.
     */
    public void addActionPerformer(CrashableActionPerformer performer) {
        this.actions.add(performer);
    }

    @SuppressWarnings("unchecked")
    private void perform(ClickEvent event) {
        //Perform all actions until all actions are performed or one crashes.
        actions.stream().sequential().anyMatch(actionPerformer -> !actionPerformer.perform(event));
    }

}
