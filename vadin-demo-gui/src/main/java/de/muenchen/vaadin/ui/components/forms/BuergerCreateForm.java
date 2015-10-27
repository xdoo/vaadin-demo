package de.muenchen.vaadin.ui.components.forms;

import com.vaadin.ui.Field;
import com.vaadin.ui.HorizontalLayout;
import de.muenchen.eventbus.events.Association;
import de.muenchen.vaadin.demo.api.local.Buerger;
import de.muenchen.vaadin.demo.i18nservice.buttons.SimpleAction;
import de.muenchen.vaadin.guilib.components.BaseComponent;
import de.muenchen.vaadin.guilib.BaseUI;
import de.muenchen.vaadin.guilib.components.actions.NavigateActions;
import de.muenchen.vaadin.guilib.components.buttons.ActionButton;
import de.muenchen.vaadin.guilib.controller.EntityController;
import de.muenchen.vaadin.ui.components.buttons.node.listener.BuergerAssociationActions;
import de.muenchen.vaadin.ui.components.buttons.node.listener.BuergerSingleActions;
import de.muenchen.vaadin.ui.components.forms.node.BuergerForm;

import java.util.Optional;

/**
 * Provides a simple Form for creating a new Buerger or a Buerger as an Association.
 *
 * @author claus.straube p.mueller
 * @version 2.0
 */
public class BuergerCreateForm extends BaseComponent {
    /** Indicates the mode of the Form. */
    private static final boolean READ_ONLY = false;
    /** The optional relation this CreateForm is for. */
    private final Optional<String> relation;
    /** The Form used to diplay the fields. */
    private final BuergerForm buergerForm;
    /** Layout for all buttons. */
    private final HorizontalLayout buttonLayout = new HorizontalLayout();
    /** Action for executing the navigation on save. */
    private final NavigateActions saveNavigation;
    /** The Button for the save action. */
    private final ActionButton saveButton = new ActionButton(Buerger.class, SimpleAction.save);

    /**
     * Create a new BuergerCreateForm that navigates to the navigateTo View on save.
     * This CreateForm is for the pure Buerger, and not as a relation of a different controller.
     *
     * @param entityController The controller.
     * @param navigateTo       The String of the view to navigate to on save.
     */
    public BuergerCreateForm(final EntityController entityController, final String navigateTo) {
        this(entityController, navigateTo, null);
    }

    /**
     * Formular zum Erstellen eines {@link Buerger}s. Über diesen Konstruktor kann zusätzlich eine Zielseite für die
     * 'abbrechen' Schaltfläche erstellt werden. Dies ist dann sinnvoll, wenn dieses Formular in einen Wizzard, bzw. in
     * eine definierte Abfolge von Formularen eingebettet wird.
     *
     * @param navigateTo Zielseite nach Druck der 'erstellen' Schaltfläche
     * @param relation   Optionale Angabe einer Assoziation, für die der Buerger ist.
     */
    public BuergerCreateForm(final EntityController entityController, final String navigateTo, final String relation) {
        super(entityController);
        this.saveNavigation = new NavigateActions(navigateTo);
        buergerForm = new BuergerForm(entityController);
        this.relation = Optional.ofNullable(relation);

        init();
    }

    /**
     * Build the basic layout and insert the headline and all Buttons.
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
     * Get the Form that displays the fields.
     *
     * @return the underlying Form.
     */
    public BuergerForm getBuergerForm() {
        return buergerForm;
    }

    /**
     * Get the layout of all Buttons.
     *
     * @return The layout of the Buttons
     */
    public HorizontalLayout getButtonLayout() {
        return buttonLayout;
    }

    /**
     * Get the Button for the save action.
     *
     * @return the Save Button.
     */
    public ActionButton getSaveButton() {
        return saveButton;
    }

    /**
     * Add the on-click functionality to the save Button. It depends on the presence and type of the relation.
     */
    private void configureSaveButton() {
        if (getRelation().isPresent()) {
            final BuergerAssociationActions buergerAssociationActions = new BuergerAssociationActions(
                    BaseUI.getCurrentI18nResolver(),
                    () -> new Association<>(getBuergerForm().getBuerger(), getRelation().get()));

            getSaveButton().addActionPerformer(buergerAssociationActions::addAssociation);
        } else {
            BuergerSingleActions buergerSingleActions = new BuergerSingleActions(getBuergerForm()::getBuerger);
            getSaveButton().addActionPerformer(buergerSingleActions::create);
        }

        getSaveButton().addActionPerformer(getSaveNavigation()::navigate);
    }

    /**
     * Get the Relation this CreateForm is for.
     *
     * @return The optional relation.
     */
    public Optional<String> getRelation() {
        return relation;
    }

    /**
     * Get the Action for the save navigation.
     * @return The NavigateActions.
     */
    public NavigateActions getSaveNavigation() {
        return saveNavigation;
    }
}
