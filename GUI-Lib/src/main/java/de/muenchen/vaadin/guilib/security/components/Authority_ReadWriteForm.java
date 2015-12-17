package de.muenchen.vaadin.guilib.security.components;

import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.HorizontalLayout;
import de.muenchen.vaadin.demo.apilib.local.Authority_;
import de.muenchen.vaadin.guilib.security.components.buttons.listener.Authority_SingleActions;
import de.muenchen.vaadin.demo.i18nservice.buttons.SimpleAction;
import de.muenchen.vaadin.guilib.components.BaseComponent;
import de.muenchen.vaadin.guilib.components.buttons.ActionButton;

/**
 * Provides a full-featured Form for a Authority_. It allows to view the current selected authority and edit it.
 *
 * @author p.mueller
 * @version 1.0
 */
public class Authority_ReadWriteForm extends BaseComponent {

    /** The default (readOnly/write) mode the Form initially starts. */
    public static final boolean DEFAULT_MODE = false;

    /** The Form displaying the current selected Authority_. */
    private final Authority_SelectedForm authorityForm;
    /** The layout for all buttons that are shown in readonly mode. */
    private final HorizontalLayout buttons = new HorizontalLayout();
    /** The layout for all buttons that are shown in the update mode. */
    private final HorizontalLayout editButtons = new HorizontalLayout();
    /** The current mode the RWForm is in. */
    private boolean edit;

    /**
     * Create a new Authority_ReadWriteForm with the internationalization of the Controller.
     * It will navigate to the navigateBack value on the back button click.
     */
    public Authority_ReadWriteForm() {
        authorityForm = new Authority_SelectedForm();
        authorityForm.reLoad();

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

        getAuthorityForm().getFormLayout().addComponents(getButtons(), getEditButtons());

        setEdit(DEFAULT_MODE);
        setCompositionRoot(getAuthorityForm());
    }

    /**
     * Create the edit Buttons for this form.
     * @return An array of all buttons.
     */
    private Component[] createEditButtons() {
        final ActionButton saveButton = new ActionButton(Authority_.class, SimpleAction.save);

        final Authority_SingleActions singleActions = new Authority_SingleActions(getAuthorityForm()::getAuthority);
        saveButton.addActionPerformer(singleActions::update);
        saveButton.addActionPerformer(clickEvent -> {
            setEdit(false);
            return true;
        });

        final ActionButton cancelButton = new ActionButton(Authority_.class, SimpleAction.cancel);

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
        final ActionButton editButton = new ActionButton(Authority_.class, SimpleAction.update);
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
        getAuthorityForm().setReadOnly(!isEdit());

        if (edit)
            getAuthorityForm().getFields().stream().findFirst().ifPresent(Field::focus);
    }

    /**
     * Get the Layout for the Buttons that is shown in edit mode.
     * @return The layout for edit mode.
     */
    public HorizontalLayout getEditButtons() {
        return editButtons;
    }

    /**
     * Get the Authority_SelectedForm used by this RWForm.
     * @return The Authority_SelectedForm.
     */
    public Authority_SelectedForm getAuthorityForm() {
        return authorityForm;
    }

}

