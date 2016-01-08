package de.muenchen.vaadin.guilib.components.buttons;

import com.vaadin.server.Page;
import com.vaadin.ui.Button;
import de.muenchen.vaadin.demo.i18nservice.I18nPaths;
import de.muenchen.vaadin.demo.i18nservice.buttons.Action;
import de.muenchen.vaadin.guilib.BaseUI;
import de.muenchen.vaadin.guilib.components.GenericSuccessNotification;

import java.util.ArrayList;
import java.util.List;

import static de.muenchen.vaadin.demo.i18nservice.I18nPaths.*;

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
    private Class entity;
    private String label;
    private Action notified;
    private boolean notify;

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
        notified = action;
        this.label = label;
        configureButton(action);
        addClickListener(this::perform);
    }

    /**
     * Create a new Actionbutton representing the action.
     *
     * The label of the button is fetched via i18n matching the action.
     *
     * @param entityClass resolves the i18n key.
     * @param action action the button should represent (is styled for).
     */
    public ActionButton(final Class entityClass, final Action action) {
        this(resolveLabel(action, entityClass), action);
        this.entity=entityClass;
        notified = action;
    }


    /**
     * Resolve the action label with help of the context [i18n].
     *
     * @param action The action that will be resolved to a i18n string.
     * @param entityClass Used to resolve the i18n key.
     * @return
     */
    private static String resolveLabel(Action action, Class entityClass) {
        final String labelPath = getFormPath(action, Component.button, Type.label);
        return BaseUI.getCurrentI18nResolver().resolveRelative(entityClass, labelPath);
    }

    /**
     * Activates or deactivates successnotifications
     *
     * @param use boolea describing if notifications are used or not;
     */
    public void useNotification(boolean use){
        notify=use;
    }

    /**
     * Sets the displayed notification to the action-appropriate one
     *
     * @param action the Action the button should display anotification for.
     */
    public void setNotifyAction(Action action){
        notified=action;
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
        showSuccessNotification();
    }

    private void showSuccessNotification() {
        if (notify) {
            if(entity!= null) {
                GenericSuccessNotification succes = new GenericSuccessNotification(
                        BaseUI.getCurrentI18nResolver().resolveRelative(entity, getNotificationPath(I18nPaths.NotificationType.success, notified, I18nPaths.Type.label)),
                        BaseUI.getCurrentI18nResolver().resolveRelative(entity, getNotificationPath(I18nPaths.NotificationType.success, notified, I18nPaths.Type.text)));
                succes.show(Page.getCurrent());
            }else{
                GenericSuccessNotification succes = new GenericSuccessNotification(
                        BaseUI.getCurrentI18nResolver().resolveRelative(label, getNotificationPath(I18nPaths.NotificationType.success, notified, I18nPaths.Type.label)),
                        BaseUI.getCurrentI18nResolver().resolveRelative(label, getNotificationPath(I18nPaths.NotificationType.success, notified, I18nPaths.Type.text)));
                succes.show(Page.getCurrent());
            }

        }
    }

}
