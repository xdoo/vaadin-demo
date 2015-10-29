package de.muenchen.vaadin.ui.components.forms;

import com.vaadin.ui.HorizontalLayout;
import de.muenchen.vaadin.demo.api.local.Buerger;
import de.muenchen.vaadin.demo.i18nservice.buttons.SimpleAction;
import de.muenchen.vaadin.guilib.components.BaseComponent;
import de.muenchen.vaadin.guilib.components.actions.NavigateActions;
import de.muenchen.vaadin.guilib.components.buttons.ActionButton;
import de.muenchen.vaadin.ui.components.buttons.node.listener.BuergerSingleActions;
import de.muenchen.vaadin.ui.components.forms.selected.SelectedBuergerForm;

/**
 * Provides a simple ReadForm with a update Button that always shows the selected Buerger.
 *
 * @author p.mueller
 * @version 1.0
 */
public class BuergerReadForm extends BaseComponent {

    /** Indicates the mode of the Form. */
    public static final boolean READ_ONLY = true;
    /** The underlying form. */
    private final SelectedBuergerForm buergerForm;
    /** The layout for all Buttons. */
    private final HorizontalLayout buttonLayout = new HorizontalLayout();
    /** The button for the update action. */
    private final ActionButton updateButton = new ActionButton(Buerger.class, SimpleAction.update);
    /** The actions for the navigate on update. */
    private final NavigateActions updateNavigation;

    /**
     * Create a new ReadForm that navigates to the specified View on the update action.
     *
     * @param navigateToUpdate The View that is navigated to on update.
     */
    public BuergerReadForm(final String navigateToUpdate) {
        buergerForm = new SelectedBuergerForm();
        buergerForm.reLoadBuerger();

        updateNavigation = new NavigateActions(navigateToUpdate);

        init();
        setIds();
    }

    private void setIds() {
        setId(getClass().getSimpleName());
        getBuergerForm().setId(getId() + "#form");
        getUpdateButton().setId(getId() + "#update-button-" + getUpdateNavigation().getNavigateTo());
    }

    /**
     * Initialize the Component.
     */
    private void init() {
        getBuergerForm().setReadOnly(READ_ONLY);

        getButtonLayout().setSpacing(true);
        getButtonLayout().addComponents(getUpdateButton());

        configureUpdateButton();

        getBuergerForm().getFormLayout().addComponent(getButtonLayout());
        setCompositionRoot(getBuergerForm());
    }

    /**
     * Get the underlying BuergerForm.
     *
     * @return The BuergerForm.
     */
    public SelectedBuergerForm getBuergerForm() {
        return buergerForm;
    }

    /**
     * Get the Layout for all Buttons.
     *
     * @return The horizontal Layout.
     */
    public HorizontalLayout getButtonLayout() {
        return buttonLayout;
    }

    /**
     * Get the Button for the update Action.
     *
     * @return The ActionButton.
     */
    public ActionButton getUpdateButton() {
        return updateButton;
    }

    /**
     * Configure the Update Button and add the actions.
     */
    private void configureUpdateButton() {
        final BuergerSingleActions singleActions = new BuergerSingleActions(getBuergerForm()::getBuerger);
        getUpdateButton().addActionPerformer(singleActions::read);
        getUpdateButton().addActionPerformer(getUpdateNavigation()::navigate);
    }

    /**
     * Get the Navigation for the update Action.
     *
     * @return The NavigateActions used by the update Button.
     */
    public NavigateActions getUpdateNavigation() {
        return updateNavigation;
    }
}
