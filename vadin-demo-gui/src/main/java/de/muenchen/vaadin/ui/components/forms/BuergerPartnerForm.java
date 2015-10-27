package de.muenchen.vaadin.ui.components.forms;

import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import de.muenchen.eventbus.events.Association;
import de.muenchen.vaadin.demo.api.local.Buerger;
import de.muenchen.vaadin.demo.i18nservice.buttons.SimpleAction;
import de.muenchen.vaadin.guilib.BaseUI;
import de.muenchen.vaadin.guilib.components.BaseComponent;
import de.muenchen.vaadin.guilib.components.GenericConfirmationWindow;
import de.muenchen.vaadin.guilib.components.actions.NavigateActions;
import de.muenchen.vaadin.guilib.components.buttons.ActionButton;
import de.muenchen.vaadin.ui.app.views.BuergerAddPartnerView;
import de.muenchen.vaadin.ui.components.buttons.node.listener.BuergerAssociationActions;
import de.muenchen.vaadin.ui.components.buttons.node.listener.BuergerSingleActions;
import de.muenchen.vaadin.ui.components.forms.selected.SelectedBuergerPartnerForm;

/**
 * Provides a simple Form to display the Partner of a Buerger.
 *
 * @author p.mueller
 * @version 1.0
 */
public class BuergerPartnerForm extends BaseComponent {

    private final String navigateToCreate;
    private final String navigateToRead;
    private final SelectedBuergerPartnerForm partnerForm;

    /**
     * Create a new BuergerPartnerForm with the navigateToCreate and navigateToRead.
     * <p>
     * It will always show the current partner of the selected Buerger. The Form won't be visible if no Partner is present.
     *
     * @param navigateToCreate The View to navigate to on the create action.
     * @param navigateToRead   The View to navigate to on the read action.
     */
    public BuergerPartnerForm(final String navigateToCreate, final String navigateToRead) {

        partnerForm = new SelectedBuergerPartnerForm() {
            @Override
            public void setBuerger(Buerger buerger) {
                setFormVisible(buerger != null);
                super.setBuerger(buerger);
            }
        };
        partnerForm.reLoadBuerger();
        partnerForm.setReadOnly(true);

        this.navigateToRead = navigateToRead;
        this.navigateToCreate = navigateToCreate;

        init();
    }

    /**
     * Set the visibility of the form.
     *
     * @param formVisible True, if it should be visible.
     */
    private void setFormVisible(boolean formVisible) {
        getPartnerForm().setVisible(formVisible);
    }

    /**
     * Build the basic layout and insert the headline and all Buttons.
     */
    private void init() {
        final VerticalLayout layout = new VerticalLayout();


        final HorizontalLayout buttonsAlwaysVisible = new HorizontalLayout();
        buttonsAlwaysVisible.setSpacing(true);
        buttonsAlwaysVisible.addComponents(createCreateButton(), createAddButton());

        final HorizontalLayout buttonsInForm = new HorizontalLayout();
        buttonsInForm.setSpacing(true);
        buttonsInForm.addComponents(createReadButton(), createDeleteButton());
        getPartnerForm().getFormLayout().addComponent(buttonsInForm);

        layout.addComponents(buttonsAlwaysVisible, getPartnerForm());
        setCompositionRoot(layout);
    }

    /**
     * Get the used form for the partner.
     *
     * @return The underlying Form.
     */
    public de.muenchen.vaadin.ui.components.forms.selected.SelectedBuergerPartnerForm getPartnerForm() {
        return partnerForm;
    }

    /**
     * Create the Button for the create action.
     *
     * @return A new ActionButton preconfigured with actions.
     */
    private ActionButton createCreateButton() {
        final ActionButton createButton = new ActionButton(Buerger.class, SimpleAction.create);
        final NavigateActions navigateActions = new NavigateActions(getNavigateToCreate());
        createButton.addClickListener(clickEvent -> {
            if (partnerForm.isVisible()) {
                GenericConfirmationWindow window = new GenericConfirmationWindow(BaseUI.getCurrentI18nResolver(), SimpleAction.override, navigateActions::navigate);
                getUI().addWindow(window);
                window.center();
                window.focus();
            } else {
                navigateActions.navigate();
            }
        });
        return createButton;
    }

    /**
     * Create the Button for the add action.
     *
     * @return A new ActionButton preconfigured with actions.
     */
    private ActionButton createAddButton() {
        final ActionButton addButton = new ActionButton(Buerger.class, SimpleAction.add);
        final NavigateActions navigateActions = new NavigateActions(BuergerAddPartnerView.NAME);
        addButton.addClickListener(clickEvent -> {
            if (partnerForm.isVisible()) {
                GenericConfirmationWindow window = new GenericConfirmationWindow(BaseUI.getCurrentI18nResolver(), SimpleAction.override, navigateActions::navigate);
                getUI().addWindow(window);
                window.center();
                window.focus();
            } else {
                navigateActions.navigate();
            }
        });
        return addButton;
    }


    /**
     * Create the Button for the read action.
     *
     * @return A new ActionButton preconfigured with actions.
     */
    private Component createReadButton() {
        final ActionButton readButton = new ActionButton(Buerger.class, SimpleAction.read);
        final BuergerSingleActions singleActions = new BuergerSingleActions(getPartnerForm()::getBuerger);
        readButton.addActionPerformer(singleActions::read);

        final NavigateActions navigateActions = new NavigateActions(getNavigateToRead());
        readButton.addActionPerformer(navigateActions::navigate);

        return readButton;
    }


    /**
     * Create the Button for the delete action.
     *
     * @return A new ActionButton preconfigured with actions.
     */
    private Component createDeleteButton() {
        final ActionButton deleteButton = new ActionButton(Buerger.class, SimpleAction.delete);

        final BuergerAssociationActions associationActions = new BuergerAssociationActions(
                () -> new Association<>(getPartnerForm().getBuerger(), Buerger.Rel.partner.name()));
        deleteButton.addActionPerformer(associationActions::removeAssociation);

        return deleteButton;
    }

    /**
     * Get the String that is navigated to on create.
     *
     * @return The String of the View that is navigated to on create.
     */
    public String getNavigateToCreate() {
        return navigateToCreate;
    }

    /**
     * Get the String that is navigated to on read.
     *
     * @return The String of the View that is navigated to on read.
     */
    public String getNavigateToRead() {
        return navigateToRead;
    }
}
