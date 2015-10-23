package de.muenchen.vaadin.guilib.components.buttons;

import com.vaadin.ui.Button;
import de.muenchen.vaadin.demo.i18nservice.I18nResolver;
import de.muenchen.vaadin.demo.i18nservice.buttons.Action;

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

    /** List of Actions to perform on click. **/
    private List<CrashableActionPerformer> actions = new ArrayList<>();

    /**
     * Create a new Actionbutton representing the action.
     * <p/>
     * The label of the button is fetched via i18n matching the action.
     *
     * @param context resolves the i18n key.
     * @param action  action the button should represent (is styled for).
     */
    public ActionButton(final I18nResolver context, final Action action) {
        this(resolveLabel(action, context), action);
    }


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

    /**
     * Add an crashable action performer as click listener.
     * @param performer performer which can crash.
     */
    public void addActionPerformer(CrashableActionPerformer performer) {
        this.actions.add(performer);
    }

    /**
     * Remove the Action Performer from the click listeners.
     *
     * @param performer The performer to remove, that is already in the list.
     * @return true, if it was in the list and is removed.
     */
    public boolean removeActionPerformer(CrashableActionPerformer performer) {
        return this.actions.remove(performer);
    }

    @SuppressWarnings("unchecked")
    private void perform(ClickEvent event) {
        //Perform all actions until all actions are performed or one crashes.
        actions.stream().sequential().anyMatch(actionPerformer -> !actionPerformer.perform(event));
    }


    /**
     * Interface for Actions to perform.
     * If an Action crashes it returns true.
     **/
    public interface CrashableActionPerformer {
        boolean perform(ClickEvent event);
    }

}
