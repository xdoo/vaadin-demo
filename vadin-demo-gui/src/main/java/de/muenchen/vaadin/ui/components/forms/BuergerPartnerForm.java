package de.muenchen.vaadin.ui.components.forms;

import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import de.muenchen.eventbus.events.Association;
import de.muenchen.vaadin.demo.api.local.Buerger;
import de.muenchen.vaadin.demo.i18nservice.buttons.SimpleAction;
import de.muenchen.vaadin.guilib.components.BaseComponent;
import de.muenchen.vaadin.guilib.components.actions.NavigateActions;
import de.muenchen.vaadin.guilib.components.buttons.ActionButton;
import de.muenchen.vaadin.guilib.controller.EntityController;
import de.muenchen.vaadin.ui.components.buttons.node.listener.BuergerAssociationActions;
import de.muenchen.vaadin.ui.components.buttons.node.listener.BuergerSingleActions;
import de.muenchen.vaadin.ui.components.forms.selected.SelectedBuergerPartnerForm;

/**
 * Created by p.mueller on 13.10.15.
 */
public class BuergerPartnerForm extends BaseComponent {

    private final String navigateToCreate;
    private final String navigateToRead;
    private final SelectedBuergerPartnerForm partnerForm;

    /**
     * Formular zum Lesen eines {@link Buerger}s. Über diesen Konstruktor kann zusätzlich eine Zielseite für die
     * 'zurück' und 'bearbeiten' Schaltflächen erstellt werden.
     *
     * @param controller
     * @param navigateToRead
     */
    public BuergerPartnerForm(EntityController controller, final String navigateToCreate, final String navigateToRead) {
        super(controller);

        partnerForm = new SelectedBuergerPartnerForm(controller) {
            @Override
            public void setBuerger(Buerger buerger) {
                setFormVisible(buerger != null);
                super.setBuerger(buerger);
            }
        };
        partnerForm.setReadOnly(true);

        this.navigateToRead = navigateToRead;
        this.navigateToCreate = navigateToCreate;

        init();
    }

    public void setFormVisible(boolean formVisible) {
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

    public de.muenchen.vaadin.ui.components.forms.selected.SelectedBuergerPartnerForm getPartnerForm() {
        return partnerForm;
    }

    private ActionButton createCreateButton() {
        final ActionButton createButton = new ActionButton(getI18nResolver(), SimpleAction.create);

        final NavigateActions navigateActions = new NavigateActions(getNavigateToCreate());
        createButton.addActionPerformer(navigateActions::navigate);

        return createButton;
    }

    private ActionButton createAddButton() {
        final ActionButton addButton = new ActionButton(getI18nResolver(), SimpleAction.add);
        addButton.addClickListener(clickEvent -> {
            throw new AssertionError("");
        });
        return addButton;
    }

    private Component createReadButton() {
        final ActionButton readButton = new ActionButton(getI18nResolver(), SimpleAction.read);
        final BuergerSingleActions singleActions = new BuergerSingleActions(getI18nResolver(), getPartnerForm()::getBuerger);
        readButton.addActionPerformer(singleActions::read);

        final NavigateActions navigateActions = new NavigateActions(getNavigateToRead());
        readButton.addActionPerformer(navigateActions::navigate);

        return readButton;
    }

    private Component createDeleteButton() {
        final ActionButton deleteButton = new ActionButton(getI18nResolver(), SimpleAction.delete);

        final BuergerAssociationActions associationActions = new BuergerAssociationActions(getI18nResolver(),
                () -> new Association<>(getPartnerForm().getBuerger(), Buerger.Rel.partner.name()));
        deleteButton.addActionPerformer(associationActions::removeAssociation);

        return deleteButton;
    }

    public String getNavigateToCreate() {
        return navigateToCreate;
    }

    public String getNavigateToRead() {
        return navigateToRead;
    }
}
