package de.muenchen.vaadin.ui.components.forms;

import com.vaadin.ui.Field;
import com.vaadin.ui.HorizontalLayout;
import de.muenchen.vaadin.demo.api.local.Buerger;
import de.muenchen.vaadin.demo.i18nservice.buttons.SimpleAction;
import de.muenchen.vaadin.guilib.components.BaseComponent;
import de.muenchen.vaadin.guilib.components.actions.NavigateActions;
import de.muenchen.vaadin.guilib.components.buttons.ActionButton;
import de.muenchen.vaadin.ui.components.buttons.node.listener.BuergerSingleActions;
import de.muenchen.vaadin.ui.components.forms.selected.SelectedBuergerForm;

/**
 * Provides a simple UpdateForm for the current selected Buerger.
 *
 * @author p.mueller
 * @version 1.0
 */
public class BuergerUpdateForm extends BaseComponent {
    /** The mode of the form. */
    private static final boolean READ_ONLY = false;
    /** The underlying form. */
    private final SelectedBuergerForm buergerForm;
    /** The layout for the buttons */
    private final HorizontalLayout buttonLayout = new HorizontalLayout();
    /** The actions for the navigation on save. */
    private final NavigateActions saveNavigation;
    /** The Button for the save action. */
    private final ActionButton saveButton = new ActionButton(Buerger.class, SimpleAction.save);

    /**
     * Create a new Update Form for the current buerger, after the save click it will navigate to the View specified by the String.
     *
     * @param navigateToSaved  The String of the View that is navigated to on save.
     */
    public BuergerUpdateForm(final String navigateToSaved) {
        saveNavigation = new NavigateActions(navigateToSaved);
        buergerForm = new SelectedBuergerForm();
        buergerForm.reLoadBuerger();

        init();
        setIds();
    }

    private void setIds() {
        setId(getClass().getSimpleName());
        getBuergerForm().getFields().forEach(f -> f.setId(getId() + "#" + f.getId()));
        getBuergerForm().setId(getId() + "#form");
        getSaveButton().setId(getId() + "#save-button-" + getSaveNavigation().getNavigateTo());
    }

    /**
     * Initialize the component.
     */
    private void init() {
        getBuergerForm().setReadOnly(READ_ONLY);

        getButtonLayout().setSpacing(true);
        getButtonLayout().addComponents(getSaveButton());

        configureSaveButton();

        getBuergerForm().getFormLayout().addComponent(getButtonLayout());
        setCompositionRoot(getBuergerForm());

        getBuergerForm().getFields().stream().findFirst().ifPresent(Field::focus);
    }

    /**
     * Get the underlying Form.
     * @return The form that displays the current buerger.
     */
    public SelectedBuergerForm getBuergerForm() {
        return buergerForm;
    }

    /**
     * Get the layout containing all buttons.
     * @return The horizontal Layout of buttons.
     */
    public HorizontalLayout getButtonLayout() {
        return buttonLayout;
    }

    /**
     * Get the Button for the Save action.
     * @return The ActionButton for save.
     */
    public ActionButton getSaveButton() {
        return saveButton;
    }

    /**
     * Configure the save Button and add all on-click actions.
     */
    private void configureSaveButton() {
        final BuergerSingleActions singleActions = new BuergerSingleActions(getBuergerForm()::getBuerger);
        getSaveButton().addActionPerformer(singleActions::update);
        getSaveButton().addActionPerformer(getSaveNavigation()::navigate);
    }

    /**
     * Get the navigation for the save action.
     * @return The NavigateActions for save.
     */
    public NavigateActions getSaveNavigation() {
        return saveNavigation;
    }
}
