package de.muenchen.vaadin.ui.components.forms;

import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import de.muenchen.eventbus.events.Association;
import de.muenchen.vaadin.demo.api.local.Buerger;
import de.muenchen.vaadin.demo.i18nservice.buttons.SimpleAction;
import de.muenchen.vaadin.guilib.components.BaseComponent;
import de.muenchen.vaadin.guilib.components.ConfirmationWindow;
import de.muenchen.vaadin.guilib.components.actions.NavigateActions;
import de.muenchen.vaadin.guilib.components.buttons.ActionButton;
import de.muenchen.vaadin.ui.components.buttons.node.listener.BuergerAssociationActions;
import de.muenchen.vaadin.ui.components.buttons.node.listener.BuergerSingleActions;
import de.muenchen.vaadin.ui.components.forms.selected.SelectedBuergerPartnerForm;

/**
 * Provides a simple Form to display the Partner of a Buerger.
 *
 * @author p.mueller
 * @version 1.1
 */
public class BuergerPartnerForm extends BaseComponent {
    /**
     * Indicates the mode of the form.
     */
    public static final boolean READ_ONLY = true;
    /**
     * The underlying form.
     */
    private final SelectedBuergerPartnerForm partnerForm = new SelectedBuergerPartnerForm() {
        @Override
        public void setBuerger(Buerger buerger) {
            setFormVisible(buerger != null);
            super.setBuerger(buerger);
        }
    };
    /**
     * The root layout for the content of this component.
     */
    private final VerticalLayout rootLayout = new VerticalLayout();
    /**
     * The layout for the buttons (that are always visible).
     */
    private final HorizontalLayout buttonLayout = new HorizontalLayout();
    /**
     * The create Button.
     */
    private final ActionButton createButton = new ActionButton(Buerger.class, SimpleAction.create);
    /**
     * The button for the add action.
     */
    private final ActionButton addButton = new ActionButton(Buerger.class, SimpleAction.add);
    /**
     * The layout for the buttons that are added to the form.
     */
    private final HorizontalLayout formButtonLayout = new HorizontalLayout();
    /**
     * The button for the read action.
     */
    private final ActionButton readButton = new ActionButton(Buerger.class, SimpleAction.read);
    /**
     * The button for the delete action.
     */
    private final ActionButton deleteButton = new ActionButton(Buerger.class, SimpleAction.delete);
    /**
     * The navigation for the create action.
     */
    private final NavigateActions createNavigation;
    /**
     * The navigation for the read action.
     */
    private final NavigateActions readNavigation;
    /**
     * The navigation for the add action.
     */
    private final NavigateActions addNavigation;

    /**
     * Create a new BuergerPartnerForm with the navigateToCreate, navigateToRead and navigateToAdd.
     * <p>
     * It will always show the current partner of the selected Buerger. The Form won't be visible if no Partner is present.
     *
     * @param navigateToCreate The View to navigate to on the create action.
     * @param navigateToRead   The View to navigate to on the read action.
     */
    public BuergerPartnerForm(String navigateToCreate, String navigateToRead, String navigateToAdd) {
        partnerForm.reLoadBuerger();
        this.createNavigation = new NavigateActions(navigateToCreate);
        this.readNavigation = new NavigateActions(navigateToRead);
        this.addNavigation = new NavigateActions(navigateToAdd);

        init();
        setIds();
    }

    /**
     * Set the visibility of the form.
     *
     * @param formVisible True, to set it visible.
     */
    private void setFormVisible(boolean formVisible) {
        getPartnerForm().setVisible(formVisible);
    }

    /**
     * Set the IDs for this component
     */
    private void setIds() {
        setId(getClass().getSimpleName());
        getPartnerForm().setId(getId() + "#form");
        getPartnerForm().getFields().forEach(f -> f.setId(getId() + "#" + f.getId()));
        getCreateButton().setId(getId() + "#create-button-" + getCreateNavigation().getNavigateTo());
        getAddButton().setId(getId() + "#add-button-" + getAddNavigation().getNavigateTo());
        getReadButton().setId(getId() + "#read-button-" + getReadNavigation().getNavigateTo());
        getDeleteButton().setId(getId() + "#delete-button");
    }

