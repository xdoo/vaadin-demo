package de.muenchen.vaadin.ui.components.forms;

import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import de.muenchen.eventbus.events.Association;
import de.muenchen.vaadin.demo.api.local.Buerger;
import de.muenchen.vaadin.demo.i18nservice.buttons.SimpleAction;
import de.muenchen.vaadin.guilib.components.BaseComponent;
import de.muenchen.vaadin.guilib.components.actions.NavigateActions;
import de.muenchen.vaadin.guilib.components.buttons.ActionButton;
import de.muenchen.vaadin.guilib.components.buttons.ConfirmationWindow;
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

    public static final boolean READ_ONLY = true;
    private final SelectedBuergerPartnerForm partnerForm = new SelectedBuergerPartnerForm() {
        @Override
        public void setBuerger(Buerger buerger) {
            setFormVisible(buerger != null);
            super.setBuerger(buerger);
        }
    };
    private final VerticalLayout rootLayout = new VerticalLayout();
    private final HorizontalLayout buttonLayout = new HorizontalLayout();
    private final ActionButton createButton = new ActionButton(Buerger.class, SimpleAction.create);
    private final ActionButton addButton = new ActionButton(Buerger.class, SimpleAction.add);
    private final HorizontalLayout formButtonLayout = new HorizontalLayout();
    private final ActionButton readButton = new ActionButton(Buerger.class, SimpleAction.read);
    private final ActionButton deleteButton = new ActionButton(Buerger.class, SimpleAction.delete);
    private final NavigateActions createNavigation;
    private final NavigateActions readNavigation;
    private final NavigateActions addNavigation;

    /**
     * Create a new BuergerPartnerForm with the navigateToCreate and navigateToRead.
     * <p/>
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

    private void setIds() {
        setId(getClass().getName());
        getPartnerForm().setId(getId() + "#form");

        getCreateButton().setId(getId() + "#create-button" + getCreateNavigation().getNavigateTo());
        getAddButton().setId(getId() + "#add-button" + getAddNavigation().getNavigateTo());
        getReadButton().setId(getId() + "#read-button" + getReadNavigation().getNavigateTo());
        getDeleteButton().setId(getId() + "#delete-button");

    }


    public VerticalLayout getRootLayout() {
        return rootLayout;
    }

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

    private void configureDeleteButton() {
        final BuergerAssociationActions associationActions = new BuergerAssociationActions(
                () -> new Association<>(getPartnerForm().getBuerger(), Buerger.Rel.partner.name()));
        getDeleteButton().addActionPerformer(associationActions::removeAssociation);
    }

    private void configureReadButton() {
        final BuergerSingleActions singleActions = new BuergerSingleActions(getPartnerForm()::getBuerger);
        getReadButton().addActionPerformer(singleActions::read);
        getReadButton().addActionPerformer(getReadNavigation()::navigate);
    }

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

    public ActionButton getCreateButton() {
        return createButton;
    }

    public ActionButton getAddButton() {
        return addButton;
    }

    public ActionButton getReadButton() {
        return readButton;
    }

    public ActionButton getDeleteButton() {
        return deleteButton;
    }

    public NavigateActions getCreateNavigation() {
        return createNavigation;
    }

    public NavigateActions getReadNavigation() {
        return readNavigation;
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
     * Get the used form for the partner.
     *
     * @return The underlying Form.
     */
    public SelectedBuergerPartnerForm getPartnerForm() {
        return partnerForm;
    }


    public HorizontalLayout getButtonLayout() {
        return buttonLayout;
    }

    public HorizontalLayout getFormButtonLayout() {
        return formButtonLayout;
    }

    public NavigateActions getAddNavigation() {
        return addNavigation;
    }
}
