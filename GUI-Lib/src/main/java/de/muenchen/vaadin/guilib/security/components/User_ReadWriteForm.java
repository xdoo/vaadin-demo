package de.muenchen.vaadin.guilib.security.components;

import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.HorizontalLayout;
import de.muenchen.vaadin.demo.apilib.local.User;
import de.muenchen.vaadin.guilib.security.components.buttons.listener.User_SingleActions;
import de.muenchen.vaadin.demo.i18nservice.buttons.SimpleAction;
import de.muenchen.vaadin.guilib.components.BaseComponent;
import de.muenchen.vaadin.guilib.components.buttons.ActionButton;

/**
 * Provides a full-featured Form for a User_. It allows to view the current selected user and edit it.
 *
 * @author p.mueller
 * @version 1.0
 */
public class User_ReadWriteForm extends BaseComponent {

    /** The default (readOnly/write) mode the Form initially starts. */
    public static final boolean DEFAULT_MODE = false;

    /** The Form displaying the current selected User_. */
    private final User_SelectedForm userForm;
    /** The layout for all buttons that are shown in readonly mode. */
    private final HorizontalLayout buttons = new HorizontalLayout();
    /** The layout for all buttons that are shown in the update mode. */
    private final HorizontalLayout editButtons = new HorizontalLayout();
    /** The current mode the RWForm is in. */
    private boolean edit;

    /**
     * Create a new User_ReadWriteForm with the internationalization of the Controller.
     * It will navigate to the navigateBack value on the back button click.
     */
    public User_ReadWriteForm() {
        userForm = new User_SelectedForm();
        userForm.reLoad();

        init();
    }

    /**
     * Initialize the Layout and all the Buttons in it.
     */
    private void init() {
        getButtons().setSpacing(true);
        getButtons().addComponents(createButtons());
        getEditButtons().setSpacing(true);
        getEditButtons().addComponents(createEditButtons());

        getUserForm().getFormLayout().addComponents(getButtons(), getEditButtons());

        setEdit(DEFAULT_MODE);
        setCompositionRoot(getUserForm());
    }

    /**
     * Create the edit Buttons for this form.
     * @return An array of all buttons.
     */
    private Component[] createEditButtons() {
        final ActionButton saveButton = new ActionButton(User.class, SimpleAction.save);

        final User_SingleActions singleActions = new User_SingleActions(getUserForm()::getUser);
        saveButton.addActionPerformer(singleActions::update);
        saveButton.addActionPerformer(clickEvent -> {
            setEdit(false);
            return true;
        });

        final ActionButton cancelButton = new ActionButton(User.class, SimpleAction.cancel);

        cancelButton.addActionPerformer(singleActions::reRead);
        cancelButton.addActionPerformer(clickEvent -> {
            setEdit(false);
            return true;
        });

        return new Component[]{cancelButton, saveButton};
    }

    /**
     * Create the Buttons for the readOnly mode.
     * @return An array of all buttons.
     */
    private Component[] createButtons() {
        final ActionButton editButton = new ActionButton(User.class, SimpleAction.update);
        editButton.addClickListener(clickEvent -> setEdit(true));

        return new Component[]{editButton};
    }

    /**
     * Get the Layout for Buttons of the readOnly mode.
     * @return The Layout for the readOnly mode Buttons.
     */
    public HorizontalLayout getButtons() {
        return buttons;
    }

    /**
     * Check if this Form is in Edit mode.
     * @return true, if it is in edit mode.
     */
    public boolean isEdit() {
        return edit;
    }

    /**
     * Set the mode of this form. True means edit and false means readOnly.
     * @param edit true means edit mode.
     */
    public void setEdit(boolean edit) {
        this.edit = edit;

        getButtons().setVisible(!isEdit());
        getEditButtons().setVisible(isEdit());
        getUserForm().setReadOnly(!isEdit());
        getUserForm().getFields().get(0).setReadOnly(true);

        if (edit)
            getUserForm().getFields().stream().findFirst().ifPresent(Field::focus);
    }

    /**
     * Get the Layout for the Buttons that is shown in edit mode.
     * @return The layout for edit mode.
     */
    public HorizontalLayout getEditButtons() {
        return editButtons;
    }

    /**
     * Get the User_SelectedForm used by this RWForm.
     * @return The User_SelectedForm.
     */
    public User_SelectedForm getUserForm() {
        return userForm;
    }

}