    /**
     * Get the root layout of this component.
     *
     * @return The vertical layout.
     */
    public VerticalLayout getRootLayout() {
        return rootLayout;
    }

    /**
     * Initialize this Component.
     */
    private void init() {
        getPartnerForm().setReadOnly(READ_ONLY);

        getRootLayout().addComponents(getButtonLayout(), getPartnerForm());

        getButtonLayout().setSpacing(true);
        getButtonLayout().addComponents(getCreateButton(), getAddButton());

        getFormButtonLayout().setSpacing(true);
        getFormButtonLayout().addComponents(getReadButton(), getDeleteButton());

        getPartnerForm().getFormLayout().addComponent(getFormButtonLayout());

        configureCreateButton();
        configureAddButton();
        configureReadButton();
        configureDeleteButton();

        setCompositionRoot(getRootLayout());
    }

    /**
     * Configure the Button for the delete Action.
     */
    private void configureDeleteButton() {
        final BuergerAssociationActions associationActions = new BuergerAssociationActions(
                () -> new Association<>(getPartnerForm().getBuerger(), Buerger.Rel.partner.name()));
        getDeleteButton().addActionPerformer(associationActions::removeAssociation);
    }

    /**
     * Configure the Button for the read action.
     */
    private void configureReadButton() {
        final BuergerSingleActions singleActions = new BuergerSingleActions(getPartnerForm()::getBuerger);
        getReadButton().addActionPerformer(singleActions::read);
        getReadButton().addActionPerformer(getReadNavigation()::navigate);
    }

    /**
     * Configure the button for the add action.
     */
    private void configureAddButton() {
        getAddButton().addClickListener(clickEvent -> {
            if (partnerForm.isVisible()) {
                final ConfirmationWindow window = new ConfirmationWindow(SimpleAction.override);
                window.addActionPerformer(getAddNavigation()::navigate);
                getUI().addWindow(window);
            } else {
                getAddNavigation().navigate();
            }
        });
    }

    /**
     * Configure the action for the create Button.
     */
    private void configureCreateButton() {
        createButton.addClickListener(clickEvent -> {
            if (partnerForm.isVisible()) {
                final ConfirmationWindow window = new ConfirmationWindow(SimpleAction.override);
                window.addActionPerformer(getCreateNavigation()::navigate);
                getUI().addWindow(window);
            } else {
                getCreateNavigation().navigate();
            }
        });
    }

    /**
     * Get the button for the create action.
     *
     * @return The create button.
     */
    public ActionButton getCreateButton() {
        return createButton;
    }

    /**
     * Get the button for the add action.
     *
     * @return The add button.
     */
    public ActionButton getAddButton() {
        return addButton;
    }

    /**
     * Get the button for the read action.
     *
     * @return The read button.
     */
    public ActionButton getReadButton() {
        return readButton;
    }

    /**
     * Get the button for the delete action.
     *
     * @return The delete button.
     */
    public ActionButton getDeleteButton() {
        return deleteButton;
    }

    /**
     * Get the Navigation for the create action.
     *
     * @return The NavigateActions for create.
     */
    public NavigateActions getCreateNavigation() {
        return createNavigation;
    }


    /**
     * Get the Navigation for the read action.
     *
     * @return The NavigateActions for read.
     */
    public NavigateActions getReadNavigation() {
        return readNavigation;
    }

    /**
     * Get the used form for the partner.
     *
     * @return The underlying Form.
     */
    public SelectedBuergerPartnerForm getPartnerForm() {
        return partnerForm;
    }

    /**
     * Get the layout for the (always visible) buttons.
     *
     * @return The horizontal layout for buttons.
     */
    public HorizontalLayout getButtonLayout() {
        return buttonLayout;
    }

    /**
     * Get the layout for the buttons in the form.
     *
     * @return The horizontal layout for buttons (that are not always shown).
     */
    public HorizontalLayout getFormButtonLayout() {
        return formButtonLayout;
    }

    /**
     * Get the Navigation for the add action.
     *
     * @return The NavigateActions for add.
     */
    public NavigateActions getAddNavigation() {
        return addNavigation;
    }
}
