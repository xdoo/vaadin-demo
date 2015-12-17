package de.muenchen.vaadin.guilib.security.components;

import com.vaadin.ui.Field;
import com.vaadin.ui.HorizontalLayout;
import de.muenchen.eventbus.events.Association;
import de.muenchen.vaadin.demo.apilib.local.Authority_;
import de.muenchen.vaadin.guilib.security.components.buttons.listener.User_AssociationActions;
import de.muenchen.vaadin.demo.i18nservice.buttons.SimpleAction;
import de.muenchen.vaadin.guilib.components.BaseComponent;
import de.muenchen.vaadin.guilib.components.actions.NavigateActions;
import de.muenchen.vaadin.guilib.components.buttons.ActionButton;

/**
 * Provides a simple Form for creating a new Authority_ as an Association.
 *
 * @author claus.straube p.mueller
 * @version 2.0
 */
public class User_Authorities_CreateForm extends BaseComponent {
    private static final boolean READ_ONLY = false;

    /**
     * The relation this CreateForm is for.
     */
    private final String relation;

    private final Authority_Form form;

    private final HorizontalLayout buttonLayout = new HorizontalLayout();
    private final NavigateActions saveNavigation;
    private final ActionButton saveButton = new ActionButton(Authority_.class, SimpleAction.save);

    /**
     * Formular zum Erstellen eines {@link Authority_}s. Über diesen Konstruktor kann zusätzlich eine Zielseite für die
     * 'abbrechen' Schaltfläche erstellt werden. Dies ist dann sinnvoll, wenn dieses Formular in einen Wizzard, bzw. in
     * eine definierte Abfolge von Formularen eingebettet wird.
     *
     * @param navigateTo Zielseite nach Druck der 'erstellen' Schaltfläche
     * @param relation Angabe einer Assoziation, für die der Authority_ ist.
     */
    public User_Authorities_CreateForm(final String navigateTo, final String relation) {
        this.saveNavigation = new NavigateActions(navigateTo);
        form = new Authority_Form();
        this.relation = relation;

        init();
    }

    /**
     * Build the basic layout and insert the headline and all Buttons.
     */
    private void init() {
        getForm().setReadOnly(READ_ONLY);


        getButtonLayout().setSpacing(true);
        getButtonLayout().addComponents(getSaveButton());

        configureSaveButton();

        getForm().getFormLayout().addComponent(getButtonLayout());
        setCompositionRoot(getForm());

        getForm().getFields().stream().findFirst().ifPresent(Field::focus);
    }

    public Authority_Form getForm() {
        return form;
    }

    public HorizontalLayout getButtonLayout() {
        return buttonLayout;
    }

    public ActionButton getSaveButton() {
        return saveButton;
    }

    private void configureSaveButton() {
		final User_AssociationActions userAssociationActions = new User_AssociationActions(
   		      () -> new Association<>(getForm().getAuthority(), getRelation()));

        getSaveButton().addActionPerformer(userAssociationActions::addAssociation);
        getSaveButton().addActionPerformer(getSaveNavigation()::navigate);
    }

    /**
     * Get the Relation this CreateForm is for.
     *
     * @return The relation.
     */
    public String getRelation() {
        return relation;
    }

    public NavigateActions getSaveNavigation() {
        return saveNavigation;
    }
}
