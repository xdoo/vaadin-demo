package de.muenchen.vaadin.ui.components.forms;

import com.vaadin.ui.Field;
import com.vaadin.ui.HorizontalLayout;
import de.muenchen.vaadin.demo.api.local.Buerger;
import de.muenchen.vaadin.demo.i18nservice.buttons.SimpleAction;
import de.muenchen.vaadin.guilib.components.BaseComponent;
import de.muenchen.vaadin.guilib.components.buttons.ActionButton;
import de.muenchen.vaadin.ui.components.buttons.node.listener.BuergerSingleActions;
import de.muenchen.vaadin.ui.components.forms.selected.SelectedBuergerForm;

/**
 * Provides a full-featured Form for a Buerger. It allows to view the current selected buerger and edit it.
 *
 * @author p.mueller
 * @version 1.1
 */
public class BuergerRWForm extends BaseComponent {

    /** The default (readOnly/write) mode the Form initially starts. */
    public static final boolean DEFAULT_MODE_IS_EDIT = false;
    /** The button for the edit action. */
    final ActionButton editButton = new ActionButton(Buerger.class, SimpleAction.update);
    /** The button for the save action. */
    final ActionButton saveButton = new ActionButton(Buerger.class, SimpleAction.save);
    /** The button for the cancel button. */
    final ActionButton cancelButton = new ActionButton(Buerger.class, SimpleAction.cancel);
    /** The Form displaying the current selected Buerger. */
    private final SelectedBuergerForm buergerForm = new SelectedBuergerForm();
    /** The layout for all buttons that are shown in readonly mode. */
    private final HorizontalLayout buttons = new HorizontalLayout();
    /** The layout for all buttons that are shown in the update mode. */
    private final HorizontalLayout editButtons = new HorizontalLayout();
    /** The current mode the RWForm is in. */
    private boolean edit;

    /**
     * Create a new BuergerRWForm with the internationalization of the Controller.
     * It will navigate to the navigateBack value on the back button click.
     */
    public BuergerRWForm() {
        getBuergerForm().reLoadBuerger();

        init();
        setIds();
    }

    /**
     * Set all the IDs for important parts of the component.
     */
    private void setIds() {
        setId(getClass().getSimpleName());
        getBuergerForm().getFields().forEach(f -> f.setId(getId() + "#" + f.getId()));
        getBuergerForm().setId(getId() + "#form");
        getEditButton().setId(getId() + "#edit-button");
        getSaveButton().setId(getId() + "#save-button");
        getCancelButton().setId(getId() + "#cancel-button");
    }

    /**
     * Initialize the Layout and all the Buttons in it.
     */
    private void init() {

        getButtons().setSpacing(true);
        getButtons().addComponent(getEditButton());

        getEditButtons().setSpacing(true);
        getEditButtons().addComponents(getCancelButton(), getSaveButton());

        getBuergerForm().getFormLayout().addComponents(getButtons(), getEditButtons());

        configureEditButton();
        configureCancelButton();
        configureSaveButton();

        setEdit(DEFAULT_MODE_IS_EDIT);
        setCompositionRoot(getBuergerForm());
    }

    /**
     * Configure the button for the save action.
     */
    private void configureSaveButton() {
        final BuergerSingleActions singleActions = new BuergerSingleActions(getBuergerForm()::getBuerger);
        saveButton.addActionPerformer(singleActions::update);
        saveButton.addActionPerformer(clickEvent -> {
            setEdit(false);
            return true;
        });

    }

    /**
     * Configure the button for the edit action.
     */
    private void configureEditButton() {
        getEditButton().addClickListener(c -> setEdit(true));
    }

    /**
     * Configure the button for the cancel button.
     */
    private void configureCancelButton() {
        final BuergerSingleActions singleActions = new BuergerSingleActions(getBuergerForm()::getBuerger);
        getCancelButton().addActionPerformer(singleActions::reRead);
        getCancelButton().addActionPerformer(clickEvent -> {
            setEdit(false);
            return true;
        });
    }

    /**
     * Get the Layout for Buttons of the readOnly mode.
     *
     * @return The Layout for the readOnly mode Buttons.
     */
    public HorizontalLayout getButtons() {
        return buttons;
    }

    /**
     * Check if this Form is in Edit mode.
     *
     * @return true, if it is in edit mode.
     */
    public boolean isEdit() {
        return edit;
    }

    /**
     * Set the mode of this form. True means edit and false means readOnly.
     *
     * @param edit true means edit mode.
     */
    public void setEdit(boolean edit) {
        this.edit = edit;

        getButtons().setVisible(!isEdit());
        getEditButtons().setVisible(isEdit());
        getBuergerForm().setReadOnly(!isEdit());

        if (edit)
            getBuergerForm().getFields().stream().findFirst().ifPresent(Field::focus);
    }

    /**
     * Get the Layout for the Buttons that is shown in edit mode.
     *
     * @return The layout for edit mode.
     */
    public HorizontalLayout getEditButtons() {
        return editButtons;
    }

    /**
     * Get the button for the edit action.
     *
     * @return The button for edit.
     */
    public ActionButton getEditButton() {
        return editButton;
    }

    /**
     * Get the button for the save button.
     *
     * @return The button for save.
     */
    public ActionButton getSaveButton() {
        return saveButton;
    }

    /**
     * Get the button for the save action.
     *
     * @return The cancel button.
     */
    public ActionButton getCancelButton() {
        return cancelButton;
    }

    /**
     * Get the BuergerForm used by this RWForm.
     *
     * @return The BuergerForm.
     */
    public SelectedBuergerForm getBuergerForm() {
        return buergerForm;
    }

}
